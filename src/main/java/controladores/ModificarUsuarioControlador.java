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
import utilidades.Utilidades;

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
            // Log de inicio de proceso
            Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarUsuarioControlador", "doPost", "Iniciando modificación de usuario...");

            // Recuperar parámetros del formulario
            long idUsuario = Long.parseLong(request.getParameter("idUsuario"));
            String nuevoNombre = request.getParameter("nuevoNombre");
            String nuevoDni = request.getParameter("nuevoDni");
            String nuevoTelefono = request.getParameter("nuevoTelefono");
            String nuevoRol = request.getParameter("nuevoRol");

            // Log de datos recibidos
            Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarUsuarioControlador", "doPost", "Recibidos datos: ID: " + idUsuario + ", Nombre: " + nuevoNombre + ", DNI: " + nuevoDni);

            // Procesar el archivo de la foto
            Part fotoPart = request.getPart("nuevaFoto");
            byte[] nuevaFoto = null;

            if (fotoPart != null && fotoPart.getSize() > 0) {
                nuevaFoto = fotoPart.getInputStream().readAllBytes();
                // Log de foto recibida
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarUsuarioControlador", "doPost", "Foto recibida: " + fotoPart.getSubmittedFileName());
            } else {
                // Log de no foto recibida
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarUsuarioControlador", "doPost", "No se recibió foto, continuará sin foto.");
            }

            // Llamar al servicio API
            String resultado = modificarServicio.modificarUsuario(
                    idUsuario, nuevoNombre, nuevoDni, nuevoTelefono, nuevoRol, nuevaFoto);

            if (resultado != null && resultado.toLowerCase().contains("actualizado")) {
                // Log de éxito
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarUsuarioControlador", "doPost", "Usuario con ID " + idUsuario + " modificado con éxito.");
                response.sendRedirect("admin?modificado=true");
            } else {
                // Log de fallo
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "ModificarUsuarioControlador", "doPost", "No se pudo modificar el usuario con ID " + idUsuario);
                response.sendRedirect("admin?modificado=false");
            }


        } catch (Exception e) {
            // Log del error
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "ModificarUsuarioControlador", "doPost", "Error en el proceso de modificación de usuario: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
