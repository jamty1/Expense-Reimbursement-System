package com.jam.data;

import com.jam.model.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Spring repository containing all
 * the reimbursement requests in the system.
 */
@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long> {
}
