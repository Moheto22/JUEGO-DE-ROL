package listeners;

import tools.Tools;
import user.User;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.LinkedHashSet;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja los eventos de ratón para la creación de nuevos usuarios.
 * Extiende MouseAdapter para capturar clics en la interfaz gráfica.
 */
public class MouseListenerCreateUser extends MouseAdapter {
    private LinkedHashSet<User> listUser; // Conjunto de usuarios existentes
    private JTextField name; // Campo de texto para ingresar el nombre del usuario
    private JPanel panelMainMenu; // Panel del menú principal
    private JPanel panelMain; // Panel principal del juego
    private JPanel panelSeleWarrior; // Panel de selección de guerrero
    private JPanel users; // Panel que muestra la lista de usuarios
    private User user; // Usuario actual (no se utiliza en este contexto)

    /**
     * Constructor para MouseListenerCreateUser.
     *
     * @param listUsers       Conjunto de usuarios existentes.
     * @param name            Campo de texto para ingresar el nombre del usuario.
     * @param panelMainMenu   Panel del menú principal.
     * @param panelMain       Panel principal del juego.
     * @param users           Panel que muestra la lista de usuarios.
     * @param user            Usuario actual.
     * @param panelSeleWarrior Panel de selección de guerrero.
     */
    public MouseListenerCreateUser(LinkedHashSet<User> listUsers, JTextField name, JPanel panelMainMenu, JPanel panelMain, JPanel users, User user, JPanel panelSeleWarrior) {
        this.listUser = listUsers;
        this.panelMainMenu = panelMainMenu;
        this.name = name;
        this.panelMain = panelMain;
        this.users = users;
        this.user = user;
        this.panelSeleWarrior = panelSeleWarrior;
    }

    /**
     * Maneja el evento de clic del ratón.
     * Se ejecuta al hacer clic para crear un nuevo usuario.
     * Valida el nombre ingresado y lo agrega a la lista de usuarios.
     * Si el usuario ya existe, muestra un mensaje de error.
     * De lo contrario, crea el usuario, actualiza la interfaz y la base de datos.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        int amount;
        String nameUser = name.getText(); // Obtener el nombre ingresado
        if (nameUser.equals("")) {
            JOptionPane.showMessageDialog(null, "Tienes que introducir un nombre"); // Mensaje de error si el campo está vacío
        } else {
            User newUser = new User(nameUser); // Crear un nuevo usuario
            amount = listUser.size(); // Contar usuarios existentes
            listUser.add(newUser); // Intentar agregar el nuevo usuario
            if (amount == listUser.size()) {
                JOptionPane.showMessageDialog(null, "Este usuario ya existe.\nIntroduce un nuevo nombre."); // Mensaje de error si el usuario ya existe
                name.setText(""); // Limpiar el campo de texto
            } else {
                JOptionPane.showMessageDialog(null, "El usuario " + name.getText() + " ha sido creado exitosamente"); // Mensaje de éxito
                name.setText(""); // Limpiar el campo de texto
                Tools.updateUsers(users, listUser, panelSeleWarrior, panelMain); // Actualizar la lista de usuarios en la interfaz
                try {
                    // Conectar a la base de datos e insertar el nuevo usuario
                    Connection con = Tools.getConnectionToDataBase();
                    PreparedStatement ps = con.prepareStatement("insert into Users(name) values( ? )");
                    ps.setString(1, newUser.getName());
                    int rows = ps.executeUpdate(); // Ejecutar la actualización
                    if (rows > 0) {
                        System.out.println("Actualización realizada"); // Confirmación de éxito
                    }
                    ps.close(); // Cerrar el PreparedStatement
                    con.close(); // Cerrar la conexión
                } catch (Exception a) {
                    System.out.println("Error al insertar"); // Mensaje de error si falla la inserción
                }
                panelMain.removeAll(); // Limpiar el panel principal
                panelMain.repaint(); // Repaint del panel principal
                panelMain.add(panelMainMenu); // Agregar el menú principal al panel
                panelMain.repaint(); // Repaint del panel principal
            }
        }
    }
}