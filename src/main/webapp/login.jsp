<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeComponents</title>
    <link rel="stylesheet" href="css/login.css">
    <style>
      .modal {
        display: none; /* Inicialmente oculto */
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        justify-content: center;
        align-items: center;
      }
      .modal-content {
        background-color: white;
        padding: 20px;
        border-radius: 8px;
        width: 300px;
      }
      .close {
        cursor: pointer;
        float: right;
        font-size: 20px;
        font-weight: bold;
      }
    </style>
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

        <!-- Formulario de Login -->
        <form id="loginForm" action="loginUsuario" method="post">
          <!-- Botón de Google -->
          <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>

          <div class="divider"></div>
          
           
          
          <input type="email" name="email" id="email" placeholder="Email o número de teléfono" required>
          <input type="password" name="password" id="password" placeholder="Contraseña" required>
          <div class="link">
            <button type="submit" class="login">Iniciar Sesión</button>
            <a href="#" class="forgot" id="forgotPasswordLink">¿Olvidaste la contraseña?</a>
          </div>
          <hr>
          <div class="button">
            <a href="registro.jsp">Crear nueva cuenta</a>
          </div>
          <!-- Campo oculto para el token de Google -->
          <input type="hidden" name="tokenGoogle" id="tokenGoogle" />
        </form>

        <!-- Formulario para recuperación de contraseña -->
        <div id="forgotPasswordModal" class="modal">
          <form id="forgotPasswordForm" action="recuperar-contrasenia" method="post">
            <div class="modal-content">
              <span class="close" onclick="closeModal()">&times;</span>
              <h2>Recuperar Contraseña</h2>
              <input type="email" name="correo" id="forgotEmail" placeholder="Ingresa tu correo" required>
              <button type="submit" id="submitForgotPassword">Recuperar Contraseña</button>
              <p id="forgotPasswordMessage">
                <% 
                  // Mostrar el mensaje desde el servlet
                  String mensaje = (String) request.getAttribute("mensaje");
                  if (mensaje != null) {
                %>
                    <script>
                      alert("<%= mensaje %>");
                    </script>
                <% 
                  }
                %>
              </p>
            </div>
          </form>
        </div>

      </div>
    </div>

    <script>
      // Mostrar el formulario de recuperación de contraseña al hacer clic en el enlace
      document.getElementById("forgotPasswordLink").addEventListener("click", function(event) {
        event.preventDefault(); // Prevenir la acción por defecto del enlace
        document.getElementById("forgotPasswordModal").style.display = "flex"; // Mostrar el modal
      });

      // Cerrar el modal al hacer clic en la "X"
      function closeModal() {
        document.getElementById("forgotPasswordModal").style.display = "none"; // Ocultar el modal
      }
    </script>

  </body>
</html>
