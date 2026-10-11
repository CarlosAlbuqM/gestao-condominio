package condominio.repository;

import condominio.model.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CondominioRepository extends JpaRepository<Condominio, Long> {
    Optional<Condominio> findFirstByNome(String nome);
    java.util.List<Condominio> findByNome(String nome);
}
