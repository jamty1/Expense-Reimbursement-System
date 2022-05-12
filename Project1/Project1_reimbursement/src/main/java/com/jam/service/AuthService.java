package com.jam.service;

import com.jam.data.UserRepository;
import com.jam.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Controller for the authentication of
 * users using the API.
 */
@Service
public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Authenticates a user into the API service.
     * @param userid The id of user using the service.
     * @param apikey The API key of the user.
     * @return The response carrying the User object if successful, otherwise, send an HTTP error status.
     */
    public ResponseEntity<User> authenticateUser(Long userid, String apikey) {
        User user;
        if (!userRepository.existsById(userid)) {
            return ResponseEntity.badRequest().build();
        }
        user = userRepository.findById(userid).get();
        String[] key = apikey.split(" ");
        if (!user.getApikey().equals(key[0])) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }
}
