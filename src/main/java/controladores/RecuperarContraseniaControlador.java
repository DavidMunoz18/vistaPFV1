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

        // Validación básica del correo
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("mensaje", "El correo electrónico es obligatorio.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validación avanzada (expresión regular)
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("mensaje", "El formato del correo electrónico no es válido.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // Generar un token único
            String token = UUID.randomUUID().toString();
            long fechaExpiracion = System.currentTimeMillis() + 86400000;  // 24 horas en milisegundos

            // Almacenar el token y su hora de generación en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("tokenRecuperacion", token);
            session.setAttribute("tokenGeneradoEn", fechaExpiracion);

            // Llamar al servicio para guardar el token y su fecha de expiración en la API
            boolean exito = autentificacionServicio.recuperarContrasenia(correo, token, fechaExpiracion);
            if (exito) {
                request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
                request.setAttribute("tipoMensaje", "exito");
            } else {
                request.setAttribute("mensaje", "No se pudo procesar la solicitud. Verifica que el correo exista en nuestro sistema.");
                request.setAttribute("tipoMensaje", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error interno al procesar la solicitud.");
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
