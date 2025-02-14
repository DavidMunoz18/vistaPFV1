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

        // Llamar al servicio para verificar el usuario
        UsuarioDto usuario = servicio.verificarUsuario(correo, password);

        if (usuario != null) {
            // Guardar el idUsuario y rol en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("rol", usuario.getRol());

            // Log para verificar que se guarda correctamente
            System.out.println("idUsuario guardado en la sesión: " + usuario.getIdUsuario());
            System.out.println("Rol del usuario: " + usuario.getRol());

            // Redirigir según el rol
            if ("admin".equals(usuario.getRol())) {
                response.sendRedirect("admin");
            } else if ("usuario".equals(usuario.getRol())) {
                response.sendRedirect("inicio");
            } else {
                request.setAttribute("errorMessage", "Rol desconocido.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Si la autenticación falla
            request.setAttribute("errorMessage", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response); // Redirigir de vuelta al formulario
        }
    }
}
