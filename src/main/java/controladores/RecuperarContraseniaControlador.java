package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.AutentificacionServicio;
import java.io.IOException;
import java.util.UUID;
import utilidades.Utilidades;

/**
 * Controlador para gestionar la recuperación de contraseñas.
 * <p>
 * Este servlet maneja la solicitud de recuperación de contraseña mediante el correo electrónico.
 * Valida el correo, genera un token único y lo almacena en la sesión para su uso posterior en el proceso de recuperación.
 * </p>
 */
@WebServlet("/recuperar-contrasenia")
public class RecuperarContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio = new AutentificacionServicio();

    /**
     * Maneja la solicitud POST para la recuperación de la contraseña.
     * <p>
     * Este método valida el correo electrónico proporcionado, genera un token único y almacena el token en la sesión
     * para que el usuario pueda usarlo en la recuperación de su contraseña.
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
        String correo = request.getParameter("correo");
        
        // Log: Inicio del proceso de recuperación
        Utilidades.escribirLog(request.getSession(), "[INFO]", "RecuperarContraseniaControlador", "doPost", 
                "Inicio de recuperación de contraseña para correo: " + correo);

        // Validación básica del correo
        if (correo == null || correo.trim().isEmpty()) {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RecuperarContraseniaControlador", "doPost", 
                    "El correo electrónico es obligatorio o está vacío.");
            request.setAttribute("mensaje", "El correo electrónico es obligatorio.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validación avanzada (expresión regular)
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RecuperarContraseniaControlador", "doPost", 
                    "Formato del correo inválido: " + correo);
            request.setAttribute("mensaje", "El formato del correo electrónico no es válido.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // Generar un token único
            String token = UUID.randomUUID().toString();
            long fechaExpiracion = System.currentTimeMillis() + 86400000;  // 24 horas en milisegundos

            // Log: Token generado
            Utilidades.escribirLog(request.getSession(), "[INFO]", "RecuperarContraseniaControlador", "doPost", 
                    "Token generado: " + token + " con fecha de expiración: " + fechaExpiracion);

            // Almacenar el token y su hora de generación en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("tokenRecuperacion", token);
            session.setAttribute("tokenGeneradoEn", fechaExpiracion);

            // Llamar al servicio para guardar el token y su fecha de expiración en la API
            boolean exito = autentificacionServicio.recuperarContrasenia(correo, token, fechaExpiracion);
            if (exito) {
                Utilidades.escribirLog(request.getSession(), "[INFO]", "RecuperarContraseniaControlador", "doPost", 
                        "Enlace de recuperación enviado a: " + correo);
                request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "RecuperarContraseniaControlador", "doPost", 
                        "No se pudo procesar la solicitud para el correo: " + correo);
                request.setAttribute("mensaje", "No se pudo procesar la solicitud. Verifica que el correo exista en nuestro sistema.");
                request.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "RecuperarContraseniaControlador", "doPost", 
                    "Error interno: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Error interno al procesar la solicitud.");
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
