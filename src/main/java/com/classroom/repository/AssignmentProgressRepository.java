package com.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classroom.entity.AssignmentProgress;

public interface AssignmentProgressRepository extends JpaRepository<AssignmentProgress,Long> {

}