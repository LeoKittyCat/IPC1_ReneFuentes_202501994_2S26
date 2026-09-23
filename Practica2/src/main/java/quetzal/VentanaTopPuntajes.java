package quetzal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class VentanaTopPuntajes extends JDialog {

    // =========================
    // ATRIBUTOS
    // =========================

    private GestionPartidas gestionPartidas;

    private JButton btnGenerarReporte;
    private JButton btnCerrar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentanaTopPuntajes(
            MenuPrincipal menuPrincipal,
            GestionPartidas gestionPartidas
    ) {

        super(menuPrincipal, "Resultados de partidas", true);

        this.gestionPartidas = gestionPartidas;

        configurarVentana();
        crearComponentes();
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
                "RESULTADOS DE PARTIDAS",
                SwingConstants.CENTER
        );

        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab("Top de Puntajes", crearPanelTop());
        pestañas.addTab("Historial", crearPanelHistorial());

        // Crea los botones antes de agregarles sus eventos
        btnGenerarReporte = new JButton("Generar reporte");
        btnCerrar = new JButton("Cerrar");

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnGenerarReporte);
        panelBoton.add(btnCerrar);

        add(lblTitulo, BorderLayout.NORTH);
        add(pestañas, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        btnGenerarReporte.addActionListener(e -> generarReporte());
        btnCerrar.addActionListener(e -> dispose());
    }

    // =========================
    // PANEL DEL TOP
    // =========================

    private JPanel crearPanelTop() {

        JPanel panelTop = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        JTable tablaTop = crearTablaTop();

        JScrollPane scrollTop
                = new JScrollPane(tablaTop);

        panelTop.add(scrollTop);
        panelTop.add(crearGrafica());

        return panelTop;
    }

    // =========================
    // TABLA DEL TOP
    // =========================

    private JTable crearTablaTop() {

        String[] columnas = {
            "Posición",
            "Piloto",
            "Nave",
            "Puntaje",
            "Fecha"
        };

        DefaultTableModel modelo
                = crearModeloTabla(columnas);

        Partida[] partidasOrdenadas
                = gestionPartidas.obtenerPartidasOrdenadas();

        int cantidad
                = gestionPartidas.getCantidadPartidas();

        // Agrega todas las partidas ordenadas por puntaje
        for (int i = 0; i < cantidad; i++) {

            Partida partida = partidasOrdenadas[i];

            Object[] fila = {
                i + 1,
                partida.getNombrePiloto(),
                partida.getTipoNave(),
                partida.getPuntaje(),
                partida.getFecha()
            };

            modelo.addRow(fila);
        }

        return new JTable(modelo);
    }

    // =========================
    // GRÁFICA DEL TOP
    // =========================

    private ChartPanel crearGrafica() {

        DefaultCategoryDataset datos
                = new DefaultCategoryDataset();

        Partida[] partidasOrdenadas
                = gestionPartidas.obtenerPartidasOrdenadas();

        int cantidad
                = gestionPartidas.getCantidadPartidas();

        /*
         * La tabla muestra todos los resultados
         * La gráfica solo muestra los primeros diez para que sea legible
         */
        int limite = Math.min(cantidad, 10);

        for (int i = 0; i < limite; i++) {

            Partida partida = partidasOrdenadas[i];

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
                "Mejores 10 puntajes",
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
    // PANEL DEL HISTORIAL
    // =========================

    private JPanel crearPanelHistorial() {

        JPanel panelHistorial = new JPanel(
                new BorderLayout()
        );

        JTable tablaHistorial = crearTablaHistorial();

        JScrollPane scrollHistorial
                = new JScrollPane(tablaHistorial);

        panelHistorial.add(
                scrollHistorial,
                BorderLayout.CENTER
        );

        return panelHistorial;
    }

    // =========================
    // TABLA DEL HISTORIAL
    // =========================

    private JTable crearTablaHistorial() {

        String[] columnas = {
            "No.",
            "Piloto",
            "Nave",
            "Puntaje",
            "Fecha"
        };

        DefaultTableModel modelo
                = crearModeloTabla(columnas);

        Partida[] partidas
                = gestionPartidas.getPartidas();

        int cantidad
                = gestionPartidas.getCantidadPartidas();

        /*
         * Lee el arreglo original
         * Por eso aparecen en el orden en que se jugaron
         */
        for (int i = 0; i < cantidad; i++) {

            Partida partida = partidas[i];

            Object[] fila = {
                i + 1,
                partida.getNombrePiloto(),
                partida.getTipoNave(),
                partida.getPuntaje(),
                partida.getFecha()
            };

            modelo.addRow(fila);
        }

        return new JTable(modelo);
    }

    // =========================
    // MODELO DE TABLA
    // =========================

    private DefaultTableModel crearModeloTabla(
            String[] columnas
    ) {

        return new DefaultTableModel(columnas, 0) {

            // Evita que el usuario cambie los resultados
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {

                return false;
            }
        };
    }
    
    // =========================
    // GENERAR REPORTE
    // =========================

    private void generarReporte() {

        try {

            GeneradorReporte generador
                    = new GeneradorReporte(gestionPartidas);

            File archivoHtml = generador.generarReporte();

            JOptionPane.showMessageDialog(
                    this,
                    "El reporte fue generado correctamente",
                    "Reporte generado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Abre el reporte usando el navegador predeterminado
            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop().browse(
                        archivoHtml.toURI()
                );

            } else {

                // Muestra la ruta si el sistema no puede abrirlo
                JOptionPane.showMessageDialog(
                        this,
                        "El reporte se encuentra en:\n"
                        + archivoHtml.getAbsolutePath()
                );
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
}