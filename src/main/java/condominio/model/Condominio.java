package condominio.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "condominios")
public class Condominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Morador> moradores;

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Unidade> unidades;

    @Column(name = "condominio_bloco", length = 50)
    private String condominioBloco;

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ocorrencia> ocorrencias;

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    public Condominio() {
    }

    public Condominio(String nome, String condominioBloco) {
        this.nome = nome;
        this.condominioBloco = condominioBloco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Morador> getMoradores() {
        return moradores;
    }

    public void setMoradores(List<Morador> moradores) {
        this.moradores = moradores;
    }

    public List<Unidade> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidade> unidades) {
        this.unidades = unidades;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getCondominioBloco() {
        return condominioBloco;
    }

    public void setCondominioBloco(String condominioBloco) {
        this.condominioBloco = condominioBloco;
    }
}
