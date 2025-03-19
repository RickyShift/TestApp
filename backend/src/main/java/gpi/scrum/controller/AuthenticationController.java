package gpi.scrum.controller;

import gpi.scrum.domain.AuthenticationResponse;
import gpi.scrum.domain.User;
import gpi.scrum.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    // Registra um novo utilizador e retorna uma resposta de autenticação
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    // Autentica um utilizador existente e retorna uma resposta de autenticação
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.authentication(user));
    }

}
