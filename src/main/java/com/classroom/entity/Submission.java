package com.classroom.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(
            strategy =
            GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "assignment_id"
    )
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(
            name = "student_id"
    )
    private User student;

    @Column(
            columnDefinition = "TEXT"
    )
    private String submissionText;

    private String fileId;

    private String fileType;

    private LocalDateTime submittedAt;

    @Enumerated(
            EnumType.STRING
    )
    private SubmissionStatus status;

    @Column(
            columnDefinition = "TEXT"
    )
    private String feedback;
}