package com.example.mineffortform.controller;

import com.example.mineffortform.model.Applicant;
import com.example.mineffortform.service.ApplicantService;
import com.example.mineffortform.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FormController {
    private static final String IMAGINE_THIS_BEING_A_REMOTE_CLOUD_STORAGE_SERVER = "dat\\";
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/submitApplication")
    public String submitApplication(@ModelAttribute Applicant applicant, @RequestParam("cv") MultipartFile file) {
        try {
            Applicant savedApplicant = applicantService.saveApplicant(applicant);

            Long id = savedApplicant.getId();
            String newFileName = applicant.getFirstName() + "_" + applicant.getLastName() + "_" + id + ".pdf";
            file.transferTo(new File(IMAGINE_THIS_BEING_A_REMOTE_CLOUD_STORAGE_SERVER + newFileName));

            String body = "Hello, " + applicant.getFirstName() + "!\n\n"
                    + "Thank you for your interest to join our Lazy Company team.\n"
                    + "We probably won't be getting back to you since we want an applicant with at least 3 years of experience for our junior positon!\n\n"
                    + "Best regards,\n"
                    + "Lazy Company";

            emailService.sendEmail(applicant.getEmail(), "Thank you for appyling to Lazy Company!", body);

            return "success";
        } catch (IOException e) {
            //e.printStackTrace();
            return "error";
        }
    }
}
