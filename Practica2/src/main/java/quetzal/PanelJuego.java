package quetzal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelJuego extends JPanel {

    // =========================
    // DATOS DEL PILOTO
    // =========================

    private Piloto piloto;

    // =========================
    // POSICIÓN Y TAMAÑO
    // =========================

    private int naveX = 80;
    private int naveY = 200;

    private final int anchoNave = 60;
    private final int altoNave = 40;

    // Cambia según la nave seleccionada
    private int velocidad;

    // =========================
    // ESTADO DE LAS TECLAS
    // =========================

    // Cada variable indica si una dirección sigue presionada
    private boolean moverArriba;
    private boolean moverAbajo;
    private boolean moverIzquierda;
    private boolean moverDerecha;

    // Ejecuta el movimiento de forma constante
    // Asi evitamos que hayan pausas entre imputs
    private Timer timerMovimiento;

    // =========================
    // CONSTRUCTOR
    // =========================

    public PanelJuego(Piloto piloto) {

        this.piloto = piloto;

        asignarVelocidad();
        configurarPanel();
        configurarTeclado();
        iniciarMovimiento();
    }

    // =========================
    // ASIGNAR VELOCIDAD
    // =========================

    private void asignarVelocidad() {

        // Cada nave tiene una velocidad diferente
        switch (piloto.getTipoNave()) {

            case "Explorador":
                velocidad = 8;
                break;

            case "Caza Estelar":
                velocidad = 6;
                break;

            case "Acorazado":
                velocidad = 4;
                break;

            default:
                velocidad = 6;
                break;
        }
    }

    // =========================
    // CONFIGURAR PANEL
    // =========================

    private void configurarPanel() {

        setBackground(Color.BLACK);

        // Permite recibir eventos del teclado
        setFocusable(true);
    }

    // =========================
    // CONFIGURAR TECLADO
    // =========================

    private void configurarTeclado() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // Marca la dirección como activa
                cambiarEstadoTecla(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {

                // Desactiva la dirección al soltar la tecla
                cambiarEstadoTecla(e.getKeyCode(), false);
            }
        });
    }

    // =========================
    // CAMBIAR ESTADO DE TECLA
    // =========================

    private void cambiarEstadoTecla(
            int tecla,
            boolean presionada
    ) {

        // Detecta la barra espaciadora para disparar
        if (tecla == KeyEvent.VK_SPACE) {

            // Solo dispara una vez por cada pulsación
            if (presionada && !espacioPresionado) {
                disparar();
            }

            espacioPresionado = presionada;
        }

        if (tecla == KeyEvent.VK_W
                || tecla == KeyEvent.VK_UP) {

            moverArriba = presionada;
        }

        if (tecla == KeyEvent.VK_S
                || tecla == KeyEvent.VK_DOWN) {

            moverAbajo = presionada;
        }

        if (tecla == KeyEvent.VK_A
                || tecla == KeyEvent.VK_LEFT) {

            moverIzquierda = presionada;
        }

        if (tecla == KeyEvent.VK_D
                || tecla == KeyEvent.VK_RIGHT) {

            moverDerecha = presionada;
        }
    } 

    // =========================
    // INICIAR MOVIMIENTO
    // =========================

    private void iniciarMovimiento() {

        /*
         * Revisa las teclas aproximadamente 60 veces por segundo
         * Esto evita depender de la repetición del teclado de Windows
         */
        timerMovimiento = new Timer(16, e -> {
            
            eliminarProyectilesInactivos();
            actualizarPosicion();
            repaint();
        });

        timerMovimiento.start();
    }
    
    // =========================
    // PROYECTILES
    // =========================

    // Guarda los disparos que aparecen en pantalla
    private final Proyectil[] proyectiles = new Proyectil[100];

    private int cantidadProyectiles = 0;

    // Guarda el momento en que se realizó el último disparo
    private long ultimoDisparo = 0;

    // Evita crear varios disparos por mantener espacio presionado
    private boolean espacioPresionado = false;

    // =========================
    // ACTUALIZAR POSICIÓN
    // =========================

    private void actualizarPosicion() {

        /*
         * Estas condiciones son independientes
         * Por eso puede moverse vertical y horizontalmente a la vez
         */
        if (moverArriba) {
            naveY -= velocidad;
        }

        if (moverAbajo) {
            naveY += velocidad;
        }

        if (moverIzquierda) {
            naveX -= velocidad;
        }

        if (moverDerecha) {
            naveX += velocidad;
        }

        validarLimites();
    }

    // =========================
    // VALIDAR LÍMITES
    // =========================

    private void validarLimites() {

        // Límites superior e izquierdo
        if (naveX < 0) {
            naveX = 0;
        }

        if (naveY < 0) {
            naveY = 0;
        }

        // Calcula los límites usando el tamaño actual del panel
        int limiteDerecho = getWidth() - anchoNave;
        int limiteInferior = getHeight() - altoNave;

        if (naveX > limiteDerecho) {
            naveX = limiteDerecho;
        }

        if (naveY > limiteInferior) {
            naveY = limiteInferior;
        }
    }
    
    // =========================
    // DISPAROS
    // =========================

    private void disparar() {

        long tiempoActual = System.currentTimeMillis();
        int tiempoRecarga = obtenerTiempoRecarga();

        // Comprueba si la nave ya puede volver a disparar
        if (tiempoActual - ultimoDisparo < tiempoRecarga) {
            return;
        }

        // Evita guardar más disparos si el arreglo está lleno
        if (cantidadProyectiles >= proyectiles.length) {
            return;
        }

        // El proyectil aparece en la punta de la nave
        int xProyectil = naveX + anchoNave;
        int yProyectil = naveY + (altoNave / 2);

        Proyectil nuevoProyectil = new Proyectil(
                xProyectil,
                yProyectil,
                this
        );

        proyectiles[cantidadProyectiles] = nuevoProyectil;
        cantidadProyectiles++;

        ultimoDisparo = tiempoActual;

        // Inicia el hilo independiente del proyectil
        nuevoProyectil.start();
    }

    private int obtenerTiempoRecarga() {

        // Cada nave tiene un tiempo diferente entre disparos
        switch (piloto.getTipoNave()) {

            case "Explorador":
                return 2000;

            case "Caza Estelar":
                return 1000;

            case "Acorazado":
                return 300;

            default:
                return 1000;
        }
    }

    // =========================
    // DIBUJAR EL JUEGO
    // =========================

    @Override
    protected void paintComponent(Graphics g) {

        // Limpia el dibujo anterior antes de volver a pintar
        super.paintComponent(g);

        dibujarEstrellas(g);
        dibujarNave(g);
        dibujarProyectiles(g);
    }
    
    // =========================
    // DIBUJO DE PROYECTILES
    // =========================

    private void dibujarProyectiles(Graphics g) {

        g.setColor(Color.YELLOW);

        for (int i = 0; i < cantidadProyectiles; i++) {

            Proyectil proyectil = proyectiles[i];

            if (proyectil != null && proyectil.isActivo()) {

                // Dibuja el disparo como una pequeña línea amarilla
                g.fillRect(
                        proyectil.getPosicionX(),
                        proyectil.getPosicionY(),
                        14,
                        4
                );
            }
        }
    }

    // =========================
    // DIBUJAR ESTRELLAS
    // =========================

    private void dibujarEstrellas(Graphics g) {

        g.setColor(Color.WHITE);

        // Estrellas fijas para decorar el fondo
        g.fillOval(100, 80, 3, 3);
        g.fillOval(220, 140, 4, 4);
        g.fillOval(350, 70, 3, 3);
        g.fillOval(480, 250, 4, 4);
        g.fillOval(600, 100, 3, 3);
        g.fillOval(720, 330, 4, 4);
        g.fillOval(820, 190, 3, 3);
        g.fillOval(900, 60, 4, 4);
        g.fillOval(950, 400, 3, 3);
    }

    // =========================
    // DIBUJAR NAVE
    // =========================

    private void dibujarNave(Graphics g) {

        // Selecciona el color según el modelo de nave
        switch (piloto.getTipoNave()) {

            case "Explorador":
                g.setColor(Color.GREEN);
                break;

            case "Caza Estelar":
                g.setColor(Color.CYAN);
                break;

            case "Acorazado":
                g.setColor(Color.ORANGE);
                break;

            default:
                g.setColor(Color.WHITE);
                break;
        }

        // Define los tres puntos que forman el triángulo
        int[] puntosX = {
            naveX,
            naveX,
            naveX + anchoNave
        };

        int[] puntosY = {
            naveY,
            naveY + altoNave,
            naveY + (altoNave / 2)
        };

        Polygon formaNave = new Polygon(
                puntosX,
                puntosY,
                3
        );

        g.fillPolygon(formaNave);

        // Dibuja la ventana de la nave
        g.setColor(Color.WHITE);

        g.fillOval(
                naveX + 22,
                naveY + 14,
                12,
                12
        );
    }

    // =========================
    // DETENER TIMER
    // =========================

    @Override
    public void removeNotify() {

        // Detiene el movimiento cuando el panel se cierra
        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }

        super.removeNotify();
    }
    
    // =========================
    // LIMPIEZA DE PROYECTILES
    // =========================

    private void eliminarProyectilesInactivos() {

        for (int i = 0; i < cantidadProyectiles; i++) {

            if (proyectiles[i] == null
                    || !proyectiles[i].isActivo()) {

                // Mueve los demás proyectiles para cerrar el espacio vacío
                for (int j = i; j < cantidadProyectiles - 1; j++) {
                    proyectiles[j] = proyectiles[j + 1];
                }

                proyectiles[cantidadProyectiles - 1] = null;
                cantidadProyectiles--;

                // Revisa nuevamente esta posición
                i--;
            }
        }
    }
}