package condominio.service;

import condominio.model.Condominio;
import condominio.model.Reserva;
import condominio.repository.CondominioRepository;
import condominio.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    private void associarCondominio(Reserva reserva) {
        if (reserva != null && reserva.getCondominioEntity() == null && reserva.getCondominio() != null) {
            String nomeCond = reserva.getCondominio();
            Condominio cond = condominioRepository.findFirstByNome(nomeCond)
                    .orElseGet(() -> condominioRepository.findAll().stream()
                            .filter(c -> c.getNome() != null && c.getNome().equalsIgnoreCase(nomeCond.trim()))
                            .findFirst()
                            .orElse(null));
            reserva.setCondominioEntity(cond);
        }
    }

    public List<Reserva> listarTodas(String condominio) {
        return reservaRepository.findByCondominioNome(condominio);
    }

    public void salvar(Reserva reserva) {
        associarCondominio(reserva);
        reservaRepository.save(reserva);
    }

    public Reserva buscarPorId(Long id, String condominio) {
        return reservaRepository.findByIdAndCondominioNome(id, condominio)
                .orElse(null);
    }

    public void atualizar(Reserva reservaAtualizada) {
        associarCondominio(reservaAtualizada);
        reservaRepository.save(reservaAtualizada);
    }

    public long contarHoje(String condominio) {
        return reservaRepository.findByCondominioNome(condominio).stream()
                .filter(r -> "Confirmada".equals(r.getStatus()))
                .filter(r -> dataEhHoje(r.getData()))
                .count();
    }

    public List<String> listarAreasComuns(String condominio) {
        if ("parque".equals(condominio)) {
            return Arrays.asList("Piscina", "Academia", "Salao de festas");
        }
        return Arrays.asList("Salao de festas", "Churrasqueira", "Quadra");
    }

    public boolean areaPermitida(String condominio, String area) {
        return area != null && listarAreasComuns(condominio).contains(area);
    }

    public boolean existeConflito(String condominio, String area, String data, String horario) {
        return reservaRepository.findByCondominioNome(condominio).stream()
                .anyMatch(reserva -> area.equals(reserva.getArea())
                        && horario.equals(reserva.getHorario())
                        && datasIguais(data, reserva.getData()));
    }

    public boolean atualizarStatus(Reserva reserva, String status) {
        if (!Arrays.asList("Aguardando", "Confirmada").contains(status)) {
            return false;
        }
        reserva.setStatus(status);
        atualizar(reserva);
        return true;
    }

    private boolean dataEhHoje(String data) {
        return datasIguais(LocalDate.now().toString(), data);
    }

    private boolean datasIguais(String primeiraData, String segundaData) {
        LocalDate primeira = converterData(primeiraData);
        LocalDate segunda = converterData(segundaData);
        return primeira != null && primeira.equals(segunda);
    }

    private LocalDate converterData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return null;
        }
        for (DateTimeFormatter formato : Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))) {
            try {
                return LocalDate.parse(data, formato);
            } catch (DateTimeParseException ignored) {
                // Tenta o próximo formato usado pela aplicação.
            }
        }
        return null;
    }
}
