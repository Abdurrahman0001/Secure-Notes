package com.secure.notes.controller;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.jwt.JwtUtils;
import com.secure.notes.jwt.LoginRequest;
import com.secure.notes.jwt.LoginResponse;
import com.secure.notes.models.User;
import com.secure.notes.services.UserService;
import com.secure.notes.services.impl.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Bad Credentials");
            body.put("status", false);
            return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(token, userDetails.getUsername(), roles);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer "
        Date expirationDate = Jwts.parser().setSigningKey(jwtUtils.key()).build().parseClaimsJws(token).getPayload().getExpiration();
        blacklistService.blacklistToken(token, expirationDate);
        return ResponseEntity.ok("Signed out successfully.");
    }

    public Date extractExpiration(String token) {
        // Assuming `jwtUtils.key()` provides the key used to sign the token.
        Key key = jwtUtils.key();

        Claims claims = Jwts.parser()
                .setSigningKey(key) // Set the key used to sign the JWT
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Get the claims body

        return claims.getExpiration(); // Extract the expiration date
    }


}
