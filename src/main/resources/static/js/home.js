/**
 * HOME.JS
 * Página principal - Sistema Integral del Laboratorio
 */

document.addEventListener("DOMContentLoaded", () => {

    console.log("Home cargado correctamente");

    // Animación suave al pasar el mouse (fallback JS si el CSS no carga)
    const cards = document.querySelectorAll(".dashboard-card");

    cards.forEach(card => {
        card.addEventListener("mouseenter", () => {
            card.style.transform = "translateY(-6px)";
        });

        card.addEventListener("mouseleave", () => {
            card.style.transform = "translateY(0)";
        });
    });

    // Futuro: aquí puedes cargar datos dinámicos
    // cargarResumenDashboard();

});

/**
 * Ejemplo futuro:
 * Cargar datos del dashboard vía API REST
 */
function cargarResumenDashboard() {
    fetch("/api/dashboard/resumen")
        .then(response => response.json())
        .then(data => {
            console.log("Resumen:", data);
            // actualizar contadores aquí
        })
        .catch(error => console.error("Error cargando resumen", error));
}

		/*(ocultar AFP si es ONP)*/
    const sistema = document.querySelector('[name="sistemaPension"]');
    const afpDiv = document.querySelector('[name="afp"]').closest('.col-md-6');

    function toggleAfp() {
        afpDiv.style.display = sistema.value === 'AFP' ? 'block' : 'none';
    }

    sistema.addEventListener('change', toggleAfp);
    document.addEventListener('DOMContentLoaded', toggleAfp);

