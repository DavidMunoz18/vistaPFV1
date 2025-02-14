package dtos;

import java.util.List;

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

    public UsuarioDto() {}

   

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



	// Getters y Setters

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}



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
