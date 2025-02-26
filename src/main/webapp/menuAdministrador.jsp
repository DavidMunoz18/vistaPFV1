<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dtos.ProductoDto"%>
<%@ page import="dtos.UsuarioDto"%>
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
       const secciones = document.querySelectorAll('.seccion');
       secciones.forEach(seccion => seccion.style.display = 'none');
       document.getElementById(seccionId).style.display = 'block';
   }
   document.addEventListener('DOMContentLoaded', function () {
       mostrarSeccion('verUsuarios');
   });

   // Funciones para Productos
   function editarProducto(id, nombre, descripcion, precio, stock) {
    mostrarSeccion('modificarProducto');
    document.getElementById('idProducto').value = id;
    document.getElementById('modNombreProducto').value = nombre;
    document.getElementById('modDescripcionProducto').value = descripcion;
    document.getElementById('modPrecioProducto').value = precio;
    document.getElementById('modStockProducto').value = stock;
}


   function eliminarProducto(id) {
       if (confirm('¿Estás seguro de eliminar este producto?')) {
           var form = document.createElement('form');
           form.action = 'eliminarProducto';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'productoId';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       }
   }

   // Funciones para Usuarios
   function editarUsuario(id, nombre, telefono, rol) {
       mostrarSeccion('modificarUsuario');
       document.getElementById('idUsuario').value = id;
       document.getElementById('nuevoNombre').value = nombre;
       document.getElementById('nuevoTelefono').value = telefono;
       document.getElementById('nuevoRol').value = rol;
   }

   function eliminarUsuario(id) {
       if (confirm('¿Estás seguro de eliminar este usuario?')) {
           var form = document.createElement('form');
           form.action = 'eliminarUsuario';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'idUsuario';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       }
   }
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
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/inicio">Inicio</a></li>
					<li class="nav-item"><a class="nav-link" href="nosotros.jsp">Nosotros</a></li>
					<li class="nav-item"><a
						href="<%=request.getContextPath()%>/productos" class="nav-link">Productos</a></li>
					<li class="nav-item"><a class="nav-link" href="login.jsp">Iniciar
							Sesión</a></li>
					<li class="nav-item"><a class="nav-link" href="registro.jsp">Registrarse</a></li>
					<li class="nav-item cart-container"><a
						href="<%=request.getContextPath()%>/carrito"> <i
							class="bi bi-cart"></i> <span class="cart-count">0</span>
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
							onclick="mostrarSeccion('agregarProducto')">Agregar
							Productos</button>
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
				<!-- Sección: Modificar Usuario -->
				<div id="modificarUsuario" class="seccion">
					<h2>Modificar Usuario</h2>
					<form action="modificarUsuario" method="post"
						enctype="multipart/form-data">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required> <label
							for="nuevoNombre">Nuevo Nombre:</label> <input type="text"
							id="nuevoNombre" name="nuevoNombre"> <label
							for="nuevoDni">Nuevo DNI:</label>
						<!-- Agregamos el campo de DNI -->
						<input type="text" id="nuevoDni" name="nuevoDni">
						<!-- Agregamos el campo de DNI -->

						<label for="nuevoTelefono">Nuevo Teléfono:</label> <input
							type="text" id="nuevoTelefono" name="nuevoTelefono"> <label
							for="nuevoRol">Nuevo Rol:</label> <input type="text"
							id="nuevoRol" name="nuevoRol"> <label for="nuevaFoto">Nueva
							Foto:</label> <input type="file" id="nuevaFoto" name="nuevaFoto"
							accept="image/*">

						<button type="submit" class="btn btn-primary mt-3">Modificar
							Usuario</button>
					</form>

				</div>

				<!-- Sección: Eliminar Usuario (opcional si se usará la acción desde la tabla) -->
				<div id="eliminarUsuario" class="seccion" style="display: none;">
					<h2>Eliminar Usuario</h2>
					<form action="eliminarUsuario" method="post">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required>
						<button type="submit" class="btn btn-danger mt-3">Eliminar
							Usuario</button>
					</form>
				</div>

				<!-- Sección: Agregar Producto -->
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

				<!-- Sección: Eliminar Producto -->
				<div id="eliminarProducto" class="seccion" style="display: none;">
					<h2>Eliminar Producto</h2>
					<form action="eliminarProducto" method="post">
						<label for="productoId" class="form-label">ID del
							Producto:</label> <input type="number" id="productoId" name="productoId"
							class="form-control" required>
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
								required readonly>
						</div>
						<div class="mb-3">
							<label for="modNombreProducto" class="form-label">Nombre
								(opcional):</label> <input type="text" id="modNombreProducto"
								name="nombre" class="form-control">
						</div>
						<div class="mb-3">
							<label for="modDescripcionProducto" class="form-label">Descripción
								(opcional):</label>
							<textarea id="modDescripcionProducto" name="descripcion"
								class="form-control" rows="3"></textarea>
						</div>
						<div class="mb-3">
							<label for="modPrecioProducto" class="form-label">Precio
								(opcional):</label> <input type="number" id="modPrecioProducto"
								name="precio" class="form-control" step="0.01">
						</div>
						<div class="mb-3">
							<label for="modStockProducto" class="form-label">Stock
								(opcional):</label> <input type="number" id="modStockProducto"
								name="stock" class="form-control">
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


				<!-- Sección: Ver Productos -->
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
								<th>Acciones</th>
							</tr>
						</thead>
						<tbody>
							<%
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
									<!-- Se puede mostrar la imagen aquí -->
								</td>
								<td><i class="fas fa-edit"
									style="cursor: pointer; color: blue;"
									onclick="editarProducto('<%=producto.getId()%>', '<%=producto.getNombre()%>', '<%=producto.getDescripcion()%>', '<%=producto.getPrecio()%>', '<%=producto.getStock()%>')"></i>
									<i class="fas fa-trash-alt"
									style="cursor: pointer; color: red;"
									onclick="eliminarProducto('<%=producto.getId()%>')"></i></td>
							</tr>
							<%
							}
							} else {
							out.println("<tr><td colspan='7'>No hay productos disponibles.</td></tr>");
							}
							%>
						</tbody>
					</table>
				</div>

				<!-- Sección: Ver Usuarios -->
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
								<th>Acciones</th>
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
								<td><i class="fas fa-edit"
									style="cursor: pointer; color: blue;"
									onclick="editarUsuario('<%=usuario.getIdUsuario()%>', '<%=usuario.getNombreUsuario()%>', '<%=usuario.getTelefonoUsuario()%>', '<%=usuario.getRol()%>')"></i>
									<i class="fas fa-trash-alt"
									style="cursor: pointer; color: red;"
									onclick="eliminarUsuario('<%=usuario.getIdUsuario()%>')"></i></td>
							</tr>
							<%
							}
							} else {
							out.println("<tr><td colspan='6'>No hay usuarios disponibles.</td></tr>");
							}
							%>
						</tbody>
					</table>
				</div>
				<!-- Otras secciones según se requiera -->
			</div>
		</div>
	</div>

<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
  <div id="liveToast" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
    <div class="d-flex">
      <div class="toast-body">
        Mensaje
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  </div>
</div>

<script>
  function mostrarToast(mensaje, tipo) {
    const toastEl = document.getElementById('liveToast');
    const toastBody = toastEl.querySelector('.toast-body');
    toastBody.innerText = mensaje;
    
    // Ajustar el color según el tipo
    toastEl.classList.remove('bg-success', 'bg-danger');
    if (tipo === 'exito') {
      toastEl.classList.add('bg-success');
    } else {
      toastEl.classList.add('bg-danger');
    }
    
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
  }

  document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('modificado')) {
      // Elimina espacios y fuerza minúsculas para evitar discrepancias
      const modificado = urlParams.get('modificado').trim().toLowerCase();
      if (modificado === 'true') {
        mostrarToast('Usuario actualizado con éxito', 'exito');
      } else {
        mostrarToast('Error al actualizar el usuario', 'error');
      }
    }
  });
</script>





</body>
</html>
