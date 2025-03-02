package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

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
import utilidades.Utilidades;

/**
 * Controlador que maneja la creación de pedidos de los usuarios.
 * <p>
 * Este servlet procesa solicitudes POST para permitir que un usuario realice un pedido.
 * Recoge los datos del pedido, valida la información de la tarjeta, encripta el número de tarjeta
 * y crea un nuevo pedido en el sistema utilizando los servicios de PedidoServicio.
 * </p>
 */
@WebServlet("/pedidos")
public class PedidoControlador extends HttpServlet {

    private PedidoServicio pedidoServicio = new PedidoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String contacto       = request.getParameter("contacto");
        String direccion      = request.getParameter("direccion");
        String metodoPago     = request.getParameter("metodoPago");
        String nombreTarjeta  = request.getParameter("nombreTarjeta");
        String numeroTarjeta  = request.getParameter("numeroTarjeta");
        String mesExpiracion  = request.getParameter("mesExpiracion");
        String anioExpiracion = request.getParameter("anioExpiracion");
        String fechaExpiracion = mesExpiracion + "/" + anioExpiracion;
        String cvc            = request.getParameter("cvv");

        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            // Se guardan los datos para reestablecerlos luego en el login
            session.setAttribute("pedidoContacto", contacto);
            session.setAttribute("pedidoDireccion", direccion);
            session.setAttribute("pedidoMetodoPago", metodoPago);
            session.setAttribute("pedidoNombreTarjeta", nombreTarjeta);
            session.setAttribute("pedidoNumeroTarjeta", numeroTarjeta);
            session.setAttribute("pedidoMesExpiracion", mesExpiracion);
            session.setAttribute("pedidoAnioExpiracion", anioExpiracion);
            session.setAttribute("pedidoCvv", cvc);

            String returnURL = "carrito"; // Se usa carrito.jsp para homogeneidad
            response.sendRedirect("login.jsp?returnURL=" + URLEncoder.encode(returnURL, "UTF-8"));
            return;
        }

        // Validar número de tarjeta
        if (!validarNumeroTarjeta(numeroTarjeta)) {
            session.setAttribute("mensaje", "El número de tarjeta no es válido. Debe contener entre 13 y 19 dígitos numéricos.");
            session.setAttribute("tipoMensaje", "error");
            // Guardar datos del formulario para repoblar (si fuera necesario)
            session.setAttribute("contacto", contacto);
            session.setAttribute("direccion", direccion);
            session.setAttribute("metodoPago", metodoPago);
            session.setAttribute("nombreTarjeta", nombreTarjeta);
            session.setAttribute("numeroTarjeta", numeroTarjeta);
            session.setAttribute("mesExpiracion", mesExpiracion);
            session.setAttribute("anioExpiracion", anioExpiracion);
            session.setAttribute("cvv", cvc);
            response.sendRedirect("carrito");
            return;
        }

        // Validar CVV
        if (!validarCvc(cvc)) {
            session.setAttribute("mensaje", "El código de seguridad (CVV) no es válido. Debe contener 3 o 4 dígitos.");
            session.setAttribute("tipoMensaje", "error");
            // Guardar datos del formulario para repoblar
            session.setAttribute("contacto", contacto);
            session.setAttribute("direccion", direccion);
            session.setAttribute("metodoPago", metodoPago);
            session.setAttribute("nombreTarjeta", nombreTarjeta);
            session.setAttribute("numeroTarjeta", numeroTarjeta);
            session.setAttribute("mesExpiracion", mesExpiracion);
            session.setAttribute("anioExpiracion", anioExpiracion);
            session.setAttribute("cvv", cvc);
            response.sendRedirect("carrito");
            return;
        }

        // Encriptar el número de tarjeta
        numeroTarjeta = encriptarDatos(numeroTarjeta);
        cvc = encriptarDatos(cvc);

        // Obtener el carrito de la sesión
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            session.setAttribute("mensaje", "El carrito está vacío");
            session.setAttribute("tipoMensaje", "error");
            RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Construir lista de productos para el pedido
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

        // Crear objeto PedidoDto
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
            mensaje = pedidoServicio.crearPedido(pedidoDto);
            Utilidades.escribirLog(session, "[INFO]", "PedidoControlador", "doPost", "Respuesta del servicio: " + mensaje);

            if (!"Pedido creado correctamente".equals(mensaje)) {
                tipoMensaje = "error";
                // Conservar datos del formulario
                session.setAttribute("contacto", contacto);
                session.setAttribute("direccion", direccion);
                session.setAttribute("metodoPago", metodoPago);
                session.setAttribute("nombreTarjeta", nombreTarjeta);
                session.setAttribute("numeroTarjeta", numeroTarjeta);
                session.setAttribute("mesExpiracion", mesExpiracion);
                session.setAttribute("anioExpiracion", anioExpiracion);
                session.setAttribute("cvv", cvc);
            } else {
                // Eliminar el carrito de la sesión en caso de éxito
                session.removeAttribute("carrito");
            }

        } catch (Exception e) {
            tipoMensaje = "error";
            mensaje = "Error al crear el pedido: " + e.getMessage();
            e.printStackTrace();
            Utilidades.escribirLog(session, "[ERROR]", "PedidoControlador", "doPost", "Error al crear el pedido: " + e.getMessage());
            // Conservar datos en caso de excepción
            session.setAttribute("contacto", contacto);
            session.setAttribute("direccion", direccion);
            session.setAttribute("metodoPago", metodoPago);
            session.setAttribute("nombreTarjeta", nombreTarjeta);
            session.setAttribute("numeroTarjeta", numeroTarjeta);
            session.setAttribute("mesExpiracion", mesExpiracion);
            session.setAttribute("anioExpiracion", anioExpiracion);
            session.setAttribute("cvv", cvc);
        }

        // Guardar el mensaje y el tipo en la sesión para que el JSP lo muestre
        session.setAttribute("mensaje", mensaje);
        session.setAttribute("tipoMensaje", tipoMensaje);

        if ("Pedido creado correctamente".equals(mensaje)) {
            // Redirigir directamente a la página de confirmación o al inicio, en lugar del carrito
            response.sendRedirect("carrito.jsp?pedidoExitoso=true");
         // Eliminar el carrito de la sesión en caso de éxito
            session.removeAttribute("carrito");
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
            dispatcher.forward(request, response);
        }

    }

    private boolean validarNumeroTarjeta(String numeroTarjeta) {
        return numeroTarjeta != null && numeroTarjeta.matches("\\d{13,19}");
    }

    private boolean validarCvc(String cvc) {
        return cvc != null && cvc.matches("\\d{3,4}");
    }

    private String encriptarDatos(String datos) {
        return new String(java.util.Base64.getEncoder().encode(datos.getBytes()));
    }
}
