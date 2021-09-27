package com.sltb.kiosks.userauth;

import com.sltb.kiosks.userauth.repo.PassengerRepo;
import com.sltb.kioskslib.library.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class SLTBUserDetailsService implements UserDetailsService {
    @Autowired
    PassengerRepo passengerRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        Optional<Passenger> passenger = passengerRepo.findById(Long.valueOf(userId));
        String pwd = passenger.isEmpty() ? "" : passenger.get().getPassword();
        return new org.springframework.security.core.userdetails.User(userId,pwd, new ArrayList<>());
    }
}