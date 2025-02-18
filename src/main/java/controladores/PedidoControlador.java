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
        // Construir la fecha de expiración a partir de mes y año
        String fechaExpiracion = request.getParameter("mesExpiracion") + "/" + request.getParameter("anioExpiracion");
        String cvc = request.getParameter("cvv");

        // Validar el número de tarjeta
        if (!validarNumeroTarjeta(numeroTarjeta)) {
            request.setAttribute("mensaje", "El número de tarjeta no es válido. Debe contener entre 13 y 19 dígitos numéricos.");
            request.setAttribute("tipoMensaje", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Validar el CVV
        if (!validarCvc(cvc)) {
            request.setAttribute("mensaje", "El código de seguridad (CVV) no es válido. Debe contener 3 o 4 dígitos.");
            request.setAttribute("tipoMensaje", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Encriptar el número de tarjeta (opcional, según tu requerimiento)
        numeroTarjeta = encriptarDatos(numeroTarjeta);

        // Obtener el carrito de la sesión (CarritoDto)
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

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
            
            // Imprimir en consola el mensaje
            System.out.println("Respuesta del servicio: " + mensaje);
            
            if (!mensaje.equals("Pedido creado correctamente")) {
                tipoMensaje = "error";
            } else {
                // Eliminar el carrito de la sesión solo cuando el pedido se haya creado correctamente
                session.removeAttribute("carrito");
            }

        } catch (Exception e) {
            tipoMensaje = "error";
            mensaje = "Error al crear el pedido: " + e.getMessage();
            e.printStackTrace(); // Mostrar en consola la traza de la excepción
        }

        // Agregar el mensaje y tipo de mensaje a la solicitud
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("tipoMensaje", tipoMensaje);

        // Redirigir a la página del carrito con el mensaje en la solicitud
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }

    private boolean validarNumeroTarjeta(String numeroTarjeta) {
        // Validar que el número de tarjeta tenga entre 13 y 19 dígitos numéricos
        return numeroTarjeta != null && numeroTarjeta.matches("\\d{13,19}");
    }

    private boolean validarCvc(String cvc) {
        // Validar que el CVV tenga 3 o 4 dígitos numéricos
        return cvc != null && cvc.matches("\\d{3,4}");
    }

    private String encriptarDatos(String datos) {
        // Ejemplo de encriptación simplificada usando Base64
        return new String(java.util.Base64.getEncoder().encode(datos.getBytes()));
    }
}
