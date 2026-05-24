package com.classroom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.classroom.entity.User;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    private AssignmentRepository
            assignmentRepository;

    @Mock
    private UserRepository
            userRepository;

    @InjectMocks
    private AssignmentService
            assignmentService;

    @Test
    void shouldCreateAssignment() {

        User teacher =
                new User();

        teacher.setTelegramId(
                1694833289L
        );

        User student =
                new User();

        student.setName(
                "Ajaysajja"
        );

        student.setTelegramId(
                1248264762L
        );

        // MOCK TEACHER
        when(
                userRepository
                        .findByTelegramId(
                                1694833289L
                        )
        ).thenReturn(
                Optional.of(
                        teacher
                )
        );

        // MOCK STUDENT
        when(
                userRepository
                        .findByName(
                                "Ajaysajja"
                        )
        ).thenReturn(
                student
        );

        User result =
                assignmentService
                        .createAssignment(
                                1694833289L,
                                "Ajaysajja",
                                "Java Notes"
                        );

        assertEquals(
                "Ajaysajja",
                result.getName()
        );
    }

    @Test
    void shouldReturnNullIfStudentNotFound() {

        when(
                userRepository
                        .findByName(
                                "Unknown"
                        )
        ).thenReturn(
                null
        );

        User result =
                assignmentService
                        .createAssignment(
                                1694833289L,
                                "Unknown",
                                "Java Notes"
                        );

        assertEquals(
                null,
                result
        );
    }
}