package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReseniaDto {

    @JsonProperty("idResena")  // Mapea "idResena" a "id"
    private Long id;
    private String contenidoResena;
    private Integer calificacion;
    private Long idUsuario;  // ID del usuario
    private Long idProducto;  // ID del producto

    // Constructor vacío
    public ReseniaDto() {}

    // Constructor con parámetros
    public ReseniaDto(Long id, String contenidoResena, Integer calificacion, Long idUsuario, Long idProducto) {
        this.id = id;
        this.contenidoResena = contenidoResena;
        this.calificacion = calificacion;
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenidoResena() {
        return contenidoResena;
    }

    public void setContenidoResena(String contenidoResena) {
        this.contenidoResena = contenidoResena;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public String toString() {
        return "ReseniaDto [id=" + id + ", contenidoResena=" + contenidoResena + ", calificacion=" + calificacion
                + ", idUsuario=" + idUsuario + ", idProducto=" + idProducto + "]";
    }
}
