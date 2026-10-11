package condominio.repository;

import condominio.model.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {
    List<Morador> findByCondominioNome(String condominioNome);
    List<Morador> findByUnidadeAndBloco(String unidade, String bloco);
}
