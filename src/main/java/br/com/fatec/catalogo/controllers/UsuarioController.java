package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@RequestParam String nomeCompleto,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String senha,
                                @RequestParam String confirmarSenha,
                                @RequestParam(defaultValue = "USER") String perfil) {
        try {
            usuarioService.cadastrarUsuario(nomeCompleto, username, email, senha, confirmarSenha, perfil);
            return "redirect:/cadastro-usuario?sucesso";
        } catch (IllegalArgumentException e) {
            return "redirect:/cadastro-usuario?erro";
        }
    }
}
