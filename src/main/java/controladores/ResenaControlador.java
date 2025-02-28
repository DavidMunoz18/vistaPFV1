package controladores;

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
import java.io.IOException;
import java.util.List;
import utilidades.Utilidades;

/**
 * Controlador para gestionar las reseñas de productos.
 */
@WebServlet("/resenas")
public class ResenaControlador extends HttpServlet {

    private ProductoServicio productoServicio;
    private ResenaServicio resenaServicio;

    @Override
    public void init() throws ServletException {
        this.productoServicio = new ProductoServicio();
        this.resenaServicio = new ResenaServicio();
        HttpSession session = null; // No hay sesión en init
        Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "init", "Servicios inicializados.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("idUsuario") == null) {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doGet", "Usuario no logueado. Redirigiendo al login.");
            response.sendRedirect("login.jsp");
            return;
        }

        Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doGet", "Usuario logueado. Continuando con la solicitud GET.");

        String idProducto = request.getParameter("id");
        ProductoDto producto = null;
        List<ReseniaDto> resenias = null;

        if (idProducto != null && !idProducto.isEmpty()) {
            Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doGet", "Se recibió idProducto: " + idProducto);
            producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));

            if (producto != null) {
                Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doGet", "Producto obtenido: " + producto.getNombre());
                resenias = resenaServicio.obtenerReseniasPorProducto(producto.getId());
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doGet", "Producto no encontrado para id: " + idProducto);
            }
        }

        request.setAttribute("producto", producto);
        request.setAttribute("resenias", resenias);

        Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doGet", "Redirigiendo a vistas.jsp.");
        request.getRequestDispatcher("/vistas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("idUsuario") == null) {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Usuario no logueado. Redirigiendo al login.");
            response.sendRedirect("login.jsp");
            return;
        }

        Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doPost", "Usuario logueado. Procesando solicitud POST para agregar reseña.");

        String idProducto = request.getParameter("idProducto");
        String contenidoResena = request.getParameter("contenidoResena");
        String calificacion = request.getParameter("calificacion");

        if (idProducto != null && !idProducto.isEmpty() && contenidoResena != null && !contenidoResena.isEmpty() && calificacion != null) {
            ReseniaDto reseniaDto = new ReseniaDto();
            reseniaDto.setContenidoResena(contenidoResena);
            reseniaDto.setCalificacion(Integer.parseInt(calificacion));

            Long idUsuario = (Long) session.getAttribute("idUsuario");

            ProductoDto productoDto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));
            if (productoDto != null) {
                Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doPost", "Producto encontrado para id: " + idProducto + ". Agregando reseña.");
                boolean exito = resenaServicio.agregarResenia(reseniaDto, productoDto, idUsuario);

                if (exito) {
                    Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doPost", "Reseña agregada exitosamente para producto id: " + idProducto);
                    response.sendRedirect("vistas.jsp?id=" + idProducto);
                } else {
                    Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Error al agregar la reseña para producto id: " + idProducto);
                    request.setAttribute("error", "Hubo un problema al agregar la reseña.");
                    request.getRequestDispatcher("/vistas.jsp").forward(request, response);
                }
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Producto no encontrado para id: " + idProducto);
                request.setAttribute("error", "Producto no encontrado.");
                request.getRequestDispatcher("/vistas.jsp").forward(request, response);
            }
        } else {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Campos obligatorios faltantes para agregar reseña.");
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/vistas.jsp").forward(request, response);
        }
    }
}
