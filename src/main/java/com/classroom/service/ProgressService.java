package com.classroom.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classroom.entity.Assignment;
import com.classroom.entity.AssignmentProgress;
import com.classroom.entity.AssignmentStatus;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.repository.AssignmentProgressRepository;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@Service
@Transactional
public class ProgressService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final TeacherStudentRepository teacherStudentRepository;
    private final AssignmentProgressRepository progressRepository;

    public ProgressService(
            UserRepository userRepository,
            AssignmentRepository assignmentRepository,
            TeacherStudentRepository teacherStudentRepository,
            AssignmentProgressRepository progressRepository) {

        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.teacherStudentRepository = teacherStudentRepository;
        this.progressRepository = progressRepository;
    }

    public TeacherStudent processStudentProgress(
            Long telegramId,
            String progressMessage) {

        System.out.println("PROCESS METHOD HIT");

        User student = userRepository.findByTelegramId(telegramId).orElse(null);

        if (student == null) return null;

        Optional<Assignment> optionalAssignment =
                assignmentRepository.findTopByStudentOrderByIdDesc(student);

        if (optionalAssignment.isEmpty()) return null;

        Assignment assignment = optionalAssignment.get();

        System.out.println("Assignment found: " + assignment.getId());

        AssignmentProgress progress = new AssignmentProgress();

        progress.setStudent(student);
        progress.setAssignment(assignment);
        progress.setMessage(progressMessage);
        progress.setCreatedAt(LocalDateTime.now());

        System.out.println("Saving progress: " + progressMessage);

        progressRepository.save(progress);

        System.out.println("Progress saved");

        if (progressMessage.equalsIgnoreCase("completed")) {
            assignment.setStatus(AssignmentStatus.WAITING_FOR_SUBMISSION);
            assignmentRepository.save(assignment);
        }

        return teacherStudentRepository.findByStudent(student).orElse(null);
    }
}