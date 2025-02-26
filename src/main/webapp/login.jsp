<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <style>
      .modal {
        display: none;
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
          <div class="link">
            <button type="submit" class="login">Iniciar Sesión</button>
            <a href="#" class="forgot" id="forgotPasswordLink">¿Olvidaste la contraseña?</a>
          </div>
          <hr>
          <div class="button">
            <a href="registro.jsp">Crear nueva cuenta</a>
          </div>
        </form>

        <!-- Formulario para recuperación de contraseña -->
        <div id="forgotPasswordModal" class="modal">
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
  document.getElementById("forgotPasswordLink").addEventListener("click", function(event) {
    event.preventDefault();
    document.getElementById("forgotPasswordModal").style.display = "flex";
  });

  // Función para cerrar el modal
  function closeModal() {
    document.getElementById("forgotPasswordModal").style.display = "none";
  }

  // Mostrar el Toast si hay mensaje enviado desde el servidor
  window.addEventListener('load', function() {
    // Se utiliza "errorMessage" enviado en caso de error; si no, se podría usar "mensaje"
    var mensaje = '<%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : (request.getAttribute("mensaje") != null ? request.getAttribute("mensaje") : "") %>';
    // Si se desea distinguir, se puede enviar "tipoMensaje" (por ejemplo, "exito" o "error") desde el Servlet
    var tipoMensaje = '<%= request.getAttribute("tipoMensaje") != null ? request.getAttribute("tipoMensaje") : "error" %>';
    
    if (mensaje.trim().length > 0) {
      var toastElement = document.getElementById('liveToast');
      var toastMessage = document.getElementById('toastMessage');
      var toastHeader = document.getElementById('toastHeader');

      toastMessage.innerText = mensaje;

      // Limpiar las clases anteriores
      toastElement.classList.remove("bg-success", "bg-danger", "text-white");

      if (tipoMensaje === "exito") {
        // Asignar fondo verde para éxito
        toastElement.classList.add("bg-success", "text-white");
        toastHeader.innerText = "Éxito";
      } else {
        // Fondo rojo para error
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
