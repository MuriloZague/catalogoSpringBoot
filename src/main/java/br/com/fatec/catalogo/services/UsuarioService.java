package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void cadastrarUsuario(String nomeCompleto,
                                String username,
                                String email,
                                String senha,
                                String confirmarSenha,
                                String perfil) {

        String nomeCompletoLimpo = nomeCompleto == null ? "" : nomeCompleto.trim();
        String usernameLimpo = username == null ? "" : username.trim();
        String emailLimpo = email == null ? "" : email.trim();
        String perfilNormalizado = perfil == null ? "USER" : perfil.trim().toUpperCase();

        if (nomeCompletoLimpo.isEmpty() || usernameLimpo.isEmpty() || emailLimpo.isEmpty()) {
            throw new IllegalArgumentException("Nome, usuario e email sao obrigatorios.");
        }

        if (senha == null || senha.isBlank() || !senha.equals(confirmarSenha)) {
            throw new IllegalArgumentException("Senha invalida ou confirmacao diferente.");
        }

        if (!"USER".equals(perfilNormalizado) && !"ADMIN".equals(perfilNormalizado)) {
            throw new IllegalArgumentException("Perfil invalido.");
        }

        if (usuarioRepository.existsByUsernameIgnoreCase(usernameLimpo)) {
            throw new IllegalArgumentException("Usuario ja cadastrado.");
        }

        if (usuarioRepository.existsByEmailIgnoreCase(emailLimpo)) {
            throw new IllegalArgumentException("Email ja cadastrado.");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNomeCompleto(nomeCompletoLimpo);
        usuario.setUsername(usernameLimpo);
        usuario.setEmail(emailLimpo);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(perfilNormalizado);

        usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(usuario.getPerfil())
                .build();
    }
}
