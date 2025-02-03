<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sobre Nosotros</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
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
              <li class="nav-item"><a class="nav-link" href="index.jsp">Inicio</a></li>
              <li class="nav-item"><a class="nav-link" href="nosotros.jsp">Nosotros</a></li>
               <li class="nav-item"><a href="<%= request.getContextPath() %>/productos">Productos</a></li>
              <li class="nav-item"><a class="nav-link" href="login.jsp">Iniciar Sesión</a></li>
              <li class="nav-item"><a class="nav-link" href="registro.jsp">Registrarse</a></li>
              <li class="nav-item cart-container">
                <a class="nav-link" href="carrito.jsp">
                  <i class="bi bi-cart"></i> <!-- Ícono de carrito -->
                  <span class="cart-count">0</span> <!-- Número de productos en el carrito -->
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    <div class="container-fluid">
        <div class="row">
            <!-- Columna de imagen a la izquierda con más largo -->
            <div class="col-12 col-md-6 imgContainer">
                <img class="blueDots" src="https://via.placeholder.com/150" alt="Puntos decorativos">
                <img class="mainImg" src="imagenes/Diseño sin título.png" alt="Imagen principal">
            </div>

            <!-- Columna de texto a la derecha -->
            <div class="col-12 col-md-6 textSide">
                <br><br><br>
                <p class="heading">Sobre Nosotros</p>
                <p class="subHeading">
                    Somos una empresa dedicada a proporcionar soluciones tecnológicas de alta calidad. Nuestra misión es transformar ideas innovadoras en productos funcionales que impacten positivamente en la vida de nuestros usuarios.
                </p>

                <!-- Sección de tarjetas -->
                <div class="row g-4">
                    <div class="col-12 col-md-6">
                        <div class="card shadow-sm border-0">
                            <div class="cardImgContainer">
                                <img class="cardImg" src="imagenes/scott-graham-5fNmWej4tAA-unsplash.jpg" alt="Icono 1">
                            </div>
                            <div class="cardText">
                                <p class="cardHeading">Innovación</p>
                                <p class="cardSubHeading">Creamos soluciones únicas y adaptadas a las necesidades del mercado.</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-md-6">
                        <div class="card shadow-sm border-0">
                            <div class="cardImgContainer">
                                <img class="cardImg" src="imagenes/Diseño sin título (1).png" alt="Icono 2">
                            </div>
                            <div class="cardText">
                                <p class="cardHeading">Calidad</p>
                                <p class="cardSubHeading">Nos aseguramos de que cada producto cumpla con los más altos estándares de calidad.</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-md-6">
                        <div class="card shadow-sm border-0">
                            <div class="cardImgContainer">
                                <img class="cardImg" src="imagenes/Diseño sin título (3).png" alt="Icono 3">
                            </div>
                            <div class="cardText">
                                <p class="cardHeading">Compromiso</p>
                                <p class="cardSubHeading">Trabajamos con pasión y dedicación para superar las expectativas de nuestros clientes.</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-md-6">
                        <div class="card shadow-sm border-0">
                            <div class="cardImgContainer">
                                <img class="cardImg" src="imagenes/Diseño sin título (2).png" alt="Icono 4">
                            </div>
                            <div class="cardText">
                                <p class="cardHeading">Sostenibilidad</p>
                                <p class="cardSubHeading">Fomentamos prácticas responsables para un futuro más verde y sostenible.</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Botón de exploración -->
                <a href="#services">
                    <a color="white" href="<%= request.getContextPath() %>/productos"><button class="explore">Explora nuestros productos</button></a>
                </a>
            </div>
        </div>
    </div>

    <!-- Enlazamos con Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
