package listeners;

import tools.Tools;
import user.Round;
import user.User;
import warriors.Magician;
import warriors.Soldier;
import warriors.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja los eventos del juego.
 * Implementa ActionListener para gestionar la lógica del juego en cada intervalo de tiempo.
 */
public class ListenerGame implements ActionListener {
    private JPanel panelGame; // Panel del juego donde se dibujan los elementos
    private JPanel panelMain; // Panel principal del juego
    private JPanel panelMainMenu; // Panel del menú principal
    private JPanel ranking; // Panel que muestra el ranking de jugadores
    private Warrior warrior; // Guerrero que está jugando
    private TreeSet<Round> listRounds; // Conjunto de rondas jugadas
    private double seconds; // Tiempo transcurrido en el juego
    private LinkedHashSet<User> listUsers; // Conjunto de usuarios registrados

    /**
     * Constructor para ListenerGame.
     *
     * @param warrior        Guerrero que está jugando.
     * @param panelMain     Panel principal del juego.
     * @param panelMainMenu Panel del menú principal.
     * @param panelGame     Panel del juego donde se dibujan los elementos.
     * @param listRounds    Conjunto de rondas jugadas.
     * @param ranking       Panel que muestra el ranking de jugadores.
     * @param listUsers     Conjunto de usuarios registrados.
     */
    public ListenerGame(Warrior warrior, JPanel panelMain, JPanel panelMainMenu, JPanel panelGame, TreeSet<Round> listRounds, JPanel ranking, LinkedHashSet<User> listUsers) {
        this.warrior = warrior;
        this.panelMain = panelMain;
        this.panelMainMenu = panelMainMenu;
        this.panelGame = panelGame;
        this.seconds = 0;
        this.listUsers = listUsers;
        this.listRounds = listRounds;
        this.ranking = ranking;
    }

    /**
     * Maneja el evento de acción en cada intervalo de tiempo.
     * Actualiza el tiempo transcurrido y verifica el estado del guerrero.
     * Si el guerrero ha muerto o ha recolectado 10 esmeraldas, actualiza el ranking y la base de datos.
     *
     * @param e El evento de acción disparado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        seconds += 0.05; // Incrementar el tiempo transcurrido
        if (this.warrior.getLive() <= 0) {
            removeAll(); // Eliminar todos los componentes del juego
            ((Timer) e.getSource()).stop(); // Detener el temporizador
        } else if (this.warrior.getEmeralds() == 10) {
            removeAll(); // Eliminar todos los componentes del juego
            User user = foundUser(panelGame.getName()); // Encontrar el usuario
            Round round = new Round(getWarrior(), seconds, user); // Crear una nueva ronda
            listRounds.add(round); // Agregar la ronda a la lista
            user.addRound(round); // Agregar la ronda al usuario
            Tools.updateRanking(ranking, listRounds); // Actualizar el ranking

            int id_User = 0; // Variable para almacenar el ID del usuario
            try {
                Connection con = Tools.getConnectionToDataBase(); // Conexión a la base de datos
                PreparedStatement ps = con.prepareStatement("select id_user from Users where name = ? ");
                ps.setString(1, user.getName());
                ResultSet rsIdUser = ps.executeQuery(); // Ejecutar consulta para obtener el ID del usuario
                rsIdUser.next();
                id_User = rsIdUser.getInt(1); // Obtener el ID del usuario
                ps.close();
                con.close();
                rsIdUser.close();
            } catch (Exception a) {
                System.out.println("No se ha podido obtener idUser"); // Mensaje de error
            }

            // Insertar la ronda en la base de datos
            try {
                Connection con = Tools.getConnectionToDataBase();
                PreparedStatement ps = con.prepareStatement("insert into Round(seconds,warrior,id_user) values ( ? , ? , ? )");
                ps.setDouble(1, seconds);
                ps.setString(2, getWarrior());
                ps.setInt(3, id_User);
                int rows = ps.executeUpdate(); // Ejecutar actualización
                if (rows > 0) {
                    System.out.println("Actualizacion realizada"); // Mensaje de éxito
                }
                ps.close();
                con.close();
            } catch (Exception a) {
                System.out.println("Error al insertar"); // Mensaje de error
            }
            ((Timer) e.getSource()).stop(); // Detener el temporizador
        }
    }

    /**
     * Devuelve el tipo de guerrero como una cadena.
     *
     * @return Tipo de guerrero (Soldado, Mago o Sacerdote).
     */
    private String getWarrior() {
        String war;
        if (warrior instanceof Soldier) {
            war = "Soldado"; // Tipo de guerrero: Soldado
        } else if (warrior instanceof Magician) {
            war = "Mago"; // Tipo de guerrero: Mago
        } else {
            war = "Sacerdote"; // Tipo de guerrero: Sacerdote
        }
        return war; // Retornar el tipo de guerrero
    }

    /**
     * Busca un usuario por su nombre.
     *
     * @param name Nombre del usuario a buscar.
     * @return El objeto User si se encuentra, null en caso contrario.
     */
    private User foundUser(String name) {
        boolean found = false;
        User user = null;
        User userInList;
        Iterator<User> userIterator = listUsers.iterator(); // Iterador sobre la lista de usuarios
        while (userIterator.hasNext() && !found) {
            userInList = userIterator.next();
            if (userInList.getName().equals(panelGame.getName())) { // Comparar nombres
                found = true; // Usuario encontrado
                user = userInList; // Asignar usuario encontrado
            }
        }
        return user; // Retornar el usuario encontrado
    }

    /**
     * Elimina todos los componentes del panel de juego y restaura el panel principal.
     */
    private void removeAll() {
        panelGame.remove(warrior.getBody()); // Eliminar el cuerpo del guerrero
        deleteHaerts(); // Eliminar íconos de vida
        deleteMana(); // Eliminar íconos de maná
        panelGame.remove(warrior.getQuanty()); // Eliminar cantidad
        panelMain.removeAll(); // Limpiar el panel principal
        panelMain.repaint(); // Repaint del panel principal
        panelMain.add(panelMainMenu); // Agregar el menú principal
        panelMain.repaint(); // Repaint del panel principal
    }

    /**
     * Elimina los íconos de maná del panel de juego.
     */
    private void deleteMana() {
        for (int i = 0; i < warrior.getLabelEnergy().size(); i++) {
            panelGame.remove(this.warrior.getLabelEnergy().get(i)); // Eliminar cada ícono de maná
        }
    }

    /**
     * Elimina los íconos de vida del panel de juego.
     */
    private void deleteHaerts() {
        for (int i = 0; i < this.warrior.getLivesIcon().size(); i++) {
            panelGame.remove(this.warrior.getLivesIcon().get(i)); // Eliminar cada ícono de vida
        }
    }
}
