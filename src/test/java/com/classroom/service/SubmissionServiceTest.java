package com.classroom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.classroom.entity.AssignmentStatus;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.SubmissionRepository;
import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private TeacherStudentRepository teacherStudentRepository;

    @InjectMocks
    private SubmissionService submissionService;

    @Test
    void shouldSubmitTextAssignment() {

        User student = new User();
        student.setTelegramId(1248264762L);

        Assignment assignment = new Assignment();
        assignment.setStatus(AssignmentStatus.WAITING_FOR_SUBMISSION);

        TeacherStudent relation = new TeacherStudent();

        // MOCK USER
        when(userRepository.findByTelegramId(1248264762L))
                .thenReturn(Optional.of(student));

        // MOCK ASSIGNMENT
        when(
                assignmentRepository
                        .findTopByStudentAndStatusOrderByIdDesc(
                                student,
                                AssignmentStatus.WAITING_FOR_SUBMISSION
                        )
        ).thenReturn(Optional.of(assignment));

        // MOCK RELATION
        when(teacherStudentRepository.findByStudent(student))
                .thenReturn(Optional.of(relation));

        TeacherStudent result =
                submissionService.submitTextAssignment(
                        1248264762L,
                        "Java Notes Completed"
                );

        assertNotNull(result);

        assertEquals(
                AssignmentStatus.COMPLETED,
                assignment.getStatus()
        );

        verify(submissionRepository)
                .save(org.mockito.ArgumentMatchers.any());

        verify(assignmentRepository)
                .save(assignment);
    }
}