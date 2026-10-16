package condominio.controller;

import condominio.model.Usuario;
import condominio.model.Reserva;
import condominio.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/reservas")
    public String listarReservas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        model.addAttribute("usuario", usuario);
        model.addAttribute("reservas", reservaService.listarTodas(condominio));
        model.addAttribute("areasComuns", reservaService.listarAreasComuns(condominio));
        return "reservas";
    }

    @GetMapping("/reservas/nova")
    public String novaReserva(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        model.addAttribute("usuario", usuario);
        Reserva reserva = new Reserva();
        reserva.setCondominio(condominio);
        model.addAttribute("reserva", reserva);
        model.addAttribute("areasComuns", reservaService.listarAreasComuns(condominio));
        return "nova-reserva";
    }

    @PostMapping("/reservas")
    public String salvarReserva(@Valid @ModelAttribute("reserva") Reserva reserva,
                                BindingResult result,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();

        if (!result.hasFieldErrors("area") && !reservaService.areaPermitida(condominio, reserva.getArea())) {
            result.rejectValue("area", "area.invalida", "Selecione uma área comum disponível.");
        }
        if (!result.hasFieldErrors("data")) {
            try {
                LocalDate.parse(reserva.getData());
            } catch (DateTimeParseException e) {
                result.rejectValue("data", "data.invalida", "Informe uma data válida.");
            }
        }
        if (!result.hasFieldErrors("area") && !result.hasFieldErrors("data")
                && !result.hasFieldErrors("horario")
                && reservaService.existeConflito(condominio, reserva.getArea(), reserva.getData(), reserva.getHorario())) {
            result.rejectValue("horario", "horario.indisponivel",
                    "Já existe uma reserva para esta área, data e horário.");
        }
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("areasComuns", reservaService.listarAreasComuns(condominio));
            return "nova-reserva";
        }

        reserva.setCondominio(condominio);
        reserva.setStatus("Aguardando");
        reserva.setData(LocalDate.parse(reserva.getData()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reservaService.salvar(reserva);
        redirectAttributes.addFlashAttribute("sucesso", "Reserva criada com sucesso.");
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/{id}/status")
    @ResponseBody
    public String toggleStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "error";
        String condominio = usuario.getCondominio();
        Reserva reserva = reservaService.buscarPorId(id, condominio);
        if (reserva != null && reservaService.atualizarStatus(reserva, status)) {
            return "ok";
        }
        return "error";
    }
}
