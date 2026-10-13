package condominio.service;

import condominio.model.Condominio;
import condominio.model.Unidade;
import condominio.repository.CondominioRepository;
import condominio.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    public List<Unidade> listarTodas(String condominio) {
        return unidadeRepository.findByCondominioNome(condominio);
    }

    public long contarOcupadas(String condominio) {
        return unidadeRepository.findByCondominioNome(condominio).stream()
                .filter(u -> "Ocupada".equals(u.getSituacao()))
                .count();
    }

    public List<String> listarBlocos(String condominio) {
        List<Condominio> condominios = condominioRepository.findByNome(condominio);
        List<String> blocosColetados = new ArrayList<>();
        for (Condominio c : condominios) {
            if (c.getCondominioBloco() == null || c.getCondominioBloco().trim().isEmpty()) {
                continue;
            }
            String bloco = c.getCondominioBloco().trim();
            if (bloco.contains("/")) {
                for (String p : bloco.split("/")) {
                    String clean = p.trim();
                    if (!clean.isEmpty() && !blocosColetados.contains(clean)) {
                        blocosColetados.add(clean);
                    }
                }
            } else if (bloco.toLowerCase().contains(" ou ")) {
                String[] partes = bloco.split("(?i) ou ");
                String primeira = partes[0].trim();
                if (!blocosColetados.contains(primeira)) blocosColetados.add(primeira);
                String segunda = partes[1].trim();
                if (!segunda.contains(" ")) {
                    String prefixo = primeira.replaceAll("\\d+$", "");
                    String segCompleta = prefixo + segunda;
                    if (!blocosColetados.contains(segCompleta)) blocosColetados.add(segCompleta);
                } else if (!blocosColetados.contains(segunda)) {
                    blocosColetados.add(segunda);
                }
            } else {
                if (!blocosColetados.contains(bloco)) {
                    blocosColetados.add(bloco);
                }
            }
        }
        return blocosColetados;
    }

    private void associarCondominio(Unidade unidade) {
        if (unidade != null && unidade.getCondominioEntity() == null && unidade.getCondominio() != null) {
            String nomeCond = unidade.getCondominio();
            String bloco = unidade.getBloco();
            Condominio cond = null;
            if (bloco != null && !bloco.trim().isEmpty()) {
                cond = condominioRepository.findByNome(nomeCond).stream()
                        .filter(c -> c.getCondominioBloco() != null && c.getCondominioBloco().equalsIgnoreCase(bloco.trim()))
                        .findFirst()
                        .orElse(null);
            }
            if (cond == null) {
                cond = condominioRepository.findFirstByNome(nomeCond)
                        .orElseGet(() -> condominioRepository.findAll().stream()
                                .filter(c -> c.getNome() != null && c.getNome().equalsIgnoreCase(nomeCond.trim()))
                                .findFirst()
                                .orElse(null));
            }
            unidade.setCondominioEntity(cond);
        }
    }

    public void salvar(Unidade unidade) {
        associarCondominio(unidade);
        unidadeRepository.save(unidade);
    }

    public void atualizarSituacao(String bloco, String numero, String condominio, String novaSituacao) {
        atualizarSituacaoEGaragem(bloco, numero, condominio, novaSituacao, null);
    }

    public void atualizarSituacaoEGaragem(String bloco, String numero, String condominio, String novaSituacao, String garagem) {
        unidadeRepository.findByCondominioNome(condominio).stream()
                .filter(u -> u.getBloco().equals(bloco) && u.getNumero().equals(numero))
                .findFirst()
                .ifPresent(u -> {
                    u.setSituacao(novaSituacao);
                    if (garagem != null && !garagem.trim().isEmpty()) {
                        u.setGaragem(garagem);
                    }
                    associarCondominio(u);
                    unidadeRepository.save(u);
                });
    }
}
