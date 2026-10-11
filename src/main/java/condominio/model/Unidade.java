package condominio.model;

import javax.persistence.*;

@Entity
@Table(name = "unidades")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bloco", nullable = false, length = 20)
    private String bloco;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "andar", length = 20)
    private String andar;

    @Column(name = "situacao", length = 50)
    private String situacao;

    @Column(name = "garagem", length = 10)
    private String garagem;

    @Column(name = "condominio_nome", length = 150)
    private String condominioNome;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    public Unidade() {
    }

    public Unidade(String bloco, String numero, String andar, String situacao, String garagem, String condominioNome) {
        this.bloco = bloco;
        this.numero = numero;
        this.andar = andar;
        this.situacao = situacao;
        this.garagem = garagem;
        this.condominioNome = condominioNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getGaragem() {
        return garagem;
    }

    public void setGaragem(String garagem) {
        this.garagem = garagem;
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
