package com.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classroom.entity.Submission;

public interface SubmissionRepository
        extends JpaRepository<
        Submission,
        Long> {

}