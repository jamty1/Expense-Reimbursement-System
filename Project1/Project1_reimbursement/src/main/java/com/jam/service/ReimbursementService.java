package com.jam.service;

import com.jam.data.ReimbursementRepository;
import com.jam.model.Reimbursement;
import com.jam.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents the reimbursement services
 * of the API.
 */
@Service
@Slf4j
public class ReimbursementService {
    private ReimbursementRepository reimbursementRepository;

    public ReimbursementService(ReimbursementRepository reimbursementRepository) {
        this.reimbursementRepository = reimbursementRepository;
    }

    /**
     * Gets the reimbursement object from the repository
     * with the specified ID.
     * @param r_id The ID of the reimbursement to get.
     * @return The reimbursement object with the matching ID.
     */
    public Reimbursement getReimbursement(Long r_id) {
        return reimbursementRepository.getById(r_id);
    }

    /**
     * Gets the list of reimbursement requests that a user has.
     * @param requester The user who requested the information.
     * @return The list of reimbursement requests that requester has.
     */
    public List<Reimbursement> getUserReimbursements(User requester) {
        log.info("[GET] User " + requester.getName() + " took a look at all his reimbursements.");
        return requester.getReimbursements();
    }

    /**
     * Gets all the reimbursement requests in the repository.
     * @param requester The user who requested the information.
     * @return The list of all reimbursement requests in the repository.
     */
    public List<Reimbursement> getAllReimbursements(User requester) {
        log.info("[GET] Manager " + requester.getName() + " grabbed all reimbursements.");
        return reimbursementRepository.findAll();
    }

    /**
     * Saves a new reimbursement request into the repository.
     * @param user The user who owns the reimbursement request.
     * @param reimbursement The information of the reimbursement request.
     * @return The reimbursement request with its unique ID given by the repository.
     */
    public Reimbursement submitReimbursementRequest(User user, Reimbursement reimbursement) {
        user.getReimbursements().add(reimbursement);
        reimbursement.setUser(user);
        log.info("[POST] User " + user.getName() + " has a new reimbursement request.");
        return reimbursementRepository.save(reimbursement);
    }

    /**
     * Approve a reimbursement request.
     * @param r_id The unique ID of the reimbursement request.
     * @param manager The manager who issued the approval.
     * @return The new information of the reimbursement request.
     */
    public Reimbursement approveReimbursement(Long r_id, User manager) {
        Reimbursement r = reimbursementRepository.getById(r_id);
        r.setApproved(true);
        log.info("[PUT] Manager " + manager.getName() + " has approved reimbursement with id " + r_id);
        return reimbursementRepository.save(r);
    }

    /**
     * Deny a reimbursement request.
     * @param r_id The unique ID of the reimbursement request.
     * @param manager The manager who issued the denial.
     * @return The new information of the reimbursement request.
     */
    public Reimbursement denyReimbursement(Long r_id, User manager) {
        Reimbursement r = reimbursementRepository.getById(r_id);
        r.setApproved(false);
        log.info("[PUT] Manager " + manager.getName() + " has denied reimbursement with id " + r_id);
        return reimbursementRepository.save(r);
    }

    /**
     * Reassign a reimbursement request to a new user.
     * @param r_id The unique ID of the reimbursement request.
     * @param manager The manager who issued the reassignment.
     * @param newUser The new user to reassign the reimbursement to.
     * @return The new information of the reimbursement request.
     */
    public Reimbursement reassignReimbursement(Long r_id, User manager, User newUser) {
        Reimbursement r = reimbursementRepository.getById(r_id);
        r.setUser(newUser);
        log.info("[PUT] Manager " + manager.getName() + " has reassigned reimbursement with id " + r_id);
        return reimbursementRepository.save(r);
    }
}
