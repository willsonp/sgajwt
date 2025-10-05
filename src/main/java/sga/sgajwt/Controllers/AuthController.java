package sga.sgajwt.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import sga.sgajwt.Dtos.MessageDto;
import sga.sgajwt.Dtos.RegisterDto;
import sga.sgajwt.Enums.RolesName;
import sga.sgajwt.Models.Role;
import sga.sgajwt.Models.UserEntity;
import sga.sgajwt.Repository.UserEntityRepository;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserEntityRepository userEntityRepository;

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
                .password(registerDto.getPassword())
                .roles(roles)
                .build();

        userEntityRepository.save(userEntity);

        return ResponseEntity.ok().body(MessageDto.builder().message("Usuario Creado Satisfatoriamente").build());
    }

}
