package condominio.model;

import javax.persistence.*;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area", nullable = false, length = 100)
    private String area;

    @Column(name = "area_info", length = 200)
    private String areaInfo;

    @Column(name = "solicitante", nullable = false, length = 100)
    private String solicitante;

    @Column(name = "data", length = 20)
    private String data;

    @Column(name = "horario", length = 20)
    private String horario;

    @Column(name = "finalidade", length = 200)
    private String finalidade;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "condominio_nome", length = 150)
    private String condominioNome;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    public Reserva() {
    }

    public Reserva(String area, String areaInfo, String solicitante, String data, String horario,
                   String finalidade, String status, String condominioNome) {
        this.area = area;
        this.areaInfo = areaInfo;
        this.solicitante = solicitante;
        this.data = data;
        this.horario = horario;
        this.finalidade = finalidade;
        this.status = status;
        this.condominioNome = condominioNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
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
