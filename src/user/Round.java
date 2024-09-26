package user;

/**
 * @author Mohamed Boutanghach
 * Clase que representa una ronda en el juego.
 * Implementa la interfaz Comparable para permitir la comparación de rondas
 * según el tiempo que tardó en completarse.
 */
public class Round implements Comparable<Round> {

    private final String warrior; // Nombre del guerrero utilizado en la ronda.
    private final double seconds;  // Tiempo que tardó en completar la ronda, en segundos.
    private final User user;       // Usuario que completó la ronda.

    /**
     * Constructor de la clase Round.
     *
     * @param warrior Nombre del guerrero utilizado en la ronda.
     * @param seconds Tiempo en segundos que tardó el usuario en completar la ronda.
     * @param user Usuario que completó la ronda.
     */
    public Round(String warrior, double seconds, User user) {
        this.warrior = warrior; // Inicializa el nombre del guerrero.
        this.seconds = seconds; // Inicializa el tiempo en segundos.
        this.user = user;       // Inicializa el usuario.
    }

    /**
     * Obtiene el nombre del guerrero.
     *
     * @return Nombre del guerrero.
     */
    public String getWarrior() {
        return warrior; // Retorna el nombre del guerrero.
    }

    /**
     * Obtiene el tiempo en segundos que tardó el usuario en completar la ronda.
     *
     * @return Tiempo en segundos.
     */
    public double getSeconds() {
        return seconds; // Retorna el tiempo en segundos.
    }

    /**
     * Obtiene el usuario que completó la ronda.
     *
     * @return Usuario que completó la ronda.
     */
    public User getUser() {
        return user; // Retorna el usuario que completó la ronda.
    }

    /**
     * Compara esta ronda con otra ronda según el tiempo en segundos.
     * Devuelve un valor negativo si esta ronda es más rápida que la otra,
     * un valor positivo si es más lenta, y cero si son iguales.
     *
     * @param o Otra ronda a comparar.
     * @return Un valor negativo, cero o un valor positivo según la comparación de tiempos.
     */
    @Override
    public int compareTo(Round o) {
        // Se invierte la comparación para que las rondas más rápidas tengan prioridad.
        if (o.seconds > this.seconds) {
            return -1; // Esta ronda es más lenta.
        } else if (o.seconds < this.seconds) {
            return 1; // Esta ronda es más rápida.
        } else {
            return 0; // Ambas rondas tienen el mismo tiempo.
        }
    }
}

