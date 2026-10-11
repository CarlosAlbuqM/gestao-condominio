package condominio.service;

import condominio.model.Usuario;
import condominio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Cadastra um novo usuário com a senha hasheada via BCrypt.
     */
    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail: " + usuario.getEmail());
        }
        // Hash da senha antes de salvar
        String senhaHash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaHash);
        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica um usuário comparando a senha informada com o hash armazenado.
     */
    public boolean autenticar(String email, String senhaInformada) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (!usuarioOpt.isPresent()) {
            return false;
        }
        return passwordEncoder.matches(senhaInformada, usuarioOpt.get().getSenha());
    }

    /**
     * Busca um usuário pelo e-mail.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Atualiza a senha de um usuário (já com hash).
     */
    public void atualizarSenha(String email, String novaSenha) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario);
        });
    }
}
