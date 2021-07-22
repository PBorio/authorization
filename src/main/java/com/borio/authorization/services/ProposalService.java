package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Proposal;

public interface ProposalService {
    Proposal save(ProposalForm any) ;
}
