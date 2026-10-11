package condominio.service;

import condominio.model.Reserva;
import condominio.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> listarTodas(String condominio) {
        return reservaRepository.findByCondominioNome(condominio);
    }

    public void salvar(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public Reserva buscarPorId(Long id, String condominio) {
        return reservaRepository.findById(id)
                .filter(r -> r.getCondominio().equals(condominio))
                .orElse(null);
    }

    public void atualizar(Reserva reservaAtualizada) {
        reservaRepository.save(reservaAtualizada);
    }

    public long contarHoje(String condominio) {
        return reservaRepository.findByCondominioNome(condominio).stream()
                .filter(r -> "Confirmada".equals(r.getStatus()))
                .count();
    }

    public List<String> listarAreasComuns(String condominio) {
        if ("parque".equals(condominio)) {
            return Arrays.asList("Piscina", "Academia", "Salao de festas");
        }
        return Arrays.asList("Salao de festas", "Churrasqueira", "Quadra");
    }
}
