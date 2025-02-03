package controladores;

import java.io.IOException;
import java.util.List;

import Dtos.ProductoDto;
import Servicios.ProductoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/inicio")
public class InicioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductoServicio productoServicio = new ProductoServicio();
        List<ProductoDto> productos = productoServicio.obtenerProductos();

        if (productos != null && !productos.isEmpty()) {
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("mensaje", "No hay productos disponibles.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}