package sga.sgajwt.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PrincipalController {

    @GetMapping()
    public String welcomen() {
    return "Si puedes Visualizar esto es porque tienes permisos Privado con JWT.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String amdin() {
    return "Si puedes Visualizar esto es porque tienes permisos Privado con JWT  y ADMIN ROLE.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")   
    public String userRole() {
    return "Si puedes Visualizar esto es porque tienes permisos Privado con JWT  y USER ROLE.";
    }

}
