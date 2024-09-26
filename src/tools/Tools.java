package tools;

import listeners.MouseListenerUser;
import user.Round;
import user.User;
import warriors.Skeleton;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

/**
 * @author Mohamed Boutanghach
 * Clase de utilidades que proporciona métodos estáticos para manejar usuarios, rondas,
 * y la conexión a la base de datos.
 */
public class Tools {

    /**
     * Busca un usuario por su nombre en un conjunto de usuarios.
     *
     * @param users Conjunto de usuarios donde buscar.
     * @param name Nombre del usuario a buscar.
     * @return El objeto User correspondiente al nombre dado, o null si no se encuentra.
     */
    public static User lookForUser(LinkedHashSet<User> users, String name) {
        User user = null; // Almacena el usuario encontrado.
        User userInList;
        boolean found = false; // Indica si el usuario ha sido encontrado.
        Iterator<User> iterator = users.iterator(); // Iterador para recorrer el conjunto.

        // Busca el usuario en el conjunto.
        while (iterator.hasNext() && !found) {
            userInList = iterator.next();
            if (userInList.getName().equals(name)) {
                found = true;
                user = userInList; // Asigna el usuario encontrado.
            }
        }
        return user; // Retorna el usuario encontrado o null.
    }

    /**
     * Actualiza el panel de ranking con la lista de rondas.
     *
     * @param ranking Panel donde se mostrará el ranking.
     * @param listRounds Conjunto de rondas a mostrar.
     */
    public static void updateRanking(JPanel ranking, TreeSet<Round> listRounds) {
        ranking.removeAll(); // Limpia el panel antes de actualizar.
        JLabel seconds, user, warriorType; // Etiquetas para mostrar la información.
        int i = 0; // Contador para la posición de las etiquetas.

        // Itera sobre la lista de rondas para actualizar el ranking.
        for (Round round : listRounds) {
            user = generateText(); // Genera una etiqueta para el usuario.
            user.setText("User: " + round.getUser().getName());
            user.setLocation(4, 4 + (10 * i) + (i * user.getHeight())); // Ubicación de la etiqueta.

            seconds = generateText(); // Genera una etiqueta para los segundos.
            seconds.setLocation(333, 4 + (10 * i) + (i * user.getHeight()));
            seconds.setText("Seconds: " + String.format("%.3f", round.getSeconds())); // Formato a 3 decimales.

            warriorType = generateText(); // Genera una etiqueta para el tipo de guerrero.
            warriorType.setText("Guerrero: " + round.getWarrior());
            warriorType.setLocation(666, 4 + (10 * i) + (i * user.getHeight()));

            // Agrega las etiquetas al panel de ranking.
            ranking.add(user);
            ranking.add(seconds);
            ranking.add(warriorType);
            ranking.setComponentZOrder(user, 0);
            ranking.setComponentZOrder(seconds, 0);
            ranking.setComponentZOrder(warriorType, 0);
            i++; // Incrementa el contador.
        }
        ranking.repaint(); // Repaint del panel para reflejar los cambios.
    }

    /**
     * Establece una conexión con la base de datos.
     *
     * @return La conexión a la base de datos, o null si falla la conexión.
     */
    public static Connection getConnectionToDataBase() {
        String url = "jdbc:mysql://localhost:3306/juegoDeRol"; // URL de la base de datos.
        String user = "root"; // Nombre de usuario para la base de datos.
        String paswd = "mysql"; // Contraseña para la base de datos.
        Connection con = null; // Conexión inicializada a null.

        // Intenta establecer la conexión.
        try {
            con = DriverManager.getConnection(url, user, paswd);
        } catch (SQLException e) {
            System.out.println("Fallo a la hora de conectarse con la base de datos"); // Mensaje de error.
        }
        return con; // Retorna la conexión.
    }

    /**
     * Genera un JLabel con configuración básica de texto.
     *
     * @return Un JLabel configurado con fuente y color.
     */
    public static JLabel generateText() {
        JLabel jLabel = new JLabel();
        jLabel.setFont((new Font("Impact", Font.ITALIC, 30))); // Establece la fuente.
        jLabel.setSize(333, 40); // Establece el tamaño del JLabel.
        jLabel.setForeground(new Color(16, 16, 16)); // Establece el color del texto.
        return jLabel; // Retorna el JLabel generado.
    }

    /**
     * Actualiza el panel de usuarios mostrando la lista de usuarios.
     *
     * @param panel Panel donde se mostrarán los usuarios.
     * @param listUsers Conjunto de usuarios a mostrar.
     * @param panelSeleWarrior Panel para seleccionar guerreros.
     * @param panelMain Panel principal de la aplicación.
     */
    public static void updateUsers(JPanel panel, LinkedHashSet<User> listUsers, JPanel panelSeleWarrior, JPanel panelMain) {
        panel.removeAll(); // Limpia el panel antes de actualizar.
        int i = 0; // Contador para la posición de los botones.

        // Itera sobre la lista de usuarios para actualizar el panel.
        for (User u : listUsers) {
            JButton button = generateJLabelUser(u, i, panelSeleWarrior, panelMain); // Genera un botón para el usuario.
            panel.add(button); // Agrega el botón al panel.
            panel.setComponentZOrder(button, 0); // Establece el orden del componente.
            i++; // Incrementa el contador.
        }
        panel.repaint(); // Repaint del panel para reflejar los cambios.
    }

