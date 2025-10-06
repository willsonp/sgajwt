package sga.sgajwt.Auth.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import sga.sgajwt.Auth.Dtos.AuthResponse;
import sga.sgajwt.Auth.Dtos.LoginDto;
import sga.sgajwt.Auth.Dtos.MessageDto;
import sga.sgajwt.Auth.Dtos.RegisterDto;
import sga.sgajwt.Auth.Enums.RolesName;
import sga.sgajwt.Auth.JWT.JwtUtils;
import sga.sgajwt.Auth.Models.Role;
import sga.sgajwt.Auth.Models.UserEntity;
import sga.sgajwt.Auth.Repository.UserEntityRepository;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private JwtUtils JwtUtils;

    @Autowired
    private AuthenticationManager authManager;

    @GetMapping("/")
    public String welcomen() {
        return "Si puedes Visualizar esto es porque tienes permisos Public.";
    }

    @PostMapping("/register")
    public ResponseEntity<?> newUser(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("Error", bindingResult.getFieldError());
            return ResponseEntity.badRequest().body(
                    MessageDto.builder().message("Se produjo un Error los campos no cumplen los Criterios").build());
        }

        if (userEntityRepository.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body(MessageDto.builder().message("Usuario Existe").build());
        }

        if (userEntityRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body(MessageDto.builder().message("Email Existe..!").build());
        }

        Set<Role> roles = registerDto.getRoles().stream()
                .map(role -> Role.builder().name(RolesName.valueOf(role)).build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .first_name(registerDto.getFirst_name())
                .last_name(registerDto.getLast_name())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(roles)
                .build();

        userEntityRepository.save(userEntity);

        return ResponseEntity.ok().body(MessageDto.builder().message("Usuario Creado Satisfatoriamente").build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> inicioSession(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    MessageDto.builder().message("Se produjo un Error los campos no cumplen los Criterios").build());
        }

        if (userEntityRepository.existsByUsername(loginDto.getUsername())) {

            Authentication authentication = authManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            final String token = JwtUtils.generateToken(userDetails.getUsername());
            
            return authentication.isAuthenticated()
                    ? ResponseEntity.ok().body(AuthResponse.builder().token(token).build())
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                            MessageDto.builder().message("Password Incorrect or Not Match").build());
        } else {
            return ResponseEntity.badRequest().body(
                    MessageDto.builder().message("UserName  Does Not Exists in the DB").build());
        }
    }

}
