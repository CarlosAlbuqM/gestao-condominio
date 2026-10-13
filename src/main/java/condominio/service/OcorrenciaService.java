package condominio.service;

import condominio.model.Condominio;
import condominio.model.Ocorrencia;
import condominio.repository.CondominioRepository;
import condominio.repository.OcorrenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    private void associarCondominio(Ocorrencia ocorrencia) {
        if (ocorrencia != null && ocorrencia.getCondominioEntity() == null && ocorrencia.getCondominio() != null) {
            String nomeCond = ocorrencia.getCondominio();
            Condominio cond = condominioRepository.findFirstByNome(nomeCond)
                    .orElseGet(() -> condominioRepository.findAll().stream()
                            .filter(c -> c.getNome() != null && c.getNome().equalsIgnoreCase(nomeCond.trim()))
                            .findFirst()
                            .orElse(null));
            ocorrencia.setCondominioEntity(cond);
        }
    }

    public List<Ocorrencia> listarTodas(String condominio) {
        return ocorrenciaRepository.findByCondominioNome(condominio);
    }

    public Ocorrencia buscarPorId(Long id, String condominio) {
        return ocorrenciaRepository.findById(id)
                .filter(o -> o.getCondominio().equals(condominio))
                .orElse(null);
    }

    public void salvar(Ocorrencia ocorrencia) {
        associarCondominio(ocorrencia);
        ocorrenciaRepository.save(ocorrencia);
    }

    public long contarAbertas(String condominio) {
        return ocorrenciaRepository.findByCondominioNome(condominio).stream()
                .filter(o -> !"Concluido".equals(o.getStatus()) && !"Concluida".equals(o.getStatus()))
                .count();
    }

    public long contarPorStatus(String condominio, String status) {
        return ocorrenciaRepository.findByCondominioNome(condominio).stream()
                .filter(o -> status.equals(o.getStatus()))
                .count();
    }

    public void atualizar(Ocorrencia ocorrenciaAtualizada) {
        associarCondominio(ocorrenciaAtualizada);
        ocorrenciaRepository.save(ocorrenciaAtualizada);
    }
}
