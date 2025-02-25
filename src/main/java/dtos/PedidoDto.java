package dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase DTO que representa un pedido realizado por un usuario en el sistema.
 * <p>
 * Este objeto se utiliza para enviar y recibir los detalles de un pedido, incluyendo la información
 * del usuario, el contacto, la dirección, el método de pago, los productos asociados, y los campos
 * calculados en la lógica de negocio como fecha del pedido, estado y total.
 * </p>
 */
public class PedidoDto {
    
    private Long idUsuario;
    private String contacto;
    private String direccion;
    private String metodoPago;
    private String nombreTarjeta;
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvc;
    
    // Nuevo campo para el detalle de productos
    private List<PedidoProductoDto> productos;
    
    // Campos agregados para la lógica de negocio (se establecerán en el Dynamic Web Project)
    private LocalDate fechaPedido;
    private String estado;
    private double total;

    /**
     * Constructor por defecto necesario para la deserialización.
     */
    public PedidoDto() {}

    /**
     * Constructor con parámetros para inicializar un objeto PedidoDto con todos los detalles del pedido.
     * 
     * @param idUsuario El ID del usuario que realiza el pedido.
     * @param contacto El contacto del usuario.
     * @param direccion La dirección de envío del pedido.
     * @param metodoPago El método de pago utilizado.
     * @param nombreTarjeta El nombre en la tarjeta de crédito.
     * @param numeroTarjeta El número de la tarjeta de crédito.
     * @param fechaExpiracion La fecha de expiración de la tarjeta de crédito.
     * @param cvc El código de seguridad de la tarjeta de crédito.
     * @param productos La lista de productos asociados al pedido.
     * @param fechaPedido La fecha en que se realizó el pedido.
     * @param estado El estado del pedido.
     * @param total El total calculado del pedido.
     */
    public PedidoDto(Long idUsuario, String contacto, String direccion, String metodoPago, String nombreTarjeta,
                     String numeroTarjeta, String fechaExpiracion, String cvc, List<PedidoProductoDto> productos,
                     LocalDate fechaPedido, String estado, double total) {
        this.idUsuario = idUsuario;
        this.contacto = contacto;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
        this.nombreTarjeta = nombreTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.cvc = cvc;
        this.productos = productos;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
    }

    // Getters y setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public List<PedidoProductoDto> getProductos() {
        return productos;
    }

    public void setProductos(List<PedidoProductoDto> productos) {
        this.productos = productos;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
