package condominio.repository;

import condominio.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    List<Ocorrencia> findByCondominioNome(String condominioNome);
    List<Ocorrencia> findByStatus(String status);
}
