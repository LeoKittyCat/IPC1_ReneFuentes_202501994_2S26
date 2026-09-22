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
    // ORDENAR PARTIDAS
    // =========================

    public Partida[] obtenerPartidasOrdenadas() {

        Partida[] partidasOrdenadas
                = new Partida[cantidadPartidas];

        // Copia las partidas para no modificar el arreglo original
        for (int i = 0; i < cantidadPartidas; i++) {
            partidasOrdenadas[i] = partidas[i];
        }

        /*
         * Ordena las partidas de mayor a menor puntaje
         * Se utiliza el método burbuja por ser sencillo
         */
        for (int i = 0; i < cantidadPartidas - 1; i++) {

            for (int j = 0;
                    j < cantidadPartidas - 1 - i;
                    j++) {

                if (partidasOrdenadas[j].getPuntaje()
                        < partidasOrdenadas[j + 1].getPuntaje()) {

                    Partida temporal = partidasOrdenadas[j];

                    partidasOrdenadas[j]
                            = partidasOrdenadas[j + 1];

                    partidasOrdenadas[j + 1] = temporal;
                }
            }
        }

        return partidasOrdenadas;
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