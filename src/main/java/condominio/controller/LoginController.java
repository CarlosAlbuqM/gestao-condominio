package condominio.controller;

import condominio.model.Usuario;
import condominio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password, 
                        Model model, 
                        HttpSession session) {
        
        if (usuarioService.autenticar(email, password)) {
            // Como autenticou com sucesso, busca o usuário e salva na sessão
            Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);
            if (usuario != null) {
                session.setAttribute("usuarioLogado", usuario);
                return "redirect:/";
            }
        }

        model.addAttribute("erro", "E-mail ou senha inválidos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
