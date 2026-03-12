package Control.DAO;

import Modelo.MinipigVO;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para Minipig.
 *
 * <p>Proposito: Abstraer las operaciones CRUD sobre la tabla {@code minipigs},
 * desacoplando la logica de negocio de la implementacion concreta de BD.</p>
 *
 * <p>Principio SOLID aplicado: ISP - define solo las operaciones necesarias.
 * DIP - {@code GestorMinipig} depende de esta abstraccion, no de la implementacion.</p>
 *
 * <p>Se comunica con: {@code MinipigDAO} (implementacion), {@code GestorMinipig} (consumidor).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public interface IMinipigDAO {

    /**
     * Inserta un nuevo minipig en la base de datos.
     *
     * @param minipig objeto {@code MinipigVO} con los datos a insertar
     * @return {@code true} si la insercion fue exitosa, {@code false} en caso contrario
     */
    boolean insertarMinipig(MinipigVO minipig);

    /**
     * Consulta un minipig por su codigo unico.
     *
     * @param codigo codigo del minipig a buscar
     * @return objeto {@code MinipigVO} si se encuentra, {@code null} si no existe
     */
    MinipigVO consultarPorCodigo(String codigo);

    /**
     * Consulta un minipig por su id de microchip.
     *
     * @param idMicrochip identificador del microchip
     * @return objeto {@code MinipigVO} si se encuentra, {@code null} si no existe
     */
    MinipigVO consultarPorMicrochip(String idMicrochip);

    /**
     * Consulta todos los minipigs registrados de una raza especifica.
     *
     * @param raza nombre de la raza a filtrar
     * @return lista de {@code MinipigVO} de esa raza; lista vacia si no hay resultados
     */
    ArrayList<MinipigVO> consultarPorRaza(String raza);

    /**
     * Consulta todos los minipigs que tienen un nombre dado.
     * Puede retornar mas de uno si comparten nombre.
     *
     * @param nombre nombre del minipig a buscar
     * @return lista de {@code MinipigVO} con ese nombre; lista vacia si no hay resultados
     */
    ArrayList<MinipigVO> consultarPorNombre(String nombre);

    /**
     * Retorna todos los minipigs almacenados en la base de datos.
     *
     * @return lista completa de {@code MinipigVO}; lista vacia si no hay registros
     */
    ArrayList<MinipigVO> listarTodos();

    /**
     * Elimina un minipig de la base de datos por su codigo.
     *
     * @param codigo codigo del minipig a eliminar
     * @return {@code true} si fue eliminado, {@code false} si no existia o fallo
     */
    boolean eliminarPorCodigo(String codigo);

    /**
     * Elimina un minipig de la base de datos por su id de microchip.
     *
     * @param idMicrochip id del microchip del minipig a eliminar
     * @return {@code true} si fue eliminado, {@code false} si no existia o fallo
     */
    boolean eliminarPorMicrochip(String idMicrochip);

    /**
     * Actualiza los datos modificables de un minipig en la base de datos.
     * No modifica {@code codigo} ni {@code idMicrochip}.
     *
     * @param minipig objeto {@code MinipigVO} con los datos actualizados
     * @return {@code true} si fue actualizado, {@code false} si fallo
     */
    boolean actualizarMinipig(MinipigVO minipig);

    /**
     * Verifica si ya existe un minipig con el codigo dado.
     *
     * @param codigo codigo a verificar
     * @return {@code true} si ya existe, {@code false} si no
     */
    boolean existePorCodigo(String codigo);
}
