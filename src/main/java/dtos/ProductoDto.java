package dtos;

import java.util.Base64;

/**
 * Clase DTO que representa un producto en el sistema.
 * <p>
 * Este objeto se utiliza para enviar y recibir los detalles de un producto, 
 * incluyendo su identificador, nombre, descripción, precio, imagen, stock y categoría.
 * </p>
 */
public class ProductoDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private byte[] imagen;
    private int stock;
    private String categoria;

    /**
     * Constructor predeterminado para crear un objeto ProductoDto vacío.
     */
    public ProductoDto() {
    }

    /**
     * Constructor para inicializar un producto con sus detalles.
     * 
     * @param id El identificador del producto.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param precio El precio del producto.
     * @param imagen La imagen del producto en formato byte array.
     * @param stock El número de unidades disponibles en stock.
     * @param categoria La categoría a la que pertenece el producto.
     */
    public ProductoDto(Long id, String nombre, String descripcion, double precio, byte[] imagen, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Obtiene el identificador del producto.
     * 
     * @return El identificador del producto.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del producto.
     * 
     * @param id El identificador del producto.
     */
    public void setId(Long id) {
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
     * Obtiene la descripción del producto.
     * 
     * @return La descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     * 
     * @param descripcion La descripción del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * Obtiene la imagen del producto en formato byte array.
     * 
     * @return La imagen del producto en formato byte array.
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del producto.
     * 
     * @param imagen La imagen del producto en formato byte array.
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la cantidad de unidades disponibles del producto en stock.
     * 
     * @return El número de unidades disponibles en stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad de unidades disponibles del producto en stock.
     * 
     * @param stock El número de unidades disponibles en stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la categoría del producto.
     * 
     * @return La categoría del producto.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     * 
     * @param categoria La categoría del producto.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Convierte la imagen del producto a una cadena en formato Base64.
     * 
     * @return La imagen del producto en formato Base64, o null si la imagen es null.
     */
    public String getImagenBase64() {
        if (imagen != null) {
            return Base64.getEncoder().encodeToString(imagen);
        }
        return null;
    }
}
