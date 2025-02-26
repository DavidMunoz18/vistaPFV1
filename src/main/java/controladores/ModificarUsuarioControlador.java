package controladores;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servicios.ModificarServicio;

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

    private ModificarServicio modificarServicio;

    @Override
    public void init() throws ServletException {
        this.modificarServicio = new ModificarServicio(); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("Iniciando modificación de usuario...");

            // Recuperar parámetros del formulario
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
            String nuevoNombre = request.getParameter("nuevoNombre");
            String nuevoDni = request.getParameter("nuevoDni");
            String nuevoTelefono = request.getParameter("nuevoTelefono");
            String nuevoRol = request.getParameter("nuevoRol");

            System.out.println("Recibidos datos: ID: " + idUsuario + ", Nombre: " + nuevoNombre + ", DNI: " + nuevoDni);

            // Procesar el archivo de la foto
            Part fotoPart = request.getPart("nuevaFoto");
            byte[] nuevaFoto = null;

            if (fotoPart != null && fotoPart.getSize() > 0) {
                nuevaFoto = fotoPart.getInputStream().readAllBytes();
                System.out.println("Foto recibida: " + fotoPart.getSubmittedFileName());
            } else {
                System.out.println("No se recibió foto, continuará sin foto.");
            }

            // Llamar al servicio API
            String resultado = modificarServicio.modificarUsuario(
                    idUsuario, nuevoNombre, nuevoDni, nuevoTelefono, nuevoRol, nuevaFoto);

            if (resultado != null && resultado.toLowerCase().contains("actualizado")) {
                response.sendRedirect("admin?modificado=true");
            } else {
                response.sendRedirect("admin?modificado=false");
            }


        } catch (Exception e) {
            // Manejo de errores
            System.out.println("Error en el proceso de modificación de usuario: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
