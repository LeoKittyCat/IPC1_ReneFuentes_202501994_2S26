package quetzal;

import javax.swing.SwingUtilities;

public class Main {

    // =========================
    // MÉTODO PRINCIPAL
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Crea el objeto que administra todos los pilotos
            GestionPilotos gestionPilotos = new GestionPilotos();

            // Abre el menú principal usando la misma gestión
            MenuPrincipal menu = new MenuPrincipal(gestionPilotos);
            menu.setVisible(true);
        });
    }
}