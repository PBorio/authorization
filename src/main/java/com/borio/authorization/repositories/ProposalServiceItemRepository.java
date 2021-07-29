package com.borio.authorization.repositories;

import com.borio.authorization.domain.ProposalServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalServiceItemRepository extends JpaRepository<ProposalServiceItem, Long> {
}
