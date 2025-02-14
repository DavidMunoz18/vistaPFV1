<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="dtos.ProductoDto"%>
<%@ page session="true"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tienda de Productos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/estilo.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
                 
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid py-4">
        <div class="row">
            <!-- Sidebar (Menú lateral) -->
            <div class="col-12 col-md-3">
                <div class="sidebar">
                    <h5 class="mb-3">Categorías</h5>
                    <ul class="list-group">
                        <li class="list-group-item"><a href="<%=request.getContextPath()%>/productos?categoria=Ordenadores" class="text-decoration-none">Ordenadores</a></li>
                        <li class="list-group-item"><a href="<%=request.getContextPath()%>/productos?categoria=Componentes" class="text-decoration-none">Componentes</a></li>
                        <li class="list-group-item"><a href="<%=request.getContextPath()%>/productos?categoria=Periféricos" class="text-decoration-none">Periféricos</a></li>
                        <li class="list-group-item"><a href="<%=request.getContextPath()%>/productos?categoria=Portátiles" class="text-decoration-none">Portátiles</a></li>
                        <li class="list-group-item"><a href="<%=request.getContextPath()%>/productos?categoria=Ofertas" class="text-decoration-none">Ofertas</a></li>
                    </ul>
                    <hr>
                    <h5 class="mb-3">Filtrar por precio</h5>
                    <form action="<%=request.getContextPath()%>/productos" method="get">
                        <div class="mb-3">
                            <label for="minPrice" class="form-label">Mínimo:</label>
                            <input type="number" id="minPrice" name="minPrice" class="form-control" placeholder="0">
                        </div>
                        <div class="mb-3">
                            <label for="maxPrice" class="form-label">Máximo:</label>
                            <input type="number" id="maxPrice" name="maxPrice" class="form-control" placeholder="1000">
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Aplicar filtros</button>
                    </form>
                </div>
            </div>

            <!-- Main content (Productos) -->
            <div class="col-12 col-md-9">
                <div class="row g-4">
                    <%
                    // Obtener la lista de productos desde el modelo
                    List<ProductoDto> productos = (List<ProductoDto>) request.getAttribute("productos");

                    if (productos != null && !productos.isEmpty()) {
                        for (ProductoDto producto : productos) {
                    %>
                    <div class="col-12 col-sm-6 col-lg-3">
                        <div class="product-card">
                            <a href="vistas.jsp?id=<%=producto.getId()%>">
                                <img src="data:image/png;base64,<%=producto.getImagenBase64()%>" alt="<%=producto.getNombre()%>" class="img-fluid">
                            </a>

                            <div class="p-3">
                                <h6 class="text-truncate"><%=producto.getNombre()%></h6>
                                <p class="mb-1"><span class="price">$<%=producto.getPrecio()%></span></p>
                                <form action="<%=request.getContextPath()%>/carrito" method="POST">
                                    <input type="hidden" name="action" value="agregar">
                                    <input type="hidden" name="id" value="<%=producto.getId()%>">
                                    <input type="hidden" name="nombre" value="<%=producto.getNombre()%>">
                                    <input type="hidden" name="precio" value="<%=producto.getPrecio()%>">
                                    <input type="hidden" name="imagen" value="<%=producto.getImagen()%>">
                                    <input type="number" name="cantidad" value="1" min="1" required>
                                    <button type="submit">Agregar al carrito</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <%
                    }
                    } else {
                    %>
                    <p>No se encontraron productos.</p>
                    <%
                    }
                    %>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	
</body>

</html>
