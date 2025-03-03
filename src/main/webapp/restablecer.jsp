<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Restablecer Contraseña</title>
 <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
  <div class="restablecer-page">
    <div class="container">
      <h2>Restablecer Contraseña</h2>
      <%-- Mostrar mensajes de error o éxito si existen --%>
      <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
      %>
      <p class="error"><%= error %></p>
      <%
        }
      %>
      <form action="NuevaContrasenia" method="post">
        <%-- Campo oculto para el token --%>
        <input type="hidden" id="token" name="token" value="<%=request.getParameter("token")%>">
        <label for="nuevaContrasenia">Nueva Contraseña:</label>
        <input type="password" id="nuevaContrasenia" name="nuevaContrasenia" required>
        <label for="confirmarContrasenia">Confirmar Contraseña:</label>
        <input type="password" id="confirmarContrasenia" name="confirmarContrasenia" required>
        <button type="submit">Actualizar Contraseña</button>
      </form>
    </div>
  </div>
  
  <script>
    // Si el token está presente en la URL, lo agrega al campo oculto
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    if (token) {
      document.getElementById('token').value = token;
    }
  </script>
</body>
</html>
