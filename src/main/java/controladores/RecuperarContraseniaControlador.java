package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.AutentificacionServicio;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/recuperar-contrasenia")
public class RecuperarContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio = new AutentificacionServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String correo = request.getParameter("correo");

        // Validación básica del correo
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("mensaje", "El correo electrónico es obligatorio.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validación avanzada (expresión regular)
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("mensaje", "El formato del correo electrónico no es válido.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // Generar un token único
            String token = UUID.randomUUID().toString();
            long fechaExpiracion = System.currentTimeMillis() + 86400000;  // 24 horas en milisegundos

            // Almacenar el token y su hora de generación en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("tokenRecuperacion", token);
            session.setAttribute("tokenGeneradoEn", fechaExpiracion);

            // Llamar al servicio para guardar el token y su fecha de expiración en la API
            boolean exito = autentificacionServicio.recuperarContrasenia(correo, token, fechaExpiracion);
            if (exito) {
                request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
            } else {
                request.setAttribute("mensaje", "No se pudo procesar la solicitud. Verifica que el correo exista en nuestro sistema.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error interno al procesar la solicitud.");
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
