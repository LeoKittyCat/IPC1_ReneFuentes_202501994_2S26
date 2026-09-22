package quetzal;

// El proyectil usa su propio hilo para moverse de forma independiente
public class Proyectil extends Thread {

    // =========================
    // ATRIBUTOS
    // =========================

    private int x;
    private int y;

    private final int velocidad;
    private boolean activo;

    private final PanelJuego panelJuego;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Proyectil(int x, int y, PanelJuego panelJuego) {

        // Guarda la posición desde donde sale el disparo
        this.x = x;
        this.y = y;

        this.panelJuego = panelJuego;

        velocidad = 10;
        activo = true;
    }

    // =========================
    // MOVIMIENTO DEL PROYECTIL
    // =========================

    @Override
    public void run() {

        // El proyectil continúa moviéndose mientras siga activo
        while (activo) {

            // Mueve el proyectil hacia la derecha
            x += velocidad;

            // Lo desactiva cuando sale de la pantalla
            if (x > panelJuego.getWidth()) {
                activo = false;
            }

            // Vuelve a dibujar el panel con la nueva posición
            panelJuego.repaint();

            try {

                // Pequeña pausa para que el movimiento se vea fluido
                Thread.sleep(16);

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

    public boolean isActivo() {
        return activo;
    }

    // =========================
    // DETENER PROYECTIL
    // =========================

    public void detener() {
        activo = false;
    }
}