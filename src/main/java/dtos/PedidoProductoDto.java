package dtos;

/**
 * Clase DTO que representa un producto dentro de un pedido.
 * <p>
 * Este objeto se utiliza para enviar y recibir los detalles de un producto en un pedido, 
 * incluyendo su identificador, nombre, cantidad y precio.
 * </p>
 */
public class PedidoProductoDto {

    private int idProducto;
    private String nombre;
    private int cantidad;
    private double precio;

    /**
     * Constructor para inicializar un producto dentro de un pedido con sus detalles.
     * 
     * @param idProducto El ID del producto.
     * @param nombre El nombre del producto.
     * @param cantidad La cantidad de unidades del producto.
     * @param precio El precio unitario del producto.
     */
    public PedidoProductoDto(int idProducto, String nombre, int cantidad, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    /**
     * Obtiene el ID del producto.
     * 
     * @return El ID del producto.
     */
    public int getidProducto() {
        return idProducto;
    }

    /**
     * Establece el ID del producto.
     * 
     * @param id El ID del producto.
     */
    public void setidProducto(int id) {
        this.idProducto = id;
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
     * Obtiene la cantidad de unidades del producto.
     * 
     * @return La cantidad del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades del producto.
     * 
     * @param cantidad La cantidad del producto.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del producto.
     * 
     * @return El precio unitario del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario del producto.
     * 
     * @param precio El precio unitario del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Devuelve una representación en cadena del objeto PedidoProductoDto.
     * 
     * @return Una cadena que representa el objeto PedidoProductoDto.
     */
    @Override
    public String toString() {
        return "ProductoPedidoDto{" +
                "id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
