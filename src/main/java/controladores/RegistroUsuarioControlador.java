package controladores;

import java.io.IOException;

import Dtos.RegistroUsuarioDto;
import Servicios.RegistroServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para manejar el registro de usuarios.
 * <p>
 * Este servlet procesa solicitudes HTTP POST para registrar un nuevo usuario,
 * utilizando un servicio específico para realizar la operación de registro.
 * </p>
 * <p>
 * Los datos del usuario se recogen desde un formulario y se encapsulan en un DTO
 * para ser procesados por el servicio.
 * </p>
 */
@WebServlet("/registroUsuario")
public class RegistroUsuarioControlador extends HttpServlet {

    /**
     * Servicio encargado de manejar la lógica de registro del usuario.
     */
    private RegistroServicio registroServicio;

    /**
     * Inicializa el controlador configurando el servicio de registro.
     * 
     * @throws ServletException si ocurre un error en la inicialización del servlet.
     */
    @Override
    public void init() throws ServletException {
        // Inicializar el servicio de registro de usuario
        this.registroServicio = new RegistroServicio();
    }

    /**
     * Procesa las solicitudes HTTP POST para registrar un usuario.
     * <p>
     * Este método recoge los datos enviados desde el formulario de registro,
     * los encapsula en un DTO, y llama al servicio para registrar al usuario.
     * Si el registro es exitoso, redirige al formulario de inicio de sesión;
     * de lo contrario, muestra un mensaje de error.
     * </p>
     * 
     * @param request  el objeto {@link HttpServletRequest} que contiene la solicitud
     *                 del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta
     *                 para el cliente.
     * @throws ServletException si ocurre un error relacionado con el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger los parámetros del formulario de registro
        
        String nombre = request.getParameter("nombreUsuario");
        
        String telefono = request.getParameter("telefonoUsuario");
        String correo = request.getParameter("emailUsuario");
        String password = request.getParameter("passwordUsuario");

        // Crear el objeto DTO con los datos del usuario
        RegistroUsuarioDto registroDto = new RegistroUsuarioDto();
        
        registroDto.setNombreUsuario(nombre);
        registroDto.setTelefonoUsuario(telefono);
        registroDto.setEmailUsuario(correo);
        registroDto.setPasswordUsuario(password);

        // Llamar al servicio para registrar al usuario
        boolean registroExitoso = registroServicio.registrarUsuario(registroDto);

        if (registroExitoso) {
            // Registro exitoso, redirigir a la página de inicio o login
            response.sendRedirect("login.jsp");
        } else {
            // Si el registro falló (correo ya existente), mostrar un mensaje de error
            request.setAttribute("errorMessage", "El correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
