package quetzal;

public class ObjetoEspacial extends Thread {

    // =========================
    // TIPOS DE OBJETO
    // =========================

    public static final String SNITCH = "Snitch";
    public static final String BLUDGER = "Bludger";
    public static final String QUAFFLE = "Quaffle";

    // =========================
    // ATRIBUTOS
    // =========================

    private int x;
    private int y;

    private final int ancho = 35;
    private final int alto = 35;

    private final int velocidad;
    private final String tipo;

    private volatile boolean activo;

    private final PanelJuego panelJuego;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ObjetoEspacial(
            int x,
            int y,
            int velocidad,
            String tipo,
            PanelJuego panelJuego
    ) {

        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.tipo = tipo;
        this.panelJuego = panelJuego;

        activo = true;
    }

    // =========================
    // MOVIMIENTO DEL OBJETO
    // =========================

    @Override
    public void run() {

        // Mueve el objeto mientras siga dentro del juego
        while (activo) {

            // Avanza desde la derecha hacia la izquierda
            x -= velocidad;

            // Lo desactiva cuando sale completamente de la pantalla
            if (x + ancho < 0) {
                activo = false;
            }

            panelJuego.repaint();

            try {

                // Mantiene el movimiento fluido
                Thread.sleep(30);

            } catch (InterruptedException e) {

                // Finaliza el hilo si llega a ser interrumpido
                activo = false;
            }
        }
    }

    // =========================
    // GETTERS
    // =========================

    public int getPosicionX() {
        return x;
    }

    public int getPosicionY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isActivo() {
        return activo;
    }

    // =========================
    // DETENER OBJETO
    // =========================

    public void detener() {
        activo = false;
    }
}