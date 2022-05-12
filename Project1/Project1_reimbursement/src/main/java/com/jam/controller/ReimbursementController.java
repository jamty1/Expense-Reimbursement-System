package com.jam.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jam.model.Reimbursement;
import com.jam.model.User;
import com.jam.model.UserType;
import com.jam.service.AuthService;
import com.jam.service.EmailService;
import com.jam.service.ReimbursementService;
import com.jam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * The API endpoint handling the reimbursement service.
 */
@RestController
@Slf4j
@RequestMapping("/api/reimbursement")
public class ReimbursementController {
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReimbursementService reimbursementService;
    @Autowired
    private UserService userService;

    /**
     * Gets all the reimbursement requests of the given user.
     * @param userid The id of the user.
     * @param apikey The API key of the user.
     * @return The HTTP response containing the list of reimbursements of the user.
     */
    @GetMapping(value = "/{id}")
    private ResponseEntity<List<Reimbursement>> getReimbursementRequests(
            @PathVariable("id") Long userid, @RequestHeader("Authorization") String apikey) {
        ResponseEntity<User> response = authService.authenticateUser(userid, apikey);
        if (!response.hasBody()) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
        User user = response.getBody();
        return ResponseEntity.ok(reimbursementService.getUserReimbursements(user));
    }

    /**
     * Submits a new reimbursement request from an employee.
     * @param userid The employee user that owns the reimbursement request.
     * @param reimbursement The reimbursement request information.
     * @param apikey The API key of the user.
     * @return The HTTP response containing the reimbursement request with its unique id and status.
     */
    @PostMapping(value = "/{id}")
    private ResponseEntity<Reimbursement> submitReimbursement(
            @PathVariable("id") Long userid,
            @Validated @RequestBody Reimbursement reimbursement,
            @RequestHeader("Authorization") String apikey) {
        ResponseEntity<User> response = authService.authenticateUser(userid, apikey);
        if (!response.hasBody()) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
        User user = response.getBody();
        if (user.isNotify()) {
            emailService.sendEmail(user.getEmail(),
                    "New Reimbursement Request Created",
                    "Your new reimbursement request is under pending review.");
        }
        return ResponseEntity.created(URI.create("/api/reimbursement/" + userid))
                .body(reimbursementService.submitReimbursementRequest(user, reimbursement));
    }

    /**
     * Updates the status of a reimbursement request.
     * @param r_id The unique id of the reimbursement request.
     * @param newStatus The new status and the id of the manager updating the status.
     * @param apikey The API key of the manager.
     * @return The HTTP response with the status of the update.
     */
    @PutMapping(value = "/{id}")
    private ResponseEntity<Void> changeReimbursementStatus(
            @PathVariable("id") Long r_id,
            @RequestBody ObjectNode newStatus,
            @RequestHeader("Authorization") String apikey) {
        String status = newStatus.get("status").asText();
        Long userid = newStatus.get("managerid").asLong();
        ResponseEntity<User> response = authService.authenticateUser(userid, apikey);
        if (!response.hasBody()) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
        User manager = response.getBody();
        // Check if user is manager
        if (!manager.getUsertype().equals(UserType.MANAGER)) {
            return ResponseEntity.status(401).build();
        }

        Reimbursement r;
        if (status.equals("approve")) {
            r = reimbursementService.approveReimbursement(r_id, manager);
            if (r.getUser().isNotify()) {
                emailService.sendEmail(r.getUser().getEmail(),
                        "Reimbursement request approved",
                        "Your reimbursement request has been approved.\n" +
                                "Details:\nID:" + r.getId() + "\nDescription: " + r.getDescription() +
                                "\nAmount: $" + r.getAmount());
            }
        } else if (status.equals("deny")) {
            r = reimbursementService.denyReimbursement(r_id, manager);
            if (r.getUser().isNotify()) {
                emailService.sendEmail(r.getUser().getEmail(),
                        "Reimbursement request denied",
                        "Your reimbursement request has been denied.\n" +
                                "Details:\nID: " + r.getId() + "\nDescription" + r.getDescription() +
                                "\nAmount: $" + r.getAmount());
            }
        } else if (status.equals("reassign")) {
            User old = reimbursementService.getReimbursement(r_id).getUser();
            Long newuserid = newStatus.get("userid").asLong();
            User newUser = userService.getUserById(newuserid);
            r = reimbursementService.reassignReimbursement(r_id, manager, newUser);
            if (old.isNotify()) {
                emailService.sendEmail(old.getEmail(),
                        "Reimbursement request reassigned",
                        "Your reimbursement request has been reassigned to " + newUser.getName() +
                                "\nDetails:\nID: " + r.getId() + "\nDescription: " + r.getDescription() +
                                "\nAmount: $" + r.getAmount());
            }
            if (newUser.isNotify()) {
                emailService.sendEmail(newUser.getEmail(),
                        "New reimbursement request reassigned to you",
                        "A new reimbursement request has been reassigned to you." +
                                "\nDetails:\nID: " + r.getId() + "\nDescription: " + r.getDescription() +
                                "\nAmount: $" + r.getAmount());
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Gets all the reimbursement requests in the repository.
     * @param m_id The user manager issuing the API request.
     * @param apikey The API key of the manager.
     * @return The HTTP response containing the list of all reimbursements in the repository.
     */
    @GetMapping(value = "/all/{id}")
    private ResponseEntity<List<Reimbursement>> getAllReimbursementRequests(
            @PathVariable("id") Long m_id, @RequestHeader("Authorization") String apikey) {
        ResponseEntity<User> response = authService.authenticateUser(m_id, apikey);
        if (!response.hasBody()) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
        // Check if user is manager
        User manager = response.getBody();
        if (!manager.getUsertype().equals(UserType.MANAGER)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(reimbursementService.getAllReimbursements(manager));
    }
}
