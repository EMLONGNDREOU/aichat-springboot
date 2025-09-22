package org.examplelongndreou.backendchataiapp.services;

import org.examplelongndreou.backendchataiapp.models.User;
import org.examplelongndreou.backendchataiapp.models.dtos.JwtDTO;
import org.examplelongndreou.backendchataiapp.repositories.UserRepository;
import org.examplelongndreou.backendchataiapp.exceptions.ExcStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private JwtEncoder jwtEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        logger.debug("UserService initialized with UserRepository. Reference to:" + this);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User createUser(User user) throws ExcStatus {
        //validate that user with the same username does not exist
        User existingUser = userRepository.findUserByUsername(user.getUsername());
        User existingEmail = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new ExcStatus(HttpStatus.BAD_REQUEST, "User with username " + user.getUsername() + " already exists.");
        }
        if (existingEmail !=null) {
            throw new ExcStatus(HttpStatus.BAD_REQUEST, "User with email " + user.getEmail() + " already exists.");
        }

      user = userRepository.save(user);
/**
 user = userRepository.insertUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getName()
        );
**/
          return user;
    }

    public User updateUser(User updatedUser) throws ExcStatus {

        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new ExcStatus(HttpStatus.NOT_FOUND, "User not found with id: " + updatedUser.getId()));

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        /*

                if (updatedUser.getPassword() != null) {
            String encodedPassword = Base64.getEncoder().encodeToString(updatedUser.getPassword().getBytes());
            existingUser.setPassword(encodedPassword);
        }

        if (updatedUser.getRole_id() != null) {
            existingUser.setRole_id(updatedUser.getRole_id());
        }

         */

        return userRepository.save(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);

        return user;
    }


    public User getLoggedInUser(Authentication authentication) throws ExcStatus {
        User loggedInUser;

        if (authentication.getPrincipal() instanceof User) {
            loggedInUser = (User) authentication.getPrincipal();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            loggedInUser = this.getUserByUsername(((Jwt) authentication.getPrincipal()).getSubject());
        } else {
            throw new ExcStatus(HttpStatus.UNAUTHORIZED, "You are not authenticated.");
        }

        return loggedInUser;
    }


    public JwtDTO generateJwtForAuthUser(Authentication authentication) {
        User logedInUser = (User) authentication.getPrincipal();

        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("localhost:8080")
                .subject(logedInUser.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
            //    .claim("role_id", logedInUser.getRole_id())
                .claim("email", logedInUser.getEmail())
                .claim("name", logedInUser.getName())
                .claim("id", logedInUser.getId())
                .build();

        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));


        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setToken(jwt.getTokenValue());
        return jwtDTO;
    }
}
