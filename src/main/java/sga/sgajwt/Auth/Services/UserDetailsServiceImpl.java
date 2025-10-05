package sga.sgajwt.Auth.Services;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sga.sgajwt.Auth.Models.UserEntity;
import sga.sgajwt.Auth.Repository.UserEntityRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();

        // Para convertir el SET en Collection y pasar las Authority al usuario
         Collection<? extends GrantedAuthority> authority = userEntity.getRoles().stream().map(role ->new SimpleGrantedAuthority("ROLE_".concat(role.getName().name()))).collect(Collectors.toSet());

        return
        new org.springframework.security.core.userdetails.User(userEntity.getUsername(),userEntity.getPassword(),true,true,true,true,authority);

    }


}
