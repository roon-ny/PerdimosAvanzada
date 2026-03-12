package Modelo;

/**
 * Value Object (VO) que representa un Minipig en el sistema.
 *
 * <p>Proposito: Transportar datos de un minipig entre las capas de la
 * aplicacion (Control &lt;-&gt; Modelo). Equivale a los campos de la tabla
 * {@code minipigs} en la base de datos.</p>
 *
 * <p>Principio SOLID aplicado: SRP - esta clase solo almacena estado, sin
 * logica de negocio ni acceso a datos.</p>
 *
 * <p>Se comunica con: {@code MinipigDAO} (persistencia) y
 * {@code GestorMinipig} (logica de negocio).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class MinipigVO {

    /** Codigo unico del minipig (no modificable). */
    private String codigo;

    /** Nombre dado por el propietario. */
    private String nombre;

    /** Genero o sexo del minipig (Macho / Hembra). */
    private String genero;

    /** Identificador unico del microchip (no modificable). */
    private String idMicrochip;

    /** Raza del minipig (ej. Juliana, Vietnamita, Gottingen). */
    private String raza;

    /** Color del pelaje. */
    private String color;

    /** Peso del minipig (ej: 32kg). */
    private String peso;

    /** Altura del minipig (ej: 43cm). */
    private String altura;

    /** Primera caracteristica fisica o comportamental. */
    private String caracteristica1;

    /** Segunda caracteristica fisica o comportamental. */
    private String caracteristica2;

    /** URL o ruta relativa de la foto del minipig. */
    private String urlFoto;

    /**
     * Constructor por defecto. Inicializa todos los campos a {@code null}.
     */
    public MinipigVO() { }

    /**
     * Constructor con todos los atributos.
     *
     * @param codigo         codigo unico
     * @param nombre         nombre del minipig
     * @param genero         genero o sexo
     * @param idMicrochip    id del microchip
     * @param raza           raza
     * @param color          color del pelaje
     * @param peso           peso
     * @param altura         altura
     * @param caracteristica1 primera caracteristica
     * @param caracteristica2 segunda caracteristica
     * @param urlFoto        ruta de la foto
     */
    public MinipigVO(String codigo, String nombre, String genero,
                     String idMicrochip, String raza, String color,
                     String peso, String altura,
                     String caracteristica1, String caracteristica2,
                     String urlFoto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.idMicrochip = idMicrochip;
        this.raza = raza;
        this.color = color;
        this.peso = peso;
        this.altura = altura;
        this.caracteristica1 = caracteristica1;
        this.caracteristica2 = caracteristica2;
        this.urlFoto = urlFoto;
    }

    /**
     * Obtiene el codigo del minipig.
     * @return codigo
     */
    public String getCodigo() { return codigo; }

    /**
     * Establece el codigo del minipig.
     * @param codigo nuevo codigo
     */
    public void setCodigo(String codigo) { this.codigo = codigo; }

    /**
     * Obtiene el nombre del minipig.
     * @return nombre
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del minipig.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el genero del minipig.
     * @return genero
     */
    public String getGenero() { return genero; }

    /**
     * Establece el genero del minipig.
     * @param genero nuevo genero
     */
    public void setGenero(String genero) { this.genero = genero; }

    /**
     * Obtiene el id del microchip.
     * @return idMicrochip
     */
    public String getIdMicrochip() { return idMicrochip; }

    /**
     * Establece el id del microchip.
     * @param idMicrochip nuevo id del microchip
     */
    public void setIdMicrochip(String idMicrochip) { this.idMicrochip = idMicrochip; }

    /**
     * Obtiene la raza del minipig.
     * @return raza
     */
    public String getRaza() { return raza; }

    /**
     * Establece la raza del minipig.
     * @param raza nueva raza
     */
    public void setRaza(String raza) { this.raza = raza; }

    /**
     * Obtiene el color del pelaje.
     * @return color
     */
    public String getColor() { return color; }

    /**
     * Establece el color del pelaje.
     * @param color nuevo color
     */
    public void setColor(String color) { this.color = color; }

    /**
     * Obtiene el peso del minipig.
     * @return peso
     */
    public String getPeso() { return peso; }

    /**
     * Establece el peso del minipig.
     * @param peso nuevo peso
     */
    public void setPeso(String peso) { this.peso = peso; }

    /**
     * Obtiene la altura del minipig.
     * @return altura
     */
    public String getAltura() { return altura; }

    /**
     * Establece la altura del minipig.
     * @param altura nueva altura
     */
    public void setAltura(String altura) { this.altura = altura; }

    /**
     * Obtiene la primera caracteristica.
     * @return caracteristica1
     */
    public String getCaracteristica1() { return caracteristica1; }

    /**
     * Establece la primera caracteristica.
     * @param caracteristica1 nueva caracteristica
     */
    public void setCaracteristica1(String caracteristica1) { this.caracteristica1 = caracteristica1; }

    /**
     * Obtiene la segunda caracteristica.
     * @return caracteristica2
     */
    public String getCaracteristica2() { return caracteristica2; }

    /**
     * Establece la segunda caracteristica.
     * @param caracteristica2 nueva caracteristica
     */
    public void setCaracteristica2(String caracteristica2) { this.caracteristica2 = caracteristica2; }

    /**
     * Obtiene la URL o ruta de la foto.
     * @return urlFoto
     */
    public String getUrlFoto() { return urlFoto; }

    /**
     * Establece la URL o ruta de la foto.
     * @param urlFoto nueva ruta de la foto
     */
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    /**
     * Representacion textual del minipig (util para depuracion y consola).
     * @return cadena con todos los datos del minipig
     */
    @Override
    public String toString() {
        return "MinipigVO{"
                + "codigo='" + codigo + '\''
                + ", nombre='" + nombre + '\''
                + ", genero='" + genero + '\''
                + ", idMicrochip='" + idMicrochip + '\''
                + ", raza='" + raza + '\''
                + ", color='" + color + '\''
                + ", peso='" + peso + '\''
                + ", altura='" + altura + '\''
                + ", caracteristica1='" + caracteristica1 + '\''
                + ", caracteristica2='" + caracteristica2 + '\''
                + ", urlFoto='" + urlFoto + '\''
                + '}';
    }
}
