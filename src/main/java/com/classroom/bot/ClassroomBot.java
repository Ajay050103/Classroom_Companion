package com.classroom.bot;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.classroom.config.BotConfig;
import com.classroom.entity.TeacherStudent;
import com.classroom.entity.User;
import com.classroom.service.AssignmentService;
import com.classroom.service.ProgressService;
import com.classroom.service.SubmissionService;
import com.classroom.service.UserService;

@Component
public class ClassroomBot extends TelegramLongPollingBot {

	private final BotConfig botConfig;
	private final UserService userService;
	private final AssignmentService assignmentService;
	private final ProgressService progressService;
	private final SubmissionService submissionService;

	public ClassroomBot(BotConfig botConfig, UserService userService, AssignmentService assignmentService,
			ProgressService progressService, SubmissionService submissionService

	) {
		this.botConfig = botConfig;
		this.userService = userService;
		this.assignmentService = assignmentService;
		this.progressService = progressService;
		this.submissionService = submissionService;

		System.out.println("Telegram Bot Bean Created");
	}

	@Override
	public String getBotUsername() {
		return botConfig.getBotUsername();
	}

	@Override
	public String getBotToken() {
		return botConfig.getBotToken();
	}

	@Override
	public void onUpdateReceived(Update update) {

		System.out.println("Message received");

		if (update.hasMessage()) {

			String text = "";

			if (update.getMessage().hasText()) {

				text = update.getMessage().getText();
			}

			Long chatId = update.getMessage().getChatId();

			String username = update.getMessage().getFrom().getUserName();

			String name = update.getMessage().getFrom().getFirstName();

			// START COMMAND
			if (text.equals("/start")) {

				sendMessage(chatId, """
						Welcome to Classroom Companion 

						Choose role:
						/teacher
						/student CODE
						""");
			}

			// TEACHER REGISTRATION
			else if (text.equals("/teacher")) {

				String response = userService.registerTeacher(chatId, username, name);

				sendMessage(chatId, response);
			}
			// STUDENT REGISTRATION
			else if (text.startsWith("/student")) {

				String trimmedText = text.trim();

				String[] split = trimmedText.split("\\s+");

				if (split.length != 2) {

					sendMessage(chatId, """
							Please use:
							/student INVITE_CODE
							""");

					return;
				}

				String inviteCode = split[1].trim();

				String response = userService.registerStudent(chatId, username, name, inviteCode);

				sendMessage(chatId, response);
			}
			// ASSIGNMENT CREATION
			else if (text.startsWith("Assign")) {

				String cleaned = text.replace("Assign", "").trim();

				String[] split = cleaned.split(" ", 2);

				if (split.length < 2) {
					sendMessage(chatId, """
							Format:
							Assign STUDENT_NAME TASK
							""");

					return;
				}

				String studentName = split[0];

				String task = split[1];

				User student = assignmentService.createAssignment(chatId, studentName, task);

				if (student == null) {

					sendMessage(chatId, "Student not found ");

					return;
				}

				// Reply to teacher
				sendMessage(chatId, "Assignment Created ");

				// Debug log
				System.out.println("Sending assignment to student: " + student.getTelegramId());

				// Send to student
				sendMessage(student.getTelegramId(), """
						 New Assignment

						Task:
						""" + task + """

						Status:
						PENDING
						""");
			}
			// FILE SUBMISSION
			else if (update.getMessage().hasDocument()) {

				Document document = update.getMessage().getDocument();

				TeacherStudent teacherStudent = submissionService.submitFileAssignment(chatId, document.getFileId(),
						"DOCUMENT");

				if (teacherStudent != null) {

					sendMessage(chatId, "File submitted successfully");

					try {

						SendDocument sendDocument = new SendDocument();

						sendDocument.setChatId(teacherStudent.getTeacher().getTelegramId().toString());

						sendDocument.setDocument(new InputFile(document.getFileId()));

						sendDocument.setCaption("Assignment submitted by " + name);

						execute(sendDocument);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// PHOTO SUBMISSION
			else if (update.getMessage().hasPhoto()) {

				List<PhotoSize> photos = update.getMessage().getPhoto();

				String fileId = photos.get(photos.size() - 1).getFileId();

				TeacherStudent teacherStudent = submissionService.submitFileAssignment(chatId, fileId, "PHOTO");

				if (teacherStudent != null) {

					sendMessage(chatId, "Photo submitted successfully");

					try {

						SendPhoto sendPhoto = new SendPhoto();

						sendPhoto.setChatId(teacherStudent.getTeacher().getTelegramId().toString());

						sendPhoto.setPhoto(new InputFile(fileId));

						sendPhoto.setCaption("Assignment image submitted by " + name);

						execute(sendPhoto);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				TeacherStudent submissionTeacher = submissionService.submitTextAssignment(chatId, text);
				if (submissionTeacher != null) {

					sendMessage(chatId, """
							Assignment submitted successfully
							""");

					sendMessage(submissionTeacher.getTeacher().getTelegramId(),"""
									New Assignment Submission
									Student:
									""" + name +

									"""

											Submission:
											""" + text);
					return;
				}
				TeacherStudent teacherStudent = progressService.processStudentProgress(chatId, text);

				if (teacherStudent != null) {

					if (text.equalsIgnoreCase("completed")) {

						sendMessage(chatId, """
								Assignment marked completed
								Please submit your assignment 
								Send your answer now.
								""");

						sendMessage(teacherStudent.getTeacher().getTelegramId(),

								"""
										Student completed assignment
										Student:
										""" + name);
					}

					else {

						sendMessage(teacherStudent.getTeacher().getTelegramId(),

								"""
										Student Progress Update

										Student:
										""" + name +

										"""

												Update:
												""" + text);
					}
				}
			}
		}
	}

	private void sendMessage(Long chatId, String text) {
		try {
			System.out.println("Sending message to chatId: " + chatId);
			SendMessage message = new SendMessage();
			message.setChatId(chatId.toString());
			message.setText(text);
			execute(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}