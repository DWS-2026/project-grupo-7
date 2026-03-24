package com.rayokross.academy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        model.addAttribute("pageTitle", "Ups, algo salió mal");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log.warn("Error 404: Página no encontrada - URI: {}", requestUri);
                model.addAttribute("errorMessage", "Lo sentimos, la página que buscas no existe o ha sido movida.");
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("Error 500: Error interno del servidor - URI: {}", requestUri);
                model.addAttribute("errorMessage",
                        "Ha ocurrido un error interno en el servidor. Estamos trabajando en ello.");
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                log.warn("Error 403: Acceso denegado - URI: {}", requestUri);
                model.addAttribute("errorMessage", "No tienes permisos suficientes para acceder a esta sección.");
                return "error/403";

            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.warn("Error 400: Petición incorrecta o URL mal formada - URI: {}", requestUri);
                model.addAttribute("errorMessage",
                        "La dirección que has introducido no es válida o tiene un formato incorrecto.");
                // Puedes crear un 400.html o reutilizar tu 404.html si prefieres
                return "error/400";
            }
        }

        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado.");
        return "error/generic";
    }
}