package gpi.scrum.service;

import gpi.scrum.domain.Role;
import gpi.scrum.domain.User;
import gpi.scrum.repository.RoleRepository;
import gpi.scrum.repository.UserRepository;
import gpi.scrum.domain.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request) {


        User user = new User();
        user.setUserName(request.getUsername());
        if (userRepository.findByUserName(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(request.getRole().getName());
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        user.setRole(role);

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);

    }

    public AuthenticationResponse authentication(User request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUserName(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String accessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(accessToken);

    }



}