package controladores;

import dtos.RegistroUsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.RegistroServicio;
import java.io.IOException;

/**
 * Controlador para manejar el registro de usuarios y el envío de códigos de verificación.
 * <p>
 * Este servlet gestiona las solicitudes para el registro de usuarios y el envío de códigos de verificación.
 * Maneja dos rutas: "/registroUsuario" para registrar al usuario y "/enviarCodigo" para enviar el código de verificación.
 * </p>
 */
@WebServlet(urlPatterns = {"/registroUsuario", "/enviarCodigo"})
public class RegistroUsuarioControlador extends HttpServlet {

    private RegistroServicio registroServicio;

    /**
     * Inicializa el servicio de registro.
     * <p>
     * Este método se ejecuta una vez cuando se inicia el servlet y prepara el servicio para manejar las solicitudes.
     * </p>
     * 
     * @throws ServletException Si ocurre un error durante la inicialización del servlet.
     */
    @Override
    public void init() throws ServletException {
        this.registroServicio = new RegistroServicio();
    }

    /**
     * Maneja las solicitudes POST para las rutas "/registroUsuario" y "/enviarCodigo".
     * <p>
     * Este método determina qué acción tomar dependiendo de la ruta solicitada, ya sea enviar un código de verificación
     * o registrar al usuario.
     * </p>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se obtiene el path para diferenciar la acción según la URL utilizada
        String path = request.getServletPath();
        if ("/enviarCodigo".equals(path)) {
            enviarCodigo(request, response);
        } else if ("/registroUsuario".equals(path)) {
            registrarUsuario(request, response);
        }
    }

    /**
     * Maneja la solicitud para enviar el código de verificación al correo del usuario.
     * <p>
     * Este método valida el correo electrónico y, si es válido, solicita al servicio que envíe un código de verificación
     * al correo proporcionado.
     * </p>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    private void enviarCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String correo = request.getParameter("emailUsuario");
        if (correo == null || correo.trim().isEmpty()) {
            response.getWriter().write("El correo es obligatorio.");
            return;
        }
        // El Dynamic Web Project genera el código, envía el correo y luego lo manda a la API para almacenar
        boolean enviado = registroServicio.enviarCodigoVerificacion(correo);
        if (enviado) {
            response.getWriter().write("Se ha enviado un código de verificación a tu correo.");
        } else {
            response.getWriter().write("No se pudo enviar el código. Verifica el correo ingresado.");
        }
    }

    /**
     * Maneja la solicitud para registrar un nuevo usuario.
     * <p>
     * Este método valida los datos del formulario de registro, incluyendo la verificación de las contraseñas y el código
     * de verificación, y luego invoca el servicio para registrar al usuario en la base de datos.
     * </p>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombreUsuario");
        String telefono = request.getParameter("telefonoUsuario");
        String correo = request.getParameter("emailUsuario");
        String password = request.getParameter("passwordUsuario");
        String confirmPassword = request.getParameter("confirmPasswordUsuario");
        String codigoVerificacion = request.getParameter("codigoVerificacion");

        if (password == null || !password.equals(confirmPassword)) {
            request.setAttribute("mensaje", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        RegistroUsuarioDto registroDto = new RegistroUsuarioDto();
        registroDto.setNombreUsuario(nombre);
        registroDto.setTelefonoUsuario(telefono);
        registroDto.setEmailUsuario(correo);
        // En este Dynamic Web Project se encriptará la contraseña antes de enviarla a la API.
        registroDto.setPasswordUsuario(password);
        registroDto.setCodigoVerificacion(codigoVerificacion);

        // Se invoca la API para registrar el usuario.
        boolean registroExitoso = registroServicio.registrarUsuario(registroDto);
        if (registroExitoso) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("mensaje", "El código de verificación es incorrecto o el correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
