package sga.sgajwt.Auth.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sga.sgajwt.Auth.Dtos.AuthResponse;
import sga.sgajwt.Auth.Dtos.LoginDto;
import sga.sgajwt.Auth.Dtos.RegisterDto;
import sga.sgajwt.Auth.Services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = {"http://localhost:4200"}) 
@RequiredArgsConstructor
class AuthController {

    @Autowired
    private final AuthService authService;
    
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto)
    {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDto registerDto)
    {
        return ResponseEntity.ok(authService.register(registerDto));
    }
}
