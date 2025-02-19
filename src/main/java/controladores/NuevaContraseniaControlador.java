package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.AutentificacionServicio;
import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Controlador para manejar el restablecimiento de la contraseña de un usuario.
 * <p>
 * Este servlet procesa solicitudes GET y POST para permitir que un usuario
 * restablezca su contraseña, validando el token de recuperación y asegurándose
 * de que las contraseñas coincidan antes de actualizar la contraseña en la base de datos.
 * </p>
 */
@WebServlet("/NuevaContrasenia")
public class NuevaContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio;

    /**
     * Inicializa el servicio de autenticación.
     * Este método se llama cuando el servlet se inicializa, y configura
     * el servicio que gestiona la autenticación y el cambio de contraseñas.
     */
    @Override
    public void init() throws ServletException {
        autentificacionServicio = new AutentificacionServicio();
    }

    /**
     * Maneja las solicitudes HTTP GET para verificar el token y redirigir
     * al usuario a la página de restablecimiento de contraseña.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
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

    /**
     * Maneja las solicitudes HTTP POST para actualizar la contraseña del usuario
     * si las contraseñas coinciden, encriptándola antes de almacenarla en la base de datos.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
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
