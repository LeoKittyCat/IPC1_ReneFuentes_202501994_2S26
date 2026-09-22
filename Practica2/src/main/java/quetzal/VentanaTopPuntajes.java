package quetzal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class VentanaTopPuntajes extends JDialog {

    // =========================
    // ATRIBUTOS
    // =========================

    private GestionPartidas gestionPartidas;

    private JTable tablaPuntajes;
    private DefaultTableModel modeloTabla;

    private JButton btnCerrar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentanaTopPuntajes(
            MenuPrincipal menuPrincipal,
            GestionPartidas gestionPartidas
    ) {

        super(menuPrincipal, "Top de Puntajes", true);

        this.gestionPartidas = gestionPartidas;

        configurarVentana();
        crearComponentes();
        cargarPuntajes();
    }

    // =========================
    // CONFIGURAR VENTANA
    // =========================

    private void configurarVentana() {

        setSize(750, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // =========================
    // CREAR COMPONENTES
    // =========================

    private void crearComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(
                "MEJORES PUNTAJES",
                SwingConstants.CENTER
        );

        String[] columnas = {
            "Posición",
            "Piloto",
            "Nave",
            "Puntaje",
            "Fecha"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            // Evita que el usuario pueda editar las celdas
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPuntajes = new JTable(modeloTabla);

        JScrollPane scrollTabla
                = new JScrollPane(tablaPuntajes);

        scrollTabla.setPreferredSize(
                new Dimension(620, 290)
        );

        btnCerrar = new JButton("Cerrar");

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        // Coloca la tabla y la gráfica una debajo de la otra
        JPanel panelCentro = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        panelCentro.add(scrollTabla);
        panelCentro.add(crearGrafica());

        add(lblTitulo, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        // Cierra solamente esta ventana
        btnCerrar.addActionListener(e -> dispose());
    }
    
    // =========================
    // CREAR GRÁFICA
    // =========================

    private ChartPanel crearGrafica() {

        DefaultCategoryDataset datos
                = new DefaultCategoryDataset();

        Partida[] partidasOrdenadas
                = gestionPartidas.obtenerPartidasOrdenadas();

        int cantidad = gestionPartidas.getCantidadPartidas();

        // La gráfica también muestra solamente los diez mejores
        int limite = Math.min(cantidad, 10);

        for (int i = 0; i < limite; i++) {

            Partida partida = partidasOrdenadas[i];

            /*
             * Se agrega la posición al nombre para evitar
             * problemas si el mismo piloto aparece varias veces
             */
            String nombre = (i + 1)
                    + ". "
                    + partida.getNombrePiloto();

            datos.addValue(
                    partida.getPuntaje(),
                    "Puntaje",
                    nombre
            );
        }

        JFreeChart grafica = ChartFactory.createBarChart(
                "Desempeño de los mejores pilotos",
                "Piloto",
                "Puntaje",
                datos,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        return new ChartPanel(grafica);
    }

    // =========================
    // CARGAR PUNTAJES
    // =========================

    private void cargarPuntajes() {

        Partida[] partidasOrdenadas
                = gestionPartidas.obtenerPartidasOrdenadas();

        int cantidad = gestionPartidas.getCantidadPartidas();

        // Muestra como máximo las diez mejores partidas
        int limite = Math.min(cantidad, 10);

        for (int i = 0; i < limite; i++) {

            Partida partida = partidasOrdenadas[i];

            Object[] fila = {
                i + 1,
                partida.getNombrePiloto(),
                partida.getTipoNave(),
                partida.getPuntaje(),
                partida.getFecha()
            };

            modeloTabla.addRow(fila);
        }
    }
}