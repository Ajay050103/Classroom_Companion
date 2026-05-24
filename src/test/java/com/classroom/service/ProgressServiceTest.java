package com.classroom.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.classroom.entity.Assignment;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.repository.AssignmentProgressRepository;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

    @Mock
    private UserRepository
            userRepository;

    @Mock
    private AssignmentRepository
            assignmentRepository;

    @Mock
    private TeacherStudentRepository
            teacherStudentRepository;

    @Mock
    private AssignmentProgressRepository
            progressRepository;

    @InjectMocks
    private ProgressService
            progressService;

    @Test
    void shouldProcessStudentProgress() {

        User student =
                new User();

        student.setTelegramId(
                1248264762L
        );

        student.setName(
                "Ajaysajja"
        );

        Assignment assignment =
                new Assignment();

        assignment.setId(
                1L
        );

        TeacherStudent relation =
                new TeacherStudent();

        // MOCK STUDENT
        when(
                userRepository
                        .findByTelegramId(
                                1248264762L
                        )
        ).thenReturn(
                Optional.of(
                        student
                )
        );

        // MOCK ASSIGNMENT
        when(
                assignmentRepository
                        .findTopByStudentOrderByIdDesc(
                                student
                        )
        ).thenReturn(
                Optional.of(
                        assignment
                )
        );

        // MOCK RELATION
        when(
                teacherStudentRepository
                        .findByStudent(
                                student
                        )
        ).thenReturn(
                Optional.of(
                        relation
                )
        );

        TeacherStudent result =
                progressService
                        .processStudentProgress(
                                1248264762L,
                                "completed"
                        );

        assertNotNull(
                result
        );
        verify(progressRepository)
        .save(
                org.mockito.ArgumentMatchers.any()
        );
    }

}