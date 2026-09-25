package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import com.studyprogress.repository.*;
import com.studyprogress.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService {
    private final CourseRepository courses;
    private final TaskRepository tasks;
    private final UserRepository users;
    private final StatisticRepository statistics;
    private final CurrentUserService current;
    private final StudyMapper mapper;
    public DashboardResponseDTO dashboard() {
        var available = courses.findAccessible(current.get().getId());
        var allTasks = available.stream().flatMap(c -> c.getTopics().stream()).flatMap(t -> t.getTasks().stream()).toList();
        long complete = allTasks.stream().filter(Task::isCompleted).count();
        long minutes = allTasks.stream().mapToLong(Task::getStudyMinutes).sum();
        double progress = available.stream().mapToDouble(mapper::progress).average().orElse(0);
        return new DashboardResponseDTO(available.size(), allTasks.size(), complete, minutes, Math.round(progress * 100) / 100.0, available.stream().map(mapper::course).toList());
    }
    public Page<StatisticResponseDTO> history(Pageable page) {
        return statistics.findByUserIdOrderByRecordedAtDesc(current.get().getId(), page).map(mapper::statistic);
    }
    @Transactional
    public void snapshot(Long userId) {
        users.findById(userId).ifPresent(user -> {
            var owned = tasks.findOwned(userId);
            Statistic statistic = new Statistic();
            statistic.setUser(user);
            statistic.setTotalTasks(owned.size());
            statistic.setTasksCompletedCount((int) owned.stream().filter(Task::isCompleted).count());
            statistic.setStudyMinutes(owned.stream().mapToInt(Task::getStudyMinutes).sum());
            statistics.save(statistic);
        });
    }
}
