package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet para cerrar la sesión del usuario.
 * <p>
 * Este servlet invalida la sesión actual y redirige al usuario a la página de login.
 * Está mapeado a la URL <code>/cerrarSesion</code>.
 * </p>
 * <p>
 * Ejemplo de uso en una página JSP:
 * <pre>
 *   &lt;li class="nav-item"&gt;
 *     &lt;a class="nav-link" href="${pageContext.request.contextPath}/cerrarSesion"&gt;Cerrar sesión&lt;/a&gt;
 *   &lt;/li&gt;
 * </pre>
 * </p>
 */
@WebServlet("/cerrarSesion")
public class CerrarSesionControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Procesa las solicitudes HTTP GET para cerrar la sesión.
     * <p>
     * Este método obtiene la sesión actual (creándola si no existe),
     * la invalida y redirige al usuario a la página de login.
     * </p>
     *
     * @param request  Objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response Objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener la sesión actual. Se puede usar getSession() para imitar cómo se obtiene en LoginUsuarioControlador.
        HttpSession session = request.getSession();
        if (session != null) {
            // Invalida la sesión para cerrar la sesión del usuario
            session.invalidate();
        }
        // Redirige a la página de login incluyendo el context path
        response.sendRedirect("inicio");
    }

    /**
     * Procesa las solicitudes HTTP POST redirigiéndolas al método doGet.
     * <p>
     * Esto permite que tanto GET como POST manejen el cierre de sesión de la misma manera.
     * </p>
     *
     * @param request  Objeto HttpServletRequest que contiene la solicitud del cliente.
     * @param response Objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
