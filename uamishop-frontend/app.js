const TOKEN = "Bearer token-super-secreto-qa";
let carritoIdActual = "";

function log(mensaje, color = "#2ecc71") {
    const consola = document.getElementById("consola");
    consola.innerHTML += `<div style="color: ${color}; margin-bottom: 5px;">> ${mensaje}</div>`;
    consola.scrollTop = consola.scrollHeight;
}

function getGatewayUrl() {
    let url = document.getElementById("gatewayUrl").value.trim();
    if (!url) { alert("Ingresa la URL del Gateway"); throw new Error("Sin URL"); }
    return url.endsWith("/") ? url.slice(0, -1) : url;
}

async function listarCategorias() {
    try {
        log("Conectando al Gateway -> Catálogo...", "#3498db");
        const res = await fetch(`${getGatewayUrl()}/api/v1/catalogo/categorias`, {
            headers: { "Authorization": TOKEN }
        });
        if(!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        log(`Éxito: Se encontraron ${data.length} categorías.`);
    } catch (e) { log(`Error en Catálogo: ${e.message}`, "#e74c3c"); }
}

async function crearCarrito() {
    try {
        log("Conectando al Gateway -> Ventas (Creando Carrito)...", "#f39c12");
        const res = await fetch(`${getGatewayUrl()}/api/v1/carritos`, {
            method: "POST",
            headers: { "Authorization": TOKEN, "Content-Type": "application/json" },
            body: JSON.stringify({ clienteId: "cliente-profesor-100" })
        });
        if(!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        carritoIdActual = data.id;
        log(`¡Carrito Creado! ID: ${carritoIdActual}`);
    } catch (e) { log(`Error al crear carrito: ${e.message}`, "#e74c3c"); }
}

async function crearOrden() {
    if (!carritoIdActual) {
        log("Error: Primero debes crear un carrito (Paso 2).", "#e74c3c");
        return;
    }
    try {
        log(`Conectando al Gateway -> Órdenes (Procesando carrito ${carritoIdActual})...`, "#27ae60");
        const res = await fetch(`${getGatewayUrl()}/api/v1/ordenes`, {
            method: "POST",
            headers: { "Authorization": TOKEN, "Content-Type": "application/json" },
            body: JSON.stringify({ carritoId: carritoIdActual })
        });
        if(!res.ok) throw new Error(`HTTP ${res.status}`);
        
        log(`¡ORDEN CREADA CON ÉXITO! El evento ha sido enviado a RabbitMQ.`, "#f1c40f");
        log(`Revisa los logs del contenedor 'uamishop-catalogo'.`, "#f1c40f");
    } catch (e) { log(`Error al crear orden: ${e.message}`, "#e74c3c"); }
}