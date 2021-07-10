package com.borio.authorization.services;

import com.borio.authorization.domain.User;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Override
    public User save(User convert) {
        return null;
    }

    @Override
    public void sendMail(User user) {

    }
}
