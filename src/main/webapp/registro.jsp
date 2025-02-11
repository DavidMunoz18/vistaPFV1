<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Usuario</title>
    <link rel="stylesheet" href="css/login.css">
    <script>
      /**
       * Función que se ejecuta al enviar el formulario de datos de registro.
       * Copia los datos ingresados en campos ocultos del formulario de verificación,
       * muestra la alerta y oculta/visualiza los formularios.
       */
      function procesarRegistroForm() {
        // Copiamos los valores de los campos visibles a los ocultos del formulario de verificación
        document.getElementById("hiddenNombre").value = document.getElementById("nombreUsuario").value;
        document.getElementById("hiddenEmail").value = document.getElementById("emailUsuario").value;
        document.getElementById("hiddenTelefono").value = document.getElementById("telefonoUsuario").value;
        document.getElementById("hiddenPassword").value = document.getElementById("passwordUsuario").value;
        document.getElementById("hiddenConfirmPassword").value = document.getElementById("confirmPasswordUsuario").value;
        
        // Se muestra la alerta al usuario
        alert("Revisa tu correo, te hemos enviado el código de verificación");
        
        // Se oculta el formulario de registro y se muestra el de verificación
        document.getElementById("registroForm").style.display = "none";
        document.getElementById("codigoForm").style.display = "block";
        
        // Retornamos true para que el formulario se envíe de forma natural (a un iframe oculto)
        return true;
      }
    </script>
    <style>
      /* El iframe se oculta para que el envío del primer formulario no recargue la página */
      #hidden_iframe { display: none; }
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
        
        <!-- Formulario 1: Datos de registro -->
        <!-- Se asigna target="hidden_iframe" para que la respuesta del envío no recargue la página -->
        <form id="registroForm" action="enviarCodigo" method="POST" target="hidden_iframe" onsubmit="return procesarRegistroForm();">
          <input type="text" name="nombreUsuario" id="nombreUsuario" placeholder="Nombre" required>
          <input type="email" name="emailUsuario" id="emailUsuario" placeholder="Email" required>
          <input type="number" name="telefonoUsuario" id="telefonoUsuario" placeholder="Teléfono" required>
          <input type="password" name="passwordUsuario" id="passwordUsuario" placeholder="Contraseña" required>
          <input type="password" name="confirmPasswordUsuario" id="confirmPasswordUsuario" placeholder="Repite Contraseña" required>
          
          <div class="link">
            <!-- Botón de tipo submit para enviar el formulario de datos -->
            <button type="submit" class="login">Registrarse</button>
          </div>
          <hr>
          <div class="button">
            <a href="login.jsp">¿Ya tienes cuenta? Inicia sesión</a>
          </div>
        </form>
        
        <!-- Formulario 2: Ingreso del código de verificación -->
        <!-- Inicialmente oculto -->
        <form id="codigoForm" action="registroUsuario" method="POST" style="display: none;">
          <!-- Campos ocultos para transportar la información ingresada en el formulario 1 -->
          <input type="hidden" name="nombreUsuario" id="hiddenNombre">
          <input type="hidden" name="emailUsuario" id="hiddenEmail">
          <input type="hidden" name="telefonoUsuario" id="hiddenTelefono">
          <input type="hidden" name="passwordUsuario" id="hiddenPassword">
          <input type="hidden" name="confirmPasswordUsuario" id="hiddenConfirmPassword">
          
          <!-- Campo visible para ingresar el código -->
          <input type="text" name="codigoVerificacion" id="codigoVerificacion" placeholder="Código de verificación" required>
          <div class="link">
            <button type="submit" class="login">Confirmar Registro</button>
          </div>
        </form>
        
        <!-- Iframe oculto para recibir la respuesta del envío del primer formulario -->
        <iframe name="hidden_iframe" id="hidden_iframe"></iframe>
        
      </div>
    </div>
  </body>
</html>
