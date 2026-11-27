package com.GIU26B.KronoCash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Vistas informativas públicas: declaración de accesibilidad y aviso legal.
 * Las rutas están permitidas en {@code SecurityConfig} para que cualquier
 * visitante pueda consultarlas antes de iniciar sesión, sin interrumpir la
 * navegación ni provocar redirecciones de autenticación.
 */
@Controller
public class LegalController {

    /** Declaración de accesibilidad WCAG 2.2 AAA. */
    @GetMapping("/accesibilidad")
    public String accessibilityStatement() {
        return "public/accessibility-statement";
    }

    /** Términos y aviso de proyecto académico. */
    @GetMapping("/terminos")
    public String terms() {
        return "public/terms";
    }
}
