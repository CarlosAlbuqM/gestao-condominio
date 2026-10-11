package condominio.service;

import condominio.model.Ocorrencia;
import condominio.repository.OcorrenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    public List<Ocorrencia> listarTodas(String condominio) {
        return ocorrenciaRepository.findByCondominioNome(condominio);
    }

    public Ocorrencia buscarPorId(Long id, String condominio) {
        return ocorrenciaRepository.findById(id)
                .filter(o -> o.getCondominio().equals(condominio))
                .orElse(null);
    }

    public void salvar(Ocorrencia ocorrencia) {
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
        ocorrenciaRepository.save(ocorrenciaAtualizada);
    }
}
