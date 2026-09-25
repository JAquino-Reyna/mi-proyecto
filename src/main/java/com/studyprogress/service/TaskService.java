package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import com.studyprogress.repository.*;
import com.studyprogress.exception.InvalidOperationException;
import com.studyprogress.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository tasks;
    private final ReminderRepository reminders;
    private final CourseAccessService access;
    private final StudyMapper mapper;
    private final ProgressEventService progress;
    public List<TaskResponseDTO> list(Long topicId) { return access.topic(topicId).getTasks().stream().map(mapper::task).toList(); }
    public TaskResponseDTO get(Long id) { return mapper.task(access.task(id)); }
    public TaskResponseDTO create(Long topicId, TaskRequestDTO request) {
        Topic topic = access.topic(topicId);
        Task task = new Task();
        task.setTopic(topic);
        apply(task, request);
        tasks.save(task);
        topic.getTasks().add(task);
        topic.setCompleted(false);
        progress.changed(topic.getCourse());
        return mapper.task(task);
    }
    public TaskResponseDTO update(Long id, TaskRequestDTO request) {
        Task task = access.task(id);
        apply(task, request);
        return mapper.task(task);
    }
    public TaskResponseDTO updateProgress(Long id, TaskProgressDTO request) {
        Task task = access.task(id);
        task.setCompleted(request.completed());
        task.setStudyMinutes(request.studyMinutes());
        progress.changed(task.getTopic().getCourse());
        return mapper.task(task);
    }
    public void delete(Long id) {
        Task task = access.task(id);
        detachReminders(task);
        task.getTopic().getTasks().remove(task);
        progress.changed(task.getTopic().getCourse());
        tasks.delete(task);
    }
    public void detachReminders(Task task) {
        for (Reminder reminder : new HashSet<>(task.getReminders())) {
            reminder.getTasks().remove(task);
            if (reminder.getTasks().isEmpty()) reminders.delete(reminder);
        }
        task.getReminders().clear();
    }
    private void apply(Task task, TaskRequestDTO request) {
        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
    }
}
