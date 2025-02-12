package controladores;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.AutentificacionServicio;

@WebServlet("/NuevaContrasenia")
public class NuevaContraseniaControlador extends HttpServlet {
    
    private AutentificacionServicio autentificacionServicio;  // Servicio que manejará la actualización

    @Override
    public void init() throws ServletException {
        // Inicializar el servicio de autentificación
        autentificacionServicio = new AutentificacionServicio();  // Puedes inyectarlo si usas un framework como Spring
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el token de la URL
        String token = request.getParameter("token");  // Captura el token de la URL

        // Validar si el token es válido
        if (token != null && autentificacionServicio.validarToken(token)) {  // Verificar si el token es válido
            request.setAttribute("token", token);  // Pasar el token al JSP
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
        } else {
            // Si no se recibe un token válido, redirigir con mensaje de error
            response.sendRedirect("error.jsp");  // Redirigir a una página de error si el token es inválido
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener las nuevas contraseñas y el token del formulario
        String nuevaContrasenia = request.getParameter("nuevaContrasenia");
        String confirmarContrasenia = request.getParameter("confirmarContrasenia");
        String token = request.getParameter("token");

        // Validar las contraseñas
        if (nuevaContrasenia != null && confirmarContrasenia != null && nuevaContrasenia.equals(confirmarContrasenia)) {
            // Llamar al servicio para actualizar la contraseña usando el token
            boolean exito = autentificacionServicio.actualizarContrasenia(nuevaContrasenia, confirmarContrasenia, token);  // Pasar el token

            if (exito) {
                // Si el proceso de actualización de contraseña es exitoso
                response.sendRedirect("login.jsp"); // Redirigir a la página de login
            } else {
                // Si hubo un error al actualizar la contraseña
                request.setAttribute("error", "Hubo un error al actualizar la contraseña.");
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            }
        } else {
            // Si las contraseñas no coinciden, mostrar un mensaje de error
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
        }
    }
}
