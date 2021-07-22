package com.borio.authorization.repositories;

import com.borio.authorization.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
