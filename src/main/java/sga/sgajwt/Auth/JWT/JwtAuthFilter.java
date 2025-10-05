package sga.sgajwt.Auth.JWT;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import sga.sgajwt.Auth.Models.UserEntity;

@Slf4j
public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {


    @Autowired 
    private JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils){
        this.jwtUtils=jwtUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        UserEntity user = null;
        String username = "";
        String password = "";

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {
            log.error("Error al Leer username y password", e.getMessage());
        } catch (DatabindException e) {
            log.error("Error al Leer username y password", e.getMessage());
        } catch (IOException e) {
            log.error("Error al Leer username y password", e.getMessage());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return getAuthenticationManager().authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
           UserDetails user = (UserDetails) authResult.getPrincipal();
             
           String token = jwtUtils.generateToken(user.getUsername());

           response.addHeader("Authorization", token);
           Map<String,Object> httpResponse = new HashMap<>();
           httpResponse.put("token", token);
           httpResponse.put("Message", "Authenticacion Correcta");
           httpResponse.put("username", user.getUsername());
            // Escribir el Mapa en la respuesta como JSON
           response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
           response.setStatus(HttpStatus.OK.value());
           response.setContentType("application/json");
           response.getWriter().flush();

         successfulAuthentication(request, response, chain, authResult);

    }

}

