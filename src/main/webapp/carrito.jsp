<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="dtos.CarritoDto"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito y Pago</title>
    <script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.x.x/dist/alpine.min.js" defer></script>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/estilo.css">
    <!-- Toastify CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <!-- Toastify JS -->
    <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
</head>
<body>
    <!-- Barra de navegación -->
    <nav class="navbar">
    <a class="navbar-brand" href="#">
        <img src="imagenes/Code components-Photoroom.png" alt="Logo Code">
    </a>
    <ul>
        <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/inicio">Inicio</a></li>
        <li><a href="nosotros.jsp">Nosotros</a></li>
        <li class="nav-item"><a href="<%= request.getContextPath() %>/productos">Productos</a></li>
        
        <% if (session != null && session.getAttribute("idUsuario") != null) { %>
            <% if ("admin".equals(session.getAttribute("rol"))) { %>
                <li><a href="<%= request.getContextPath() %>/admin">Admin</a></li>
            <% } %>
            <li><a href="<%= request.getContextPath() %>/cerrarSesion">Cerrar sesión</a></li>
        <% } else { %>
            <li><a href="login.jsp">Iniciar Sesión</a></li>
            <li><a href="registro.jsp">Registrarse</a></li>
        <% } %>
        
        <li class="cart-container">
            <a href="<%= request.getContextPath() %>/carrito">
                <i class="bi bi-cart"></i> <!-- Ícono de carrito -->
                <span class="cart-count">
                    <%
                        List<CarritoDto> carrito = (List<CarritoDto>) request.getAttribute("carrito");
                        out.print(carrito != null ? carrito.size() : 0);
                    %>
                </span> <!-- Número de productos en el carrito -->
            </a>
        </li>
    </ul>
</nav>


    <%
    // Obtener el mensaje y tipo de mensaje desde la sesión
    String mensaje = (String) session.getAttribute("mensaje");
    String tipoMensaje = (String) session.getAttribute("tipoMensaje");
    if(mensaje != null && tipoMensaje != null) {
%>
<script type="text/javascript">
    Toastify({
        text: "<%= mensaje %>",
        duration: 3000, 
        gravity: "top", 
        position: "right", 
        backgroundColor: "<%= tipoMensaje.equals("success") ? "#4CAF50" : "#FF5733" %>",
        close: true
    }).showToast();
</script>
<%
    // Eliminar los atributos para que no se muestren en futuras solicitudes
    session.removeAttribute("mensaje");
    session.removeAttribute("tipoMensaje");
    }
