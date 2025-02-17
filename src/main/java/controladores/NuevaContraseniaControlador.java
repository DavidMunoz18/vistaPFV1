package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.AutentificacionServicio;
import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet("/NuevaContrasenia")
public class NuevaContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio; 

    @Override
    public void init() throws ServletException {
        autentificacionServicio = new AutentificacionServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token != null) {
            Long fechaExpiracion = autentificacionServicio.obtenerFechaExpiracionDelToken(token);
            
            if (fechaExpiracion != null && System.currentTimeMillis() <= fechaExpiracion) {
                request.setAttribute("token", token);
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "El token ha expirado o es inválido.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Token no proporcionado.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nuevaContrasenia = request.getParameter("nuevaContrasenia");
        String confirmarContrasenia = request.getParameter("confirmarContrasenia");
        String token = request.getParameter("token");

        if (nuevaContrasenia == null || confirmarContrasenia == null || !nuevaContrasenia.equals(confirmarContrasenia)) {
            request.setAttribute("error", "Las contraseñas no coinciden o están vacías.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            return;
        }

        // Encriptar la nueva contraseña usando BCrypt
        String contraseniaEncriptada = BCrypt.hashpw(nuevaContrasenia, BCrypt.gensalt());

        // Llamar al servicio para actualizar la contraseña
        boolean exito = autentificacionServicio.actualizarContrasenia(contraseniaEncriptada, token);
        if (exito) {
            request.setAttribute("mensaje", "Contraseña actualizada exitosamente.");
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Hubo un error al actualizar la contraseña.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
        }
    }
}
