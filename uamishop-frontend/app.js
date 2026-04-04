const TOKEN = "Bearer token-super-secreto-qa";
const CLIENTE_ID = "11111111-1111-1111-1111-111111111111"; // UUID real

// Productos de prueba que normalmente vendrían del GET /catalogo
const PRODUCTOS_BD = [
    { id: "22222222-2222-2222-2222-222222222222", nombre: "Laptop Developer Pro", precio: 25000.00, sku: "LAP-001" },
    { id: "33333333-3333-3333-3333-333333333333", nombre: "Monitor 4K Curvo", precio: 8500.00, sku: "MON-002" },
    { id: "44444444-4444-4444-4444-444444444444", nombre: "Teclado Mecánico RGB", precio: 1500.00, sku: "TEC-003" }
];

let carrito = []; // Memoria local del carrito

function log(mensaje, color = "#a3be8c") {
    const consola = document.getElementById("consola");
    consola.innerHTML += `<div style="color: ${color}; margin-bottom: 5px;">> ${mensaje}</div>`;
    consola.scrollTop = consola.scrollHeight;
}

function getGatewayUrl() {
    let url = document.getElementById("gatewayUrl").value.trim();
    return url.endsWith("/") ? url.slice(0, -1) : url;
}

// 1. DIBUJAR CATÁLOGO
function renderizarCatalogo() {
    const contenedor = document.getElementById("catalogo");
    contenedor.innerHTML = "";
    
    PRODUCTOS_BD.forEach(prod => {
        const div = document.createElement("div");
        div.className = "tarjeta-producto";
        div.innerHTML = `
            <h3>${prod.nombre}</h3>
            <div style="font-size: 0.8rem; color: gray;">SKU: ${prod.sku}</div>
            <div class="precio">$${prod.precio.toLocaleString('es-MX')}</div>
            <button onclick="agregarAlCarrito('${prod.id}')">+ Agregar</button>
        `;
        contenedor.appendChild(div);
    });
}

// 2. GESTIÓN DEL CARRITO LOCAL
function agregarAlCarrito(idProducto) {
    const producto = PRODUCTOS_BD.find(p => p.id === idProducto);
    const itemExistente = carrito.find(i => i.productoId === idProducto);

    if (itemExistente) {
        itemExistente.cantidad++;
    } else {
        carrito.push({
            productoId: producto.id,
            nombre: producto.nombre,
            cantidad: 1,
            precioUnitario: producto.precio
        });
    }
    actualizarUICarrito();
}

function actualizarUICarrito() {
    const contenedor = document.getElementById("listaCarrito");
    const btnPagar = document.getElementById("btnPagar");
    
    if (carrito.length === 0) {
        contenedor.innerHTML = `<p style="color: gray; font-style: italic;">El carrito está vacío</p>`;
        document.getElementById("totalCarrito").innerText = "Total: $0.00 MXN";
        btnPagar.disabled = true;
        return;
    }

    let html = "";
    let total = 0;

    carrito.forEach(item => {
        const subtotal = item.cantidad * item.precioUnitario;
        total += subtotal;
        html += `
            <div class="item-carrito">
                <span>${item.cantidad}x ${item.nombre}</span>
                <strong>$${subtotal.toLocaleString('es-MX')}</strong>
            </div>
        `;
    });

    contenedor.innerHTML = html;
    document.getElementById("totalCarrito").innerText = `Total: $${total.toLocaleString('es-MX')} MXN`;
    btnPagar.disabled = false; // Habilitamos el botón de pago
}

// 3. LA MAGIA: PROCESAR COMPRA (Carrito Backend + Orden Backend)
async function procesarCompra() {
    const btnPagar = document.getElementById("btnPagar");
    btnPagar.disabled = true;
    btnPagar.innerText = "⏳ Procesando...";

    try {
        log("Iniciando transacción segura...", "#61afef");

        // Paso A: Crear el Carrito Oficial en el Backend
        log("Registrando carrito en microservicio de Ventas...", "#e5c07b");
        const resCarrito = await fetch(`${getGatewayUrl()}/api/v1/carritos`, {
            method: "POST",
            headers: { "Authorization": TOKEN, "Content-Type": "application/json" },
            body: JSON.stringify({ clienteId: CLIENTE_ID })
        });
        if(!resCarrito.ok) throw new Error("Fallo al crear carrito backend");
        const dataCarrito = await resCarrito.json();
        const carritoIdReal = dataCarrito.id;

        // Paso B: Enviar la Orden Oficial con datos del formulario
        log(`Creando Orden para el carrito ${carritoIdReal}...`, "#e5c07b");
        const resOrden = await fetch(`${getGatewayUrl()}/api/v1/ordenes`, {
            method: "POST",
            headers: { "Authorization": TOKEN, "Content-Type": "application/json" },
            body: JSON.stringify({
                clienteId: CLIENTE_ID,
                carritoId: carritoIdReal,
                items: carrito.map(item => ({
                    productoId: item.productoId,
                    cantidad: item.cantidad,
                    precioUnitario: item.precioUnitario
                })),
                direccionEnvio: {
                    calle: document.getElementById("inputCalle").value,
                    colonia: document.getElementById("inputColonia").value,
                    ciudad: document.getElementById("inputCiudad").value,
                    estado: document.getElementById("inputEstado").value,
                    codigoPostal: document.getElementById("inputCP").value,
                    pais: "México",
                    telefono: document.getElementById("inputTelefono").value
                }
            })
        });

        if(!resOrden.ok) throw new Error(`HTTP ${resOrden.status} al crear orden`);
        
        log(`¡ÉXITO! Orden creada y validada por el Dominio.`, "#98c379");
        log(`Evento 'ProductoComprado' colocado en RabbitMQ.`, "#c678dd");

        // Limpiar el carrito local tras el éxito
        carrito = [];
        actualizarUICarrito();

    } catch (error) {
        log(`❌ Error crítico: ${error.message}`, "#e06c75");
    } finally {
        btnPagar.innerText = "🛒 Confirmar y Pagar";
        actualizarUICarrito(); // Restaura el estado del botón si hubo error
    }
}

// Inicializar la tienda al cargar la página
window.onload = () => {
    renderizarCatalogo();
    actualizarUICarrito();
};