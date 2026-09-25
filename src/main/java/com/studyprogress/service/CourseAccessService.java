package com.studyprogress.service;

import com.studyprogress.model.*;
import com.studyprogress.repository.*;
import com.studyprogress.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseAccessService {
    private final CurrentUserService current;
    private final CourseRepository courses;
    private final TopicRepository topics;
    private final TaskRepository tasks;
    public Course course(Long id) {
        Course course = courses.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        User user = current.get();
        if (!owner(course, user) && course.getCollaborators().stream().noneMatch(u -> u.getId().equals(user.getId()))) throw new ForbiddenOperationException("No tienes acceso a este curso");
        return course;
    }
    public Course owned(Long id) {
        Course course = course(id);
        if (!owner(course, current.get())) throw new ForbiddenOperationException("Solo el propietario puede realizar esta operación");
        return course;
    }
    public Topic topic(Long id) {
        Topic topic = topics.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado"));
        course(topic.getCourse().getId());
        return topic;
    }
    public Task task(Long id) {
        Task task = tasks.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
        course(task.getTopic().getCourse().getId());
        return task;
    }
    private boolean owner(Course course, User user) { return course.getUser().getId().equals(user.getId()); }
}
