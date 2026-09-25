package com.studyprogress.service;

import com.studyprogress.dto.*;
import com.studyprogress.model.*;
import com.studyprogress.repository.ReminderRepository;
import com.studyprogress.exception.*;
import com.studyprogress.mapper.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReminderService {
    private final ReminderRepository reminders;
    private final CurrentUserService current;
    private final CourseAccessService access;
    private final StudyMapper mapper;
    public Page<ReminderResponseDTO> list(Pageable page) { return reminders.findByUserId(current.get().getId(), page).map(mapper::reminder); }
    public ReminderResponseDTO get(Long id) { return mapper.reminder(find(id)); }
    public ReminderResponseDTO create(ReminderRequestDTO request) {
        Reminder reminder = new Reminder();
        reminder.setUser(current.get());
        apply(reminder, request);
        return mapper.reminder(reminders.save(reminder));
    }
    public ReminderResponseDTO update(Long id, ReminderRequestDTO request) {
        Reminder reminder = find(id);
        if (reminder.isSent()) throw new InvalidReminderException("Un recordatorio enviado no se puede modificar");
        apply(reminder, request);
        reminder.setAttempts(0);
        reminder.setLastAttempt(null);
        return mapper.reminder(reminder);
    }
    public void delete(Long id) { reminders.delete(find(id)); }
    private Reminder find(Long id) {
        Reminder reminder = reminders.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recordatorio no encontrado"));
        if (!reminder.getUser().getId().equals(current.get().getId())) throw new ForbiddenOperationException("Este recordatorio pertenece a otro usuario");
        return reminder;
    }
    private void apply(Reminder reminder, ReminderRequestDTO request) {
        Set<Task> selected = new HashSet<>();
        for (Long taskId : request.taskIds()) {
            Task task = access.task(taskId);
            if (task.isCompleted()) throw new InvalidReminderException("No se pueden programar recordatorios para tareas completadas");
            selected.add(task);
        }
        reminder.setReminderTime(request.reminderTime());
        reminder.setTasks(selected);
    }
}
