package com.classroom.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.classroom.entity.Assignment;
import com.classroom.entity.AssignmentStatus;
import com.classroom.entity.User;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.UserRepository;

@Service
public class AssignmentService {

    private final AssignmentRepository
            assignmentRepository;

    private final UserRepository
            userRepository;

    public AssignmentService(
            AssignmentRepository
                    assignmentRepository,
            UserRepository userRepository
    ) {

        this.assignmentRepository =
                assignmentRepository;

        this.userRepository =
                userRepository;
    }

    public User createAssignment(
            Long teacherTelegramId,
            String studentName,
            String task
    ) {

        User teacher =
                userRepository
                        .findByTelegramId(
                                teacherTelegramId
                        )
                        .orElse(null);

        User student =
                userRepository
                        .findByName(
                                studentName
                        );

        if(student == null
                || teacher == null) {

            return null;
        }

        Assignment assignment =
                new Assignment();

        assignment.setTitle(
                "Assignment"
        );

        assignment.setDescription(
                task
        );

        assignment.setStatus(
                AssignmentStatus.PENDING
        );

        assignment.setTeacher(
                teacher
        );

        assignment.setStudent(
                student
        );

        assignment.setDueDate(
                LocalDateTime.now()
                        .plusDays(3)
        );

        assignment.setCreatedAt(
                LocalDateTime.now()
        );

        assignmentRepository
                .save(assignment);

        return student;
    }
}