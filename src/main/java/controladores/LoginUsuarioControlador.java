package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.AutentificacionServicio;
import dtos.UsuarioDto;
import utilidades.Utilidades; 

/**
 * Controlador para manejar el inicio de sesión de un usuario.
 * <p>
 * Este servlet procesa solicitudes HTTP POST para autenticar a un usuario,
 * verificar sus credenciales y redirigirlo a la página correspondiente según su rol.
 * </p>
 */
@WebServlet("/loginUsuario")
public class LoginUsuarioControlador extends HttpServlet {

    private AutentificacionServicio servicio;

    /**
     * Método de inicialización del servlet.
     * Este método se ejecuta cuando el servlet es cargado y prepara el servicio de autenticación.
     * 
     * @throws ServletException Si ocurre un error al inicializar el servlet.
     */
    @Override
    public void init() throws ServletException {
        this.servicio = new AutentificacionServicio();
    }

    /**
     * Maneja las solicitudes HTTP POST para procesar el inicio de sesión de un usuario.
     * Verifica las credenciales proporcionadas y redirige a la página correspondiente según el rol del usuario.
     * 
     * @param request La solicitud HTTP recibida, que contiene los parámetros del formulario de inicio de sesión.
     * @param response La respuesta HTTP que será enviada al cliente, con una redirección o mensaje de error.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger los parámetros del formulario de inicio de sesión
        String correo = request.getParameter("email");
        String password = request.getParameter("password");

        // Log: Correo y contraseña recibidos
        Utilidades.escribirLog(request.getSession(), "[INFO]", "LoginUsuarioControlador", "doPost", 
                "Correo recibido: " + correo + ", Contraseña recibida: " + password);

        // Llamar al servicio para verificar el usuario con las credenciales proporcionadas
        UsuarioDto usuario = servicio.verificarUsuario(correo, password);

        if (usuario != null) {
            // Si las credenciales son válidas, guardar el idUsuario y rol en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("rol", usuario.getRol());

            // Log: Datos guardados correctamente en la sesión
            Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                    "idUsuario guardado en la sesión: " + usuario.getIdUsuario() + ", Rol del usuario: " + usuario.getRol());

            // Redirigir según el rol del usuario
            if ("admin".equals(usuario.getRol())) {
                // Log: Redirigiendo a la página de administración
                Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                        "Redirigiendo a la página de administración.");
                response.sendRedirect("admin");
            } else if ("usuario".equals(usuario.getRol())) {
                // Log: Redirigiendo a la página de inicio
                Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                        "Redirigiendo a la página de inicio.");
                response.sendRedirect("inicio");
            } else {
                // Log: Rol desconocido
                Utilidades.escribirLog(session, "[ERROR]", "LoginUsuarioControlador", "doPost", 
                        "Rol desconocido.");

                // Si el rol es desconocido, mostrar un error
                request.setAttribute("errorMessage", "Rol desconocido.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Log: Autenticación fallida
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "LoginUsuarioControlador", "doPost", 
                    "Autenticación fallida para el correo: " + correo);

            // Si la autenticación falla, mostrar un mensaje de error
            request.setAttribute("errorMessage", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response); // Redirigir de vuelta al formulario de login
        }
    }
}
