package quetzal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaSeleccionPiloto extends JDialog {

    // =========================
    // ATRIBUTOS
    // =========================

    private MenuPrincipal menuPrincipal;
    private GestionPilotos gestionPilotos;
    private GestionPartidas gestionPartidas;

    private JComboBox<String> comboPilotos;
    private JButton btnComenzar;
    private JButton btnCancelar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentanaSeleccionPiloto(
            MenuPrincipal menuPrincipal,
            GestionPilotos gestionPilotos,
            GestionPartidas gestionPartidas
    ) {

        super(menuPrincipal, "Seleccionar piloto", true);

        this.menuPrincipal = menuPrincipal;
        this.gestionPilotos = gestionPilotos;
        this.gestionPartidas = gestionPartidas;

        configurarVentana();
        crearComponentes();
    }

    // =========================
    // CONFIGURAR VENTANA
    // =========================

    private void configurarVentana() {
        setSize(400, 200);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // =========================
    // CREAR COMPONENTES
    // =========================

    private void crearComponentes() {
        
        // Primero mostramos la lista desplegable con JComboBox
        JLabel lblPiloto = new JLabel("Selecciona un piloto:");

        comboPilotos = new JComboBox<>();

        // Agrega al combo los pilotos registrados
        for (int i = 0;
                i < gestionPilotos.getCantidadPilotos();
                i++) {

            Piloto piloto = gestionPilotos.getPilotos()[i];

            comboPilotos.addItem(
                    piloto.getNombre() //Toma el nombre
                    + " - " // Le pone un separador
                    + piloto.getTipoNave() // Toma el tipo de nave
            );
        }

        JPanel panelDatos = new JPanel(
                new GridLayout(2, 1, 10, 10)
        ); //2 filas, 1 columna, 10 pixeles de separacion horizontal y vertical

        panelDatos.add(lblPiloto);
        panelDatos.add(comboPilotos);
        
        // Se crean los botones
        btnComenzar = new JButton("Comenzar");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        
        //Se guardan los botones recien creados
        panelBotones.add(btnComenzar);
        panelBotones.add(btnCancelar);

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(10, 20)
        );
        
        // Agrega 25 pixeles de espacio
        panelPrincipal.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );

        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Coloca el panel dentro de la ventana
        add(panelPrincipal);
        
        // Conecta los botones con sus acciones
        agregarEventos();
    }

    // =========================
    // EVENTOS DE LOS BOTONES
    // =========================

    private void agregarEventos() {

        btnComenzar.addActionListener(e -> comenzarPartida());

        btnCancelar.addActionListener(e -> dispose());
    }

    // =========================
    // COMENZAR PARTIDA
    // =========================

    private void comenzarPartida() {

        int posicionSeleccionada =
                comboPilotos.getSelectedIndex();

        Piloto pilotoSeleccionado =
                gestionPilotos.getPilotos()[posicionSeleccionada];

    VentanaJuego ventanaJuego = new VentanaJuego(
            menuPrincipal,
            pilotoSeleccionado,
            gestionPartidas
    );

        menuPrincipal.setVisible(false);

        dispose();

        ventanaJuego.setVisible(true);
    }
}