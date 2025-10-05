package sga.sgajwt.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PrincipalController {

    @GetMapping()
    public String welcomen() {
    return "Si puedes Visualizar esto es porque tienes permisos Privado.";
}
}
