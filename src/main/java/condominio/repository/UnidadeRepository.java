package condominio.repository;

import condominio.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    List<Unidade> findByCondominioNome(String condominioNome);
    List<Unidade> findByBlocoAndNumero(String bloco, String numero);
}
