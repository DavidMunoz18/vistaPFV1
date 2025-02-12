<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,dtos.ProductoDto"%>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Code Components</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
    <div
      id="carouselExampleAutoplaying"
      class="carousel slide"
      data-bs-ride="carousel"
    >
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img src="imagenes/carrusel.png" class="d-block w-100" alt="..." />
        </div>
        <div class="carousel-item">
          <img src="" class="d-block w-100" alt="..." />
        </div>
        <div class="carousel-item">
          <img src="..." class="d-block w-100" alt="..." />
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#carouselExampleAutoplaying"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#carouselExampleAutoplaying"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
   <div class="container my-5">
  <h2 class="mb-4">Nuevos productos</h2>
  <div class="row">
    <%
    // Obtener la lista de productos desde el modelo
    List<ProductoDto> productos = (List<ProductoDto>) request.getAttribute("productos");

    if (productos != null && !productos.isEmpty()) {
        int count = 0; // Contador para limitar a 6 productos
        for (ProductoDto producto : productos) {
            if (count >= 6) { // Mostrar solo los primeros 6 productos
                break;
            }
            count++;
    %>
    <div class="col-md-2">
      <div class="card product-card">
        <a href="vistas.jsp?id=<%=producto.getId()%>">
          <img
            src="data:image/png;base64,<%=producto.getImagenBase64()%>"
            alt="<%=producto.getNombre()%>"
            class="card-img-top"
            height="150"
            width="200"
          />
        </a>
        <div class="card-body">
          <h5 class="card-title text-truncate"><%=producto.getNombre()%></h5>
          <p class="card-text">
            <span class="fw-bold">$<%=producto.getPrecio()%></span>
          </p>
          <form action="<%=request.getContextPath()%>/carrito" method="POST">
            <input type="hidden" name="action" value="agregar">
            <input type="hidden" name="id" value="<%=producto.getId()%>">
            <input type="hidden" name="nombre" value="<%=producto.getNombre()%>">
            <input type="hidden" name="precio" value="<%=producto.getPrecio()%>">
            <input type="hidden" name="imagen" value="<%=producto.getImagen()%>">
            <input type="number" name="cantidad" value="1" min="1" required>
            <button type="submit" class="btn btn-primary">Agregar al carrito</button>
          </form>
        </div>
      </div>
    </div>
    <%
        }
    } else {
    %>
    <div class="col-12">
      <p>No hay productos disponibles.</p>
    </div>
    <%
    }
    %>
  </div>
  <div class="text-end mt-3">
    <a class="btn btn-link" href="#"> Ver Más Productos </a>
  </div>