%>


    <!-- Resto del contenido de la página de carrito -->
    <div class="min-w-screen min-h-screen bg-gray-50 py-5">
        <div class="px-5">
            <div class="mb-2">
                <h1 class="text-3xl md:text-5xl font-bold text-gray-600">Pagar.</h1>
            </div>
            <div class="mb-5 text-gray-400">
                <a href="index.jsp" class="focus:outline-none hover:underline text-gray-500">Inicio</a>
                / <a href="carrito.jsp" class="focus:outline-none hover:underline text-gray-500">Carrito</a>
                / <span class="text-gray-600">Pagar</span>
            </div>
        </div>

        <div class="w-full bg-white border-t border-b border-gray-200 px-5 py-10 text-gray-800">
            <div class="w-full">
                <div class="-mx-3 md:flex items-start">
                    <div class="px-3 md:w-7/12 lg:pr-10">
                        <div class="w-full mx-auto text-gray-800 font-light mb-6 border-b border-gray-200 pb-6">
                            <!-- Iteración de productos en el carrito -->
                            <%
                            if (carrito != null && !carrito.isEmpty()) {
                                for (CarritoDto producto : carrito) {
                            %>
                            <div class="w-full flex items-center mb-6">
                                <div class="overflow-hidden rounded-lg w-16 h-16 bg-gray-50 border border-gray-200">
                                    <img src="data:image/jpeg;base64,<%=producto.getImagen()%>" alt="Imagen producto">
                                </div>
                                <div class="flex-grow pl-3">
                                    <h5>Producto ID: <%=producto.getId()%></h5>
                                    <h6 class="font-semibold uppercase text-gray-600"><%=producto.getNombre()%></h6>
                                    <p class="text-gray-400">x <%=producto.getCantidad()%></p>
                                </div>
                                <div class="flex items-center space-x-2">
                                    <span class="font-semibold text-gray-600 text-xl"><%="€" + producto.getPrecio()%></span>
                                    <!-- Botón para eliminar producto -->
                                    <form action="carrito" method="POST">
                                        <input type="hidden" name="_method" value="DELETE">
                                        <input type="hidden" name="action" value="eliminar">
                                        <input type="hidden" name="id" value="<%=producto.getId()%>">
                                        <button type="submit" class="btn btn-danger">Eliminar</button>
                                    </form>
                                </div>
                            </div>
                            <%
                                }
                            } else {
                            %>
                            <p>No hay productos en el carrito.</p>
                            <%
                            }
                            %>
                        </div>

                        <!-- Cálculos de subtotal, impuestos y total -->
                        <div class="mb-6 pb-6 border-b border-gray-200 text-gray-800">
                            <div class="w-full flex mb-3 items-center">
                                <div class="flex-grow">
                                    <span class="text-gray-600">Subtotal</span>
                                </div>
                                <div class="pl-3">
                                    <span class="font-semibold">
                                        <%
                                        double subtotal = 0;
                                        if (carrito != null && !carrito.isEmpty()) {
                                            for (CarritoDto producto : carrito) {
                                                subtotal += producto.getPrecio() * producto.getCantidad();
                                            }
                                        }
                                        out.print("€" + subtotal);
                                        %>
                                    </span>
                                </div>
                            </div>
                            <div class="w-full flex items-center">
                                <div class="flex-grow">
                                    <span class="text-gray-600">Impuestos (IVA)</span>
                                </div>
                                <div class="pl-3">
                                    <span class="font-semibold">
                                        <%
                                        double impuestos = subtotal * 0.21; // Ejemplo de IVA del 21%
                                        out.print("€" + impuestos);
                                        %>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl">
                            <div class="w-full flex items-center">
                                <div class="flex-grow">
                                    <span class="text-gray-600">Total</span>
                                </div>
                                <div class="pl-3">
                                    <span class="font-semibold text-gray-400 text-sm">AUD</span>
                                    <span class="font-semibold">
                                        <%
                                        double total = subtotal + impuestos;
                                        out.print("€" + total);
                                        %>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Formulario de pago -->
                    <form action="pedidos" method="POST">
                        <input type="hidden" name="idUsuario" value="${idUsuario}">
                        <!-- Información de contacto -->
                        <div class="w-full mx-auto rounded-lg bg-white border border-gray-200 p-3 text-gray-800 font-light mb-6">
                            <div class="w-full flex mb-3 items-center">
                                <div class="w-32">
                                    <span class="text-gray-600 font-semibold">Contacto</span>
                                </div>
                                <div class="flex-grow pl-3">
                                    <input class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                                        placeholder="Contacto" name="contacto" maxlength="19"  required>
                                </div>
                            </div>
                            <div class="w-full flex items-center">
                                <div class="w-32">
                                    <span class="text-gray-600 font-semibold">Dirección de facturación</span>
                                </div>
                                <div class="flex-grow pl-3">
                                    <input class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                                        placeholder="Dirección facturación" name="direccion" maxlength="19"  required>
                                </div>
                            </div>
                        </div>

                        <!-- Información de pago -->
                        <div class="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6">
                            <div class="w-full p-3 border-b border-gray-200">
                                <div class="mb-5">
                                    <label for="type1" class="flex items-center cursor-pointer">
                                        <input type="radio" class="form-radio h-5 w-5 text-indigo-500" name="metodoPago" id="type1" value="tarjeta" checked>
                                        <img src="https://leadershipmemphis.org/wp-content/uploads/2020/08/780370.png" class="h-6 ml-3">
                                    </label>
                                </div>
                                <div>
                                    <div class="mb-3">
                                        <label class="text-gray-600 font-semibold text-sm mb-2 ml-1">Nombre en la tarjeta</label>
                                        <div>
                                            <input class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                                                placeholder="Nombre" type="text" name="nombreTarjeta" maxlength="19" required>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <label class="text-gray-600 font-semibold text-sm mb-2 ml-1">Número de tarjeta</label>
                                        <div>
                                            <input class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                                                placeholder="0000 0000 0000 0000" type="text" id="numeroTarjeta"  maxlength="19"  name="numeroTarjeta" required>
                                        </div>
                                    </div>
                                    <div class="mb-3 -mx-2 flex items-end">
                                        <div class="px-2 w-1/4">
                                            <label class="text-gray-600 font-semibold text-sm mb-2 ml-1">Fecha de expiración</label>
                                            <div>
                                                <select class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer" name="mesExpiracion" required>
                                                    <option value="01">01 - Enero</option>
                                                    <option value="02">02 - Febrero</option>
                                                    <option value="03">03 - Marzo</option>
                                                    <option value="04">04 - Abril</option>
                                                    <option value="05">05 - Mayo</option>
                                                    <option value="06">06 - Junio</option>
                                                    <option value="07">07 - Julio</option>
                                                    <option value="08">08 - Agosto</option>
                                                    <option value="09">09 - Septiembre</option>
                                                    <option value="10">10 - Octubre</option>
                                                    <option value="11">11 - Noviembre</option>
                                                    <option value="12">12 - Diciembre</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="px-2 w-1/4">
                                            <select class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer" name="anioExpiracion" required>
                                                <option value="2025">2025</option>
                                                <option value="2026">2026</option>
                                                <option value="2027">2027</option>
                                                <option value="2028">2028</option>
                                                <option value="2029">2029</option>
                                                <option value="2030">2030</option>
                                            </select>
                                        </div>
                                        <div class="px-2 w-1/4">
                                            <label class="text-gray-600 font-semibold text-sm mb-2 ml-1">CVV</label>
                                            <div>
                                                <input class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                                                    placeholder="000" type="text" id="cvv"  maxlength="3"  name="cvv" required>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <button class="block w-full max-w-xs mx-auto bg-indigo-500 hover:bg-indigo-700 focus:bg-indigo-700 text-white rounded-lg px-3 py-2 font-semibold">Pagar ahora</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>
