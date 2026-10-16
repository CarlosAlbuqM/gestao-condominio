package condominio.repository;

import condominio.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCondominioNome(String condominioNome);
    Optional<Reserva> findByIdAndCondominioNome(Long id, String condominioNome);
    List<Reserva> findBySolicitante(String solicitante);
}
