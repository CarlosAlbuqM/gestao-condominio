package condominio.model;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "condominio", length = 100)
    private String condominio;

    public Usuario() {
    }

    public Usuario(String email, String senha, String nome, String condominio) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.condominio = condominio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCondominio() {
        return condominio;
    }

    public void setCondominio(String condominio) {
        this.condominio = condominio;
    }
}
