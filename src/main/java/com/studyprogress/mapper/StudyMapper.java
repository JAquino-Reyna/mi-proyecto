package com.studyprogress.mapper;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StudyMapper {
    public UserResponseDTO user(User u) { return new UserResponseDTO(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.isEnabled()); }
    public CategoryResponseDTO category(Category c) { return new CategoryResponseDTO(c.getId(), c.getName(), c.getDescription()); }
    public TaskResponseDTO task(Task t) {
        return new TaskResponseDTO(t.getId(), t.getTopic().getId(), t.getTitle(), t.getDescription(), t.getDueDate(), t.isCompleted(), t.getStudyMinutes());
    }
    public boolean completed(Topic t) {
        return t.getTasks().isEmpty() ? t.isCompleted() : t.getTasks().stream().allMatch(Task::isCompleted);
    }
    public double progress(Course c) {
        int total = 0;
        int complete = 0;
        for (Topic topic : c.getTopics()) {
            total += Math.max(1, topic.getTasks().size());
            complete += topic.getTasks().isEmpty() ? (topic.isCompleted() ? 1 : 0) : (int) topic.getTasks().stream().filter(Task::isCompleted).count();
        }
        return total == 0 ? 0 : Math.round(10000.0 * complete / total) / 100.0;
    }
    public CourseResponseDTO course(Course c) {
        return new CourseResponseDTO(c.getId(), c.getTitle(), c.getDescription(), c.getCategory().getId(), c.getUser().getId(), progress(c), c.isPublicAccess(), c.getCollaborators().stream().map(User::getId).sorted().toList());
    }
    public TopicResponseDTO topic(Topic t) {
        return new TopicResponseDTO(t.getId(), t.getCourse().getId(), t.getTitle(), t.getOrderIndex(), completed(t), t.getTasks().stream().map(this::task).toList());
    }
    public CourseDetailDTO detail(Course c) { return new CourseDetailDTO(course(c), c.getTopics().stream().map(this::topic).toList()); }
    public ReminderResponseDTO reminder(Reminder r) {
        return new ReminderResponseDTO(r.getId(), r.getReminderTime(), r.isSent(), r.getAttempts(), r.getTasks().stream().map(Task::getId).collect(Collectors.toSet()));
    }
    public StatisticResponseDTO statistic(Statistic s) {
        return new StatisticResponseDTO(s.getId(), s.getRecordedAt(), s.getTotalTasks(), s.getTasksCompletedCount(), s.getStudyMinutes());
    }
    public PublicCourseDTO publicCourse(Course c) {
        return new PublicCourseDTO(c.getTitle(), c.getDescription(), progress(c), c.getTopics().stream()
                .map(t -> new PublicTopicDTO(t.getTitle(), completed(t), t.getTasks().stream()
                        .map(task -> new PublicTaskDTO(task.getTitle(), task.isCompleted())).toList())).toList());
    }
}
