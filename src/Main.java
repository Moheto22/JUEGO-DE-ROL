import listeners.*;
import tools.Tools;
import user.Round;
import user.User;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * @author Mohamed Boutanghach
 * Clase principal que inicializa la aplicación y crea la interfaz gráfica
 * del juego "Fallen Legends".
 */
public class Main {

    /**
     * Método principal que configura el marco de la ventana y los diferentes paneles del juego.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Crear panel principal
        JPanel panelMain = generatePanelMain();

        // Generar lista de usuarios
        LinkedHashSet<User> listUsers = generateListUsers();

        // Generar lista de rondas basadas en usuarios
        TreeSet<Round> listRounds = generateListRounds(listUsers);

        // Configurar la ventana principal (JFrame)
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        User user = null;
        JFrame frame = new JFrame("Main");
        Image icon = toolkit.getImage("src/images/logo.jpg");
        frame.setIconImage(icon);
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(toolkit.getScreenSize());
        frame.setTitle("Fallen Legends");
        frame.setVisible(true);

        // Crear listas de etiquetas de muros y suelos
        ArrayList<JLabel> listWalls = new ArrayList<>();
        ArrayList<JLabel> listFloor = new ArrayList<>();

        // Crear diferentes paneles del juego
        JPanel panelGame = generatePanelGame(panelMain, listFloor, listWalls);
        JPanel panelMainMenu = generateMainMenu(panelMain);
        JPanel ranking = generatePanelItems(panelMain);

        // Actualizar el ranking de los jugadores
        Tools.updateRanking(ranking, listRounds);

        JPanel panelRanking = generatePanelRanking(panelMain, ranking, panelMainMenu);
        JPanel panelSeleWarrior = generatePanelSeleWarrior(panelMain, panelGame, listWalls, listFloor, panelMainMenu, listRounds, ranking, listUsers);
        JPanel panelMenuSelUser = generateMenuSelUser(panelMain);
        JPanel panelCreateUser = generateCreateUser(panelMain);
        JPanel users = generatePanelItems(panelMenuSelUser);

        // Actualizar la lista de usuarios
        Tools.updateUsers(users, listUsers, panelSeleWarrior, panelMain);

        // Agregar botones para crear usuario
        addBotonsCreateUser(panelCreateUser, panelMain, listUsers, panelMainMenu, users, user, panelSeleWarrior);

        // Añadir el panel de selección de usuario
        panelMenuSelUser.add(users);
        panelMenuSelUser.setComponentZOrder(users, 0);

        // Agregar botones al menú principal
        addBotonsMenu(panelMainMenu, panelCreateUser, panelMain, panelMenuSelUser, listUsers, panelRanking);

        // Presentar el menú principal
        presentation(panelMain, panelMainMenu);
    }

    /**
     * Genera una lista de rondas recuperadas de la base de datos y relacionadas con los usuarios.
     *
     * @param listUsers Lista de usuarios registrados.
     * @return TreeSet<Round> Lista de rondas ordenada por tiempos.
     */
    private static TreeSet<Round> generateListRounds(LinkedHashSet<User> listUsers) {
        TreeSet<Round> list = new TreeSet<>();
        try {
            Connection con = Tools.getConnectionToDataBase();
            PreparedStatement ps = con.prepareStatement("SELECT r.seconds, r.warrior, u.name FROM Round as r INNER JOIN Users as u ON u.id_user = r.id_user");
            ResultSet rs = ps.executeQuery();

            // Recorre los resultados de la consulta y agrega cada ronda a la lista
            while (rs.next()) {
                list.add(new Round(rs.getString(2), rs.getDouble(1), Tools.lookForUser(listUsers, rs.getString(3))));
            }
            ps.close();
            con.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("No se ha podido obtener información");
        }

        return list;
    }

    /**
     * Genera una lista de usuarios obtenidos desde la base de datos.
     *
     * @return LinkedHashSet<User> Lista de usuarios.
     */
    private static LinkedHashSet<User> generateListUsers() {
        LinkedHashSet<User> list = new LinkedHashSet<>();
        try {
            Connection con = Tools.getConnectionToDataBase();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM Users");
            ResultSet rs = ps.executeQuery();

            // Agrega cada usuario a la lista
            while (rs.next()) {
                list.add(new User(rs.getString(1)));
            }
            ps.close();
            con.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("No se ha podido obtener información");
        }
        return list;
    }

