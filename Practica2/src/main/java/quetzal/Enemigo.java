package quetzal;

public class Enemigo extends Thread {

    // =========================
    // ATRIBUTOS
    // =========================

    private int x;
    private int y;

    private final int ancho = 45;
    private final int alto = 35;

    private final int velocidad;

    // volatile permite que los cambios sean vistos por los otros hilos
    private volatile boolean activo;

    private final PanelJuego panelJuego;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Enemigo(
            int x,
            int y,
            int velocidad,
            PanelJuego panelJuego
    ) {

        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.panelJuego = panelJuego;

        activo = true;
    }

    // =========================
    // MOVIMIENTO DEL ENEMIGO
    // =========================

    @Override
    public void run() {

        // El enemigo avanza mientras siga activo
        while (activo) {

            // Mueve al enemigo hacia la izquierda
            x -= velocidad;

            // Lo desactiva cuando sale de la pantalla
            if (x + ancho < 0) {
                activo = false;
            }

            // Actualiza el dibujo del juego
            panelJuego.repaint();

            try {

                // Pausa pequeña para que el movimiento sea fluido
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

    public boolean isActivo() {
        return activo;
    }

    // =========================
    // DETENER ENEMIGO
    // =========================

    public void detener() {
        activo = false;
    }
}