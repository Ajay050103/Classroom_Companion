package com.classroom.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.classroom.entity.Role;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeacherStudentRepository teacherStudentRepository;

    public UserService(
            UserRepository userRepository,
            TeacherStudentRepository teacherStudentRepository) {

        this.userRepository = userRepository;
        this.teacherStudentRepository = teacherStudentRepository;
    }

    public String registerTeacher(
            Long telegramId,
            String username,
            String name) {

        Optional<User> existingUser =
                userRepository.findByTelegramId(telegramId);

        if (existingUser.isPresent()) {
            return "You are already registered.";
        }

        User teacher = new User();

        teacher.setTelegramId(telegramId);
        teacher.setUsername(username);
        teacher.setName(name);
        teacher.setRole(Role.TEACHER);
        teacher.setCreatedAt(LocalDateTime.now());

        userRepository.save(teacher);

        String inviteCode = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        TeacherStudent ts = new TeacherStudent();

        ts.setTeacher(teacher);
        ts.setInviteCode(inviteCode);

        teacherStudentRepository.save(ts);

        return """
                Teacher Registered 

                Invite Code:
                """ + inviteCode;
    }

    public String registerStudent(
            Long telegramId,
            String username,
            String name,
            String inviteCode) {

        TeacherStudent teacherStudent =
                teacherStudentRepository
                        .findByInviteCode(inviteCode)
                        .orElse(null);

        if (teacherStudent == null) {
            return "Invalid Invite Code ";
        }

        User existingUser =
                userRepository.findByTelegramId(telegramId)
                        .orElse(null);

        if (existingUser != null) {
            return "You are already registered.";
        }

        User student = new User();

        student.setTelegramId(telegramId);
        student.setUsername(username);
        student.setName(name);
        student.setRole(Role.STUDENT);
        student.setCreatedAt(LocalDateTime.now());

        userRepository.save(student);

        teacherStudent.setStudent(student);

        teacherStudentRepository.save(teacherStudent);

        return """
                Successfully linked
                with teacher 
                """;
    }
}