</div>
     <div class="container my-5">
        <div class="container my-5">
            <div class="row justify-content-between">
              <div class="col-md-2">
                <div class="card category-card">
                  <img
                    alt="Complementos"
                    class="card-img-top"
                    height="300"
                    src="imagenes/Component 17.png"
                    width="300"
                  />
                  <div class="card-body text-center">
                    
                    <a class="btn btn-link" href="#"> Ver todos </a>
                  </div>
                </div>
              </div>
              <div class="col-md-2">
                <div class="card category-card">
                  <img
                    alt="Portátiles MSI"
                    class="card-img-top"
                    height="300"
                    src="imagenes/Portatiles.png"
                    width="300"
                  />
                  <div class="card-body text-center">                    
                    <a class="btn btn-link" href="#"> Ver todos </a>
                  </div>
                </div>
              </div>
              <div class="col-md-2">
                <div class="card category-card">
                  <img
                    alt="Ordenadores de mesa"
                    class="card-img-top"
                    height="300"
                    src="imagenes/ordenadormesa.png"
                    width="300"
                  />
                  <div class="card-body text-center">                    
                    <a class="btn btn-link" href="#"> Ver todos </a>
                  </div>
                </div>
              </div>
              <div class="col-md-2">
                <div class="card category-card">
                  <img
                    alt="Monitores"
                    class="card-img-top"
                    height="300"
                    src="imagenes/monitor.png"
                    width="300"
                  />
                  <div class="card-body text-center">
                    <a class="btn btn-link" href="#"> Ver todos </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
          <div class="container my-5 text-center">
            <div class="row">
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/roccat.png" />
              </div>
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/logomsi.png" />
              </div>
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/logorazer.png" />
              </div>
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/logotherma.png" />
              </div>
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/logoadata.png" />
              </div>
              <div class="col-6 col-md-2">
                <img style="height: 100px;" alt="Marca Logo" class="img-fluid" src="imagenes/logohp.png" />
              </div>
            </div>
          </div>
    <div class="container my-5">
      <h2 class="mb-4">
        Síguenos en Instagram para recibir noticias, ofertas y más
      </h2>
      <div class="row">
        <div class="col-md-3">
          <div class="card">
            <img
              alt="Instagram Publicación"
              class="card-img-top"
              height="150"
              src="imagenes/image 29.png"
              width="100"
            />
            <div class="card-body">
              <p class="card-text">
                El conocimiento compra una PC excelente con un excelente
                rendimiento. No te pierdas esta oportunidad de mejorar tu
                experiencia de juego.
              </p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card">
            <img
              alt="Instagram Publicación"
              class="card-img-top"
              height="150"
              src="imagenes/image 290.png"
              width="300"
            />
            <div class="card-body">
              <p class="card-text">
                El conocimiento compra una PC excelente con un excelente
                rendimiento. No te pierdas esta oportunidad de mejorar tu
                experiencia de juego.
              </p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card">
            <img
              alt="Instagram Publicación"
              class="card-img-top"
              height="150"
              src="imagenes/image 291.png"
              width="300"
            />
            <div class="card-body">
              <p class="card-text">
                El conocimiento compra una PC excelente con un excelente
                rendimiento. No te pierdas esta oportunidad de mejorar tu
                experiencia de juego.
              </p>
            </div>
          </div>
        </div>
        
        
        <div class="col-md-3">
          <div class="card">
            <img
              alt="Instagram Publicación"
              class="card-img-top"
              height="150"
              src="imagenes/image 293.png"
              width="400"
            />
            <div class="card-body">
              <p class="card-text">
                El conocimiento compra una PC excelente con un excelente
                rendimiento. No te pierdas esta oportunidad de mejorar tu
                experiencia de juego.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br>
    <div class="container my-5 text-center">
      <div class="row">
        <div class="col-md-4">
          <i class="fas fa-headset fa-3x mb-3"> </i>
          <h5>Soporte técnico</h5>
          <p>Hasta 3 años de garantía disponible para su tranquilidad.</p>
        </div>
        <div class="col-md-4">
          <i class="fas fa-user fa-3x mb-3"> </i>
          <h5>Cuenta personal</h5>
          <p>
            Con grandes descuentos, entrega gratuita y acceso prioritario en
            soporte dedicado.
          </p>
        </div>
        <div class="col-md-4">
          <i class="fas fa-tags fa-3x mb-3"> </i>
          <h5>Ahorros increíbles</h5>
          <p>
            Hasta 70% de descuento en productos nuevos para estar seguro de su
            mejor precio.
          </p>
        </div>
      </div>
    </div>
    <br>
    <footer class="footer text-light py-5" style="background-color: #000;">
      <div class="container">
        <div class="row">
          <!-- Boletín -->
          <div class="col-md-4 mb-4">
            <h5>Suscríbete a nuestro boletín</h5>
            <p class="small">Sé el primero en enterarte de las últimas ofertas y noticias.</p>
            <form class="d-flex">
              <input
                aria-label="Email"
                class="form-control me-2"
                placeholder="Tu email"
                type="email"
                required
                style="border-radius: 50px;" />
                <button
                class="btn"
                type="submit"
                style="border-radius: 50px; background-color: #0156FF; color: #fff;">
                Suscribirse
              </button>
            </form>
          </div>
          <!-- Enlaces rápidos -->
          <div class="col-md-2 mb-4">
            <h5>Información</h5>
            <ul class="list-unstyled small">
              <li><a href="#" class="text-white">Sobre nosotros</a></li>
              <li><a href="#" class="text-white">Política de privacidad</a></li>
              <li><a href="#" class="text-white">Términos y condiciones</a></li>
              <li><a href="#" class="text-white">Devoluciones</a></li>
            </ul>
          </div>
          <div class="col-md-2 mb-4">
            <h5>Piezas de PC</h5>
            <ul class="list-unstyled small">
              <li><a href="#" class="text-white">Procesadores</a></li>
              <li><a href="#" class="text-white">Tarjetas gráficas</a></li>
              <li><a href="#" class="text-white">Placas base</a></li>
              <li><a href="#" class="text-white">Memoria RAM</a></li>
            </ul>
          </div>
          <div class="col-md-2 mb-4">
            <h5>Portátiles</h5>
            <ul class="list-unstyled small">
              <li><a href="#" class="text-white">Gaming</a></li>
              <li><a href="#" class="text-white">Trabajo</a></li>
              <li><a href="#" class="text-white">Estudiantes</a></li>
              <li><a href="#" class="text-white">Diseño gráfico</a></li>
            </ul>
          </div>
          <!-- Redes sociales -->
          <div class="col-md-2 mb-4 text-md-end text-center">
            <h5>Síguenos</h5>
            <div class="d-flex justify-content-md-end justify-content-center">
              <a class="text-white me-3" href="#"><i class="fab fa-facebook-f"></i></a>
              <a class="text-white me-3" href="#"><i class="fab fa-twitter"></i></a>
              <a class="text-white me-3" href="#"><i class="fab fa-instagram"></i></a>
              <a class="text-white" href="#"><i class="fab fa-linkedin-in"></i></a>
            </div>
          </div>
        </div>
        <!-- Derechos de autor -->
        <div class="row mt-4">
          <div class="col text-center">
            <p class="text-white small mb-0">© 2024 Code Components Pty. Ltd. Todos los derechos reservados.</p>
          </div>
        </div>
      </div>
    </footer>
    
    
        

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
  </body>
</html>