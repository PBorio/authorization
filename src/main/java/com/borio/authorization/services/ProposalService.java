package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.controllers.forms.ProposalServiceItemForm;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.ProposalServiceItem;

public interface ProposalService {
    Proposal save(ProposalForm any) ;

    ProposalServiceItem saveServiceItem(ProposalServiceItemForm proposalServiceItemForm);

    void deleteServiceItem(Long proposalId, Long serviceId);

    Proposal findById(Long id);
}
