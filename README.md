# Dynamic Web Project - Ecommerce

Este proyecto es un Dynamic Web Project que funciona como la capa de presentación y lógica de negocio de un ecommerce de ordenadores. Se conecta a la API desarrollada en Spring Boot para manejar la persistencia de datos, enviando y recibiendo información en formato JSON. Este proyecto gestiona toda la lógica de negocio, incluyendo validaciones, procesos de autenticación, manejo de sesiones y operaciones con el carrito de compras y pedidos.

## Funcionalidades

- **Gestión de Usuarios**
  - Registro de usuarios con código de verificación por correo electrónico.
  - Inicio de sesión con validaciones y recuperación de contraseña mediante correo electrónico.
  - Manejo de sesiones para mantener la autenticación.

- **Gestión de Productos**
  - Mostrar productos disponibles en la tienda.
  - Ver detalles de un producto específico.
  - Agregar reseñas y calificaciones a los productos.

- **Carrito de Compras**
  - Agregar productos al carrito.
  - Mostrar productos en el carrito.
  - Eliminar productos del carrito.
  - Limpiar el carrito por completo.

- **Pedidos**
  - Registrar un pedido con los productos del carrito.
  - Enviar información de pago junto con el pedido.

- **Panel de Administración**
  - CRUD completo para la gestión de usuarios.
  - CRUD completo para la gestión de productos.

## Conexión con la API

Este proyecto se comunica con la API en Spring Boot mediante solicitudes HTTP, enviando y recibiendo datos en formato JSON. Todas las validaciones y la lógica de negocio se realizan en este proyecto antes de interactuar con la API.

## Tecnologías utilizadas

- **Java EE (Servlets, JSP)** para el desarrollo web.
- **JSP y HTML/CSS** para la interfaz de usuario.
- **Bootstrap** para mejorar la presentación visual.
- **JavaScript** para mejorar la interacción en el frontend.
- **Apache Tomcat** como servidor web.

## Autor

David Muñoz-Polanco Muñoz

