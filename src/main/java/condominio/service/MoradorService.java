package condominio.service;

import condominio.model.Condominio;
import condominio.model.Morador;
import condominio.repository.CondominioRepository;
import condominio.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    private void associarCondominio(Morador morador) {
        if (morador != null && morador.getCondominioEntity() == null && morador.getCondominio() != null) {
            String nomeCond = morador.getCondominio();
            String bloco = morador.getBloco();
            Condominio cond = null;
            if (bloco != null && !bloco.trim().isEmpty()) {
                cond = condominioRepository.findByNome(nomeCond).stream()
                        .filter(c -> c.getCondominioBloco() != null && c.getCondominioBloco().equalsIgnoreCase(bloco.trim()))
                        .findFirst()
                        .orElse(null);
            }
            if (cond == null) {
                cond = condominioRepository.findFirstByNome(nomeCond).orElse(null);
            }
            morador.setCondominioEntity(cond);
        }
    }

    public List<Morador> listarTodos(String condominio) {
        return moradorRepository.findByCondominioNome(condominio);
    }

    public Morador buscarPorId(Long id, String condominio) {
        return moradorRepository.findByIdAndCondominioNome(id, condominio)
                .orElse(null);
    }

    public void salvar(Morador morador) {
        associarCondominio(morador);
        moradorRepository.saveAndFlush(morador);
    }

    public void atualizar(Morador moradorAtualizado) {
        associarCondominio(moradorAtualizado);
        moradorRepository.saveAndFlush(moradorAtualizado);
    }

    public boolean cpfJaCadastrado(String cpf, Long idIgnorado) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        return moradorRepository.findAll().stream()
                .filter(morador -> !morador.getId().equals(idIgnorado))
                .map(Morador::getCpf)
                .map(this::somenteDigitos)
                .anyMatch(cpf::equals);
    }

    public boolean atualizarSituacao(Morador morador, String situacao) {
        if (!Arrays.asList("Ativo", "Inativo").contains(situacao)) {
            return false;
        }
        morador.setSituacao(situacao);
        atualizar(morador);
        return true;
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
        moradorRepository.findByIdAndCondominioNome(id, condominio)
                .ifPresent(moradorRepository::delete);
    }

    private String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }
}
