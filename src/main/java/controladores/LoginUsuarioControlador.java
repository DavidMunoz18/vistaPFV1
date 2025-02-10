package controladores;

import java.io.IOException;

import Servicios.AutentificacionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        // Recoger los parámetros del formulario
        String correo = request.getParameter("email");
        String password = request.getParameter("password");

        // Imprimir los valores para depuración
        System.out.println("Correo recibido: " + correo);
        System.out.println("Contraseña recibida: " + password);

        // Llamar al servicio para verificar el usuario (devuelve boolean)
        boolean isValidUser = servicio.verificarUsuario(correo, password);

        System.out.println("isValidUser: " + isValidUser);

        if (isValidUser) {
            // Obtener el rol y el idUsuario desde el servicio
            String rol = servicio.getRol();
            Long idUsuario = servicio.getId();

            if (rol != null && idUsuario != null) {
                // Guardar el idUsuario y rol en la sesión
                HttpSession session = request.getSession();
                session.setAttribute("idUsuario", idUsuario);
                session.setAttribute("rol", rol);

                // Log para verificar que se guarda correctamente
                System.out.println("idUsuario guardado en la sesión: " + idUsuario);
                System.out.println("Rol del usuario: " + rol);

                // Redirigir según el rol
                if ("admin".equals(rol)) {
                    response.sendRedirect("admin");
                } else if ("usuario".equals(rol)) {
                    response.sendRedirect("inicio");
                } else {
                    request.setAttribute("errorMessage", "Rol desconocido.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Si no se puede obtener el rol o ID
                request.setAttribute("errorMessage", "Error en la autenticación. Respuesta vacía.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Si la autenticación falla
            request.setAttribute("errorMessage", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response); // Redirigir de vuelta al formulario
        }
    }
}