    /**
     * Genera el panel del ranking que muestra los resultados de los jugadores.
     *
     * @param panelMain     Panel principal de la interfaz.
     * @param ranking       Panel que contiene el ranking de jugadores.
     * @param panelMainMenu Panel del menú principal.
     * @return JPanel Panel de ranking.
     */
    private static JPanel generatePanelRanking(JPanel panelMain, JPanel ranking, JPanel panelMainMenu) {
        JPanel panel = new JPanel();
        panel.setLocation(0, 0);
        panel.setLayout(null);
        panel.setSize(panelMain.getSize());
        panel.add(setImageBackground("src/images/fondoMenu.jpg", panel.getSize()));

        // Título "RANKING"
        JLabel text = new JLabel("RANKING");
        text.setSize(147, 50);
        text.setFont(new Font("Impact", Font.ITALIC, 40));
        text.setForeground(Color.white);
        text.setLocation(panel.getWidth() / 2 - text.getWidth() / 2, 30);

        // Botón "VOLVER" para regresar al menú principal
        JButton boton = generateBotonStandard();
        boton.setText("VOLVER");
        boton.setSize(120, 50);
        boton.setLocation(panel.getWidth() / 2 - boton.getWidth() / 2, 900);

        // Agregar componentes al panel
        panel.add(boton);
        panel.setComponentZOrder(boton, 0);
        panel.add(text);
        panel.setComponentZOrder(text, 0);

        // Configuración del panel de ranking
        ranking.setSize(1000, ranking.getHeight());
        ranking.setLocation(panel.getWidth() / 2 - ranking.getWidth() / 2, ranking.getY() - 50);
        panel.add(ranking);
        panel.setComponentZOrder(ranking, 0);

        // Agregar acción al botón "VOLVER"
        boton.addMouseListener(new MouseListenerPresentation(panelMain, panelMainMenu));
        return panel;
    }

    /**
     * Genera el panel del juego donde se muestra el mapa y los elementos del mismo (muros y suelo).
     *
     * @param panelMain Panel principal de la interfaz.
     * @param listFloor Lista de suelos.
     * @param listWalls Lista de muros.
     * @return JPanel Panel del juego.
     */
    private static JPanel generatePanelGame(JPanel panelMain, ArrayList<JLabel> listFloor, ArrayList<JLabel> listWalls) {
        JPanel panel = new JPanel();
        panel.setSize(panelMain.getSize());
        panel.setLayout(null);
        panel.add(setImageBackground("src/images/backGround.png", panel.getSize()));

        // Agregar el mapa del juego
        addMapGame(panel, listWalls, listFloor);

        // Etiqueta de mana y icono de esmeralda
        JLabel mana = generateLabelMana();
        panel.add(mana);
        panel.setComponentZOrder(mana, 0);
        JLabel emerald = generateEmeraldIcon();
        panel.add(emerald);
        panel.setComponentZOrder(emerald, 0);

        panel.repaint();
        return panel;
    }

    /**
     * Genera el ícono de la esmeralda que se muestra en el panel del juego.
     *
     * @return JLabel Etiqueta con el ícono de esmeralda.
     */
    private static JLabel generateEmeraldIcon() {
        JLabel emerald = new JLabel();
        emerald.setSize(60, 70);
        Image img = new ImageIcon("src/images/emerald.gif").getImage();
        Icon icon = new ImageIcon(img.getScaledInstance(emerald.getWidth(), emerald.getHeight(), Image.SCALE_DEFAULT));
        emerald.setIcon(icon);
        emerald.setLocation(1750, 50);
        return emerald;
    }

    /**
     * Genera la barra de mana que se muestra en el panel del juego.
     *
     * @return JLabel Etiqueta con la barra de mana.
     */
    private static JLabel generateLabelMana() {
        JLabel mana = new JLabel();
        mana.setSize(520, 70);
        Icon icon = new ImageIcon("src/images/barraEnergi.png");
        mana.setIcon(icon);
        mana.setLocation(116, 70);
        return mana;
    }

    /**
     * Agrega el mapa del juego (muros y suelos) al panel.
     *
     * @param panel     Panel donde se dibuja el mapa.
     * @param listWalls Lista de etiquetas que representan los muros.
     * @param listFloor Lista de etiquetas que representan los suelos.
     */
    private static void addMapGame(JPanel panel, ArrayList<JLabel> listWalls, ArrayList<JLabel> listFloor) {
        generateWalls(listWalls, panel);
        generateFloor(listFloor, panel);
    }

