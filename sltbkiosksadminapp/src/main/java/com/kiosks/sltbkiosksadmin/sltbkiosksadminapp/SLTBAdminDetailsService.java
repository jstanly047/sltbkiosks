package com.kiosks.sltbkiosksadmin.sltbkiosksadminapp;

import com.sltb.kioskslib.library.model.Admin;
import com.kiosks.sltbkiosksadmin.sltbkiosksadminapp.repo.AccountRepo;
import com.kiosks.sltbkiosksadmin.sltbkiosksadminapp.repo.AdminRepo;
import com.kiosks.sltbkiosksadmin.sltbkiosksadminapp.repo.PassengerRepo;
import com.sltb.kioskslib.library.model.Account;
import com.sltb.kioskslib.library.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class SLTBAdminDetailsService implements UserDetailsService {
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    PassengerRepo passengerRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        Optional<Admin> passenger = adminRepo.findById(userId);
        String pwd = passenger.isEmpty() ? "" : passenger.get().getPasswordHash();
        return new org.springframework.security.core.userdetails.User(userId,pwd, new ArrayList<>());
    }

    public boolean addPassengerDetails(Passenger passenger) {
        if (passengerRepo.findById(passenger.getAccountid()).isPresent())
        {
            return false;
        }
        passenger.setPassword(passwordEncoder.encode(passenger.getPassword()));
        passengerRepo.save(passenger);
        Account account = new Account();
        account.setAccountId(passenger.getAccountid());
        accountRepo.save(account);
        return true;
    }
}