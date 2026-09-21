package quetzal;

public class GestionPilotos {

    // Guarda como máximo 100 pilotos
    private Piloto[] pilotos = new Piloto[100];

    // Indica cuántas posiciones del arreglo están ocupadas
    private int cantidadPilotos = 0;


    public boolean registrarPiloto(String nombre, String tipoNave) {

        // Evita registrar datos vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        if (tipoNave == null || tipoNave.trim().isEmpty()) {
            return false;
        }

        // Evita nombres repetidos
        if (buscarPiloto(nombre) != null) {
            return false;
        }

        // Evita guardar más pilotos cuando el arreglo ya está lleno
        if (cantidadPilotos >= pilotos.length) {
            return false;
        }

        Piloto nuevoPiloto = new Piloto(nombre.trim(), tipoNave);
        pilotos[cantidadPilotos] = nuevoPiloto;
        cantidadPilotos++;

        return true;
    }

    public Piloto buscarPiloto(String nombre) {

        if (nombre == null) {
            return null;
        }

        // Recorre solamente las posiciones utilizadas
        for (int i = 0; i < cantidadPilotos; i++) {

            if (pilotos[i].getNombre().equalsIgnoreCase(nombre.trim())) {
                return pilotos[i];
            }
        }

        return null;
    }

    public Piloto[] getPilotos() {
        return pilotos;
    }

    public int getCantidadPilotos() {
        return cantidadPilotos;
    }
}