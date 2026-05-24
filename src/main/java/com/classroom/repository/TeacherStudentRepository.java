package com.classroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;

public interface TeacherStudentRepository
        extends JpaRepository<
        TeacherStudent,
        Long> {

    Optional<TeacherStudent> findByInviteCode(String code);
    Optional<TeacherStudent> findByStudent(User student);
}