package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import com.studyprogress.repository.TopicRepository;
import com.studyprogress.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopicService {
    private final TopicRepository topics;
    private final CourseAccessService access;
    private final TaskService tasks;
    private final StudyMapper mapper;
    private final ProgressEventService progress;
    public List<TopicResponseDTO> list(Long courseId) { return access.course(courseId).getTopics().stream().map(mapper::topic).toList(); }
    public TopicResponseDTO get(Long id) { return mapper.topic(access.topic(id)); }
    public TopicResponseDTO create(Long courseId, TopicRequestDTO request) {
        Course course = access.course(courseId);
        Topic topic = new Topic();
        topic.setCourse(course);
        apply(topic, request);
        topics.save(topic);
        course.getTopics().add(topic);
        progress.changed(course);
        return mapper.topic(topic);
    }
    public TopicResponseDTO update(Long id, TopicRequestDTO request) {
        Topic topic = access.topic(id);
        apply(topic, request);
        return mapper.topic(topic);
    }
    public TopicResponseDTO complete(Long id, TopicProgressDTO request) {
        Topic topic = access.topic(id);
        topic.setCompleted(request.completed());
        topic.getTasks().forEach(t -> t.setCompleted(request.completed()));
        progress.changed(topic.getCourse());
        return mapper.topic(topic);
    }
    public void delete(Long id) {
        Topic topic = access.topic(id);
        topic.getTasks().forEach(tasks::detachReminders);
        topic.getCourse().getTopics().remove(topic);
        topics.delete(topic);
        progress.changed(topic.getCourse());
    }
    private void apply(Topic topic, TopicRequestDTO request) {
        topic.setTitle(request.title().trim());
        topic.setOrderIndex(request.orderIndex());
    }
}
