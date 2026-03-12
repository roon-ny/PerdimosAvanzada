package Vista;

import Control.Logica.GestorMinipig;
import Modelo.MinipigVO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Clase de manejo de eventos de la interfaz grafica.
 *
 * <p>Proposito: Separar la logica de eventos del diseno de la vista.
 * Implementa {@code ActionListener} y delega la logica al gestor.</p>
 *
 * <p>Principio SOLID aplicado: SRP - solo maneja eventos de UI.
 * No contiene logica de negocio ni acceso a datos.
 * OCP - nuevos eventos pueden agregarse sin modificar {@code VentanaPrincipal}.</p>
 *
 * <p>Se comunica con: {@code VentanaPrincipal} (para leer/escribir campos)
 * y {@code GestorMinipig} (para operaciones de negocio).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class EventosVentana implements ActionListener {

    /** Referencia a la ventana principal para leer y escribir campos. */
    private VentanaPrincipal vista;

    /** Referencia al gestor de logica de negocio. */
    private GestorMinipig gestor;

    // Comandos de accion (identificadores de cada boton)
    /** Comando para insertar un minipig. */
    public static final String CMD_INSERTAR   = "INSERTAR";
    /** Comando para buscar por codigo. */
    public static final String CMD_BUSCAR_COD = "BUSCAR_COD";
    /** Comando para buscar por microchip. */
    public static final String CMD_BUSCAR_MIC = "BUSCAR_MIC";
    /** Comando para buscar por raza. */
    public static final String CMD_BUSCAR_RAZA = "BUSCAR_RAZA";
    /** Comando para buscar por nombre. */
    public static final String CMD_BUSCAR_NOM  = "BUSCAR_NOM";
    /** Comando para eliminar por codigo. */
    public static final String CMD_ELIM_COD   = "ELIM_COD";
    /** Comando para eliminar por microchip. */
    public static final String CMD_ELIM_MIC   = "ELIM_MIC";
    /** Comando para cargar datos de un minipig para modificar. */
    public static final String CMD_CARGAR_MOD = "CARGAR_MOD";
    /** Comando para guardar los cambios de la modificacion. */
    public static final String CMD_GUARDAR_MOD = "GUARDAR_MOD";
    /** Comando para limpiar los campos de la interfaz. */
    public static final String CMD_LIMPIAR    = "LIMPIAR";
    /** Comando para salir del aplicativo. */
    public static final String CMD_SALIR      = "SALIR";
    /** Comando para seleccionar un minipig del combo. */
    public static final String CMD_COMBO_SEL  = "COMBO_SEL";

    /**
     * Constructor que recibe referencias a la vista y al gestor.
     *
     * @param vista   ventana principal
     * @param gestor  gestor de logica de negocio
     */
    public EventosVentana(VentanaPrincipal vista, GestorMinipig gestor) {
        this.vista  = vista;
        this.gestor = gestor;
    }

    /**
     * Punto central de despacho de eventos.
     * Delega a un metodo especifico segun el comando del boton.
     *
     * @param e evento de accion generado por un componente
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CMD_INSERTAR:    performInsertar();    break;
            case CMD_BUSCAR_COD:  performBuscarCodigo();  break;
            case CMD_BUSCAR_MIC:  performBuscarMicrochip(); break;
            case CMD_BUSCAR_RAZA: performBuscarRaza();   break;
            case CMD_BUSCAR_NOM:  performBuscarNombre(); break;
            case CMD_ELIM_COD:    performEliminarCodigo(); break;
            case CMD_ELIM_MIC:    performEliminarMicrochip(); break;
            case CMD_CARGAR_MOD:  performCargarParaModificar(); break;
            case CMD_GUARDAR_MOD: performGuardarModificacion(); break;
            case CMD_LIMPIAR:     performLimpiar();      break;
            case CMD_SALIR:       performSalir();        break;
            case CMD_COMBO_SEL:   performComboSeleccion(); break;
            default: break;
        }
    }

    /**
     * Maneja la insercion de un nuevo minipig.
     * Lee los campos del panel de insercion y llama al gestor.
     */
    private void performInsertar() {
        MinipigVO minipig = vista.obtenerDatosInsercion();
        if (minipig == null) return;

        if (gestor.existeMinipig(minipig.getCodigo())) {
            vista.mostrarMensaje(
                "Ya existe un minipig con el codigo: " + minipig.getCodigo(),
                "Registro duplicado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestor.registrarMinipig(minipig)) {
            vista.mostrarMensaje("Minipig registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            vista.refrescarCombo(gestor.obtenerTodos());
            vista.limpiarCampos();
        } else {
            vista.mostrarMensaje("No se pudo registrar el minipig.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja la busqueda de un minipig por codigo.
     */
    private void performBuscarCodigo() {
        String codigo = vista.getCampoBusqueda();
        if (codigo.isEmpty()) { vista.mostrarMensaje("Ingrese un codigo.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        MinipigVO m = gestor.buscarPorCodigo(codigo);
        if (m != null) {
            vista.mostrarMinipig(m);
        } else {
            vista.mostrarMensaje("No se encontro minipig con codigo: " + codigo, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Maneja la busqueda de un minipig por id de microchip.
     */
    private void performBuscarMicrochip() {
        String chip = vista.getCampoBusqueda();
        if (chip.isEmpty()) { vista.mostrarMensaje("Ingrese un id de microchip.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        MinipigVO m = gestor.buscarPorMicrochip(chip);
        if (m != null) {
            vista.mostrarMinipig(m);
        } else {
            vista.mostrarMensaje("No se encontro minipig con microchip: " + chip, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Maneja la busqueda de minipigs por raza.
     */
    private void performBuscarRaza() {
        String raza = vista.getCampoBusqueda();
        if (raza.isEmpty()) { vista.mostrarMensaje("Ingrese una raza.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        ArrayList<MinipigVO> lista = gestor.buscarPorRaza(raza);
        vista.mostrarListaMinipigs(lista, "Resultados por raza: " + raza);
    }

    /**
     * Maneja la busqueda de minipigs por nombre.
     */
    private void performBuscarNombre() {
        String nombre = vista.getCampoBusqueda();
        if (nombre.isEmpty()) { vista.mostrarMensaje("Ingrese un nombre.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        ArrayList<MinipigVO> lista = gestor.buscarPorNombre(nombre);
        vista.mostrarListaMinipigs(lista, "Resultados por nombre: " + nombre);
    }

    /**
     * Maneja la eliminacion de un minipig por codigo.
     */
    private void performEliminarCodigo() {
        String codigo = vista.getCampoEliminar();
        if (codigo.isEmpty()) { vista.mostrarMensaje("Ingrese el codigo.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        int conf = vista.confirmarAccion("¿Eliminar el minipig con codigo " + codigo + "?");
        if (conf != JOptionPane.YES_OPTION) return;
        if (gestor.eliminarPorCodigo(codigo)) {
            vista.mostrarMensaje("Minipig eliminado.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            vista.refrescarCombo(gestor.obtenerTodos());
            vista.limpiarCampos();
        } else {
            vista.mostrarMensaje("No se encontro o no se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja la eliminacion de un minipig por microchip.
     */
    private void performEliminarMicrochip() {
        String chip = vista.getCampoEliminar();
        if (chip.isEmpty()) { vista.mostrarMensaje("Ingrese el id de microchip.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        int conf = vista.confirmarAccion("¿Eliminar el minipig con microchip " + chip + "?");
        if (conf != JOptionPane.YES_OPTION) return;
        if (gestor.eliminarPorMicrochip(chip)) {
            vista.mostrarMensaje("Minipig eliminado.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            vista.refrescarCombo(gestor.obtenerTodos());
            vista.limpiarCampos();
        } else {
            vista.mostrarMensaje("No se encontro o no se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los datos de un minipig en el panel de modificacion para ser editados.
     */
    private void performCargarParaModificar() {
        String codigo = vista.getCodigoModificacion();
        if (codigo.isEmpty()) { vista.mostrarMensaje("Ingrese el codigo a modificar.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        MinipigVO m = gestor.buscarPorCodigo(codigo);
        if (m != null) {
            vista.cargarDatosEnModificacion(m);
        } else {
            vista.mostrarMensaje("No se encontro minipig con ese codigo.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Guarda los cambios realizados sobre el DTO en memoria y los persiste en BD.
     */
    private void performGuardarModificacion() {
        MinipigVO minipigActualizado = vista.obtenerDatosModificacion();
        if (minipigActualizado == null) return;
        if (gestor.actualizarMinipig(minipigActualizado)) {
            vista.mostrarMensaje("Minipig actualizado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            vista.refrescarCombo(gestor.obtenerTodos());
        } else {
            vista.mostrarMensaje("No se pudo actualizar el minipig.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia todos los campos de la interfaz activa.
     */
    private void performLimpiar() {
        vista.limpiarCampos();
    }

    /**
     * Muestra todos los minipigs en consola y cierra el aplicativo.
     */
    private void performSalir() {
        gestor.imprimirTodosEnConsola();
        System.exit(0);
    }

    /**
     * Muestra la informacion del minipig seleccionado en el combo desplegable.
     */
    private void performComboSeleccion() {
        String codigoNombre = vista.getComboSeleccionado();
        if (codigoNombre == null || codigoNombre.isEmpty()) return;
        String codigo = codigoNombre.split(" - ")[0].trim();
        MinipigVO m = gestor.buscarPorCodigo(codigo);
        if (m != null) {
            vista.mostrarMinipig(m);
        }
    }
}
