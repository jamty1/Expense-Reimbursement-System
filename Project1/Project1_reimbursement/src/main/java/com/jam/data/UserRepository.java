package com.jam.data;

import com.jam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Spring repository containing all the
 * users in the system.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
