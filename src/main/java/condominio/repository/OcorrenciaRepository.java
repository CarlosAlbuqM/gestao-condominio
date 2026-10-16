package condominio.repository;

import condominio.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    List<Ocorrencia> findByCondominioNome(String condominioNome);
    Optional<Ocorrencia> findByIdAndCondominioNome(Long id, String condominioNome);
    List<Ocorrencia> findByStatus(String status);
}
