package dtos;

/**
 * Clase DTO para representar las credenciales de un usuario al momento de iniciar sesión.
 * <p>
 * Este objeto se utiliza para enviar y recibir los datos de inicio de sesión del usuario, 
 * como el correo electrónico, la contraseña y el rol, a través de la API.
 * </p>
 */
public class LoginUsuarioDto {

    private String email;

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email El correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