    /**
     * Genera las diferentes áreas del piso en el panel.
     * Utiliza la función generateFloorArea para añadir bloques de piso en
     * posiciones específicas.
     *
     * @param listFloor Lista que contiene las etiquetas de los bloques de piso.
     * @param panel     Panel en el que se añadirán los bloques de piso.
     */
    private static void generateFloor(ArrayList<JLabel> listFloor, JPanel panel) {
        generateFloorArea(panel, listFloor, 116, 166, 8, 8);
        generateFloorArea(panel, listFloor, 244, 198, 43, 4);
        generateFloorArea(panel, listFloor, 404, 262, 5, 9);
        generateFloorArea(panel, listFloor, 852, 262, 5, 9);
        generateFloorArea(panel, listFloor, 148, 406, 73, 4);
        generateFloorArea(panel, listFloor, 148, 470, 4, 4);
        generateFloorArea(panel, listFloor, 692, 470, 5, 9);
        generateFloorArea(panel, listFloor, 148, 534, 16, 9);
        generateFloorArea(panel, listFloor, 404, 614, 34, 4);
        generateFloorArea(panel, listFloor, 1076, 310, 5, 6);
        generateFloorArea(panel, listFloor, 1076, 246, 26, 4);
        generateFloorArea(panel, listFloor, 868, 678, 5, 5);
        generateFloorArea(panel, listFloor, 868, 758, 41, 5);
        generateFloorArea(panel, listFloor, 1492, 246, 18, 14);
        generateFloorArea(panel, listFloor, 1684, 470, 6, 11);
        generateFloorArea(panel, listFloor, 1524, 646, 16, 5);
        generateFloorArea(panel, listFloor, 1428, 550, 6, 13);
        generateFloorArea(panel, listFloor, 1220, 550, 13, 5);
        generateFloorArea(panel, listFloor, 1220, 470, 6, 5);
    }

    /**
     * Genera un área del piso con bloques en una cuadrícula dada por las coordenadas (x, y)
     * y el número de bloques de base y altura.
     *
     * @param panel     Panel en el que se añadirán los bloques de piso.
     * @param listFloor Lista que contiene las etiquetas de los bloques de piso.
     * @param x         Coordenada X de la esquina superior izquierda del área de piso.
     * @param y         Coordenada Y de la esquina superior izquierda del área de piso.
     * @param base      Número de bloques en la base.
     * @param high      Número de bloques en la altura.
     */
    private static void generateFloorArea(JPanel panel, ArrayList<JLabel> listFloor, int x, int y, int base, int high) {
        for (int i = 0; i < base; i++) {
            for (int j = 0; j < high; j++) {
                JLabel bloc = generateNewBloc("f");
                bloc.setLocation(x + (i * 16), y + (j * 16));
                panel.add(bloc);
                listFloor.add(bloc);
                panel.setComponentZOrder(bloc, 0);
            }
        }
        panel.repaint();
    }

    /**
     * Genera las diferentes áreas de paredes en el panel.
     * Utiliza las funciones generateHLine y generateVLine para añadir líneas
     * horizontales y verticales de bloques de pared en posiciones específicas.
     *
     * @param listWalls Lista que contiene las etiquetas de los bloques de pared.
     * @param panel     Panel en el que se añadirán los bloques de pared.
     */
    private static void generateWalls(ArrayList<JLabel> listWalls, JPanel panel) {
        generateHLine(panel, listWalls, 100, 150, 10);
        generateVLine(panel, listWalls, 100, 166, 9);
        generateHLine(panel, listWalls, 100, 294, 10);
        generateVLine(panel, listWalls, 244, 166, 2);
        generateVLine(panel, listWalls, 244, 262, 2);
        generateHLine(panel, listWalls, 260, 182, 43);
        generateHLine(panel, listWalls, 260, 262, 9);
        generateVLine(panel, listWalls, 388, 278, 7);
        generateHLine(panel, listWalls, 132, 390, 17);
        generateVLine(panel, listWalls, 132, 390, 18);
        generateHLine(panel, listWalls, 132, 678, 46);
        generateVLine(panel, listWalls, 852, 694, 9);
        generateHLine(panel, listWalls, 852, 838, 43);
        generateHLine(panel, listWalls, 948, 742, 29);
        generateVLine(panel, listWalls, 948, 614, 8);
        generateHLine(panel, listWalls, 212, 470, 30);
        generateVLine(panel, listWalls, 212, 486, 3);
        generateHLine(panel, listWalls, 228, 518, 12);
        generateHLine(panel, listWalls, 484, 262, 23);
        generateVLine(panel, listWalls, 404, 534, 5);
        generateHLine(panel, listWalls, 420, 598, 17);
        generateVLine(panel, listWalls, 676, 486, 7);
        generateHLine(panel, listWalls, 772, 470, 28);
        generateHLine(panel, listWalls, 788, 598, 11);
        generateVLine(panel, listWalls, 772, 486, 8);
        generateVLine(panel, listWalls, 484, 278, 8);
        generateHLine(panel, listWalls, 500, 390, 22);
        generateHLine(panel, listWalls, 948, 390, 8);
        generateVLine(panel, listWalls, 1060, 230, 10);
        generateHLine(panel, listWalls, 1076, 230, 45);
        generateVLine(panel, listWalls, 1780, 246, 30);
        generateVLine(panel, listWalls, 1204, 486, 9);
        generateHLine(panel, listWalls, 1156, 390, 11);
        generateVLine(panel, listWalls, 1316, 406, 8);
        generateHLine(panel, listWalls, 1316, 534, 14);
        generateVLine(panel, listWalls, 1524, 550, 6);
        generateVLine(panel, listWalls, 1156, 310, 5);
        generateHLine(panel, listWalls, 1172, 310, 20);
        generateVLine(panel, listWalls, 1476, 326, 10);
        generateHLine(panel, listWalls, 1492, 470, 12);
        generateHLine(panel, listWalls, 1204, 630, 14);
        generateHLine(panel, listWalls, 1540, 630, 9);
        generateHLine(panel, listWalls, 1524, 726, 17);
        generateVLine(panel, listWalls, 1524, 742, 6);
        generateVLine(panel, listWalls, 1412, 646, 7);
        generateVLine(panel, listWalls, 1668, 486, 9);
        generateVLine(panel, listWalls, 836, 278, 7);
        generateVLine(panel, listWalls, 932, 198, 13);
    }

