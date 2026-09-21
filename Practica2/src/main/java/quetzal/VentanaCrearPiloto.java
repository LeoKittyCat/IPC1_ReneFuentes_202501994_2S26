package quetzal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaCrearPiloto extends JDialog {

    // =========================
    // ATRIBUTOS
    // =========================

    private GestionPilotos gestionPilotos;

    private JTextField txtNombre;
    private JComboBox<String> comboNaves;

    private JButton btnGuardar;
    private JButton btnCancelar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentanaCrearPiloto(
            MenuPrincipal menuPrincipal,
            GestionPilotos gestionPilotos
    ) {
        super(menuPrincipal, "Crear piloto", true);
        // Lo vuelve modal, osea primero termina o cierra el registro
        // antes de volver a utilizar el menu.

        this.gestionPilotos = gestionPilotos;

        configurarVentana();
        crearComponentes();
    }

    // =========================
    // CONFIGURAR VENTANA
    // =========================

    private void configurarVentana() {
        setSize(400, 250);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // =========================
    // CREAR COMPONENTES
    // =========================

    private void crearComponentes() {

        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 10, 15));

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblNave = new JLabel("Tipo de nave:");

        txtNombre = new JTextField();
        
        // Declaramos los tipos de nave
        String[] tiposNave = {
            "Explorador",
            "Caza Estelar",
            "Acorazado"
        };

        comboNaves = new JComboBox<>(tiposNave);

        panelDatos.add(lblNombre);
        panelDatos.add(txtNombre);
        panelDatos.add(lblNave);
        panelDatos.add(comboNaves);

        JPanel panelBotones = new JPanel();

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 20));
        panelPrincipal.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        panelPrincipal.add(panelDatos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        agregarEventos();
    }

    // =========================
    // EVENTOS DE LOS BOTONES
    // =========================

    private void agregarEventos() {

        btnGuardar.addActionListener(e -> guardarPiloto());

        btnCancelar.addActionListener(e -> dispose());
    }

    // =========================
    // GUARDAR PILOTO
    // =========================

    private void guardarPiloto() {

        String nombre = txtNombre.getText();
        String tipoNave = comboNaves.getSelectedItem().toString();

        boolean registrado = gestionPilotos.registrarPiloto(
                nombre,
                tipoNave
        );

        if (registrado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Piloto registrado correctamente"
            );

            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre está vacío, repetido o no se puede registrar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}