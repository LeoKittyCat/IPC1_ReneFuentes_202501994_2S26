package quetzal;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame {

    // =========================
    // ATRIBUTOS
    // =========================

    private GestionPilotos gestionPilotos;

    private JButton btnJugar;
    private JButton btnCrearPiloto;
    private JButton btnTopPuntajes;
    private JButton btnSalir;

    // =========================
    // CONSTRUCTOR
    // =========================

    public MenuPrincipal(GestionPilotos gestionPilotos) {

        this.gestionPilotos = gestionPilotos;

        configurarVentana();
        crearComponentes();
    }

    // =========================
    // CONFIGURAR VENTANA
    // =========================

    private void configurarVentana() {
        setTitle("Quetzal Space Defender");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // =========================
    // CREAR COMPONENTES
    // =========================

    private void crearComponentes() {

        JLabel lblTitulo = new JLabel(
                "QUETZAL SPACE DEFENDER",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel panelBotones = new JPanel(
                new GridLayout(4, 1, 10, 15)
        );

        btnJugar = new JButton("Jugar");
        btnCrearPiloto = new JButton("Crear piloto");
        btnTopPuntajes = new JButton("Top de puntajes");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnJugar);
        panelBotones.add(btnCrearPiloto);
        panelBotones.add(btnTopPuntajes);
        panelBotones.add(btnSalir);

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(20, 30)
        );

        panelPrincipal.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        40,
                        70,
                        40,
                        70
                )
        );

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        add(panelPrincipal);

        agregarEventos();
    }

    // =========================
    // EVENTOS DE LOS BOTONES
    // =========================

    private void agregarEventos() {

        btnCrearPiloto.addActionListener(e -> abrirRegistroPiloto());

        btnJugar.addActionListener(e -> comprobarPilotos());

        btnTopPuntajes.addActionListener(e -> mostrarTopPuntajes());

        btnSalir.addActionListener(e -> System.exit(0));
    }

    // =========================
    // ABRIR REGISTRO DE PILOTO
    // =========================

    private void abrirRegistroPiloto() {

        VentanaCrearPiloto ventana = new VentanaCrearPiloto(
                this,
                gestionPilotos
        );

        ventana.setVisible(true);
    }

    // =========================
    // COMPROBAR PILOTOS
    // =========================

    private void comprobarPilotos() {

        if (gestionPilotos.getCantidadPilotos() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes registrar un piloto",
                    "No hay pilotos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        VentanaSeleccionPiloto ventana =
                new VentanaSeleccionPiloto(
                        this,
                        gestionPilotos
                );

        ventana.setVisible(true);
    }

    // =========================
    // MOSTRAR TOP DE PUNTAJES
    // =========================

    private void mostrarTopPuntajes() {

        JOptionPane.showMessageDialog(
                this,
                "Todavía no hay partidas registradas"
        );
    }
}