    /**
     * Genera una línea vertical de bloques en una posición dada.
     *
     * @param panel  Panel en el que se añadirán los bloques.
     * @param array  Lista que contiene las etiquetas de los bloques.
     * @param x      Coordenada X donde se colocará la línea.
     * @param y      Coordenada Y donde se colocará la línea.
     * @param amount Cantidad de bloques en la línea.
     */
    private static void generateVLine(JPanel panel, ArrayList<JLabel> array, int x, int y, int amount) {
        JLabel bloc;
        for (int i = 0; i < amount; i++) {
            bloc = generateNewBloc("w");
            bloc.setLocation(x, y + (bloc.getHeight() * i));
            panel.add(bloc);
            array.add(bloc);
            panel.setComponentZOrder(bloc, 0);
        }
        panel.repaint();
    }

    /**
     * Genera una línea horizontal de bloques en una posición dada.
     *
     * @param panel  Panel en el que se añadirán los bloques.
     * @param array  Lista que contiene las etiquetas de los bloques.
     * @param x      Coordenada X donde se colocará la línea.
     * @param y      Coordenada Y donde se colocará la línea.
     * @param amount Cantidad de bloques en la línea.
     */
    private static void generateHLine(JPanel panel, ArrayList<JLabel> array, int x, int y, int amount) {
        JLabel bloc;
        for (int i = 0; i < amount; i++) {
            bloc = generateNewBloc("w");
            bloc.setLocation(x + (bloc.getWidth() * i), y);
            panel.add(bloc);
            array.add(bloc);
            panel.setComponentZOrder(bloc, 0);
        }
        panel.repaint();
    }

    /**
     * Genera un nuevo bloque en forma de JLabel que puede ser una pared ("w") o un suelo ("f").
     *
     * @param tipe El tipo de bloque que se quiere generar: "w" para pared, otro valor para suelo.
     * @return Un JLabel con el ícono correspondiente al bloque (pared o suelo).
     */
    private static JLabel generateNewBloc(String tipe) {
        JLabel label = new JLabel();
        Icon icon;
        if (tipe.equalsIgnoreCase("w")) {
            icon = new ImageIcon("src/images/wall.png");
        } else {
            icon = new ImageIcon("src/images/floor.png");
        }
        label.setSize(icon.getIconWidth(), icon.getIconHeight());
        label.setIcon(icon);
        return label;
    }

