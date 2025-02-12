package controladores;

import java.io.IOException;
import java.util.List;

import dtos.ProductoDto;
import dtos.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.AutentificacionServicio;
import servicios.ProductoServicio;

@WebServlet("/admin")
public class MenuAdministradorControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        // Verificar que el usuario esté autenticado y tenga rol de administrador
        HttpSession sesion = solicitud.getSession(false);
        if (sesion == null || sesion.getAttribute("rol") == null || 
                !sesion.getAttribute("rol").toString().equalsIgnoreCase("admin")) {
            // Redirigir al login si no está autenticado o no tiene rol "admin"
            respuesta.sendRedirect(solicitud.getContextPath() + "/login.jsp");
            return;
        }

        // Obtener listas de productos y usuarios
        ProductoServicio servicioProducto = new ProductoServicio();
        AutentificacionServicio servicioUsuario = new AutentificacionServicio();

        List<ProductoDto> listaProductos = servicioProducto.obtenerProductos();
        List<UsuarioDto> listaUsuarios = servicioUsuario.obtenerUsuarios();

        // Depuración en consola
        System.out.println("Tamaño de la lista de productos: " + (listaProductos != null ? listaProductos.size() : "null"));
        System.out.println("Tamaño de la lista de usuarios: " + (listaUsuarios != null ? listaUsuarios.size() : "null"));

        // Enviar las listas al JSP
        solicitud.setAttribute("productos", listaProductos);
        solicitud.setAttribute("usuarios", listaUsuarios);

        // Redirigir a menuAdministrador.jsp
        solicitud.getRequestDispatcher("/menuAdministrador.jsp").forward(solicitud, respuesta);
    }
}
