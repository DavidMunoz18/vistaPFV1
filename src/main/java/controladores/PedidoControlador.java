package controladores;

import java.io.IOException;

import Dtos.PedidoDto;
import Servicios.PedidoServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/pedidos")
public class PedidoControlador extends HttpServlet {

    private PedidoServicio pedidoServicio = new PedidoServicio();  // Instancia del servicio

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el idUsuario de la sesión
        HttpSession session = request.getSession();
        Long idUsuario = (Long) session.getAttribute("idUsuario");

        // Verificar que el idUsuario está presente
        if (idUsuario == null) {
            response.sendRedirect("login.jsp");  // Redirigir a login si no hay sesión
            return;
        }

        // Recoger los parámetros del pedido
        String contacto = request.getParameter("contacto");
        String direccion = request.getParameter("direccion");
        String metodoPago = request.getParameter("metodoPago");
        String nombreTarjeta = request.getParameter("nombreTarjeta");
        String numeroTarjeta = request.getParameter("numeroTarjeta");
        String fechaExpiracion = request.getParameter("fechaExpiracionMes") + "/" + request.getParameter("fechaExpiracionAnio");
        String cvc = request.getParameter("cvc");

        // Crear el objeto PedidoDto con los datos recibidos
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setContacto(contacto);
        pedidoDto.setDireccion(direccion);
        pedidoDto.setMetodoPago(metodoPago);
        pedidoDto.setNombreTarjeta(nombreTarjeta);
        pedidoDto.setNumeroTarjeta(numeroTarjeta);
        pedidoDto.setFechaExpiracion(fechaExpiracion);
        pedidoDto.setCvc(cvc);
        pedidoDto.setIdUsuario(idUsuario);  // Usar el idUsuario de la sesión

        try {
            // Llamar al servicio para crear el pedido
            String mensaje = pedidoServicio.crearPedido(pedidoDto);

            if (mensaje.equals("Pedido creado correctamente")) {
                response.setStatus(HttpServletResponse.SC_CREATED);  // Código de éxito
                response.getWriter().write(mensaje);
            } else {
                // En caso de error al crear el pedido
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(mensaje);
            }

        } catch (Exception e) {
            e.printStackTrace();  // Imprimir detalles del error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Error en el servidor
            response.getWriter().write("Error al crear el pedido: " + e.getMessage());  // Mensaje de error
        }
    }
}
