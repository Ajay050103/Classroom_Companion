package com.classroom.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classroom.entity.Assignment;
import com.classroom.entity.AssignmentStatus;
import com.classroom.entity.Submission;
import com.classroom.entity.SubmissionStatus;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.repository.AssignmentRepository;
import com.classroom.repository.SubmissionRepository;
import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@Service
public class SubmissionService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final TeacherStudentRepository teacherStudentRepository;

    public SubmissionService(
            UserRepository userRepository,
            AssignmentRepository assignmentRepository,
            SubmissionRepository submissionRepository,
            TeacherStudentRepository teacherStudentRepository
    ) {

        this.userRepository =
                userRepository;

        this.assignmentRepository =
                assignmentRepository;

        this.submissionRepository =
                submissionRepository;

        this.teacherStudentRepository =
                teacherStudentRepository;
    }

    @Transactional
    public TeacherStudent
    submitTextAssignment(
            Long telegramId,
            String submissionText
    ) {

        User student =
                userRepository
                        .findByTelegramId(
                                telegramId
                        )
                        .orElse(null);

        if(student == null) {
            return null;
        }

        Optional<Assignment>
                optionalAssignment =
                assignmentRepository
                        .findTopByStudentAndStatusOrderByIdDesc(
                                student,
                                AssignmentStatus
                                        .WAITING_FOR_SUBMISSION
                        );

        if(optionalAssignment.isEmpty()) {
            return null;
        }

        Assignment assignment =
                optionalAssignment.get();

        Submission submission =
                new Submission();

        submission.setStudent(
                student
        );

        submission.setAssignment(
                assignment
        );

        submission.setSubmissionText(
                submissionText
        );

        submission.setSubmittedAt(
                LocalDateTime.now()
        );

        submission.setStatus(
                SubmissionStatus.SUBMITTED
        );

        submissionRepository
                .save(submission);

        assignment.setStatus(
                AssignmentStatus.COMPLETED
        );

        assignmentRepository
                .save(assignment);

        return teacherStudentRepository
                .findByStudent(
                        student
                )
                .orElse(null);
    }
    @Transactional
    public TeacherStudent
    submitFileAssignment(
            Long telegramId,
            String fileId,
            String fileType
    ) {

        User student =
                userRepository
                        .findByTelegramId(
                                telegramId
                        )
                        .orElse(null);

        if(student == null) {
            return null;
        }

        Optional<Assignment>
                optionalAssignment =
                assignmentRepository
                        .findTopByStudentAndStatusOrderByIdDesc(
                                student,
                                AssignmentStatus
                                        .WAITING_FOR_SUBMISSION
                        );

        if(optionalAssignment.isEmpty()) {
            return null;
        }

        Assignment assignment =
                optionalAssignment.get();

        Submission submission =
                new Submission();

        submission.setStudent(
                student
        );

        submission.setAssignment(
                assignment
        );

        submission.setFileId(
                fileId
        );

        submission.setFileType(
                fileType
        );

        submission.setSubmittedAt(
                LocalDateTime.now()
        );

        submission.setStatus(
                SubmissionStatus.SUBMITTED
        );

        submissionRepository
                .save(submission);

        assignment.setStatus(
                AssignmentStatus.COMPLETED
        );

        assignmentRepository
                .save(assignment);

        return teacherStudentRepository
                .findByStudent(
                        student
                )
                .orElse(null);
    }
}