package quetzal;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VentanaJuego extends JFrame {

    // =========================
    // ATRIBUTOS
    // =========================

    private MenuPrincipal menuPrincipal;
    private Piloto piloto;
    private PanelJuego panelJuego;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentanaJuego(
            MenuPrincipal menuPrincipal,
            Piloto piloto
    ) {
        this.menuPrincipal = menuPrincipal;
        this.piloto = piloto;

        configurarVentana();
        crearComponentes();
        configurarCierre();
    }

    // =========================
    // CONFIGURAR VENTANA
    // =========================

    private void configurarVentana() {
        setTitle("Quetzal Space Defender");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // =========================
    // CREAR COMPONENTES
    // =========================

    private void crearComponentes() {

        JLabel lblPiloto = new JLabel(
                "Piloto: " + piloto.getNombre()
        );

        JLabel lblNave = new JLabel(
                "Nave: " + piloto.getTipoNave()
        );

        JLabel lblPuntaje = new JLabel(
                "Puntaje: 0"
        );

        JPanel panelInformacion = new JPanel();

        panelInformacion.add(lblPiloto);
        panelInformacion.add(lblNave);
        panelInformacion.add(lblPuntaje);

        panelJuego = new PanelJuego(piloto);

        add(panelInformacion, BorderLayout.NORTH);
        add(panelJuego, BorderLayout.CENTER);

        // Espera a que la ventana abra para activar el teclado
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {

                SwingUtilities.invokeLater(() -> {
                    panelJuego.requestFocusInWindow();
                });
            }
        });
    }

    // =========================
    // REGRESAR AL MENÚ
    // =========================

    private void configurarCierre() {

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                menuPrincipal.setVisible(true);
            }
        });
    }
}