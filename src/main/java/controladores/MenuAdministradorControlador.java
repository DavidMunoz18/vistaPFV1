package controladores;

import java.io.IOException;
import java.util.List;

import Dtos.ProductoDto;
import Dtos.UsuarioDto;
import Servicios.AutentificacionServicio;
import Servicios.ProductoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin")
public class MenuAdministradorControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener listas de productos y usuarios
        ProductoServicio productoServicio = new ProductoServicio();
        AutentificacionServicio usuarioServicio = new AutentificacionServicio();

        List<ProductoDto> productos = productoServicio.obtenerProductos();
        List<UsuarioDto> usuarios = usuarioServicio.obtenerUsuarios();

        // Depuración en consola
        System.out.println("Tamaño de la lista de productos: " + (productos != null ? productos.size() : "null"));
        System.out.println("Tamaño de la lista de usuarios: " + (usuarios != null ? usuarios.size() : "null"));

        // Enviar las listas al JSP
        request.setAttribute("productos", productos);
        request.setAttribute("usuarios", usuarios);

        // Redirigir a menuAdministrador.jsp
        request.getRequestDispatcher("/menuAdministrador.jsp").forward(request, response);
    }
}
