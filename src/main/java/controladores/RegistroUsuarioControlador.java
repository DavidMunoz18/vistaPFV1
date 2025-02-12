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
 * Controlador para manejar el registro de usuarios.
 * Este servlet procesa solicitudes HTTP POST tanto para enviar el código de verificación
 * como para registrar al usuario, utilizando el servicio de registro.
 */
@WebServlet(urlPatterns = {"/registroUsuario", "/enviarCodigo"})
public class RegistroUsuarioControlador extends HttpServlet {

    private RegistroServicio registroServicio;

    @Override
    public void init() throws ServletException {
        this.registroServicio = new RegistroServicio();
    }

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

    private void enviarCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String correo = request.getParameter("emailUsuario");

        if (correo == null || correo.trim().isEmpty()) {
            response.getWriter().write("El correo es obligatorio.");
            return;
        }

        boolean enviado = registroServicio.enviarCodigoVerificacion(correo);

        if (enviado) {
            response.getWriter().write("Se ha enviado un código de verificación a tu correo.");
        } else {
            response.getWriter().write("No se pudo enviar el código. Verifica el correo ingresado.");
        }
    }

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
        registroDto.setPasswordUsuario(password);
        registroDto.setCodigoVerificacion(codigoVerificacion);

        boolean registroExitoso = registroServicio.registrarUsuario(registroDto);

        if (registroExitoso) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("mensaje", "El código de verificación es incorrecto o el correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
