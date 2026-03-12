package Control.Logica;

import Control.DAO.IMinipigDAO;
import Control.DAO.MinipigDAO;
import Modelo.MinipigVO;
import Vista.VentanaPrincipal;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 * Gestor de la logica de negocio del aplicativo MiniPig.
 *
 * <p>Proposito: Coordinar la carga de datos iniciales, la comunicacion
 * con el DAO, y el lanzamiento de la interfaz grafica. Es el puente
 * entre la Vista y el DAO.</p>
 *
 * <p>Principio SOLID aplicado: SRP - solo orquesta la logica de negocio.
 * DIP - depende de {@code IMinipigDAO} (abstraccion), no de {@code MinipigDAO} directamente.</p>
 *
 * <p>Se comunica con: {@code IMinipigDAO} (datos), {@code CargadorPropiedades}
 * (precarga), {@code VentanaPrincipal} (vista).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class GestorMinipig {

    /** Objeto de acceso a datos para operaciones CRUD. */
    private IMinipigDAO minipigDAO;

    /**
     * Constructor. Inicializa el DAO, carga los datos del properties
     * y lanza la interfaz grafica.
     */
    public GestorMinipig() {
        minipigDAO = new MinipigDAO();
        cargarDatosIniciales();
        lanzarVista();
    }

    /**
     * Lee el archivo de propiedades y carga los minipigs en la BD.
     * Los campos con valor {@code null} quedan pendientes de completar desde la UI.
     * No inserta si el codigo ya existe en la BD.
     */
    private void cargarDatosIniciales() {
        Map<String, String[]> datosCrudos = CargadorPropiedades.cargarDatosCrudos();

        for (Map.Entry<String, String[]> entrada : datosCrudos.entrySet()) {
            String[] campos = entrada.getValue();
            if (campos == null || campos.length < 11) {
                System.out.println("[Gestor] Entrada mal formada: " + entrada.getKey());
                continue;
            }

            // Crear MinipigVO a partir de datos crudos (responsabilidad del Gestor, no del CargadorPropiedades)
            MinipigVO minipig = new MinipigVO(
                    campos[0],  // codigo
                    campos[1],  // nombre
                    campos[2],  // genero
                    campos[3],  // idMicrochip
                    campos[4],  // raza
                    campos[5],  // color
                    campos[6],  // peso
                    campos[7],  // altura
                    campos[8],  // caracteristica1
                    campos[9],  // caracteristica2
                    campos[10]  // urlFoto
            );

            // Si el codigo ya existe, no reinsertar
            if (minipig.getCodigo() != null && minipigDAO.existePorCodigo(minipig.getCodigo())) {
                continue;
            }

            // Si tiene campos null, se inserta igualmente con valores vacios
            // y la Vista solicitara completar al usuario
            minipigDAO.insertarMinipig(minipig);
        }
    }

    /**
     * Lanza la ventana principal en el hilo de eventos de Swing.
     */
    private void lanzarVista() {
        GestorMinipig gestor = this;
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }

    // =====================================================================
    // METODOS DE NEGOCIO - llamados desde la Vista
    // =====================================================================

    /**
     * Registra un nuevo minipig en la BD, validando que no exista previamente.
     *
     * @param minipig datos del nuevo minipig
     * @return {@code true} si fue insertado, {@code false} si ya existia o fallo
     */
    public boolean registrarMinipig(MinipigVO minipig) {
        if (minipigDAO.existePorCodigo(minipig.getCodigo())) {
            return false;
        }
        return minipigDAO.insertarMinipig(minipig);
    }

    /**
     * Busca un minipig por su codigo.
     *
     * @param codigo codigo a buscar
     * @return {@code MinipigVO} encontrado o {@code null}
     */
    public MinipigVO buscarPorCodigo(String codigo) {
        return minipigDAO.consultarPorCodigo(codigo);
    }

    /**
     * Busca un minipig por su id de microchip.
     *
     * @param idMicrochip id del microchip
     * @return {@code MinipigVO} encontrado o {@code null}
     */
    public MinipigVO buscarPorMicrochip(String idMicrochip) {
        return minipigDAO.consultarPorMicrochip(idMicrochip);
    }

    /**
     * Retorna todos los minipigs de una raza dada.
     *
     * @param raza nombre de la raza
     * @return lista de {@code MinipigVO}
     */
    public ArrayList<MinipigVO> buscarPorRaza(String raza) {
        return minipigDAO.consultarPorRaza(raza);
    }

    /**
     * Retorna todos los minipigs con un nombre dado.
     *
     * @param nombre nombre a buscar
     * @return lista de {@code MinipigVO}
     */
    public ArrayList<MinipigVO> buscarPorNombre(String nombre) {
        return minipigDAO.consultarPorNombre(nombre);
    }

    /**
     * Retorna todos los minipigs registrados.
     *
     * @return lista completa de {@code MinipigVO}
     */
    public ArrayList<MinipigVO> obtenerTodos() {
        return minipigDAO.listarTodos();
    }

    /**
     * Elimina un minipig por su codigo.
     *
     * @param codigo codigo del minipig a eliminar
     * @return {@code true} si fue eliminado
     */
    public boolean eliminarPorCodigo(String codigo) {
        return minipigDAO.eliminarPorCodigo(codigo);
    }

    /**
     * Elimina un minipig por su id de microchip.
     *
     * @param idMicrochip id del microchip
     * @return {@code true} si fue eliminado
     */
    public boolean eliminarPorMicrochip(String idMicrochip) {
        return minipigDAO.eliminarPorMicrochip(idMicrochip);
    }

    /**
     * Actualiza los datos de un minipig en la BD.
     * Solo se modifican los campos permitidos (no codigo ni microchip).
     *
     * @param minipig objeto con los datos actualizados
     * @return {@code true} si fue actualizado
     */
    public boolean actualizarMinipig(MinipigVO minipig) {
        return minipigDAO.actualizarMinipig(minipig);
    }

    /**
     * Verifica si un codigo ya esta registrado en la BD.
     *
     * @param codigo codigo a verificar
     * @return {@code true} si existe
     */
    public boolean existeMinipig(String codigo) {
        return minipigDAO.existePorCodigo(codigo);
    }

    /**
     * Imprime en consola todos los minipigs registrados.
     * Llamado al cerrar el aplicativo (boton Salir).
     * La impresion se hace aqui (Control.Logica), no en el modelo.
     */
    public void imprimirTodosEnConsola() {
        ArrayList<MinipigVO> todos = minipigDAO.listarTodos();
        System.out.println("\n===== LISTADO FINAL DE MINIPIGS EN BD =====");
        if (todos.isEmpty()) {
            System.out.println("No hay minipigs registrados.");
        } else {
            for (int i = 0; i < todos.size(); i++) {
                MinipigVO m = todos.get(i);
                System.out.println("--- Minipig #" + (i + 1) + " ---");
                System.out.println("  Codigo      : " + m.getCodigo());
                System.out.println("  Nombre      : " + m.getNombre());
                System.out.println("  Genero      : " + m.getGenero());
                System.out.println("  Microchip   : " + m.getIdMicrochip());
                System.out.println("  Raza        : " + m.getRaza());
                System.out.println("  Color       : " + m.getColor());
                System.out.println("  Peso        : " + m.getPeso());
                System.out.println("  Altura      : " + m.getAltura());
                System.out.println("  Caract. 1   : " + m.getCaracteristica1());
                System.out.println("  Caract. 2   : " + m.getCaracteristica2());
                System.out.println("  Foto        : " + m.getUrlFoto());
            }
        }
        System.out.println("===========================================\n");
    }
}
