package user;

import java.util.Objects;
import java.util.TreeSet;

/**
 * @author Mohamed Boutanghach
 * Clase que representa a un usuario en el sistema.
 * Un usuario tiene un nombre y una lista de rondas completadas,
 * que se almacenan en un conjunto ordenado.
 */
public class User {
    private final String name; // Nombre del usuario.
    TreeSet<Round> listRound = new TreeSet<>(); // Conjunto de rondas completadas por el usuario.

    /**
     * Constructor de la clase User.
     *
     * @param name Nombre del usuario.
     */
    public User(String name) {
        this.name = name; // Inicializa el nombre del usuario.
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return Nombre del usuario.
     */
    public String getName() {
        return name; // Retorna el nombre del usuario.
    }

    /**
     * Añade una ronda a la lista de rondas completadas por el usuario.
     *
     * @param round La ronda a añadir.
     */
    public void addRound(Round round) {
        listRound.add(round); // Agrega la ronda al conjunto de rondas.
    }

    /**
     * Compara este objeto User con otro objeto para determinar si son iguales.
     * Dos usuarios se consideran iguales si tienen el mismo nombre.
     *
     * @param o Objeto a comparar.
     * @return true si los usuarios son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Comparación de referencia.
        if (o == null || getClass() != o.getClass()) return false; // Verificación de tipo.
        User user = (User) o; // Casting a User.
        return name.equals(user.name); // Comparación de nombres.
    }

    /**
     * Genera un código hash para el objeto User.
     *
     * @return Código hash basado en el nombre del usuario.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name); // Retorna el código hash del nombre.
    }
}
