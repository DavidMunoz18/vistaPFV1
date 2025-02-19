package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase DTO que representa una reseña de un producto realizada por un usuario.
 * <p>
 * Esta clase se utiliza para enviar y recibir los datos de una reseña a través de la API.
 * Incluye información como el contenido de la reseña, la calificación, el ID del usuario
 * que realiza la reseña y el ID del producto sobre el cual se realiza la reseña.
 * </p>
 */
public class ReseniaDto {

    @JsonProperty("idResena")  // Mapea "idResena" a "id"
    private Long id;
    private String contenidoResena;
    private Integer calificacion;
    private Long idUsuario;  // ID del usuario
    private Long idProducto;  // ID del producto

    /**
     * Constructor vacío.
     * <p>
     * Este constructor es utilizado para la creación de objetos vacíos de la clase.
     * </p>
     */
    public ReseniaDto() {}

    /**
     * Constructor con parámetros para inicializar los valores de la reseña.
     * 
     * @param id El identificador único de la reseña.
     * @param contenidoResena El contenido textual de la reseña.
     * @param calificacion La calificación asignada a la reseña (generalmente de 1 a 5).
     * @param idUsuario El ID del usuario que realiza la reseña.
     * @param idProducto El ID del producto sobre el que se realiza la reseña.
     */
    public ReseniaDto(Long id, String contenidoResena, Integer calificacion, Long idUsuario, Long idProducto) {
        this.id = id;
        this.contenidoResena = contenidoResena;
        this.calificacion = calificacion;
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el ID de la reseña.
     * 
     * @return El ID de la reseña.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la reseña.
     * 
     * @param id El ID de la reseña.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el contenido de la reseña.
     * 
     * @return El contenido textual de la reseña.
     */
    public String getContenidoResena() {
        return contenidoResena;
    }

    /**
     * Establece el contenido de la reseña.
     * 
     * @param contenidoResena El contenido textual de la reseña.
     */
    public void setContenidoResena(String contenidoResena) {
        this.contenidoResena = contenidoResena;
    }

    /**
     * Obtiene la calificación de la reseña.
     * 
     * @return La calificación de la reseña.
     */
    public Integer getCalificacion() {
        return calificacion;
    }

    /**
     * Establece la calificación de la reseña.
     * 
     * @param calificacion La calificación de la reseña.
     */
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * Obtiene el ID del usuario que realiza la reseña.
     * 
     * @return El ID del usuario.
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario que realiza la reseña.
     * 
     * @param idUsuario El ID del usuario.
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el ID del producto sobre el cual se realiza la reseña.
     * 
     * @return El ID del producto.
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el ID del producto sobre el cual se realiza la reseña.
     * 
     * @param idProducto El ID del producto.
     */
    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Representación en formato String de la reseña.
     * 
     * @return Una cadena de texto con los detalles de la reseña.
     */
    @Override
    public String toString() {
        return "ReseniaDto [id=" + id + ", contenidoResena=" + contenidoResena + ", calificacion=" + calificacion
                + ", idUsuario=" + idUsuario + ", idProducto=" + idProducto + "]";
    }
}
