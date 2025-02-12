package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dtos.CarritoDto;
import dtos.PedidoDto;
import dtos.PedidoProductoDto;
import servicios.PedidoServicio;
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
        String cvc = request.getParameter("cvv");

        // Obtener el carrito de la sesión (CarritoDto)
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

       
        session.setAttribute("carrito", carrito);  // carrito es la lista de productos en el carrito

        // Imprimir el contenido del carrito en consola para depuración
        if (carrito == null) {
            System.out.println("Carrito en sesión: null");
        } else if (carrito.isEmpty()) {
            System.out.println("Carrito en sesión: vacío");
        } else {
            System.out.println("Contenido del carrito:");
            for (CarritoDto item : carrito) {
                System.out.println("- ID: " + item.getId() + ", Nombre: " + item.getNombre() + 
                                   ", Cantidad: " + item.getCantidad() + ", Precio: " + item.getPrecio());
            }
        }

        // Verificar que el carrito no esté vacío
        if (carrito == null || carrito.isEmpty()) {
            request.setAttribute("mensaje", "El carrito está vacío");
            request.setAttribute("tipoMensaje", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Crear la lista de productos para el pedido (solo los campos necesarios)
        List<PedidoProductoDto> productosPedido = new ArrayList<>();
        for (CarritoDto carritoDto : carrito) {
            PedidoProductoDto productoPedidoDto = new PedidoProductoDto(
                    carritoDto.getId(),
                    carritoDto.getNombre(),
                    carritoDto.getCantidad(),
                    carritoDto.getPrecio()
            );
            productosPedido.add(productoPedidoDto);
        }

        // Crear el objeto PedidoDto con los datos recibidos
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setContacto(contacto);
        pedidoDto.setDireccion(direccion);
        pedidoDto.setMetodoPago(metodoPago);
        pedidoDto.setNombreTarjeta(nombreTarjeta);
        pedidoDto.setNumeroTarjeta(numeroTarjeta);
        pedidoDto.setFechaExpiracion(fechaExpiracion);
        pedidoDto.setCvc(cvc);
        pedidoDto.setIdUsuario(idUsuario);
        pedidoDto.setProductos(productosPedido);

        String mensaje = null;
        String tipoMensaje = "success";

        try {
            // Llamar al servicio para crear el pedido
            mensaje = pedidoServicio.crearPedido(pedidoDto);

            if (!mensaje.equals("Pedido creado correctamente")) {
                tipoMensaje = "error";
            } else {
                // Eliminar el carrito de la sesión solo cuando el pedido se haya creado correctamente
                session.removeAttribute("carrito");
            }

        } catch (Exception e) {
            tipoMensaje = "error";
            mensaje = "Error al crear el pedido: " + e.getMessage();
        }

        // Agregar el mensaje y tipo de mensaje a la solicitud
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("tipoMensaje", tipoMensaje);

        // Redirigir a la página del carrito con el mensaje en la solicitud
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
