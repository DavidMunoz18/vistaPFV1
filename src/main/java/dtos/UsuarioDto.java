package dtos;

/**
 * Clase DTO que representa los datos de un usuario.
 * <p>
 * Esta clase se utiliza para enviar y recibir los datos de un usuario, tales como el ID,
 * nombre, teléfono, correo electrónico, contraseña y rol, a través de la API.
 * </p>
 */
public class UsuarioDto {

    private long idUsuario;
    private String nombreUsuario;
    private String telefonoUsuario;
    private String emailUsuario;
    private String contrasena;
    private String rol;

    // ===========================
    // Constructores, Getters y Setters
    // ===========================

    /**
     * Constructor vacío.
     * <p>
     * Este constructor es utilizado para la creación de objetos vacíos de la clase.
     * </p>
     */
    public UsuarioDto() {}

    /**
     * Constructor con parámetros para inicializar los valores del usuario.
     * 
     * @param idUsuario El ID único del usuario.
     * @param nombreUsuario El nombre del usuario.
     * @param telefonoUsuario El número de teléfono del usuario.
     * @param emailUsuario El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     * @param rol El rol del usuario (por ejemplo, "admin", "usuario").
     */
    public UsuarioDto(long idUsuario, String nombreUsuario, String telefonoUsuario, String emailUsuario,
                      String contrasena, String rol) {
        super();
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.emailUsuario = emailUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    /**
     * Obtiene el ID del usuario.
     * 
     * @return El ID del usuario.
     */
    public long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario.
     * 
     * @param idUsuario El ID del usuario.
     */
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return El nombre del usuario.
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombreUsuario El nombre del usuario.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     * 
     * @return El número de teléfono del usuario.
     */
    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    /**
     * Establece el número de teléfono del usuario.
     * 
     * @param telefonoUsuario El número de teléfono del usuario.
     */
    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return El correo electrónico del usuario.
     */
    public String getEmailUsuario() {
        return emailUsuario;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param emailUsuario El correo electrónico del usuario.
     */
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    /**
     * Obtiene el rol del usuario.
     * 
     * @return El rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     * 
     * @param rol El rol del usuario.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return La contraseña del usuario.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param contrasena La contraseña del usuario.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Representación en formato String del objeto UsuarioDto.
     * 
     * @return Una cadena con los detalles del usuario.
     */
    @Override
    public String toString() {
        return "UsuarioDto{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", telefonoUsuario='" + telefonoUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
