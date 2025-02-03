package Dtos;

import java.util.Base64;

public class ProductoDto {

    private Long id; 
    private String nombre;
    private String descripcion;
    private double precio;
    private byte[] imagen;  
    private int stock;

    // Constructor vacío (necesario para algunas bibliotecas de serialización/deserialización)
    public ProductoDto() {
    }

    // Constructor completo
    public ProductoDto(Long id, String nombre, String descripcion, double precio, byte[] imagen, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getImagenBase64() {
        return Base64.getEncoder().encodeToString(imagen);
    }
}
