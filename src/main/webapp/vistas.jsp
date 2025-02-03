<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="Dtos.ProductoDto"%>
<%@ page import="Dtos.ReseniaDto"%>
<%@ page import="Servicios.ProductoServicio"%>
<%@ page import="Servicios.ResenaServicio"%>
<%@ page import="java.util.List"%>
<%@ page session="true"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles del Producto - <%= request.getParameter("id") %></title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Iconos de Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Estilos personalizados -->
    <link rel="stylesheet" href="css/estilo.css">
</head>

<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">
          <img src="imagenes/Code components-Photoroom.png" alt="Logo">
        </a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavDropdown"
          aria-controls="navbarNavDropdown"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/inicio">Inicio</a></li>
            <li class="nav-item"><a class="nav-link" href="nosotros.jsp">Nosotros</a></li>
           <li class="nav-item"><a href="<%= request.getContextPath() %>/productos">Productos</a></li>

            <li class="nav-item"><a class="nav-link" href="login.jsp">Iniciar Sesión</a></li>
            <li class="nav-item"><a class="nav-link" href="registro.jsp">Registrarse</a></li>
            <li class="nav-item cart-container">
              <a href="<%= request.getContextPath() %>/carrito">
                <i class="bi bi-cart"></i> <!-- Ícono de carrito -->
                <span class="cart-count">0</span> <!-- Número de productos en el carrito -->
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Contenido principal -->
    <div class="container py-5">
        <%
            String idProducto = request.getParameter("id");
            ProductoDto producto = null;
            List<ReseniaDto> resenias = null;

            // Comprobamos que el idProducto no sea nulo o vacío
            if (idProducto != null && !idProducto.isEmpty()) {
                // Llamamos al servicio para obtener el producto por su ID
                ProductoServicio productoServicio = new ProductoServicio();
                producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));

                if (producto != null) {
                    // Si el producto existe, obtenemos las reseñas relacionadas
                    ResenaServicio resenaServicio = new ResenaServicio();
                    resenias = resenaServicio.obtenerReseniasPorProducto(Long.parseLong(idProducto));
                }
            }
        %>

        <% if (producto != null) { %>
        <!-- Detalles del producto -->
        <div class="row product-detail">
            <div class="col-md-6">
                <img src="data:image/png;base64,<%= producto.getImagenBase64() %>" alt="<%= producto.getNombre() %>" class="img-fluid">
            </div>
            <div class="col-md-6">
                <h1><%= producto.getNombre() %></h1>
                <p><strong>Precio:</strong> $<%= producto.getPrecio() %></p>
                <p><strong>Descripción:</strong> <%= producto.getDescripcion() %></p>
            </div>
        </div>

        <!-- Reseñas del producto -->
        <h2 class="mt-4">Reseñas del Producto</h2>
        <% if (resenias != null && !resenias.isEmpty()) { %>
        <ul class="list-group">
            <% for (ReseniaDto resenia : resenias) { %>
            <li class="list-group-item">
                <p><strong>Calificación:</strong> <%= resenia.getCalificacion() %> / 5</p>
                <p><strong>Comentario:</strong> <%= resenia.getContenidoResena() %></p>
            </li>
            <% } %>
        </ul>
        <% } else { %>
        <p>No hay reseñas para este producto.</p>
        <% } %>

        <!-- Formulario para agregar reseñas -->
        <h3 class="mt-4">Agregar una Nueva Reseña</h3>
        <form action="resenas" method="POST" class="mt-3">
            <input type="hidden" name="idProducto" value="<%= producto.getId() %>">
            <div class="mb-3">
                <label for="calificacion" class="form-label">Calificación (1-5):</label>
                <input type="number" class="form-control" id="calificacion" name="calificacion" min="1" max="5" required>
            </div>
            <div class="mb-3">
                <label for="contenidoResena" class="form-label">Comentario:</label>
                <textarea class="form-control" id="contenidoResena" name="contenidoResena" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Enviar Reseña</button>
        </form>

        <% } else { %>
        <p>Producto no encontrado.</p>
        <% } %>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
