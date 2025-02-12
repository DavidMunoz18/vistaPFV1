package controladores;

import java.io.IOException;
import java.util.List;

import dtos.ProductoDto;
import dtos.ReseniaDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.ProductoServicio;
import servicios.ResenaServicio;

@WebServlet("/resenas")
public class ResenaControlador extends HttpServlet {

    private ProductoServicio productoServicio;
    private ResenaServicio resenaServicio;

    // Inicialización de los servicios
    @Override
    public void init() throws ServletException {
        this.productoServicio = new ProductoServicio();
        this.resenaServicio = new ResenaServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Comprobar si el usuario está logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            // Si no está logueado, redirigir al login
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener idProducto del parámetro
        String idProducto = request.getParameter("id");
        ProductoDto producto = null;
        List<ReseniaDto> resenias = null;

        if (idProducto != null && !idProducto.isEmpty()) {
            // Obtener producto
            producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));
            if (producto != null) {
                // Obtener reseñas del producto
                resenias = resenaServicio.obtenerReseniasPorProducto(producto.getId());
            }
        }

        // Establecer atributos en la solicitud para la vista JSP
        request.setAttribute("producto", producto);
        request.setAttribute("resenias", resenias);

        // Redirigir a la vista JSP
        request.getRequestDispatcher("/vistas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Comprobar si el usuario está logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            // Si no está logueado, redirigir al login
            response.sendRedirect("login.jsp");
            return;
        }

        String idProducto = request.getParameter("idProducto");
        String contenidoResena = request.getParameter("contenidoResena");
        String calificacion = request.getParameter("calificacion");

        if (idProducto != null && !idProducto.isEmpty() && contenidoResena != null && !contenidoResena.isEmpty() && calificacion != null) {
            // Crear objeto ReseniaDto
            ReseniaDto reseniaDto = new ReseniaDto();
            reseniaDto.setContenidoResena(contenidoResena);
            reseniaDto.setCalificacion(Integer.parseInt(calificacion));

            // Obtener idUsuario desde la sesión
            Long idUsuario = (Long) session.getAttribute("idUsuario");

            // Obtener producto y agregar reseña
            ProductoDto productoDto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));
            if (productoDto != null) {
                boolean exito = resenaServicio.agregarResenia(reseniaDto, productoDto, idUsuario);
                if (exito) {
                    // Redirigir para mostrar la página con la nueva reseña
                    response.sendRedirect("vistas.jsp?id=" + idProducto);
                } else {
                    request.setAttribute("error", "Hubo un problema al agregar la reseña.");
                    request.getRequestDispatcher("/vistas.jsp").forward(request, response);
                }
            }
        } else {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/vistas.jsp").forward(request, response);
        }
    }
}
