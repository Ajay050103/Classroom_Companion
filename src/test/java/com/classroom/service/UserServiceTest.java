package com.classroom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.classroom.repository.TeacherStudentRepository;
import com.classroom.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeacherStudentRepository teacherStudentRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterTeacher() {

        Long chatId = 123456L;
        String username = "ajay";
        String name = "Ajay";

        when(userRepository.findByTelegramId(chatId))
                .thenReturn(Optional.empty());

        String response =
                userService.registerTeacher(
                        chatId,
                        username,
                        name
                );

        assertEquals(
                true,
                response.contains("Teacher Registered")
        );
    }
}