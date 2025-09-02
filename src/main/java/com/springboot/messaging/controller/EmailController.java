package com.springboot.messaging.controller;
import com.springboot.messaging.Entity.User;
import com.springboot.messaging.service.UserService;
import com.springboot.messaging.Entity.Email;
import com.springboot.messaging.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @GetMapping("/compose")
    public String showComposeEmail() {
        return "compose";
    }

    @GetMapping("")
    public String showEmailsPage(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return "error";
        }
        User user = userOpt.get();
        List<Email> inboxEmails = emailService.getInbox(user.getId());
        for (Email email : inboxEmails) {
            System.out.println("Email ID: " + email.getId() +
                    " Subject: " + email.getSubject());
        }

        model.addAttribute("emails", inboxEmails);
        return "emails";
    }


    @PostMapping("/send")
    public String sendEmail(
            @RequestParam("receiverEmail") String receiverEmail,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            Principal principal) {

        String sender = principal.getName();
        System.out.println("🔍 Principal name: " + sender);

        emailService.sendEmail(sender, receiverEmail, subject, body);

        return "redirect:/emails/sent";
    }


    @GetMapping("/sent")
    public String showSentEmailsPage(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("Principal email: " + userEmail);

        Optional<User> userOpt = userService.findByUsername(userEmail);
        if (userOpt.isEmpty()) {
            System.out.println("No user found for email " + userEmail);
            return "error";
        }

        User sender = userOpt.get();
        System.out.println("User found: " + sender);

        Long senderId = sender.getId();

        List<Email> sentEmails = emailService.getSentEmails(senderId);
        System.out.println("Sent emails count: " + (sentEmails == null ? "null" : sentEmails.size()));

        model.addAttribute("sentEmails", sentEmails);

        return "sentEmails";
    }


    @GetMapping("/view/{id}")
    public String viewEmail(@PathVariable Long id, Model model, Principal principal) {
        System.out.println("Viewing email with id: " + id);

        Optional<Email> emailOpt = emailService.findById(id);
        if (emailOpt.isEmpty()) {
            System.out.println("No email found with id: " + id);
            return "error";
        }

        Email email = emailOpt.get();
        System.out.println("Email subject: " + email.getSubject());

        if (principal == null) {
            System.out.println("Principal is null!");
            return "error";
        }

        String loggedInUsername = principal.getName();

        if (!email.getSender().getUsername().equals(loggedInUsername) &&
                !email.getReceiver().getUsername().equals(loggedInUsername)) {
            return "error";
        }

        System.out.println("Principal name: " + principal.getName());
        model.addAttribute("email", email);
        return "emailDetails";
    }
}
