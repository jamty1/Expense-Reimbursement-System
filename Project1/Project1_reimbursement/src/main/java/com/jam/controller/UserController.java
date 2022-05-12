package com.jam.controller;

import com.jam.model.User;
import com.jam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

/**
 * API endpoint handler for user related
 * requests.
 */
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Gets all the users in the User repository.
     * @return The list of users in the user repository.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Creates a new user with the specified information.
     * @param user The new user to be created.
     * @return The newly created user with a unique user id and an API key.
     */
    @PostMapping
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user) {
        User newUser = userService.addUser(user);
        return ResponseEntity.created(URI.create("/api/user"))
                .header("Authorization", "Basic " + newUser.getApikey())
                .body(newUser);
    }

    /**
     * Update the email notification subscription of a user.
     * @param id The ID of the user to unsubscribe.
     * @return The HTTP response containing the new user object.
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> unsubscribe(@PathVariable("id") Long id,
                                            @PathParam(value = "notify") boolean notify) {
        if (notify) {
            return ResponseEntity.ok(userService.subscribeEmail(id));
        }
        return ResponseEntity.ok(userService.unsubscribeEmail(id));
    }
}
