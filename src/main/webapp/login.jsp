<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Se recuperan los mensajes de la sesión en caso de venir de una redirección (por ejemplo, nueva contraseña)
    String mensajeSession = "";
    String tipoMensajeSession = "error";
    if(session.getAttribute("mensaje") != null) {
        mensajeSession = session.getAttribute("mensaje").toString();
        if(session.getAttribute("tipoMensaje") != null) {
            tipoMensajeSession = session.getAttribute("tipoMensaje").toString();
        }
        session.removeAttribute("mensaje");
        session.removeAttribute("tipoMensaje");
    }
%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeComponents</title>
    <link rel="stylesheet" href="css/login.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
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

        <!-- Toast de notificación -->
        <div class="toast-container position-fixed bottom-0 end-0 p-3">
          <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
              <strong class="me-auto" id="toastHeader">Notificación</strong>
              <small class="text-muted">Ahora</small>
              <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Cerrar"></button>
            </div>
            <div class="toast-body" id="toastMessage">
              <!-- Aquí se insertará el mensaje dinámico -->
            </div>
          </div>
        </div>

        <!-- Formulario de Login -->
        <form id="loginForm" action="loginUsuario" method="post">
          <input type="email" name="email" id="email" placeholder="Email o número de teléfono" required>
          <input type="password" name="password" id="password" placeholder="Contraseña" required>
          <!-- Campo oculto para enviar el parámetro returnURL -->
          <input type="hidden" name="returnURL" value="${param.returnURL}">
          <div class="link">
            <button type="submit" class="login">Iniciar Sesión</button>
            <a href="#" class="forgot" id="olvidarContraLink">¿Olvidaste la contraseña?</a>
          </div>
          <hr>
          <div class="button">
            <a href="registro.jsp">Crear nueva cuenta</a>
          </div>
        </form>

        <!-- Formulario para recuperación de contraseña -->
        <div id="olvidarContraModal" class="modal">
          <form id="forgotPasswordForm" action="recuperar-contrasenia" method="post">
            <div class="modal-content">
              <span class="close" onclick="closeModal()">&times;</span>
              <h2>Recuperar Contraseña</h2>
              <input type="email" name="correo" id="forgotEmail" placeholder="Ingresa tu correo" required>
              <button type="submit">Recuperar Contraseña</button>
            </div>
          </form>
        </div>
      </div>
    </div>

   <script>
      // Mostrar modal de recuperación de contraseña al hacer clic en el enlace
      document.getElementById("olvidarContraLink").addEventListener("click", function(event) {
        event.preventDefault();
        document.getElementById("olvidarContraModal").style.display = "flex";
      });

      // Función para cerrar el modal
      function closeModal() {
        document.getElementById("olvidarContraModal").style.display = "none";
      }

      // Mostrar el Toast si hay mensaje enviado desde el servidor o desde la sesión
      window.addEventListener('load', function() {
        var mensaje = '<%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : ( request.getAttribute("mensaje") != null ? request.getAttribute("mensaje") : mensajeSession ) %>';
        var tipoMensaje = '<%= request.getAttribute("tipoMensaje") != null ? request.getAttribute("tipoMensaje") : tipoMensajeSession %>';
        
        if (mensaje.trim().length > 0) {
          var toastElement = document.getElementById('liveToast');
          var toastMessage = document.getElementById('toastMessage');
          var toastHeader = document.getElementById('toastHeader');

          toastMessage.innerText = mensaje;
          toastElement.classList.remove("bg-success", "bg-danger", "text-white");

          if (tipoMensaje === "exito") {
            toastElement.classList.add("bg-success", "text-white");
            toastHeader.innerText = "Éxito";
          } else {
            toastElement.classList.add("bg-danger", "text-white");
            toastHeader.innerText = "Error";
          }

          var toast = new bootstrap.Toast(toastElement);
          toast.show();
        }
      });
    </script>

  </body>
</html>
