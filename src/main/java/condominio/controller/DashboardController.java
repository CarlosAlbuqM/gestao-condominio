package condominio.controller;

import condominio.model.Condominio;
import condominio.model.Usuario;
import condominio.repository.CondominioRepository;
import condominio.service.MoradorService;
import condominio.service.UnidadeService;
import condominio.service.OcorrenciaService;
import condominio.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CondominioRepository condominioRepository;

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session, HttpServletResponse response) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        String condominioNome = usuario.getCondominio();

        // Buscar dados do condomínio na tabela condominios
        java.util.List<Condominio> condominios = condominioRepository.findByNome(condominioNome);
        Condominio condominioEntity = condominios.isEmpty() ? null : condominios.get(0);
        String blocosTexto = condominios.stream()
                .map(Condominio::getCondominioBloco)
                .filter(b -> b != null && !b.trim().isEmpty())
                .collect(java.util.stream.Collectors.joining(", "));
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("condominioEntity", condominioEntity);
        model.addAttribute("blocosTexto", blocosTexto.isEmpty() ? null : blocosTexto);
        model.addAttribute("moradoresAtivos", moradorService.contarAtivos(condominioNome));
        model.addAttribute("unidadesOcupadas", moradorService.contarUnidadesOcupadas(condominioNome));
        model.addAttribute("totalUnidades", unidadeService.listarTodas(condominioNome).size());
        model.addAttribute("ocorrenciasAbertas", ocorrenciaService.contarAbertas(condominioNome));
        model.addAttribute("reservasHoje", reservaService.contarHoje(condominioNome));
        model.addAttribute("moradores", moradorService.listarAtivos(condominioNome));
        model.addAttribute("reservas", reservaService.listarTodas(condominioNome));
        return "dashboard";
    }
}
