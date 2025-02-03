package controladores;

import java.io.IOException;

import Servicios.EliminarServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para manejar la eliminación de un usuario.
 * <p>
 * Este servlet procesa solicitudes HTTP POST para eliminar un usuario de la base de datos
 * utilizando un servicio específico.
 * </p>
 */
@WebServlet("/eliminarUsuario")
public class EliminarUsuarioControlador extends HttpServlet {

    /**
     * Servicio encargado de manejar la lógica de eliminación de usuarios.
     */
    private EliminarServicio eliminarServicio = new EliminarServicio();

    /**
     * Procesa las solicitudes HTTP POST para eliminar un usuario.
     * <p>
     * Este método recibe el ID del usuario como parámetro, llama al servicio correspondiente
     * para eliminar al usuario, y redirige al usuario a la página de menú del administrador
     * con un mensaje de resultado.
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
        try {
            // Recuperar el ID del usuario desde los parámetros del formulario
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));

            // Llamar al servicio para eliminar el usuario
            String resultado = eliminarServicio.eliminarUsuario(idUsuario);

            // Redirigir con el resultado
            request.setAttribute("resultado", resultado);
            request.getRequestDispatcher("menuAdministrador.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Manejo de errores cuando el ID no es un número válido
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido.");
        } catch (Exception e) {
            // Manejo de errores genéricos durante la ejecución
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
