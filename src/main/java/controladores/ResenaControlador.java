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
import java.net.URLEncoder;

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
        
        // Si el usuario no está logueado, redirigir al login pasando la URL de retorno
        if (session.getAttribute("idUsuario") == null) {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doGet", "Usuario no logueado. Redirigiendo al login.");
            String returnURL = "vistas.jsp";
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()){
                returnURL += "?id=" + request.getParameter("id");
            }
            response.sendRedirect("login.jsp?returnURL=" + URLEncoder.encode(returnURL, "UTF-8"));
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
        
        // Si el usuario no está logueado, redirigir al login pasando la URL de retorno
        if (session.getAttribute("idUsuario") == null) {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Usuario no logueado. Redirigiendo al login.");
            String returnURL = "vistas.jsp";
            if (request.getParameter("idProducto") != null && !request.getParameter("idProducto").isEmpty()){
                returnURL += "?id=" + request.getParameter("idProducto");
            }
            response.sendRedirect("login.jsp?returnURL=" + URLEncoder.encode(returnURL, "UTF-8"));
            return;
        }

        Utilidades.escribirLog(session, "[INFO]", "ResenaControlador", "doPost", "Usuario logueado. Procesando solicitud POST para agregar reseña.");

        String idProducto = request.getParameter("idProducto");
        String contenidoResena = request.getParameter("contenidoResena");
        String calificacion = request.getParameter("calificacion");

        if (idProducto != null && !idProducto.isEmpty() &&
            contenidoResena != null && !contenidoResena.isEmpty() &&
            calificacion != null) {

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
                    session.setAttribute("mensaje", "Reseña agregada exitosamente.");
                    session.setAttribute("tipoMensaje", "success");
                    response.sendRedirect("vistas.jsp?id=" + idProducto);
                } else {
                    Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Error al agregar la reseña para producto id: " + idProducto);
                    session.setAttribute("mensaje", "Hubo un problema al agregar la reseña.");
                    session.setAttribute("tipoMensaje", "error");
                    response.sendRedirect("vistas.jsp?id=" + idProducto);
                }
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Producto no encontrado para id: " + idProducto);
                session.setAttribute("mensaje", "Producto no encontrado.");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("vistas.jsp");
            }
        } else {
            Utilidades.escribirLog(session, "[ERROR]", "ResenaControlador", "doPost", "Campos obligatorios faltantes para agregar reseña.");
            session.setAttribute("mensaje", "Todos los campos son obligatorios.");
            session.setAttribute("tipoMensaje", "error");
            // Se redirige a la vista con el id del producto (si se dispone) para que se repopule la información
            response.sendRedirect("vistas.jsp?id=" + (idProducto != null ? idProducto : ""));
        }
    }
}
