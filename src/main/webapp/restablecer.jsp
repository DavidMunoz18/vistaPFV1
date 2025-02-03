<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Restablecer Contraseña</title>
<style>
body {
	font-family: Arial, sans-serif;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background-color: #f4f4f4;
}

.container {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	width: 300px;
	text-align: center;
}

input {
	width: 100%;
	padding: 8px;
	margin: 10px 0;
	border: 1px solid #ccc;
	border-radius: 4px;
}

button {
	width: 100%;
	padding: 10px;
	background-color: #0073e6;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

button:hover {
	background-color: #005bb5;
}

.error {
	color: red;
	margin-bottom: 10px;
}

.success {
	color: green;
	margin-bottom: 10px;
}
</style>
</head>
<body>

	<div class="container">
		<h2>Restablecer Contraseña</h2>

		<%-- Mostrar mensajes de error o éxito si existen --%>
		<%
		String error = (String) request.getAttribute("error");
		%>
		<%
		if (error != null) {
		%>
		<p class="error"><%=error%></p>
		<%
		}
		%>

		<form action="NuevaContrasenia" method="post">
			<%-- Campo oculto para el token --%>
			<input type="hidden" id="token" name="token"
				value="<%=request.getParameter("token")%>"> <label
				for="nuevaContrasenia">Nueva Contraseña:</label> <input
				type="password" id="nuevaContrasenia" name="nuevaContrasenia"
				required> <label for="confirmarContrasenia">Confirmar
				Contraseña:</label> <input type="password" id="confirmarContrasenia"
				name="confirmarContrasenia" required>

			<button type="submit">Actualizar Contraseña</button>
		</form>

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
