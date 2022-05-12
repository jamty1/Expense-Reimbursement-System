package com.jam.service;

import com.jam.data.UserRepository;
import com.jam.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
 * Represents the user service
 * in the API.
 */
@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets the user with the specified ID.
     * @param id The ID to find in the repository.
     * @return The user's information with the matching ID.
     */
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    /**
     * Gets all the users in the user repository.
     * @return The list of all the users in the repository.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Adds a new user to the repository.
     * @param newUser The new user to add to the repository.
     * @return The new user with a unique ID and API key.
     */
    public User addUser(User newUser) {
        String originalInput = newUser.getName() + ":" + newUser.getPassword();
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        newUser.setApikey(encodedString);
        log.info("[POST] User " + newUser.getName() + " has been added into the system.");
        return userRepository.save(newUser);
    }

    /**
     * Subscribe the user to email notifications.
     * @param id The ID of the user to subscribe.
     * @return The user object of the user that subscribed.
     */
    public User subscribeEmail(Long id) {
        User u = userRepository.findById(id).get();
        u.setNotify(true);
        userRepository.save(u);
        log.info("[PATCH] User " + u.getName() + " has subscribed from notifications." );
        return u;
    }

    /**
     * Unsubscribe the user to email notifications.
     * @param id The ID of the user to unsubscribe.
     * @return The user object of the user that unsubscribed.
     */
    public User unsubscribeEmail(Long id) {
        User u = userRepository.findById(id).get();
        u.setNotify(false);
        userRepository.save(u);
        log.info("[PATCH] User " + u.getName() + " has unsubscribed from notifications." );
        return u;
    }
}
