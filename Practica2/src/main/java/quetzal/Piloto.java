package quetzal;

public class Piloto {

    // =========================
    // ATRIBUTOS
    // =========================

    private String nombre;
    private String tipoNave;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Piloto(String nombre, String tipoNave) {
        this.nombre = nombre;
        this.tipoNave = tipoNave;
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public String getNombre() {
        return nombre;
    }

    public String getTipoNave() {
        return tipoNave;
    }

    public void setTipoNave(String tipoNave) {
        this.tipoNave = tipoNave;
    }
}