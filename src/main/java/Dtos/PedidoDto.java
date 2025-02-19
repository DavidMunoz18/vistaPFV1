package dtos;

import java.util.List;

/**
 * Clase DTO que representa un pedido realizado por un usuario en el sistema.
 * <p>
 * Este objeto se utiliza para enviar y recibir los detalles de un pedido, incluyendo la información
 * del usuario, el contacto, la dirección, el método de pago, y los productos asociados al pedido.
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

    /**
     * Constructor por defecto necesario para la deserialización.
     */
    public PedidoDto() {}

    /**
     * Constructor con parámetros para inicializar un objeto PedidoDto con los detalles de un pedido.
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
     */
    public PedidoDto(Long idUsuario, String contacto, String direccion, String metodoPago, String nombreTarjeta,
                     String numeroTarjeta, String fechaExpiracion, String cvc, List<PedidoProductoDto> productos) {
        super();
        this.idUsuario = idUsuario;
        this.contacto = contacto;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
        this.nombreTarjeta = nombreTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.cvc = cvc;
        this.productos = productos;
    }

    // Getters y setters
    
    /**
     * Obtiene el ID del usuario que realiza el pedido.
     * 
     * @return El ID del usuario.
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario que realiza el pedido.
     * 
     * @param idUsuario El ID del usuario.
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el contacto del usuario para el pedido.
     * 
     * @return El contacto del usuario.
     */
    public String getContacto() {
        return contacto;
    }

    /**
     * Establece el contacto del usuario para el pedido.
     * 
     * @param contacto El contacto del usuario.
     */
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    /**
     * Obtiene la dirección de envío del pedido.
     * 
     * @return La dirección de envío del pedido.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de envío del pedido.
     * 
     * @param direccion La dirección de envío del pedido.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el método de pago utilizado para el pedido.
     * 
     * @return El método de pago utilizado.
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado para el pedido.
     * 
     * @param metodoPago El método de pago utilizado.
     */
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene el nombre de la tarjeta de crédito utilizada para el pago.
     * 
     * @return El nombre de la tarjeta de crédito.
     */
    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    /**
     * Establece el nombre de la tarjeta de crédito utilizada para el pago.
     * 
     * @param nombreTarjeta El nombre de la tarjeta de crédito.
     */
    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    /**
     * Obtiene el número de la tarjeta de crédito utilizada para el pago.
     * 
     * @return El número de la tarjeta de crédito.
     */
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * Establece el número de la tarjeta de crédito utilizada para el pago.
     * 
     * @param numeroTarjeta El número de la tarjeta de crédito.
     */
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    /**
     * Obtiene la fecha de expiración de la tarjeta de crédito utilizada para el pago.
     * 
     * @return La fecha de expiración de la tarjeta de crédito.
     */
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * Establece la fecha de expiración de la tarjeta de crédito utilizada para el pago.
     * 
     * @param fechaExpiracion La fecha de expiración de la tarjeta de crédito.
     */
    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * Obtiene el código de seguridad (CVC) de la tarjeta de crédito utilizada para el pago.
     * 
     * @return El código de seguridad (CVC) de la tarjeta de crédito.
     */
    public String getCvc() {
        return cvc;
    }

    /**
     * Establece el código de seguridad (CVC) de la tarjeta de crédito utilizada para el pago.
     * 
     * @param cvc El código de seguridad (CVC) de la tarjeta de crédito.
     */
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    /**
     * Obtiene la lista de productos asociados al pedido.
     * 
     * @return La lista de productos asociados al pedido.
     */
    public List<PedidoProductoDto> getProductos() {
        return productos;
    }

    /**
     * Establece la lista de productos asociados al pedido.
     * 
     * @param productos La lista de productos asociados al pedido.
     */
    public void setProductos(List<PedidoProductoDto> productos) {
        this.productos = productos;
    }
}
