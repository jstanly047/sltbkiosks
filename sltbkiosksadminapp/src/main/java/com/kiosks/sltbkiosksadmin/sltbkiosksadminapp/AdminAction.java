package com.kiosks.sltbkiosksadmin.sltbkiosksadminapp;


import com.sltb.kioskslib.library.auth.JWTUtils;
import com.sltb.kioskslib.library.auth.models.AuthenticationRequest;
import com.sltb.kioskslib.library.auth.models.AuthenticationResponse;
import com.sltb.kioskslib.library.auth.models.Status;
import com.sltb.kioskslib.library.model.ActionResponse;
import com.sltb.kioskslib.library.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminAction {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SLTBAdminDetailsService sltbAdminDetailsService;
    @Autowired
    private JWTUtils jwtUtils;

    @RequestMapping(value="/hello")
    public ResponseEntity<?> addUser(@RequestHeader(name="Authorization") String authHeader)
    {
        String jwt = authHeader.split(" ")[1].trim();
        String userName = jwtUtils.extractUserName(jwt);
        return ResponseEntity.ok(new ActionResponse(Status.SUCCESS,"Hello" + userName));
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody Passenger newPassenger) throws Exception
    {
        if (sltbAdminDetailsService.addPassengerDetails(newPassenger) == false)
        {
            return ResponseEntity.ok(new ActionResponse(Status.FAILED,"Account ID already exist"));
        }

        return ResponseEntity.ok(new ActionResponse(Status.SUCCESS,"New user created"));
    }

    @RequestMapping(value="/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws  Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = sltbAdminDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, Status.SUCCESS, "Auth Success"));
    }
}
