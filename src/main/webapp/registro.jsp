<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeComponents</title>
    <link rel="stylesheet" href="css/login.css">
    
    <!-- Cargar el script de Google OAuth -->
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id" content="868357228953-9padqblpv73igf2gf1el580cbpree75p.apps.googleusercontent.com">
  </head>
  <body>
    <div class="container flex">
      <div class="CodeComponents-page flex">
        <div class="text">
          <h1>Code</h1>
          <h1>Components</h1>
          <p>Recibe ofertas increíbles</p>
          <p>de tus productos favoritos</p>
        </div>
        
        <form action="registroUsuario" method="POST">
          <!-- Botón Google oficial -->
          <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
          
          <div class="divider">
            <span>O bien</span>
          </div>
          
          <input type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Nombre" required>
          <input type="email" name="emailUsuario" id="emailUsuario" placeholder="Email" required>
          <input type="number" name="telefonoUsuario" id="telefonoUsuario" placeholder="Teléfono" required>
          <input type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Contraseña" required>
          <input type="password" name="confirmPasswordUsuario" id="confirmPasswordUsuario" placeholder="Repite Contraseña" required>
          
          <div class="link">
            <button type="submit" class="login">Registrarse</button>
          </div>
          <hr>
          <div class="button">
            <a href="login.jsp">¿Ya tienes cuenta? Inicia sesión</a>
          </div>
        </form>
      </div>
    </div>

    <!-- Función JavaScript para manejar la autenticación de Google -->
    <script>
      function onSignIn(googleUser) {
        var idToken = googleUser.getAuthResponse().id_token;

        // Redirigir al backend con el ID Token de Google para validarlo
        window.location.href = "/auth/google?token=" + idToken;
      }
    </script>
  </body>
</html>
