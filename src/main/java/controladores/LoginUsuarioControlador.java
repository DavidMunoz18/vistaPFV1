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

    @Override
    public void init() throws ServletException {
        this.servicio = new AutentificacionServicio();
    }

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
            session.setAttribute("nombreUsuario", usuario.getNombreUsuario());
            session.setAttribute("rol", usuario.getRol());

            // Log: Datos guardados correctamente en la sesión
            Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                    "idUsuario guardado en la sesión: " + usuario.getIdUsuario() + ", Rol del usuario: " + usuario.getRol());

            // Si se recibió el parámetro returnURL, redirigir al usuario a esa URL
            String returnURL = request.getParameter("returnURL");
            if(returnURL != null && !returnURL.isEmpty()){
                Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                        "Redirigiendo a returnURL: " + returnURL);
                response.sendRedirect(returnURL);
                return;
            }

            // Redirigir según el rol del usuario si no se proporciona returnURL
            if ("admin".equals(usuario.getRol())) {
                Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                        "Redirigiendo a la página de administración.");
                response.sendRedirect("admin");
            } else if ("usuario".equals(usuario.getRol())) {
                Utilidades.escribirLog(session, "[INFO]", "LoginUsuarioControlador", "doPost", 
                        "Redirigiendo a la página de inicio.");
                response.sendRedirect("inicio");
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "LoginUsuarioControlador", "doPost", 
                        "Rol desconocido.");
                request.setAttribute("errorMessage", "Rol desconocido.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Log: Autenticación fallida
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "LoginUsuarioControlador", "doPost", 
                    "Autenticación fallida para el correo: " + correo);
            request.setAttribute("errorMessage", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
