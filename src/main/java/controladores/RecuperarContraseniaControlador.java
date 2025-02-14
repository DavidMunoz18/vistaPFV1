package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.AutentificacionServicio;
import java.io.IOException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/recuperar-contrasenia")
public class RecuperarContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio = new AutentificacionServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");

        // Validación básica del correo
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("mensaje", "El correo electrónico es obligatorio.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validación más avanzada del correo (expresión regular)
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("mensaje", "El formato del correo electrónico no es válido.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // Generar un token de recuperación único
            String token = UUID.randomUUID().toString();

           

            // Este controlador solo maneja la parte de envío de correo y generación de token
            boolean exito = autentificacionServicio.recuperarContrasenia(correo, token);
            if (exito) {
                request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
            } else {
                request.setAttribute("mensaje", "No se pudo procesar la solicitud. Verifica que el correo exista en nuestro sistema.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // En producción, utiliza un framework de logging
            request.setAttribute("mensaje", "Error interno al procesar la solicitud.");
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
