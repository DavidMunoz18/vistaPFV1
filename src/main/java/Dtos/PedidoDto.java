package dtos;

import java.util.List;

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

    public PedidoDto() {}

    
    
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
    
    
}
