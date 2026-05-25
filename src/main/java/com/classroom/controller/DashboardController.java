package com.classroom.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.classroom.entity.Assignment;
import com.classroom.repository.AssignmentRepository;

@Controller
public class DashboardController {

    private final AssignmentRepository
            assignmentRepository;

    public DashboardController(
            AssignmentRepository assignmentRepository
    ) {
        this.assignmentRepository =
                assignmentRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Model model
    ) {
    	List<Assignment> assignments = assignmentRepository.findAll();
    	model.addAttribute("assignments", assignments);
        return "dashboard";
    }
}