    /**
     * Genera el panel para la selección de guerrero, donde el jugador puede elegir entre diferentes avatares.
     *
     * @param panelMain     El panel principal de la aplicación.
     * @param panelGame     El panel del juego principal.
     * @param listWalls     Lista de etiquetas que representan las paredes del juego.
     * @param listFloor     Lista de etiquetas que representan el suelo del juego.
     * @param panelMainMenu El panel principal del menú.
     * @param listRounds    Conjunto de rondas del juego.
     * @param ranking       Panel del ranking de jugadores.
     * @param listUsers     Conjunto de usuarios registrados.
     * @return Un JPanel configurado para la selección de guerrero.
     */
    private static JPanel generatePanelSeleWarrior(JPanel panelMain, JPanel panelGame, ArrayList<JLabel> listWalls, ArrayList<JLabel> listFloor, JPanel panelMainMenu, TreeSet<Round> listRounds, JPanel ranking, LinkedHashSet<User> listUsers) {
        JPanel panel = new JPanel();
        panel.setSize(panelMain.getSize());
        panel.setLayout(null);
        panel.add(setImageBackground("src/images/fondoSeleWarr.jpg", panelMain.getSize()));

        // Añadir los diferentes avatares para seleccionar
        generateLabelWarrior("src/images/magicAvatar.png", 0, panel, panelMain, panelGame, listWalls, listFloor, panelMainMenu, listRounds, ranking, listUsers);
        generateLabelWarrior("src/images/priestAvatar.png", 1, panel, panelMain, panelGame, listWalls, listFloor, panelMainMenu, listRounds, ranking, listUsers);
        generateLabelWarrior("src/images/soldierAvatar.png", 2, panel, panelMain, panelGame, listWalls, listFloor, panelMainMenu, listRounds, ranking, listUsers);

        panel.repaint();
        return panel;
    }

