package br.com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para redirecionar todas as requisições não-API para o index.html
 * Isso é necessário para o funcionamento correto de Single Page Applications (SPA)
 */
@Controller
public class SpaController {

    /**
     * Redireciona todas as requisições que não são para arquivos estáticos ou APIs
     * para o index.html, permitindo que o frontend lide com o roteamento
     *
     * @return O nome da view (index.html)
     */
    @GetMapping(value = {
            "/",
            "/index.html",
            "/pages/**",
            "/**/{path:[^\\.]*}"
    })
    public String forwardToIndex() {
        return "forward:/index.html";
    }
} 