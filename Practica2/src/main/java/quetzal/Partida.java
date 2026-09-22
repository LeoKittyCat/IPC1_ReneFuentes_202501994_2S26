package quetzal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Partida {

    // =========================
    // ATRIBUTOS
    // =========================

    private String nombrePiloto;
    private String tipoNave;
    private int puntaje;
    private String fecha;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Partida(
            String nombrePiloto,
            String tipoNave,
            int puntaje
    ) {

        this.nombrePiloto = nombrePiloto;
        this.tipoNave = tipoNave;
        this.puntaje = puntaje;

        // Guarda la fecha y hora en que terminó la partida
        DateTimeFormatter formato
                = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        fecha = LocalDateTime.now().format(formato);
    }

    // =========================
    // GETTERS
    // =========================

    public String getNombrePiloto() {
        return nombrePiloto;
    }

    public String getTipoNave() {
        return tipoNave;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public String getFecha() {
        return fecha;
    }
}