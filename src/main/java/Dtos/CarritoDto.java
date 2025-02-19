package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Representa un producto en el carrito de compras.
 * <p>
 * Esta clase es utilizada para mapear los datos del carrito de compras en formato JSON.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "nombre", "cantidad", "precio", "imagen"})
public class CarritoDto {

    @JsonProperty("id")  // Mapea el "id" de la API a "id"
    private int id;

    private String nombre;
    private int cantidad;
    private double precio;
    private String imagen;

    /**
     * Constructor completo de la clase CarritoDto.
     * <p>
     * Inicializa los campos con los valores proporcionados.
     * </p>
     * 
     * @param id El ID del producto en el carrito.
     * @param nombre El nombre del producto.
     * @param cantidad La cantidad del producto en el carrito.
     * @param precio El precio del producto.
     * @param imagen La URL de la imagen del producto.
     */
    public CarritoDto(int id, String nombre, int cantidad, double precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imagen = imagen;
    }

    /**
     * Constructor predeterminado necesario para la deserialización JSON.
     * <p>
     * Este constructor es utilizado por Jackson para crear objetos de la clase a partir de datos JSON.
     * </p>
     */
    public CarritoDto() {}

    /**
     * Obtiene el ID del producto en el carrito.
     * 
     * @return El ID del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del producto en el carrito.
     * 
     * @param id El ID del producto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     * 
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * 
     * @param nombre El nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad del producto en el carrito.
     * 
     * @return La cantidad del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto en el carrito.
     * 
     * @param cantidad La cantidad del producto.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio del producto.
     * 
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * 
     * @param precio El precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la URL de la imagen del producto.
     * 
     * @return La URL de la imagen del producto.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la URL de la imagen del producto.
     * 
     * @param imagen La URL de la imagen del producto.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Devuelve una representación en forma de cadena del objeto CarritoDto.
     * <p>
     * Esta representación es útil para la depuración y el registro de eventos.
     * </p>
     * 
     * @return Una cadena que representa al objeto CarritoDto.
     */
    @Override
    public String toString() {
        return "CarritoDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
