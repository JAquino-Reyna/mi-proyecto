package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import com.studyprogress.repository.*;
import com.studyprogress.exception.*;
import com.studyprogress.mapper.StudyMapper;
import com.studyprogress.event.CourseSharedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courses;
    private final CategoryRepository categories;
    private final UserRepository users;
    private final CurrentUserService current;
    private final CourseAccessService access;
    private final StudyMapper mapper;
    private final ApplicationEventPublisher events;
    private final TaskService tasks;
    private final ProgressEventService progress;
    public Page<CourseResponseDTO> list(String search, Pageable page) {
        return courses.findAccessible(current.get().getId(), search, page).map(mapper::course);
    }
    public CourseDetailDTO get(Long id) { return mapper.detail(access.course(id)); }
    public CourseResponseDTO create(CourseRequestDTO request) {
        Course course = new Course();
        course.setUser(current.get());
        apply(course, request);
        return mapper.course(courses.save(course));
    }
    public CourseResponseDTO update(Long id, CourseRequestDTO request) {
        Course course = access.owned(id);
        apply(course, request);
        return mapper.course(course);
    }
    public void delete(Long id) {
        Course course = access.owned(id);
        course.getTopics().forEach(topic -> topic.getTasks().forEach(tasks::detachReminders));
        courses.delete(course);
        progress.changed(course);
    }
    public ShareResponseDTO share(Long id, ShareRequestDTO request) {
        Course course = access.owned(id);
        course.setPublicAccess(request.enabled());
        course.setShareCode(request.enabled() ? UUID.randomUUID().toString() : null);
        return new ShareResponseDTO(course.getShareCode(), request.enabled() ? "/api/v1/public/courses/" + course.getShareCode() : null);
    }
    public PublicCourseDTO publicCourse(String code) {
        return mapper.publicCourse(courses.findByShareCodeAndPublicAccessTrue(code).orElseThrow(() -> new ResourceNotFoundException("Enlace no disponible")));
    }
    public CourseResponseDTO addCollaborator(Long id, CollaboratorRequestDTO request) {
        Course course = access.owned(id);
        User user = users.findById(request.userId()).filter(User::isEnabled).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (course.getUser().getId().equals(user.getId())) throw new InvalidOperationException("El propietario ya tiene acceso");
        if (course.getCollaborators().stream().anyMatch(u -> u.getId().equals(user.getId()))) throw new DuplicateResourceException("El usuario ya colabora en el curso");
        course.getCollaborators().add(user);
        events.publishEvent(new CourseSharedEvent(this, user.getEmail(), course.getTitle()));
        return mapper.course(course);
    }
    public void removeCollaborator(Long id, Long userId) {
        Course course = access.owned(id);
        if (!course.getCollaborators().removeIf(u -> u.getId().equals(userId))) throw new ResourceNotFoundException("Colaborador no encontrado");
    }
    private void apply(Course course, CourseRequestDTO request) {
        course.setTitle(request.title().trim());
        course.setDescription(request.description());
        course.setCategory(categories.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")));
    }
}
