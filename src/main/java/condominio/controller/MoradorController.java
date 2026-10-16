package condominio.controller;

import condominio.model.Usuario;
import condominio.model.Morador;
import condominio.service.MoradorService;
import condominio.service.UnidadeService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class MoradorController {

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private UnidadeService unidadeService;

    @InitBinder("morador")
    public void configurarBindingMorador(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, "cpf", new StringTrimmerEditor(true));
    }

    @GetMapping("/moradores")
    public String listarMoradores(Model model, HttpSession session, HttpServletResponse response) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        String condominio = usuario.getCondominio();
        model.addAttribute("usuario", usuario);
        model.addAttribute("moradores", moradorService.listarTodos(condominio));
        return "moradores";
    }

    @GetMapping("/moradores/novo")
    public String cadastroMorador(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        Morador morador = new Morador();
        morador.setCondominio(condominio);
        prepararFormulario(model, usuario, morador);
        return "cadastro-morador";
    }

    @PostMapping("/moradores")
    public String salvarMorador(@Valid @ModelAttribute("morador") Morador morador,
                                BindingResult result,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login";
        }

        String condominio = usuario.getCondominio();
        validarUnidade(morador, result, condominio);
        String cpfNormalizado = normalizarCpf(morador.getCpf());
        validarCpfDuplicado(cpfNormalizado, null, result);
        if (result.hasErrors()) {
            prepararFormulario(model, usuario, morador);
            return "cadastro-morador";
        }

        morador.setCpf(cpfNormalizado);
        morador.setCondominio(condominio);
        morador.setSituacao("Ativo");
        morador.setUnidade(morador.getBloco() + ", " + morador.getNumero());
        String codigoVaga = gerarCodigoVaga(morador.getBloco(), morador.getNumero());
        morador.setVagasGaragem(codigoVaga);
        morador.setMoradoresUnidade(1);
        if (morador.getTipo() == null || morador.getTipo().trim().isEmpty()) {
            morador.setTipo("Proprietario");
        }
        morador.setDataCadastro(java.time.LocalDate.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        try {
            moradorService.salvar(morador);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("cpf", "cpf.duplicado", "Este CPF já está cadastrado.");
            prepararFormulario(model, usuario, morador);
            return "cadastro-morador";
        }
        unidadeService.atualizarSituacaoEGaragem(morador.getBloco(), morador.getNumero(),
                condominio, "Ocupada", codigoVaga);
        redirectAttributes.addFlashAttribute("sucesso", "Morador cadastrado com sucesso.");
        return "redirect:/moradores";
    }

    @GetMapping("/moradores/{id}")
    public String detalhesMorador(@PathVariable Long id,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        Morador morador = moradorService.buscarPorId(id, condominio);
        if (morador != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("morador", morador);
            return "morador";
        }
        redirectAttributes.addFlashAttribute("erro", "Morador não encontrado neste condomínio.");
        return "redirect:/moradores";
    }

    @GetMapping("/moradores/{id}/editar")
    public String editarMorador(@PathVariable Long id,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        Morador morador = moradorService.buscarPorId(id, condominio);
        if (morador != null) {
            prepararFormulario(model, usuario, morador);
            return "editar-morador";
        }
        redirectAttributes.addFlashAttribute("erro", "Morador não encontrado neste condomínio.");
        return "redirect:/moradores";
    }

    @PostMapping("/moradores/{id}/editar")
    public String atualizarMorador(@PathVariable Long id,
                                   @Valid @ModelAttribute("morador") Morador dadosForm,
                                   BindingResult result,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        Morador existente = moradorService.buscarPorId(id, condominio);
        if (existente == null) {
            redirectAttributes.addFlashAttribute("erro", "Morador não encontrado neste condomínio.");
            return "redirect:/moradores";
        }

        validarUnidade(dadosForm, result, condominio);
        String cpfNormalizado = normalizarCpf(dadosForm.getCpf());
        validarCpfDuplicado(cpfNormalizado, id, result);
        if (result.hasErrors()) {
            dadosForm.setId(id);
            prepararFormulario(model, usuario, dadosForm);
            return "editar-morador";
        }
        String blocoAnterior = existente.getBloco();
        String numeroAnterior = existente.getNumero();

        dadosForm.setCpf(cpfNormalizado);
        existente.setNome(dadosForm.getNome());
        existente.setEmail(dadosForm.getEmail());
        existente.setCpf(dadosForm.getCpf());
        existente.setTelefone(dadosForm.getTelefone());
        existente.setBloco(dadosForm.getBloco());
        existente.setNumero(dadosForm.getNumero());
        existente.setUnidade(dadosForm.getBloco() + ", " + dadosForm.getNumero());
        String codigoVaga = gerarCodigoVaga(dadosForm.getBloco(), dadosForm.getNumero());
        existente.setVagasGaragem(codigoVaga);
        try {
            moradorService.atualizar(existente);
        } catch (DataIntegrityViolationException e) {
            dadosForm.setId(id);
            result.rejectValue("cpf", "cpf.duplicado", "Este CPF já está cadastrado.");
            prepararFormulario(model, usuario, dadosForm);
            return "editar-morador";
        }
        
        if (!blocoAnterior.equals(dadosForm.getBloco()) || !numeroAnterior.equals(dadosForm.getNumero())) {
            long moradoresAtivosNaUnidadeAnterior = moradorService.listarAtivos(condominio).stream()
                    .filter(m -> m.getBloco().equals(blocoAnterior) && m.getNumero().equals(numeroAnterior))
                    .count();
            if (moradoresAtivosNaUnidadeAnterior == 0) {
                unidadeService.atualizarSituacao(blocoAnterior, numeroAnterior, condominio, "Disponivel");
            }
            unidadeService.atualizarSituacaoEGaragem(dadosForm.getBloco(), dadosForm.getNumero(), condominio, "Ocupada", codigoVaga);
        } else {
            unidadeService.atualizarSituacaoEGaragem(dadosForm.getBloco(), dadosForm.getNumero(), condominio, "Ocupada", codigoVaga);
        }

        redirectAttributes.addFlashAttribute("sucesso", "Dados do morador atualizados com sucesso.");
        return "redirect:/moradores";
    }

    @PostMapping("/moradores/{id}/situacao")
    @ResponseBody
    public String toggleSituacao(@PathVariable Long id, @RequestParam String situacao, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "error";
        String condominio = usuario.getCondominio();
        Morador morador = moradorService.buscarPorId(id, condominio);
        if (morador != null) {
            String situacaoAnterior = morador.getSituacao();
            if (!moradorService.atualizarSituacao(morador, situacao)) {
                return "error";
            }

            if (!situacaoAnterior.equals(situacao)) {
                String bloco = morador.getBloco();
                String numero = morador.getNumero();
                long moradoresAtivosNaUnidade = moradorService.listarAtivos(condominio).stream()
                        .filter(m -> m.getBloco().equals(bloco) && m.getNumero().equals(numero))
                        .count();

                if (moradoresAtivosNaUnidade > 0) {
                    unidadeService.atualizarSituacao(bloco, numero, condominio, "Ocupada");
                } else {
                    unidadeService.atualizarSituacao(bloco, numero, condominio, "Disponivel");
                }
            }
            return "ok";
        }
        return "error";
    }

    @PostMapping("/moradores/{id}/excluir")
    public String excluirMorador(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";
        String condominio = usuario.getCondominio();
        Morador morador = moradorService.buscarPorId(id, condominio);
        if (morador != null) {
            String bloco = morador.getBloco();
            String numero = morador.getNumero();
            moradorService.excluir(id, condominio);
            
            long moradoresAtivosNaUnidade = moradorService.listarAtivos(condominio).stream()
                    .filter(m -> m.getBloco().equals(bloco) && m.getNumero().equals(numero))
                    .count();
            if (moradoresAtivosNaUnidade == 0) {
                unidadeService.atualizarSituacao(bloco, numero, condominio, "Disponivel");
            }
            redirectAttributes.addFlashAttribute("sucesso", "Morador excluído com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Morador não encontrado neste condomínio.");
        }
        return "redirect:/moradores";
    }

    private void prepararFormulario(Model model, Usuario usuario, Morador morador) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("morador", morador);
        model.addAttribute("blocos", unidadeService.listarBlocos(usuario.getCondominio()));
        model.addAttribute("numeros", unidadeService.listarNumeros(usuario.getCondominio()));
    }

    private void validarUnidade(Morador morador, BindingResult result, String condominio) {
        if (!result.hasFieldErrors("bloco") && !result.hasFieldErrors("numero")
                && !unidadeService.existeUnidade(morador.getBloco(), morador.getNumero(), condominio)) {
            result.rejectValue("numero", "unidade.invalida", "A unidade selecionada não pertence ao condomínio.");
        }
    }

    private void validarCpfDuplicado(String cpf, Long idIgnorado, BindingResult result) {
        if (!result.hasFieldErrors("cpf") && moradorService.cpfJaCadastrado(cpf, idIgnorado)) {
            result.rejectValue("cpf", "cpf.duplicado", "Este CPF já está cadastrado.");
        }
    }

    private String normalizarCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    private String gerarCodigoVaga(String bloco, String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return "01 vaga";
        }
        String digitos = numero.replaceAll("\\D+", "");
        if (digitos.isEmpty()) {
            return "01 vaga";
        }
        char primeiroNumero = digitos.charAt(0);
        char ultimoNumero = digitos.charAt(digitos.length() - 1);

        String idBlocoTorre = "";
        if (bloco != null && !bloco.trim().isEmpty()) {
            String b = bloco.trim().toUpperCase();
            if (b.contains("TORRE 1") || b.endsWith("1")) {
                idBlocoTorre = "1";
            } else if (b.contains("TORRE 2") || b.endsWith("2")) {
                idBlocoTorre = "2";
            } else if (b.contains("BLOCO A") || b.endsWith("A")) {
                idBlocoTorre = "A";
            } else if (b.contains("BLOCO B") || b.endsWith("B")) {
                idBlocoTorre = "B";
            } else if (b.contains("1")) {
                idBlocoTorre = "1";
            } else if (b.contains("2")) {
                idBlocoTorre = "2";
            } else if (b.contains("A")) {
                idBlocoTorre = "A";
            } else if (b.contains("B")) {
                idBlocoTorre = "B";
            } else {
                idBlocoTorre = String.valueOf(b.charAt(b.length() - 1));
            }
        }
        return "" + primeiroNumero + ultimoNumero + idBlocoTorre;
    }
}
