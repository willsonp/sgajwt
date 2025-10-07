package sga.sgajwt.Auth.Services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sga.sgajwt.Auth.Dtos.AuthResponse;
import sga.sgajwt.Auth.Dtos.LoginDto;
import sga.sgajwt.Auth.Dtos.RegisterDto;
import sga.sgajwt.Auth.Enums.RolesName;
import sga.sgajwt.Auth.JWT.JwtService;
import sga.sgajwt.Auth.Models.Role;
import sga.sgajwt.Auth.Models.UserEntity;
import sga.sgajwt.Auth.Repository.UserEntityRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserEntity user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.generateToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public AuthResponse register(RegisterDto registerDto) {

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

        userRepository.save(userEntity);

        return AuthResponse.builder()
            .token(jwtService.generateToken(userEntity))
            .build();
        
    }
}
