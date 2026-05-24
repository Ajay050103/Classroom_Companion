package com.classroom.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.classroom.bot.ClassroomBot;
import com.classroom.entity.Assignment;
import com.classroom.entity.AssignmentStatus;
import com.classroom.repository.AssignmentRepository;

@Component
public class ReminderScheduler {

    private final AssignmentRepository
            assignmentRepository;

    private final ClassroomBot
            classroomBot;

    public ReminderScheduler(
            AssignmentRepository assignmentRepository,
            ClassroomBot classroomBot
    ) {

        this.assignmentRepository =
                assignmentRepository;

        this.classroomBot =
                classroomBot;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendReminders() {

        System.out.println(
                "Checking assignments..."
        );

        List<Assignment>
                assignments =
                assignmentRepository
                        .findByStatus(
                                AssignmentStatus
                                        .PENDING
                        );

        for(Assignment assignment
                : assignments) {

            try {

                long daysLeft =
                        java.time.Duration
                                .between(
                                        LocalDateTime
                                                .now(),
                                        assignment
                                                .getDueDate()
                                )
                                .toDays();

                String reminder =
                        "";

                if(daysLeft == 2) {

                    reminder =
                            """
                            ⏰ Reminder

                            You have a pending assignment.

                            Task:
                            """
                            +
                            assignment
                                    .getDescription()

                            +

                            """

                            Due in
                            """
                            +
                            daysLeft
                            +
                            " days.";
                }

                else if(daysLeft == 1) {

                    reminder =
                            """
                            🚨 Urgent Reminder

                            Assignment deadline
                            is tomorrow!

                            Task:
                            """
                            +
                            assignment
                                    .getDescription();
                }

                else if(daysLeft <= 0) {

                    reminder =
                            """
                            ⚠️ Assignment due today!

                            Task:
                            """
                            +
                            assignment
                                    .getDescription()

                            +

                            """

                            Submit immediately.
                            """;
                }

                if(!reminder.isEmpty()) {

                    SendMessage
                            message =
                            new SendMessage();

                    message.setChatId(
                            assignment
                                    .getStudent()
                                    .getTelegramId()
                                    .toString()
                    );

                    message.setText(
                            reminder
                    );

                    classroomBot
                            .execute(message);

                    System.out.println(
                            "Reminder sent"
                    );
                }

            } catch(Exception e) {

                e.printStackTrace();
            }
        }
    }
}