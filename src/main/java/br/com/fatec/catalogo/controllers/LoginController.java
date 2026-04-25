package br.com.fatec.catalogo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro-usuario")
    public String cadastroUsuario() {
        return "cadastro-usuario";
    }
}
