package Modelo.Conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase Singleton que gestiona la conexion a la Base de Datos MySQL.
 *
 * <p>Proposito: Proveer una unica instancia de conexion a la BD, leyendo
 * los parametros desde {@code data/db.properties} para evitar rutas
 * o credenciales "quemadas" en el codigo fuente.</p>
 *
 * <p>Principio SOLID aplicado: SRP - solo gestiona la conexion.
 * No crea objetos del modelo ni tiene logica de negocio.</p>
 *
 * <p>Se comunica con: {@code MinipigDAO} (unico consumidor de la conexion).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class Conexion {

    /** Instancia unica de la conexion (patron Singleton). */
    private static Connection cn = null;

    /** Ruta relativa al archivo de propiedades de BD. */
    private static final String RUTA_PROPS = "data/db.properties";

    /**
     * Constructor privado para evitar instanciacion directa (Singleton).
     */
    private Conexion() { }

    /**
     * Retorna la conexion activa a la BD.
     * Lee los parametros desde {@code data/db.properties}.
     * Si la conexion ya existe y sigue abierta, la reutiliza.
     *
     * @return objeto {@code Connection} listo para usar, o {@code null} si falla
     */
    public static Connection getConexion() {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(RUTA_PROPS)) {
                props.load(fis);
            } catch (IOException ioEx) {
                System.out.println("[Conexion] No se pudo leer db.properties: " + ioEx.getMessage());
                return null;
            }

            String url      = props.getProperty("db.url");
            String usuario  = props.getProperty("db.usuario");
            String clave    = props.getProperty("db.contrasena");

            cn = DriverManager.getConnection(url, usuario, clave);

        } catch (SQLException ex) {
            System.out.println("[Conexion] No se pudo establecer conexion a la BD: " + ex.getMessage());
        }
        return cn;
    }

    /**
     * Cierra y libera la conexion activa.
     * Debe llamarse al finalizar cada operacion DAO.
     */
    public static void desconectar() {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
            }
        } catch (SQLException ex) {
            System.out.println("[Conexion] Error al cerrar la conexion: " + ex.getMessage());
        } finally {
            cn = null;
        }
    }
}