    /**
     * Genera un botón para representar un usuario en el panel.
     *
     * @param u Usuario que se representará en el botón.
     * @param i Índice del usuario, utilizado para la ubicación del botón.
     * @param panelSeleWarrior Panel donde se seleccionarán los guerreros.
     * @param panelMain Panel principal de la aplicación.
     * @return Un JButton configurado para representar al usuario.
     */
    private static JButton generateJLabelUser(User u, int i, JPanel panelSeleWarrior, JPanel panelMain) {
        JButton button = new JButton(u.getName()); // Crea un botón con el nombre del usuario.
        button.setSize(460, 39);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 164, 0)); // Establece el color de fondo.
        button.setFont(new Font("Impact", Font.ITALIC, 30)); // Establece la fuente.
        button.setForeground(Color.white); // Establece el color del texto.
        button.setLocation(20, 39 + button.getHeight() * i); // Calcula la ubicación del botón.
        button.addMouseListener(new MouseListenerUser(button, panelSeleWarrior, panelMain)); // Agrega un listener para el mouse.
        return button; // Retorna el botón creado.
    }

    /**
     * Valida el movimiento de un JLabel dentro de los límites de las paredes.
     *
     * @param body JLabel que representa el cuerpo a mover.
     * @param listWalls Lista de paredes representadas como JLabels.
     * @param speed Velocidad de movimiento.
     * @param key Tecla presionada para el movimiento ('a', 's', 'd', 'w').
     * @return La nueva velocidad; 0 si hay una colisión.
     */
    public static int validateMove(JLabel body, ArrayList<JLabel> listWalls, int speed, char key) {
        boolean valid = true; // Indica si el movimiento es válido.
        int i = 0; // Contador para iterar sobre las paredes.
        Point oriLoc = body.getLocation(); // Almacena la ubicación original.

        // Mueve el JLabel según la tecla presionada.
        switch (key) {
            case 'a':
                body.setLocation(body.getX() - speed, body.getY());
                break;
            case 's':
                body.setLocation(body.getX(), body.getY() + speed);
                break;
            case 'd':
                body.setLocation(body.getX() + speed, body.getY());
                break;
            case 'w':
                body.setLocation(body.getX(), body.getY() - speed);
                break;
        }

        Rectangle bodyR, wallR; // Rectángulos para las colisiones.
        bodyR = body.getBounds(); // Obtiene el área del cuerpo.

        // Verifica colisiones con las paredes.
        while (i < listWalls.size() && valid) {
            wallR = listWalls.get(i).getBounds(); // Obtiene el área de la pared.
            if (wallR.intersects(bodyR)) {
                valid = false; // Hay colisión.
            } else {
                i++;
            }
        }

        if (!valid) {
            speed = 0; // Resetea la velocidad si hay colisión.
        }

        body.setLocation(oriLoc); // Restaura la ubicación original.
        return speed; // Retorna la velocidad.
    }

    /**
     * Genera una lista de iconos de vida (corazones) según la cantidad de vidas.
     *
     * @param live Número de vidas a representar.
     * @return Lista de JLabels representando iconos de vida.
     */
    public static ArrayList<JLabel> generateLiveIcon(int live) {
        ArrayList<JLabel> array = new ArrayList<>(); // Lista para almacenar los iconos.
        JLabel icon; // Etiqueta para cada icono.

        // Crea los iconos de vida.
        for (int i = 0; i < live; i++) {
            icon = new JLabel();
            icon.setSize(50, 50); // Establece el tamaño del icono.
            ImageIcon img = new ImageIcon("src/images/heart.png"); // Carga la imagen del corazón.
            Icon imgIcon = new ImageIcon(
                    img.getImage().getScaledInstance(icon.getWidth(), icon.getHeight(), Image.SCALE_SMOOTH)
            ); // Escala la imagen.
            icon.setIcon(imgIcon); // Establece el icono en la etiqueta.
            array.add(icon); // Agrega el icono a la lista.
        }
        return array; // Retorna la lista de iconos de vida.
    }

    /**
     * Verifica si hay intersección entre un JLabel y una lista de esqueletos.
     *
     * @param body JLabel que representa el cuerpo a verificar.
     * @param skeletons Lista de esqueletos a verificar.
     * @return El índice del esqueleto con el que colisiona, o -1 si no hay colisión.
     */
    public static int intersectSkel(JLabel body, ArrayList<Skeleton> skeletons) {
        int i = 0; // Contador para iterar sobre los esqueletos.
        Rectangle user, skel; // Rectángulos para las colisiones.
        user = body.getBounds(); // Obtiene el área del cuerpo.
        boolean intersect = false; // Indica si hay intersección.

        // Verifica colisiones con los esqueletos.
        while (i < skeletons.size() && !intersect) {
            skel = skeletons.get(i).getBody().getBounds(); // Obtiene el área del esqueleto.
            intersect = skel.intersects(user); // Verifica si hay colisión.
            if (!intersect) {
                i++;
            }
        }

        if (!intersect) {
            i = -1; // No hay intersección.
        }
        return i; // Retorna el índice del esqueleto o -1.
    }

    /**
     * Verifica múltiples intersecciones entre un JLabel y una lista de esqueletos.
     *
     * @param body JLabel que representa el cuerpo a verificar.
     * @param skeletons Lista de esqueletos a verificar.
     * @return Una lista de índices de los esqueletos con los que colisiona.
     */
    public static ArrayList<Integer> multiIntersectSkel(JLabel body, ArrayList<Skeleton> skeletons) {
        Rectangle user, skel; // Rectángulos para las colisiones.
        user = body.getBounds(); // Obtiene el área del cuerpo.
        ArrayList<Integer> array = new ArrayList<>(); // Lista para almacenar los índices de colisión.

        // Verifica colisiones con todos los esqueletos.
        for (int i = 0; i < skeletons.size(); i++) {
            skel = skeletons.get(i).getBody().getBounds(); // Obtiene el área del esqueleto.
            if (skel.intersects(user)) {
                array.add(i); // Agrega el índice a la lista si hay colisión.
            }
        }
        return array; // Retorna la lista de índices de colisión.
    }
}