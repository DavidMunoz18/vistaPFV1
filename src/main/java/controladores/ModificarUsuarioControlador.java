package controladores;

import java.io.IOException;

import Servicios.ModificarServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Controlador para manejar la modificación de un usuario.
 * <p>
 * Este servlet procesa solicitudes HTTP POST para modificar la información de un usuario,
 * incluyendo su nombre, DNI, teléfono, rol y foto, utilizando un servicio específico.
 * </p>
 * <p>
 * Soporta la subida de archivos utilizando la anotación {@link MultipartConfig}.
 * </p>
 */
@WebServlet("/modificarUsuario")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class ModificarUsuarioControlador extends HttpServlet {

    /**
     * Servicio encargado de manejar la lógica de modificación del usuario.
     */
    private ModificarServicio modificarServicio;

    /**
     * Inicializa el controlador configurando el servicio de modificación.
     * 
     * @throws ServletException si ocurre un error en la inicialización del servlet.
     */
    @Override
    public void init() throws ServletException {
        this.modificarServicio = new ModificarServicio(); // Inicializa el servicio aquí
    }

    /**
     * Procesa las solicitudes HTTP POST para modificar un usuario.
     * <p>
     * Este método recoge los datos enviados desde el formulario, incluyendo el ID del usuario,
     * el nuevo nombre, DNI, teléfono, rol y foto. Llama al servicio para realizar la
     * modificación y redirige al usuario a la página de menú del administrador
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
            // Recuperar parámetros del formulario
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
            String nuevoNombre = request.getParameter("nuevoNombre");
            String nuevoDni = request.getParameter("nuevoDni");
            String nuevoTelefono = request.getParameter("nuevoTelefono");
            String nuevoRol = request.getParameter("nuevoRol");

            // Procesar el archivo de la foto
            Part fotoPart = request.getPart("nuevaFoto");
            byte[] nuevaFoto = null;

            if (fotoPart != null && fotoPart.getSize() > 0) {
                nuevaFoto = fotoPart.getInputStream().readAllBytes();
            }

            // Llamar al servicio API
            String resultado = modificarServicio.modificarUsuario(
                    idUsuario, nuevoNombre, nuevoDni, nuevoTelefono, nuevoRol, nuevaFoto);

            // Redirigir con el resultado
            request.setAttribute("resultado", resultado);
            request.getRequestDispatcher("menuAdministrador.jsp").forward(request, response);

        } catch (Exception e) {
            // Manejo de errores durante la ejecución
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
