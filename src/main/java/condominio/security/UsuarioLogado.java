package condominio.security;

import condominio.model.Usuario;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



public class UsuarioLogado implements UserDetails, CredentialsContainer {

    private final Long id;
    private final String email;
    private final String nome;
    private final String condominio;
    private String senha;

    public UsuarioLogado(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nome = usuario.getNome();
        this.condominio = usuario.getCondominio();
        this.senha = usuario.getSenha();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCondominio() {
        return condominio;
    }

    // ===== Métodos exigidos pelo UserDetails =====

    @Override
    public String getUsername() {
        return email; // no nosso sistema, o "login" é o e-mail
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_SINDICO"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // ===== CredentialsContainer =====

    @Override
    public void eraseCredentials() {
        this.senha = null; // apaga o hash depois do login
    }
}