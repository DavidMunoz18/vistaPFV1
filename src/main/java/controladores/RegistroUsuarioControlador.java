package controladores;

import dtos.RegistroUsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.RegistroServicio;
import java.io.IOException;
import utilidades.Utilidades;

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
        Utilidades.escribirLog(null, "[INFO]", "RegistroUsuarioControlador", "init", "Servicio de registro inicializado.");
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
        Utilidades.escribirLog(request.getSession(), "[INFO]", "RegistroUsuarioControlador", "doPost", "Procesando solicitud POST en path: " + request.getServletPath());
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
        Utilidades.escribirLog(request.getSession(), "[INFO]", "RegistroUsuarioControlador", "enviarCodigo", "Procesando envío de código de verificación para correo: " + correo);
        if (correo == null || correo.trim().isEmpty()) {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RegistroUsuarioControlador", "enviarCodigo", "Correo obligatorio no proporcionado.");
            response.getWriter().write("El correo es obligatorio.");
            return;
        }
        boolean enviado = registroServicio.enviarCodigoVerificacion(correo);
        if (enviado) {
            Utilidades.escribirLog(request.getSession(), "[INFO]", "RegistroUsuarioControlador", "enviarCodigo", "Código de verificación enviado a: " + correo);
            response.getWriter().write("Se ha enviado un código de verificación a tu correo.");
        } else {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RegistroUsuarioControlador", "enviarCodigo", "Error al enviar el código a: " + correo);
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

        Utilidades.escribirLog(request.getSession(), "[INFO]", "RegistroUsuarioControlador", "registrarUsuario", "Inicio proceso de registro para: " + correo);
        if (password == null || !password.equals(confirmPassword)) {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RegistroUsuarioControlador", "registrarUsuario", "Contraseñas no coinciden para: " + correo);
            request.setAttribute("mensaje", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        RegistroUsuarioDto registroDto = new RegistroUsuarioDto();
        registroDto.setNombreUsuario(nombre);
        registroDto.setTelefonoUsuario(telefono);
        registroDto.setEmailUsuario(correo);
        registroDto.setPasswordUsuario(password);
        registroDto.setCodigoVerificacion(codigoVerificacion);

        boolean registroExitoso = registroServicio.registrarUsuario(registroDto);
        if (registroExitoso) {
            Utilidades.escribirLog(request.getSession(), "[INFO]", "RegistroUsuarioControlador", "registrarUsuario", "Registro exitoso para: " + correo);
            response.sendRedirect("login.jsp");
        } else {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RegistroUsuarioControlador", "registrarUsuario", "Error en registro para: " + correo);
            request.setAttribute("mensaje", "El código de verificación es incorrecto o el correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
