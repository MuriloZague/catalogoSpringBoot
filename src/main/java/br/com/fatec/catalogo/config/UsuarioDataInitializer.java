package br.com.fatec.catalogo.config;

import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        criarUsuarioSeNaoExistir("Administrador", "admin", "admin@catalogo.local", "12345", "ADMIN");
        criarUsuarioSeNaoExistir("Aluno", "aluno", "aluno@catalogo.local", "12345", "USER");
    }

    private void criarUsuarioSeNaoExistir(String nome, String username, String email, String senha, String perfil) {
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            return;
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNomeCompleto(nome);
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(perfil);
        usuarioRepository.save(usuario);
    }
}
