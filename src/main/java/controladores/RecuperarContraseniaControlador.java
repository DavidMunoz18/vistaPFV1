package controladores;

import Servicios.AutentificacionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recuperar-contrasenia")
public class RecuperarContraseniaControlador extends HttpServlet {

    private AutentificacionServicio autentificacionServicio = new AutentificacionServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");

        // Verificar si el correo está vacío o no tiene un formato válido
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("mensaje", "Correo electrónico es necesario.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);  // Redirigir al mismo JSP
            return;
        }

        // Llamada al servicio para iniciar la recuperación de contraseña
        boolean exito = autentificacionServicio.recuperarContrasenia(correo);

        // Redirigir con el mensaje adecuado
        if (exito) {
            request.setAttribute("mensaje", "Recuperación de contraseña exitosa, revisa tu correo.");
        } else {
            request.setAttribute("mensaje", "No se pudo recuperar la contraseña. Verifica el correo.");
        }
        
        // Redirigir de nuevo al mismo JSP (login.jsp en este caso)
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
