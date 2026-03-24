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
        // Obtenemos el código de estado del error desde la petición
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Título por defecto para la pestaña del navegador
        model.addAttribute("pageTitle", "Ups, algo salió mal");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // Logueamos la URI que causó el problema para depuración
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
            }
        }

        // Si es otro tipo de error, mostramos una vista genérica
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado.");
        return "error/generic";
    }
}