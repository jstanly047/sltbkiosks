package com.sltb.kiosks.userauth;


import com.sltb.kioskslib.library.auth.JWTUtils;
import com.sltb.kioskslib.library.auth.models.AuthenticationRequest;
import com.sltb.kioskslib.library.auth.models.AuthenticationResponse;
import com.sltb.kioskslib.library.auth.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAction {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SLTBUserDetailsService sltbUserDetailService;
    @Autowired
    private JWTUtils jwtUtils;

    @RequestMapping(value="/userauth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws  Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = sltbUserDetailService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, Status.SUCCESS, "Auth Success"));
    }
}
