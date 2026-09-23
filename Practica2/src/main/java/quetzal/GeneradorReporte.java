package quetzal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GeneradorReporte {

    // =========================
    // ATRIBUTOS
    // =========================

    private GestionPartidas gestionPartidas;

    // =========================
    // CONSTRUCTOR
    // =========================

    public GeneradorReporte(
            GestionPartidas gestionPartidas
    ) {

        this.gestionPartidas = gestionPartidas;
    }

    // =========================
    // GENERAR REPORTE
    // =========================

    public File generarReporte() throws IOException {

        // Crea una carpeta para guardar el reporte
        File carpetaReportes = new File("reportes");

        if (!carpetaReportes.exists()) {

            boolean carpetaCreada
                    = carpetaReportes.mkdirs();

            if (!carpetaCreada) {
                throw new IOException(
                        "No se pudo crear la carpeta de reportes"
                );
            }
        }

        File archivoGrafica = new File(
                carpetaReportes,
                "grafica_puntajes.png"
        );

        File archivoHtml = new File(
                carpetaReportes,
                "reporte_partidas.html"
        );

        generarImagenGrafica(archivoGrafica);
        generarArchivoHtml(archivoHtml, archivoGrafica);

        return archivoHtml;
    }

    // =========================
    // GENERAR IMAGEN
    // =========================

    private void generarImagenGrafica(
            File archivoGrafica
    ) throws IOException {

        DefaultCategoryDataset datos
                = new DefaultCategoryDataset();

        Partida[] partidasOrdenadas
                = gestionPartidas.obtenerPartidasOrdenadas();

        int cantidad
                = gestionPartidas.getCantidadPartidas();

        int limite = Math.min(cantidad, 10);

        // Agrega los mejores diez resultados a la gráfica
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

        // Convierte la gráfica en una imagen
        BufferedImage imagen
                = grafica.createBufferedImage(700, 400);

        // Guarda la imagen usando ImageIO
        ImageIO.write(
                imagen,
                "png",
                archivoGrafica
        );
    }

    // =========================
    // GENERAR ARCHIVO HTML
    // =========================

    private void generarArchivoHtml(
            File archivoHtml,
            File archivoGrafica
    ) throws IOException {

        /*
         * PrintWriter escribe el contenido del reporte
         * FileWriter crea el archivo HTML
         */
        try (PrintWriter escritor = new PrintWriter(
                new FileWriter(archivoHtml)
        )) {

            escribirInicioHtml(escritor);
            escribirTabla(escritor);

            escritor.println("<h2>Gráfica de puntajes</h2>");

            escritor.println(
                    "<img src=\""
                    + archivoGrafica.getName()
                    + "\" alt=\"Gráfica de puntajes\">"
            );

            escritor.println("</body>");
            escritor.println("</html>");
        }
    }

    // =========================
    // INICIO DEL HTML
    // =========================

    private void escribirInicioHtml(
            PrintWriter escritor
    ) {

        escritor.println("<!DOCTYPE html>");
        escritor.println("<html lang=\"es\">");
        escritor.println("<head>");
        escritor.println("<meta charset=\"UTF-8\">");
        escritor.println("<title>Reporte de Partidas</title>");

        escritor.println("<style>");
        escritor.println("body {");
        escritor.println("font-family: Arial, sans-serif;");
        escritor.println("margin: 40px;");
        escritor.println("}");

        escritor.println("table {");
        escritor.println("width: 100%;");
        escritor.println("border-collapse: collapse;");
        escritor.println("margin-bottom: 30px;");
        escritor.println("}");

        escritor.println("th, td {");
        escritor.println("border: 1px solid black;");
        escritor.println("padding: 8px;");
        escritor.println("text-align: center;");
        escritor.println("}");

        escritor.println("th {");
        escritor.println("background-color: #dddddd;");
        escritor.println("}");

        escritor.println("img {");
        escritor.println("max-width: 700px;");
        escritor.println("width: 100%;");
        escritor.println("}");

        escritor.println("</style>");
        escritor.println("</head>");
        escritor.println("<body>");

        escritor.println(
                "<h1>Reporte de partidas</h1>"
        );

        escritor.println(
                "<p>Quetzal Space Defender</p>"
        );
    }

    // =========================
    // TABLA DEL HISTORIAL
    // =========================

    private void escribirTabla(
            PrintWriter escritor
    ) {

        escritor.println("<h2>Historial completo</h2>");
        escritor.println("<table>");

        escritor.println(
                "<tr>"
                + "<th>No.</th>"
                + "<th>Piloto</th>"
                + "<th>Nave</th>"
                + "<th>Puntaje</th>"
                + "<th>Fecha</th>"
                + "</tr>"
        );

        Partida[] partidas
                = gestionPartidas.getPartidas();

        int cantidad
                = gestionPartidas.getCantidadPartidas();

        for (int i = 0; i < cantidad; i++) {

            Partida partida = partidas[i];

            escritor.println(
                    "<tr>"
                    + "<td>" + (i + 1) + "</td>"
                    + "<td>"
                    + escaparTexto(partida.getNombrePiloto())
                    + "</td>"
                    + "<td>"
                    + escaparTexto(partida.getTipoNave())
                    + "</td>"
                    + "<td>"
                    + partida.getPuntaje()
                    + "</td>"
                    + "<td>"
                    + partida.getFecha()
                    + "</td>"
                    + "</tr>"
            );
        }

        escritor.println("</table>");
    }

    // =========================
    // PROTEGER TEXTO DEL HTML
    // =========================

    private String escaparTexto(String texto) {

        // Evita que algunos símbolos dañen el archivo HTML
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}