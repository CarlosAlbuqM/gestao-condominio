package condominio.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "moradores")
public class Morador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do morador é obrigatório.")
    @Size(min = 3, max = 100,
        message = "O nome deve ter entre 3 e 100 caracteres.")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O e-mail do morador é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    @Column(name = "email", length = 150)
    private String email;

    @CPF(message = "Informe um CPF válido.")
    @Column(name = "cpf", unique = true, length = 14)
    private String cpf;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "unidade", length = 20)
    private String unidade;

    @NotBlank(message = "Selecione o bloco.")
    @Column(name = "bloco", length = 20)
    private String bloco;

    @NotBlank(message = "Selecione a unidade.")
    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "situacao", length = 50)
    private String situacao;

    @Column(name = "data_cadastro", length = 20)
    private String dataCadastro;

    @Column(name = "vagas_garagem", length = 10)
    private String vagasGaragem;

    @Column(name = "moradores_unidade")
    private int moradoresUnidade;

    @Column(name = "condominio_nome", length = 150)
    private String condominioNome;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    public Morador() {
    }

    public Morador(String nome, String email, String cpf, String telefone, String tipo,
                   String unidade, String bloco, String numero, String situacao, String dataCadastro,
                   String vagasGaragem, int moradoresUnidade, String condominioNome) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.tipo = tipo;
        this.unidade = unidade;
        this.bloco = bloco;
        this.numero = numero;
        this.situacao = situacao;
        this.dataCadastro = dataCadastro;
        this.vagasGaragem = vagasGaragem;
        this.moradoresUnidade = moradoresUnidade;
        this.condominioNome = condominioNome;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(String vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public int getMoradoresUnidade() {
        return moradoresUnidade;
    }

    public void setMoradoresUnidade(int moradoresUnidade) {
        this.moradoresUnidade = moradoresUnidade;
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
