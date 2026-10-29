package condominio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título da ocorrência é obrigatório.")
    @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres.")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "O local da ocorrência é obrigatório.")
    @Size(max = 100, message = "O local deve ter no máximo 100 caracteres.")
    @Column(name = "local", length = 100)
    private String local;

    @NotBlank(message = "A data da ocorrência é obrigatória.")
    @Column(name = "data", length = 20)
    private String data;

    @Column(name = "responsavel", length = 100)
    private String responsavel;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "condominio_nome", length = 150)
    private String condominioNome;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    public Ocorrencia() {
    }

    public Ocorrencia(String titulo, String local, String data, String responsavel, String status, String condominioNome) {
        this.titulo = titulo;
        this.local = local;
        this.data = data;
        this.responsavel = responsavel;
        this.status = status;
        this.condominioNome = condominioNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondominioNome() {
        return condominioNome;
    }

    public void setCondominioNome(String condominioNome) {
        this.condominioNome = condominioNome;
    }

    // Compatibilidade com código legado
    public String getCondominio() {
        return condominioNome;
    }

    public void setCondominio(String condominio) {
        this.condominioNome = condominio;
    }

    public Condominio getCondominioEntity() {
        return condominio;
    }

    public void setCondominioEntity(Condominio condominio) {
        this.condominio = condominio;
    }
}
