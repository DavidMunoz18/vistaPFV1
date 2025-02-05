<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="Dtos.ProductoDto"%>
<%@ page import="Dtos.UsuarioDto"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administrador</title>
<link rel="stylesheet" href="css/administrador.css">
<link rel="stylesheet" href="css/estilo.css">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
   function mostrarSeccion(seccionId) {
       // Oculta todas las secciones
       const secciones = document.querySelectorAll('.seccion');
       secciones.forEach(seccion => seccion.style.display = 'none');
       // Muestra solo la sección seleccionada
       document.getElementById(seccionId).style.display = 'block';
   }
   // Mostrar la primera sección por defecto
   document.addEventListener('DOMContentLoaded', function () {
       mostrarSeccion('modificarUsuario');
   });
</script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">
			<a class="navbar-brand" href="#"> <img
				src="imagenes/Code components-Photoroom.png" alt="Logo">
			</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
				aria-controls="navbarNavDropdown" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNavDropdown">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link" href="index.jsp">Inicio</a></li>
					<li class="nav-item"><a class="nav-link" href="nosotros.jsp">Nosotros</a></li>
					<li class="nav-item"><a
						href="<%=request.getContextPath()%>/productos">Productos</a></li>

					<li class="nav-item"><a class="nav-link" href="login.jsp">Iniciar
							Sesión</a></li>
					<li class="nav-item"><a class="nav-link" href="registro.jsp">Registrarse</a></li>
					<li class="nav-item cart-container"><a
						href="<%=request.getContextPath()%>/carrito"> <i
							class="bi bi-cart"></i> <!-- Ícono de carrito --> <span
							class="cart-count">0</span> <!-- Número de productos en el carrito -->
					</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
			<!-- Menú lateral -->
			<div class="col-md-3 bg-light py-4">
				<h5>Menú</h5>
				<ul class="nav flex-column">
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('modificarUsuario')">Modificar
							Usuario</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('eliminarUsuario')">Eliminar
							Usuario</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('agregarProducto')">Agregar
							Productos</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('eliminarProducto')">Eliminar
							Productos</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('modificarProducto')">Modificar
							Producto</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('verProductos')">Ver Productos</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('verUsuarios')">Ver Usuarios</button>
					</li>









				</ul>
			</div>
			<!-- Contenido principal -->
			<div class="col-md-9 py-4">
				<!-- Sección: Ver Productos -->
				<div id="modificarUsuario" class="seccion">
					<h2>Modificar Usuario</h2>
					<form action="modificarUsuario" method="post"
						enctype="multipart/form-data">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required> <label
							for="nuevoNombre">Nuevo Nombre:</label> <input type="text"
							id="nuevoNombre" name="nuevoNombre"> <label
							for="nuevoTelefono">Nuevo Teléfono:</label> <input type="text"
							id="nuevoTelefono" name="nuevoTelefono"> <label
							for="nuevaFoto">Nueva Foto:</label> <input type="file"
							id="nuevaFoto" name="nuevaFoto" accept="image/*">
						<button type="submit" class="btn btn-primary mt-3">Modificar
							Usuario</button>
					</form>
				</div>

				<!-- Sección: Eliminar Usuario -->
				<div id="eliminarUsuario" class="seccion" style="display: none;">
					<h2>Eliminar Usuario</h2>
					<form action="eliminarUsuario" method="post">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required>
						<button type="submit" class="btn btn-danger mt-3">Eliminar
							Usuario</button>
					</form>
				</div>

				<!-- Sección: Agregar Productos -->
				<div id="agregarProducto" class="seccion" style="display: none;">
					<h2>Agregar Producto</h2>
					<form action="productosAniadir" method="post"
						enctype="multipart/form-data">
						<label for="nombreProducto">Nombre:</label> <input type="text"
							id="nombreProducto" name="nombre" required> <label
							for="descripcionProducto">Descripción:</label>
						<textarea id="descripcionProducto" name="descripcion" required></textarea>

						<label for="precioProducto">Precio:</label> <input type="number"
							id="precioProducto" name="precio" step="0.01" required> <label
							for="stockProducto">Stock:</label> <input type="number"
							id="stockProducto" name="stock" required> <label
							for="categoriaProducto">Categoría:</label> <select
							id="categoriaProducto" name="categoria" required>
							<option value="Ordenadores">Ordenadores</option>
							<option value="Componentes">Componentes</option>
							<option value="Periféricos">Periféricos</option>
							<option value="Portátiles">Portátiles</option>
							<option value="Ofertas">Ofertas</option>
						</select> <label for="imagenProducto">Imagen:</label> <input type="file"
							id="imagenProducto" name="imagen" accept="image/*" required>

						<button type="submit" class="btn btn-success mt-3">Agregar
							Producto</button>
					</form>
				</div>


				<div id="eliminarProducto" class="seccion">
					<h2>Eliminar Producto</h2>
					<form action="eliminarProducto" method="post">
						<div class="mb-3">
							<label for="productoId" class="form-label">ID del
								Producto:</label> <input type="number" id="productoId" name="productoId"
								class="form-control" required>
						</div>
						<button type="submit" class="btn btn-danger mt-3">Eliminar
							Producto</button>
					</form>
				</div>
				<!-- Sección: Modificar Producto -->
				<div id="modificarProducto" class="seccion" style="display: none;">
					<h2>Modificar Producto</h2>
					<form action="modificarProducto" method="post"
						enctype="multipart/form-data">
						<div class="mb-3">
							<label for="idProducto" class="form-label">ID Producto:</label> <input
								type="number" id="idProducto" name="id" class="form-control"
								required>
						</div>
						<div class="mb-3">
							<label for="nombreProducto" class="form-label">Nombre
								(opcional):</label> <input type="text" id="nombreProducto" name="nombre"
								class="form-control">
						</div>
						<div class="mb-3">
							<label for="descripcionProducto" class="form-label">Descripción
								(opcional):</label>
							<textarea id="descripcionProducto" name="descripcion"
								class="form-control" rows="3"></textarea>
						</div>
						<div class="mb-3">
							<label for="precioProducto" class="form-label">Precio
								(opcional):</label> <input type="number" id="precioProducto"
								name="precio" class="form-control" step="0.01">
						</div>
						<div class="mb-3">
							<label for="stockProducto" class="form-label">Stock
								(opcional):</label> <input type="number" id="stockProducto" name="stock"
								class="form-control">
						</div>
						<div class="mb-3">
							<label for="imagenProducto" class="form-label">Imagen
								(opcional):</label> <input type="file" id="imagenProducto" name="imagen"
								class="form-control" accept="image/*">
						</div>
						<button type="submit" class="btn btn-warning mt-3">Modificar
							Producto</button>

					</form>
				</div>
				<div id="verProductos" class="seccion" style="display: none;">
					<h2>Lista de Productos</h2>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nombre</th>
								<th>Descripción</th>
								<th>Precio</th>
								<th>Stock</th>
								<th>Imagen</th>
							</tr>
						</thead>
						<tbody>
							<%
							// Se asume que el servlet ha establecido el atributo "productos" con una List<ProductoDto>
							List<ProductoDto> productos = (List<ProductoDto>) request.getAttribute("productos");
							if (productos != null && !productos.isEmpty()) {
								for (ProductoDto producto : productos) {
							%>
							<tr>
								<td><%=producto.getId()%></td>
								<td><%=producto.getNombre()%></td>
								<td><%=producto.getDescripcion()%></td>
								<td><%=producto.getPrecio()%>€</td>
								<td><%=producto.getStock()%></td>
								<td>
									<!-- Aquí puedes colocar la imagen correspondiente si la tienes -->

								</td>
							</tr>
							<%
							}
							} else {
							out.println("<tr><td colspan='6'>No hay productos disponibles.</td></tr>");
							}
							%>
						</tbody>
					</table>
				</div>
				<div id="verUsuarios" class="seccion" style="display: none;">
					<h2>Lista de Usuarios</h2>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nombre</th>
								<th>Teléfono</th>
								<th>Email</th>
								<th>Rol</th>
							</tr>
						</thead>
						<tbody>
							<%
							List<UsuarioDto> usuarios = (List<UsuarioDto>) request.getAttribute("usuarios");
							if (usuarios != null && !usuarios.isEmpty()) {
								for (UsuarioDto usuario : usuarios) {
							%>
							<tr>
								<td><%=usuario.getIdUsuario()%></td>
								<td><%=usuario.getNombreUsuario()%></td>
								<td><%=usuario.getTelefonoUsuario()%></td>
								<td><%=usuario.getEmailUsuario()%></td>
								<td><%=usuario.getRol()%></td>
							</tr>
							<%
							}
							} else {
							out.println("<tr><td colspan='5'>No hay usuarios disponibles.</td></tr>");
							}
							%>
						</tbody>
					</table>
				</div>




				<!-- Otrs secciones -->
			</div>
		</div>
	</div>
</body>
</html>