    /**
     * Genera un JLabel que representa a un guerrero o avatar, el cual se puede seleccionar.
     *
     * @param fileName      El nombre del archivo de imagen que representa al guerrero.
     * @param position      La posición horizontal del guerrero en el panel.
     * @param panel         El panel donde se mostrará el guerrero.
     * @param panelMain     El panel principal de la aplicación.
     * @param panelGame     El panel del juego principal.
     * @param listWalls     Lista de etiquetas que representan las paredes del juego.
     * @param listFloor     Lista de etiquetas que representan el suelo del juego.
     * @param panelMainMenu El panel principal del menú.
     * @param listRounds    Conjunto de rondas del juego.
     * @param ranking       Panel del ranking de jugadores.
     * @param listUsers     Conjunto de usuarios registrados.
     * @return Un JLabel que representa al guerrero seleccionado.
     */
    private static JLabel generateLabelWarrior(String fileName, int position, JPanel panel, JPanel panelMain, JPanel panelGame, ArrayList<JLabel> listWalls, ArrayList<JLabel> listFloor, JPanel panelMainMenu, TreeSet<Round> listRounds, JPanel ranking, LinkedHashSet<User> listUsers) {
        JLabel label = new JLabel();
        label.setName(fileName);
        label.setSize(400, 400);

        // Posiciona el guerrero horizontalmente en el panel basado en la posición
        label.setLocation(panel.getWidth() / 4 + ((panel.getWidth() / 4) * position) - label.getWidth() / 2, panel.getHeight() / 2 - label.getHeight() / 2);

        // Carga la imagen del guerrero y ajusta su tamaño
        ImageIcon image = new ImageIcon(fileName);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH));
        label.setIcon(icon);

        // Añade un MouseListener para detectar cuando se selecciona el guerrero
        label.addMouseListener(new MouseListenerWarrior(label, panelMain, panelGame, listWalls, listFloor, panelMainMenu, panel, listRounds, ranking, listUsers));

        // Añadir el JLabel del guerrero al panel
        panel.add(label);
        panel.setComponentZOrder(label, 0);
        return label;
    }

    /**
     * Genera un panel que mostrará los ítems disponibles en el juego.
     *
     * @param panelO El panel principal donde se ubicará el panel de ítems.
     * @return Un JPanel que contiene los ítems del juego.
     */
    private static JPanel generatePanelItems(JPanel panelO) {
        JPanel panel = new JPanel();
        panel.setSize(500, 801);
        panel.setLayout(null);
        panel.setLocation(panelO.getWidth() / 2 - panel.getWidth() / 2, 150);

        // Establece un color de fondo con transparencia
        panel.setBackground(new Color(255, 255, 255, 136));

        // Establece un borde alrededor del panel
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 164, 0), 3));

        panel.repaint();
        return panel;
    }

    /**
     * Genera un panel que representa el menú de selección de usuario.
     *
     * @param panelMain El panel principal donde se ubicará el menú de selección de usuario.
     * @return Un JPanel con la configuración del menú de selección de usuario.
     */
    private static JPanel generateMenuSelUser(JPanel panelMain) {
        JPanel panel = new JPanel();
        panel.setSize(panelMain.getSize());
        panel.add(setImageBackground("src/images/menuJugar.jpg", panelMain.getSize()));
        panel.setLayout(null);
        panel.repaint();
        return panel;
    }

    /**
     * Añade los botones y campos necesarios para la creación de un nuevo usuario.
     *
     * @param panel            El panel donde se añadirán los botones y campos.
     * @param panelMain        El panel principal de la aplicación.
     * @param listUsers        La lista de usuarios existentes.
     * @param panelMainMenu    El panel principal del menú.
     * @param users            El panel de usuarios.
     * @param user             El usuario actual.
     * @param panelSeleWarrior El panel de selección de guerrero.
     */
    private static void addBotonsCreateUser(JPanel panel, JPanel panelMain, LinkedHashSet<User> listUsers, JPanel panelMainMenu, JPanel users, User user, JPanel panelSeleWarrior) {
        JLabel title = generateTitleCreateUser(panelMain);
        panel.add(title);

        JTextField name = generateTextName(panelMain, title);
        panel.add(name);

        JButton create = generateBotonCreate(panelMain, listUsers, name, panelMainMenu, users, user, panelSeleWarrior);
        panel.add(create);

        // Asegura que los componentes añadidos estén en el frente
        panel.setComponentZOrder(title, 0);
        panel.setComponentZOrder(create, 0);
        panel.setComponentZOrder(name, 0);

        panel.repaint();
    }

    /**
     * Añade los botones del menú principal: Jugar, Crear Usuario, Mostrar Ranking y Salir.
     *
     * @param menu             El panel del menú principal.
     * @param panelCreateUser  El panel de creación de usuario.
     * @param panelMain        El panel principal de la aplicación.
     * @param panelMenuSelUser El panel del menú de selección de usuario.
     * @param listUsers        La lista de usuarios existentes.
     * @param panelRanking     El panel del ranking de jugadores.
     */
    private static void addBotonsMenu(JPanel menu, JPanel panelCreateUser, JPanel panelMain, JPanel panelMenuSelUser, LinkedHashSet<User> listUsers, JPanel panelRanking) {
        JButton playGame = generateBotonPlayGame(panelMenuSelUser, panelMain, listUsers);
        JButton createUser = generateBotonCreateUser(playGame, panelCreateUser, panelMain);
        JButton showRanking = generateBotonShowRanking(createUser, panelRanking, panelMain);
        JButton exit = generateBotonExit(showRanking);

        // Añadir los botones al panel del menú
        menu.add(playGame);
        menu.add(createUser);
        menu.add(showRanking);
        menu.add(exit);

        // Asegura que los botones estén en el frente del panel
        menu.setComponentZOrder(playGame, 0);
        menu.setComponentZOrder(createUser, 0);
        menu.setComponentZOrder(showRanking, 0);
        menu.setComponentZOrder(exit, 0);

        menu.repaint();
    }

    /**
     * Genera un panel para la creación de un nuevo usuario.
     *
     * @param panelMain El panel principal donde se mostrará el panel de creación de usuario.
     * @return Un JPanel configurado para la creación de un nuevo usuario.
     */
    private static JPanel generateCreateUser(JPanel panelMain) {
        JPanel panel = new JPanel();
        panel.setSize(panelMain.getSize());
        panel.add(setImageBackground("src/images/fondoMenu.jpg", panelMain.getSize()));
        panel.repaint();
        return panel;
    }

    /**
     * Genera un campo de texto para ingresar el nombre del usuario.
     *
     * @param panelMain El panel principal de la aplicación.
     * @param title     El JLabel que contiene el título del campo de creación de usuario.
     * @return Un JTextField donde el usuario podrá ingresar su nombre.
     */
    private static JTextField generateTextName(JPanel panelMain, JLabel title) {
        JTextField textArea = new JTextField();
        textArea.setSize(300, 30);
        textArea.setFont(new Font("Impact", Font.ITALIC, 20));

        // Posiciona el campo de texto debajo del título
        textArea.setLocation(panelMain.getWidth() / 2 - textArea.getWidth() / 2, title.getY() + title.getHeight() + title.getHeight());

        return textArea;
    }

    /**
     * Genera un JLabel que actúa como título para la creación de usuario, con el texto "Introduce tu nombre".
     *
     * @param panelMain El panel principal que contiene el menú de creación de usuario.
     * @return Un JLabel con el texto del título.
     */
    private static JLabel generateTitleCreateUser(JPanel panelMain) {
        JLabel label = new JLabel("Introduce tu nombre");
        label.setSize(260, 30);
        label.setForeground(Color.white);
        label.setFont(new Font("Impact", Font.ITALIC, 30));
        label.setLocation(panelMain.getWidth() / 2 - label.getWidth() / 2, 600);
        return label;
    }

    /**
     * Genera un botón para crear un nuevo usuario.
     *
     * @param panelMain        El panel principal de la aplicación.
     * @param listUsers        La lista de usuarios registrados.
     * @param name             El campo de texto donde el usuario ingresa su nombre.
     * @param panelMainMenu    El panel del menú principal.
     * @param users            El panel que contiene la lista de usuarios.
     * @param user             El objeto User que representa al usuario actual.
     * @param panelSeleWarrior El panel de selección de guerrero.
     * @return Un botón configurado para crear un nuevo usuario.
     */
    private static JButton generateBotonCreate(JPanel panelMain, LinkedHashSet<User> listUsers, JTextField name, JPanel panelMainMenu, JPanel users, User user, JPanel panelSeleWarrior) {
        JButton button = generateBotonStandard();
        button.setText("CREAR");
        button.setSize(300, 50);
        button.setLocation(panelMain.getWidth() / 2 - button.getWidth() / 2, 700);
        button.addMouseListener(new MouseListenerCreateUser(listUsers, name, panelMainMenu, panelMain, users, user, panelSeleWarrior));
        return button;
    }

    /**
     * Genera el panel del menú principal de la aplicación.
     *
     * @param panelMain El panel principal de la aplicación.
     * @return Un JPanel configurado como menú principal.
     */
    private static JPanel generateMainMenu(JPanel panelMain) {
        JPanel menu = new JPanel();
        menu.setSize(panelMain.getSize());
        menu.setLayout(null);
        menu.setLocation(0, 0);
        menu.add(setImageBackground("src/images/fondoMenu.jpg", menu.getSize()));
        return menu;
    }

    /**
     * Genera un botón de "Salir" en el menú principal.
     *
     * @param showRanking El botón de mostrar ranking, utilizado para posicionar el botón de salir.
     * @return Un botón configurado con la funcionalidad de salir del juego.
     */
    private static JButton generateBotonExit(JButton showRanking) {
        JButton button = generateBotonStandard();
        button.setText("SALIR");
        button.setSize(showRanking.getSize());
        button.setLocation(showRanking.getX(), showRanking.getY() + 20 + button.getHeight());
        button.addMouseListener(new MouseListenerExit());
        return button;
    }

    /**
     * Genera un botón para mostrar el ranking de jugadores.
     *
     * @param createUser   El botón de crear usuario, utilizado para posicionar el botón de ranking.
     * @param panelRanking El panel que contiene el ranking de jugadores.
     * @param panelMain    El panel principal de la aplicación.
     * @return Un botón configurado para mostrar el ranking.
     */
    private static JButton generateBotonShowRanking(JButton createUser, JPanel panelRanking, JPanel panelMain) {
        JButton button = generateBotonStandard();
        button.setText("MOSTRAR RANKING");
        button.setSize(createUser.getSize());
        button.setLocation(createUser.getX(), createUser.getY() + 20 + button.getHeight());
        button.addMouseListener(new MouseListenerRanking(panelMain, panelRanking));
        return button;
    }

    /**
     * Genera un botón para acceder a la creación de un nuevo usuario.
     *
     * @param playGame        El botón de jugar partida, utilizado para posicionar el botón de crear usuario.
     * @param panelCreateUser El panel para crear un nuevo usuario.
     * @param panelMain       El panel principal de la aplicación.
     * @return Un botón configurado para acceder a la creación de usuario.
     */
    private static JButton generateBotonCreateUser(JButton playGame, JPanel panelCreateUser, JPanel panelMain) {
        JButton button = generateBotonStandard();
        button.setText("CREAR USUARIO");
        button.setSize(playGame.getSize());
        button.setLocation(playGame.getX(), playGame.getY() + 20 + button.getHeight());
        button.addMouseListener(new MouseListenerPresentation(panelMain, panelCreateUser));
        return button;
    }

    /**
     * Genera un botón para iniciar una nueva partida en el juego.
     *
     * @param panelMenuSelUser El panel de selección de usuario.
     * @param panelMain        El panel principal de la aplicación.
     * @param listUsers        La lista de usuarios registrados.
     * @return Un botón configurado para jugar una nueva partida.
     */
    private static JButton generateBotonPlayGame(JPanel panelMenuSelUser, JPanel panelMain, LinkedHashSet<User> listUsers) {
        JButton button = generateBotonStandard();
        button.setText("JUGAR PARTIDA");
        button.setSize(300, 35);
        button.setLocation(200, 300);
        button.addMouseListener(new MouseListenerSeleUser(panelMain, panelMenuSelUser, listUsers));
        return button;
    }

    /**
     * Presenta el panel de introducción o presentación del juego.
     *
     * @param panelMain     El panel principal de la aplicación.
     * @param panelMainMenu El panel del menú principal.
     */
    private static void presentation(JPanel panelMain, JPanel panelMainMenu) {
        JPanel presentation = generatePanelPresentation(panelMain, panelMainMenu);
        panelMain.add(presentation);
        panelMain.repaint();
    }

    /**
     * Genera un panel de presentación con una imagen de fondo y un botón para comenzar a jugar.
     *
     * @param panelMain     El panel principal de la aplicación.
     * @param panelMainMenu El panel del menú principal, al que se redirige al pulsar el botón.
     * @return Un JPanel configurado para la pantalla de presentación.
     */
    private static JPanel generatePanelPresentation(JPanel panelMain, JPanel panelMainMenu) {
        JPanel panel = new JPanel();
        panel.setSize(panelMain.getSize());
        panel.setLocation(0, 0);
        panel.setLayout(null);

        // Agrega la imagen de fondo.
        panel.add(setImageBackground("src/images/presentacion.jpg", panelMain.getSize()));

        // Genera y añade el botón de presentación.
        JButton boton = generateBotonPresentation(panelMain, panelMainMenu);
        panel.add(boton);
        panel.setComponentZOrder(boton, 0);  // Asegura que el botón esté en el frente.
        return panel;
    }

    /**
     * Establece una imagen de fondo dentro de un JLabel escalado al tamaño deseado.
     *
     * @param s Ruta de la imagen a utilizar como fondo.
     * @param d Dimensión en la que se escalará la imagen.
     * @return Un JLabel que contiene la imagen de fondo ajustada a la dimensión proporcionada.
     */
    private static JLabel setImageBackground(String s, Dimension d) {
        JLabel background = new JLabel();
        background.setSize(d);
        background.setLocation(0, 0);

        // Carga la imagen y ajusta su tamaño.
        ImageIcon image = new ImageIcon(s);
        Icon icon = new ImageIcon(
                image.getImage().getScaledInstance(background.getWidth(), background.getHeight(), Image.SCALE_SMOOTH)
        );
        background.setIcon(icon);
        return background;
    }

    /**
     * Genera un botón estándar para la pantalla de presentación que, al pulsar, redirige al menú principal.
     *
     * @param panelMain     El panel principal de la aplicación.
     * @param panelMainMenu El panel del menú principal.
     * @return Un botón configurado para iniciar el juego desde la pantalla de presentación.
     */
    private static JButton generateBotonPresentation(JPanel panelMain, JPanel panelMainMenu) {
        JButton button = generateBotonStandard();
        button.setText("JUGAR");
        button.setSize(200, 50);

        // Posiciona el botón en el centro horizontal del panel, en la parte inferior.
        button.setLocation(panelMain.getWidth() / 2 - button.getWidth() / 2, 800);
        button.setForeground(Color.white);

        // Añade el evento de mouse para pasar al menú principal.
        button.addMouseListener(new MouseListenerPresentation(panelMain, panelMainMenu));
        return button;
    }

    /**
     * Genera un botón estándar con estilo predefinido, utilizado en varias pantallas del juego.
     *
     * @return Un botón estándar con el estilo configurado (fuente, color de fondo, etc.).
     */
    private static JButton generateBotonStandard() {
        JButton button = new JButton();

        // Desactiva el foco pintado para que no se muestre el borde predeterminado al seleccionar el botón.
        button.setFocusPainted(false);

        // Configura la fuente y el estilo del botón.
        button.setFont(new Font("Impact", Font.ITALIC, 30));
        button.setBackground(new Color(255, 164, 0));  // Color naranja de fondo.
        return button;
    }

    /**
     * Genera el panel principal de la aplicación, ajustado al tamaño completo de la pantalla.
     *
     * @return Un JPanel configurado con el tamaño de la pantalla y sin disposición automática (layout null).
     */
    private static JPanel generatePanelMain() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Crea un panel ajustado al tamaño de la pantalla del dispositivo.
        JPanel panel = new JPanel();
        panel.setSize(toolkit.getScreenSize());
        panel.setLayout(null);  // Desactiva el layout automático para permitir posicionamiento manual.
        return panel;
    }
}