package com.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classroom.entity.Assignment;
import com.classroom.entity.AssignmentStatus;
import com.classroom.entity.User;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
	
	Optional<Assignment> findTopByStudentAndStatusOrderByIdDesc(User student, AssignmentStatus status);
	Optional<Assignment> findTopByStudentOrderByIdDesc(User student);
	List<Assignment> findByStatus(AssignmentStatus status);
   
}