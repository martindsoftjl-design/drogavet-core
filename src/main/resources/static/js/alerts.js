document.addEventListener('DOMContentLoaded', () => {

    /*
     * ============================================================
     * ALERTAS DE CONFIRMACIÓN (FLASH ATTRIBUTES)
     * ============================================================
     */
    const mensaje = document.body.dataset.mensaje;
    const tipo = document.body.dataset.tipo;

    if (mensaje) {

        let icon = 'success';
        let title = '¡Operación exitosa!';

        switch (tipo) {
            case 'danger':
            case 'error':
                icon = 'error';
                title = 'Error';
                break;
            case 'warning':
                icon = 'warning';
                title = 'Advertencia';
                break;
            case 'info':
                icon = 'info';
                title = 'Información';
                break;
        }

        Swal.fire({
            icon,
            title,
            text: mensaje,
            confirmButtonText: 'OK'
        });
    }

    /*
     * ============================================================
     * CONFIRMACIÓN GLOBAL DE ELIMINACIÓN
     * ============================================================
     * Usar: class="btn-eliminar"
     * data-nombre="empresa / área / empleado"
     */
    document.querySelectorAll('.btn-eliminar').forEach(boton => {

        boton.addEventListener('click', function (e) {
            e.preventDefault();

            const url = this.getAttribute('href');
            const nombre = this.dataset.nombre || 'registro';

            Swal.fire({
                title: '¿Estás seguro?',
                text: `Esta acción eliminará el ${nombre}.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar',
                confirmButtonColor: '#dc3545'
            }).then(result => {
                if (result.isConfirmed) {
                    window.location.href = url;
                }
            });
        });
    });

    /*
     * ============================================================
     * CONFIRMACIÓN GLOBAL DE GUARDAR / EDITAR
     * ============================================================
     * Usar: class="form-confirmar"
     */
    document.querySelectorAll('.form-confirmar').forEach(form => {

        form.addEventListener('submit', function (e) {
            e.preventDefault();

            Swal.fire({
                title: '¿Confirmar operación?',
                text: '¿Deseas guardar los cambios?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Sí, guardar',
                cancelButtonText: 'Cancelar',
                confirmButtonColor: '#198754'
            }).then(result => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });

});
