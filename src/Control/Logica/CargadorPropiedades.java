package Control.Logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Clase responsable de leer el archivo de propiedades de minipigs.
 *
 * <p>Proposito: Leer y retornar los datos crudos (como arreglos de String)
 * del archivo {@code data/minipigs.properties}. No crea objetos del modelo.</p>
 *
 * <p>Principio SOLID aplicado: SRP - solo lee propiedades y retorna datos crudos.
 * La creacion de objetos {@code MinipigVO} es responsabilidad de {@code GestorMinipig}.</p>
 *
 * <p>Se comunica con: {@code GestorMinipig} (unico consumidor).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class CargadorPropiedades {

    /** Ruta relativa al archivo de propiedades de minipigs. */
    private static final String RUTA_PROPS = "data/minipigs.properties";

    /** Numero esperado de campos por cada entrada de minipig. */
    private static final int CAMPOS_ESPERADOS = 11;

    /**
     * Constructor privado: clase de utilidad, no debe instanciarse.
     */
    private CargadorPropiedades() { }

    /**
     * Carga el archivo de propiedades y retorna los datos crudos de cada minipig.
     *
     * <p>El mapa resultado tiene como clave el nombre de la propiedad
     * (ej. "Minipig1") y como valor un arreglo de {@code String} con los
     * 11 campos separados por coma. Los campos con valor literal "null"
     * se retornan como {@code null} para que el gestor los detecte.</p>
     *
     * @return mapa ordenado de nombre a arreglo de campos; mapa vacio si hay error
     */
    public static Map<String, String[]> cargarDatosCrudos() {
        Map<String, String[]> datos = new LinkedHashMap<>();
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(RUTA_PROPS)) {
            props.load(fis);
        } catch (IOException ex) {
            System.out.println("[CargadorPropiedades] No se pudo leer " + RUTA_PROPS
                    + ": " + ex.getMessage());
            return datos;
        }

        for (String clave : props.stringPropertyNames()) {
            if (!clave.toLowerCase().startsWith("minipig")) {
                continue;
            }
            String linea = props.getProperty(clave).trim();
            String[] partes = linea.split(",", CAMPOS_ESPERADOS);

            // Normalizar: recortar espacios y convertir "null" literal a null real
            for (int i = 0; i < partes.length; i++) {
                partes[i] = partes[i].trim();
                if ("null".equalsIgnoreCase(partes[i])) {
                    partes[i] = null;
                }
            }
            datos.put(clave, partes);
        }
        return datos;
    }
}
