package quetzal;

public class GestionPartidas {

    // =========================
    // ATRIBUTOS
    // =========================

    private final Partida[] partidas;
    private int cantidadPartidas;

    // =========================
    // CONSTRUCTOR
    // =========================

    public GestionPartidas() {

        // Permite guardar hasta cien partidas
        partidas = new Partida[100];

        cantidadPartidas = 0;
    }

    // =========================
    // REGISTRAR PARTIDA
    // =========================

    public boolean registrarPartida(Partida partida) {

        // Revisa que exista espacio dentro del arreglo
        if (cantidadPartidas >= partidas.length) {
            return false;
        }

        partidas[cantidadPartidas] = partida;
        cantidadPartidas++;

        return true;
    }

    // =========================
    // GETTERS
    // =========================

    public Partida[] getPartidas() {
        return partidas;
    }

    public int getCantidadPartidas() {
        return cantidadPartidas;
    }
}