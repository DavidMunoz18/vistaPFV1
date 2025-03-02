package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.AutentificacionServicio;
import java.io.IOException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import utilidades.Utilidades;

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

            // Log de validación del token
            Utilidades.escribirLog(request.getSession(), "[INFO]", "NuevaContraseniaControlador", "doGet", "Recibido token: " + token);

            if (fechaExpiracion != null && System.currentTimeMillis() <= fechaExpiracion) {
                // Log de token válido
                Utilidades.escribirLog(request.getSession(), "[INFO]", "NuevaContraseniaControlador", "doGet", "Token válido, redirigiendo a restablecer.jsp");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            } else {
                // Log de token expirado o inválido
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "NuevaContraseniaControlador", "doGet", "El token ha expirado o es inválido.");
                request.setAttribute("error", "El token ha expirado o es inválido.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            // Log de error por token no proporcionado
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "NuevaContraseniaControlador", "doGet", "Token no proporcionado.");
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

        // Log de las contraseñas recibidas
        Utilidades.escribirLog(request.getSession(), "[INFO]", "NuevaContraseniaControlador", "doPost", "Recibidas contraseñas para cambio.");

        if (nuevaContrasenia == null || confirmarContrasenia == null || !nuevaContrasenia.equals(confirmarContrasenia)) {
            // Log de error si las contraseñas no coinciden
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "NuevaContraseniaControlador", "doPost", "Las contraseñas no coinciden o están vacías.");
            request.setAttribute("error", "Las contraseñas no coinciden o están vacías.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            return;
        }

        // Encriptar la nueva contraseña usando BCrypt
        String contraseniaEncriptada = BCrypt.hashpw(nuevaContrasenia, BCrypt.gensalt());

        // Log de encriptación de la contraseña
        Utilidades.escribirLog(request.getSession(), "[INFO]", "NuevaContraseniaControlador", "doPost", "Contraseña encriptada correctamente.");

        // Llamar al servicio para actualizar la contraseña
        boolean exito = autentificacionServicio.actualizarContrasenia(contraseniaEncriptada, token);
        if (exito) {
            // Log de éxito en la actualización
            Utilidades.escribirLog(request.getSession(), "[INFO]", "NuevaContraseniaControlador", "doPost", "Contraseña actualizada exitosamente.");
            // Guardar el mensaje en sesión para que persista en el redirect
            request.getSession().setAttribute("mensaje", "Contraseña actualizada exitosamente.");
            request.getSession().setAttribute("tipoMensaje", "exito");
            response.sendRedirect("login.jsp");
        } else {
            // Log de fallo en la actualización
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "NuevaContraseniaControlador", "doPost", "Hubo un error al actualizar la contraseña.");
            request.setAttribute("error", "Hubo un error al actualizar la contraseña.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
        }
    }
}
