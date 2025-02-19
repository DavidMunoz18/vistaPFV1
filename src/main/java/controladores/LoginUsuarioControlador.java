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

        // Imprimir los valores para depuración (puede ser útil durante el desarrollo)
        System.out.println("Correo recibido: " + correo);
        System.out.println("Contraseña recibida: " + password);

        // Llamar al servicio para verificar el usuario con las credenciales proporcionadas
        UsuarioDto usuario = servicio.verificarUsuario(correo, password);

        if (usuario != null) {
            // Si las credenciales son válidas, guardar el idUsuario y rol en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("rol", usuario.getRol());

            // Log para verificar que los datos se guardan correctamente en la sesión
            System.out.println("idUsuario guardado en la sesión: " + usuario.getIdUsuario());
            System.out.println("Rol del usuario: " + usuario.getRol());

            // Redirigir según el rol del usuario
            if ("admin".equals(usuario.getRol())) {
                // Si el usuario es un administrador, redirigir a la página de administración
                response.sendRedirect("admin");
            } else if ("usuario".equals(usuario.getRol())) {
                // Si el usuario es un usuario normal, redirigir a la página de inicio
                response.sendRedirect("inicio");
            } else {
                // Si el rol es desconocido, mostrar un error
                request.setAttribute("errorMessage", "Rol desconocido.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // Si la autenticación falla, mostrar un mensaje de error
            request.setAttribute("errorMessage", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response); // Redirigir de vuelta al formulario de login
        }
    }
}
