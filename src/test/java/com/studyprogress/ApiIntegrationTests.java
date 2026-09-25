package com.studyprogress;

import com.fasterxml.jackson.databind.*;
import com.studyprogress.model.User;
import com.studyprogress.repository.*;
import com.studyprogress.service.*;
import com.studyprogress.exception.EmailDeliveryException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserRepository users;
    @Autowired CategoryRepository categories;
    @Autowired ReminderRepository reminders;
    @Autowired ReminderDeliveryService delivery;
    @MockBean EmailService email;

    JsonNode body(MvcResult result) throws Exception { return json.readTree(result.getResponse().getContentAsString()); }
    ResultActions request(MockHttpServletRequestBuilder request, String token, Object value) throws Exception {
        if (token != null) request.header("Authorization", "Bearer " + token);
        if (value != null) request.contentType("application/json").content(json.writeValueAsString(value));
        return mvc.perform(request);
    }
    JsonNode register() throws Exception {
        return body(request(post("/api/v1/auth/register"), null, Map.of("fullName", "Estudiante Prueba", "email", UUID.randomUUID()+"@example.com", "password", "Password123"))
                .andExpect(status().isCreated()).andReturn());
    }
    String token(JsonNode auth) { return auth.path("token").asText(); }
    long course(String token) throws Exception {
        long category = categories.findAll().get(0).getId();
        return body(request(post("/api/v1/courses"), token, Map.of("title", "Álgebra", "description", "Repaso", "categoryId", category)).andExpect(status().isCreated()).andReturn()).path("id").asLong();
    }
    long topic(String token, long course) throws Exception {
        return body(request(post("/api/v1/courses/"+course+"/topics"), token, Map.of("title", "Matrices", "orderIndex", 0)).andExpect(status().isCreated()).andReturn()).path("id").asLong();
    }
    long task(String token, long topic) throws Exception {
        return body(request(post("/api/v1/topics/"+topic+"/tasks"), token, Map.of("title", "Resolver ejercicios")).andExpect(status().isCreated()).andReturn()).path("id").asLong();
    }
    @Test void registrationLoginAndRotation() throws Exception {
        JsonNode auth = register();
        String userEmail = auth.path("user").path("email").asText();
        assertTrue(users.findByEmail(userEmail).orElseThrow().getPassword().startsWith("$2"));
        request(post("/api/v1/auth/login"), null, Map.of("email", userEmail, "password", "incorrecta")).andExpect(status().isUnauthorized());
        request(post("/api/v1/auth/login"), null, Map.of("email", userEmail, "password", "Password123")).andExpect(status().isOk());
        request(post("/api/v1/auth/register"), null, Map.of("fullName", "Duplicado", "email", userEmail.toUpperCase(Locale.ROOT), "password", "Password123")).andExpect(status().isConflict());
        JsonNode rotated = body(request(post("/api/v1/auth/refresh"), null, Map.of("refreshToken", auth.path("refreshToken").asText())).andExpect(status().isOk()).andReturn());
        request(post("/api/v1/auth/refresh"), null, Map.of("refreshToken", auth.path("refreshToken").asText())).andExpect(status().isUnauthorized());
        request(post("/api/v1/auth/logout"), null, Map.of("refreshToken", rotated.path("refreshToken").asText())).andExpect(status().isNoContent());
        request(post("/api/v1/auth/refresh"), null, Map.of("refreshToken", rotated.path("refreshToken").asText())).andExpect(status().isUnauthorized());
        request(get("/api/v1/users/me"), token(auth), null).andExpect(status().isOk()).andExpect(jsonPath("password").doesNotExist());
    }
    @Test void validationAndConsistentErrors() throws Exception {
        request(post("/api/v1/auth/register"), null, Map.of("fullName", "A", "email", "bad", "password", "weak"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("details.password").exists()).andExpect(jsonPath("timestamp").exists());
        request(get("/api/v1/courses"), null, null).andExpect(status().isUnauthorized()).andExpect(jsonPath("status").value(401));
        request(get("/api/v1/courses"), "invalid", null).andExpect(status().isUnauthorized());
        String token = token(register());
        request(get("/api/v1/tasks/9999999"), token, null).andExpect(status().isNotFound());
        request(get("/api/v1/tasks/no-number"), token, null).andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/courses").header("Authorization", "Bearer "+token).contentType("application/json").content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("path").value("/api/v1/courses"));
        request(post("/api/v1/categories"), token, Map.of("name", "No autorizado")).andExpect(status().isForbidden());
    }
    @Test void ownershipCollaborationAndRevocation() throws Exception {
        JsonNode owner = register(), guest = register();
        String a = token(owner), b = token(guest);
        long course = course(a), topic = topic(a, course), task = task(a, topic);
        request(get("/api/v1/courses/"+course), b, null).andExpect(status().isForbidden());
        request(patch("/api/v1/tasks/"+task+"/progress"), b, Map.of("completed", true, "studyMinutes", 30)).andExpect(status().isForbidden());
        request(post("/api/v1/courses/"+course+"/collaborators"), a, Map.of("userId", guest.path("user").path("id").asLong())).andExpect(status().isCreated());
        request(get("/api/v1/courses/"+course), b, null).andExpect(status().isOk());
        request(patch("/api/v1/tasks/"+task+"/progress"), b, Map.of("completed", true, "studyMinutes", 30)).andExpect(status().isOk());
        request(delete("/api/v1/courses/"+course), b, null).andExpect(status().isForbidden());
        request(delete("/api/v1/courses/"+course+"/collaborators/"+guest.path("user").path("id").asLong()), a, null).andExpect(status().isNoContent());
        request(get("/api/v1/topics/"+topic), b, null).andExpect(status().isForbidden());
    }
    @Test void progressDashboardAndSnapshots() throws Exception {
        String token = token(register());
        long course = course(token), topic = topic(token, course), first = task(token, topic), second = task(token, topic);
        request(patch("/api/v1/tasks/"+first+"/progress"), token, Map.of("completed", true, "studyMinutes", 25)).andExpect(status().isOk());
        request(get("/api/v1/courses/"+course), token, null).andExpect(jsonPath("course.progressPercentage").value(50.0));
        request(get("/api/v1/statistics/dashboard"), token, null).andExpect(jsonPath("completedTasks").value(1)).andExpect(jsonPath("studyMinutes").value(25));
        request(patch("/api/v1/topics/"+topic+"/progress"), token, Map.of("completed", true)).andExpect(status().isOk());
        request(get("/api/v1/courses/"+course), token, null).andExpect(jsonPath("course.progressPercentage").value(100.0));
        request(patch("/api/v1/tasks/"+second+"/progress"), token, Map.of("completed", false, "studyMinutes", 5)).andExpect(status().isOk());
        request(get("/api/v1/topics/"+topic), token, null).andExpect(jsonPath("completed").value(false));
        request(patch("/api/v1/tasks/"+second+"/progress"), token, Map.of("completed", false, "studyMinutes", -1)).andExpect(status().isBadRequest());
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> request(get("/api/v1/statistics"), token, null).andExpect(jsonPath("totalElements").value(org.hamcrest.Matchers.greaterThan(0))));
    }
    @Test void publicSharingContainsNoPersonalData() throws Exception {
        String token = token(register());
        long course = course(token);
        task(token, topic(token, course));
        JsonNode share = body(request(put("/api/v1/courses/"+course+"/share"), token, Map.of("enabled", true)).andExpect(status().isOk()).andReturn());
        String path = share.path("path").asText();
        request(get(path), null, null).andExpect(status().isOk()).andExpect(jsonPath("title").value("Álgebra"))
                .andExpect(jsonPath("ownerId").doesNotExist()).andExpect(jsonPath("topics[0].tasks[0].studyMinutes").doesNotExist());
        request(put("/api/v1/courses/"+course+"/share"), token, Map.of("enabled", false)).andExpect(status().isOk());
        request(get(path), null, null).andExpect(status().isNotFound());
    }
    @Test void remindersValidateOwnershipAndCascadeDeletion() throws Exception {
        String owner = token(register()), outsider = token(register());
        long course = course(owner), topic = topic(owner, course), task = task(owner, topic);
        Map<String,Object> payload = Map.of("reminderTime", Instant.now().plusSeconds(3600).toString(), "taskIds", List.of(task));
        request(post("/api/v1/reminders"), outsider, payload).andExpect(status().isForbidden());
        request(post("/api/v1/reminders"), owner, Map.of("reminderTime", Instant.now().minusSeconds(60).toString(), "taskIds", List.of(task))).andExpect(status().isBadRequest());
        long reminder = body(request(post("/api/v1/reminders"), owner, payload).andExpect(status().isCreated()).andReturn()).path("id").asLong();
        request(get("/api/v1/reminders/"+reminder), outsider, null).andExpect(status().isForbidden());
        request(delete("/api/v1/courses/"+course), owner, null).andExpect(status().isNoContent());
        request(get("/api/v1/reminders/"+reminder), owner, null).andExpect(status().isNotFound());
        request(get("/api/v1/tasks/"+task), owner, null).andExpect(status().isNotFound());
    }
    @Test void adminCategoriesRolesAndRestrictions() throws Exception {
        JsonNode auth = register();
        var admin = users.findById(auth.path("user").path("id").asLong()).orElseThrow();
        admin.setRole(User.Role.ROLE_ADMIN);
        users.save(admin);
        String token = token(auth);
        long category = body(request(post("/api/v1/categories"), token, Map.of("name", "Categoría "+UUID.randomUUID())).andExpect(status().isCreated()).andReturn()).path("id").asLong();
        request(put("/api/v1/categories/"+category), token, Map.of("name", "Cambio "+UUID.randomUUID())).andExpect(status().isOk());
        request(get("/api/v1/users"), token, null).andExpect(status().isOk());
        request(delete("/api/v1/categories/"+category), token, null).andExpect(status().isNoContent());
        long course = course(token);
        long usedCategory = categories.findAll().get(0).getId();
        request(delete("/api/v1/categories/"+usedCategory), token, null).andExpect(status().isConflict());
        request(get("/api/v1/courses?search=Álgebra&size=1"), token, null).andExpect(jsonPath("size").value(1));
        request(delete("/api/v1/courses/"+course), token, null).andExpect(status().isNoContent());
    }
    @Test void reminderDeliveryRetriesWithoutMarkingFailureAsSent() throws Exception {
        String token = token(register());
        long task = task(token, topic(token, course(token)));
        long id = body(request(post("/api/v1/reminders"), token, Map.of("reminderTime", Instant.now().plusSeconds(100).toString(), "taskIds", List.of(task))).andReturn()).path("id").asLong();
        var reminder = reminders.findById(id).orElseThrow();
        reminder.setReminderTime(Instant.now().minusSeconds(5));
        reminders.save(reminder);
        doThrow(new EmailDeliveryException("SMTP no disponible")).when(email).send(anyString(), eq("Tareas pendientes"), anyString());
        delivery.deliver(id);
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> assertEquals(1, reminders.findById(id).orElseThrow().getAttempts()));
        assertFalse(reminders.findById(id).orElseThrow().isSent());
        doNothing().when(email).send(anyString(), eq("Tareas pendientes"), anyString());
        reminder = reminders.findById(id).orElseThrow();
        reminder.setLastAttempt(Instant.now().minusSeconds(301));
        reminders.save(reminder);
        delivery.deliver(id);
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> assertTrue(reminders.findById(id).orElseThrow().isSent()));
        delivery.deliver(id);
        verify(email, timeout(2000).times(2)).send(anyString(), eq("Tareas pendientes"), anyString());
    }
}
