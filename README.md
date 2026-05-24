# Classroom Companion Bot 🎓

A Telegram-based Classroom Management System built using **Spring Boot**, **MySQL**, **Hibernate/JPA**, and **Telegram Bot API**.

The system helps teachers assign work, monitor student progress, receive submissions, and automatically notify users through Telegram.

---

# Features

## Teacher Features
- Teacher Registration
- Invite Students using Invite Code
- Assign Tasks to Students
- Receive Student Progress Updates
- Receive Assignment Submissions
- Receive Text / Image / PDF Work
- Pending Assignment Reminder

## Student Features
- Register using Invite Code
- Receive Assignments in Telegram
- Update Assignment Progress
- Mark Assignment as Completed
- Submit Text Assignments
- Submit Images and Documents

---

# Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Backend Development |
| Spring Boot | Application Framework |
| Spring Data JPA | Database Operations |
| Hibernate | ORM |
| MySQL | Database |
| Telegram Bot API | Communication |
| Maven | Dependency Management |
| JUnit 5 | Unit Testing |
| Mockito | Mock Testing |

---

# System Architecture

```text
Teacher
   │
   ▼
Telegram Bot
   │
   ▼
Spring Boot Backend
   │
   ├── User Service
   ├── Assignment Service
   ├── Progress Service
   ├── Submission Service
   │
   ▼
MySQL Database
   │
   ├── users
   ├── teacher_student
   ├── assignments
   ├── assignment_progress
   └── submissions

                   ┌────────────────┐
                │    Teacher     │
                └────────┬───────┘
                         │
                    Assign Task
                         │
                         ▼
              ┌──────────────────┐
              │ Telegram Bot API │
              └────────┬─────────┘
                       │
                       ▼
             ┌────────────────────┐
             │ Spring Boot Server │
             └────────┬───────────┘
                      │
      ┌───────────────┼───────────────┐
      ▼               ▼               ▼
 User Service   Assignment      Progress
                   Service        Service
                                       │
                                       ▼
                               Submission Service
                                       │
                                       ▼
                              ┌────────────────┐
                              │ MySQL Database │
                              └────────────────┘
Project Workflow
Teacher Registration
/teacher

Teacher gets registered and receives an invite code.

Student Registration
/student INVITE_CODE

Student joins teacher classroom.

Assignment Creation

Teacher sends:

Assign STUDENT_NAME Task Description

Student receives assignment automatically.

Student Progress Update

Student sends:

completed

Teacher receives notification.

Assignment status becomes:

WAITING_FOR_SUBMISSION
Assignment Submission

Student submits:

Text
Image
PDF / Document

Teacher automatically receives submission.

Assignment status becomes:

COMPLETED
Setup Steps
1. Clone Repository
git clone YOUR_REPOSITORY_LINK
2. Configure Environment Variables

Create a .env file in project root.

Example:

DB_USERNAME=root
DB_PASSWORD=your_password

BOT_USERNAME=your_bot_username
BOT_TOKEN=your_bot_token
3. Configure Database

Create MySQL database:

CREATE DATABASE classroom_companion;
4. Install Dependencies
mvn clean install
5. Run Project
mvn spring-boot:run

Application runs on:

http://localhost:8081
Database Schema
users

Stores teacher and student information.

teacher_student

Stores teacher-student relationship and invite code.

assignments

Stores assignment details and status.

assignment_progress

Stores progress updates from students.

submissions

Stores submitted assignment details.

Prompt Strategy

This project currently uses command-based interaction through Telegram.

Teacher Commands
/teacher
Assign STUDENT_NAME TASK
Student Commands
/student CODE
completed
Submission Inputs
Text
Image
PDF
Document

The bot processes user messages and routes them to corresponding service classes.

Bot Design

The Telegram bot acts as an intelligent classroom assistant.

Main Components
ClassroomBot

Handles Telegram updates.

UserService

Manages teacher and student registration.

AssignmentService

Creates and assigns tasks.

ProgressService

Tracks student progress.

SubmissionService

Handles assignment submissions.

Testing

Implemented unit testing using:

JUnit 5
Mockito
Tested Services
UserServiceTest
AssignmentServiceTest
ProgressServiceTest
SubmissionServiceTest
Known Limitations
No web dashboard yet
File contents are not stored in MySQL (only Telegram file IDs)
No AI-based assignment evaluation
No analytics dashboard
Single classroom workflow per invite relationship
Requires Telegram account
Future Improvements
Teacher Dashboard
Student Dashboard
Real File Storage in Database/Cloud
AI Assignment Evaluation
Smart Reminder System
Assignment Analytics
Author

Ajay Sajja

Built as a Classroom Management Automation Project using Spring Boot and Telegram Bot API.
