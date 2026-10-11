package condominio.service;

import condominio.model.Morador;
import condominio.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    public List<Morador> listarTodos(String condominio) {
        return moradorRepository.findByCondominioNome(condominio);
    }

    public Morador buscarPorId(Long id, String condominio) {
        return moradorRepository.findById(id)
                .filter(m -> m.getCondominio().equals(condominio))
                .orElse(null);
    }

    public void salvar(Morador morador) {
        moradorRepository.save(morador);
    }

    public void atualizar(Morador moradorAtualizado) {
        moradorRepository.save(moradorAtualizado);
    }

    public List<Morador> listarAtivos(String condominio) {
        return moradorRepository.findByCondominioNome(condominio).stream()
                .filter(m -> "Ativo".equals(m.getSituacao()))
                .collect(java.util.stream.Collectors.toList());
    }

    public long contarAtivos(String condominio) {
        return moradorRepository.findByCondominioNome(condominio).stream()
                .filter(m -> "Ativo".equals(m.getSituacao()))
                .count();
    }

    public long contarUnidadesOcupadas(String condominio) {
        return moradorRepository.findByCondominioNome(condominio).stream()
                .filter(m -> "Ativo".equals(m.getSituacao()))
                .map(m -> m.getBloco() + m.getNumero())
                .distinct()
                .count();
    }

    public void excluir(Long id, String condominio) {
        moradorRepository.findById(id).ifPresent(m -> {
            if (m.getCondominio().equals(condominio)) {
                moradorRepository.delete(m);
            }
        });
    }
}
