package quetzal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

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
        
        iniciarGeneracionObjetos();
        asignarVelocidad();
        configurarPanel();
        configurarTeclado();
        iniciarMovimiento();
        iniciarGeneracionEnemigos();
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
        // Esto no se si venia en la gui pero la verdadd asi se controla mejor
        timerMovimiento = new Timer(16, e -> {

            actualizarPosicion();

            detectarColisiones();
            detectarColisionesConObjetos();

            eliminarProyectilesInactivos();
            eliminarEnemigosInactivos();
            eliminarObjetosInactivos();

            repaint();
        });

        timerMovimiento.start();
    }
    
    // =========================
    // GENERACIÓN DE ENEMIGOS
    // =========================

    private void iniciarGeneracionEnemigos() {

        // Crea un enemigo nuevo cada segundo y medio
        timerEnemigos = new Timer(1500, e -> {
            generarEnemigo();
        });

        timerEnemigos.start();
    }

    private void generarEnemigo() {

        // Evita crear más enemigos si el arreglo está lleno
        if (cantidadEnemigos >= enemigos.length) {
            return;
        }

        int altoDisponible = getHeight() - 35;

        // Evita generar una posición inválida al iniciar la ventana
        if (altoDisponible <= 0) {
            return;
        }

        // El enemigo aparece en una altura aleatoria
        int posicionY = random.nextInt(altoDisponible);

        // Aparece un poco afuera del lado derecho
        int posicionX = getWidth() + 45;

        // La velocidad también puede variar un poco
        // Genera numeros del 0 al 2 y le suma 3
        int velocidadEnemigo = 3 + random.nextInt(3);

        Enemigo nuevoEnemigo = new Enemigo(
                posicionX,
                posicionY,
                velocidadEnemigo,
                this
        );

        enemigos[cantidadEnemigos] = nuevoEnemigo;
        cantidadEnemigos++;

        // Inicia el hilo independiente del enemigo
        nuevoEnemigo.start();
    }
    
    // =========================
    // OBJETOS ESPECIALES
    // =========================

    // Guarda los premios y obstáculos que aparecen
    private final ObjetoEspacial[] objetosEspaciales
            = new ObjetoEspacial[30];

    private int cantidadObjetos = 0;

    private Timer timerObjetos;
    private Timer timerBloqueo;

    // Indica si un Bludger bloqueó la nave
    private boolean naveBloqueada = false;
    
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
    // ENEMIGOS
    // =========================

    // Guarda los enemigos que están dentro del juego
    private final Enemigo[] enemigos = new Enemigo[50];

    private int cantidadEnemigos = 0;

    // Se encarga de crear enemigos cada cierto tiempo
    private Timer timerEnemigos;

    private final Random random = new Random();

    // =========================
    // ACTUALIZAR POSICIÓN
    // =========================

    private void actualizarPosicion() {
        
        // No permite moverse mientras el Bludger este activo
                if (naveBloqueada) {
            return;
        }
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
        dibujarEnemigos(g);
        dibujarObjetosEspaciales(g);
        dibujarNave(g);
        dibujarProyectiles(g);
        dibujarPuntaje(g);
    }
    // =========================
    // DIBUJO DE OBJETOS
    // =========================

    private void dibujarObjetosEspaciales(Graphics g) {

        for (int i = 0; i < cantidadObjetos; i++) {

            ObjetoEspacial objeto = objetosEspaciales[i];

            if (objeto == null || !objeto.isActivo()) {
                continue;
            }

            int x = objeto.getPosicionX();
            int y = objeto.getPosicionY();

            switch (objeto.getTipo()) {

                case ObjetoEspacial.SNITCH:

                    // Snitch dorado
                    g.setColor(Color.YELLOW);
                    g.fillOval(x, y, 35, 35);
                    break;

                case ObjetoEspacial.BLUDGER:

                    // Asteroide gris
                    g.setColor(Color.GRAY);
                    g.fillOval(x, y, 35, 35);
                    break;

                case ObjetoEspacial.QUAFFLE:

                    // Contenedor azul
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, 35, 35);
                    break;
            }
        }
    }
    
    // =========================
    // DIBUJO DEL PUNTAJE
    // =========================

    private void dibujarPuntaje(Graphics g) {

        g.setColor(Color.WHITE);

        // Muestra el puntaje en la esquina superior izquierda
        g.drawString(
                "Puntaje: " + puntaje,
                15,
                20
        );
        
                if (naveBloqueada) {

            g.setColor(Color.RED);
            g.drawString(
                    "Nave bloqueada",
                    15,
                    40
            );
        }
    }
    
    // =========================
    // DIBUJO DE ENEMIGOS
    // =========================

    private void dibujarEnemigos(Graphics g) {

        for (int i = 0; i < cantidadEnemigos; i++) {

            Enemigo enemigo = enemigos[i];

            if (enemigo != null && enemigo.isActivo()) {

                int x = enemigo.getPosicionX();
                int y = enemigo.getPosicionY();

                // Dibuja el cuerpo del enemigo
                g.setColor(Color.RED);

                g.fillRect(
                        x,
                        y,
                        enemigo.getAncho(),
                        enemigo.getAlto()
                );

                // Dibuja una ventana para distinguirlo
                g.setColor(Color.YELLOW);

                g.fillOval(
                        x + 8,
                        y + 10,
                        12,
                        12
                );
            }
        }
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
        if (timerEnemigos != null) {
            timerEnemigos.stop();
        }
        
        // Timers de los objetos o perks o como se llamen
        if (timerObjetos != null) {
            timerObjetos.stop();
        }

        if (timerBloqueo != null) {
            timerBloqueo.stop();
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
    
    // =========================
    // LIMPIEZA DE ENEMIGOS
    // =========================

    private void eliminarEnemigosInactivos() {

        for (int i = 0; i < cantidadEnemigos; i++) {

            if (enemigos[i] == null
                    || !enemigos[i].isActivo()) {

                // Mueve los enemigos para cerrar el espacio vacío
                for (int j = i; j < cantidadEnemigos - 1; j++) {
                    enemigos[j] = enemigos[j + 1];
                }

                enemigos[cantidadEnemigos - 1] = null;
                cantidadEnemigos--;

                // Revisa nuevamente la posición actual
                i--;
            }
        }
    }
    
    // =========================
    // PUNTAJE
    // =========================

    private int puntaje = 0;
    
    // =========================
    // COLISIONES
    // =========================

    private void detectarColisiones() {

        // Revisa cada proyectil contra cada enemigo
        for (int i = 0; i < cantidadProyectiles; i++) {

            Proyectil proyectil = proyectiles[i];

            if (proyectil == null || !proyectil.isActivo()) {
                continue;
            }

            for (int j = 0; j < cantidadEnemigos; j++) {

                Enemigo enemigo = enemigos[j];

                if (enemigo == null || !enemigo.isActivo()) {
                    continue;
                }

                if (hayColision(proyectil, enemigo)) {

                    // Desactiva ambos objetos cuando chocan
                    proyectil.detener();
                    enemigo.detener();

                    // lo comento para que  no de puntos puntaje += 10;

                    // Un proyectil solo puede destruir un enemigo
                    break;
                }
            }
        }
    }
    
        private boolean hayColision(
            Proyectil proyectil,
            Enemigo enemigo
    ) {

        int proyectilX = proyectil.getPosicionX();
        int proyectilY = proyectil.getPosicionY();

        int enemigoX = enemigo.getPosicionX();
        int enemigoY = enemigo.getPosicionY();

        /*
         * Revisa si el rectángulo del proyectil
         * está tocando el rectángulo del enemigo
         */
        
        // 14 y 4 son el alto y ancho con lo que dibujamos el proyectil
        return proyectilX < enemigoX + enemigo.getAncho()
                && proyectilX + 14 > enemigoX
                && proyectilY < enemigoY + enemigo.getAlto()
                && proyectilY + 4 > enemigoY;
    }
        // =========================
        // GENERACIÓN DE OBJETOS
        // =========================

        private void iniciarGeneracionObjetos() {

            // Crea un objeto especial cada cinco segundos
            timerObjetos = new Timer(5000, e -> {
                generarObjetoEspacial();
            });

            timerObjetos.start();
        }

        private void generarObjetoEspacial() {

            // Evita superar el tamaño del arreglo
            if (cantidadObjetos >= objetosEspaciales.length) {
                return;
            }

            int altoDisponible = getHeight() - 35;

            if (altoDisponible <= 0) {
                return;
            }

            int posicionX = getWidth() + 35;
            int posicionY = random.nextInt(altoDisponible);

            String tipo;
            int numeroAleatorio = random.nextInt(10);

            /*
             * El Snitch aparece menos veces
             * Los otros dos objetos son más comunes
             */
            if (numeroAleatorio == 0) {
                tipo = ObjetoEspacial.SNITCH;

            } else if (numeroAleatorio <= 4) {
                tipo = ObjetoEspacial.BLUDGER;

            } else {
                tipo = ObjetoEspacial.QUAFFLE;
            }

            ObjetoEspacial nuevoObjeto = new ObjetoEspacial(
                    posicionX,
                    posicionY,
                    4,
                    tipo,
                    this
            );

            objetosEspaciales[cantidadObjetos] = nuevoObjeto;
            cantidadObjetos++;

            // Inicia el movimiento independiente del objeto
            nuevoObjeto.start();
        }
        
        // =========================
        // COLISIÓN CON OBJETOS
        // =========================

        private void detectarColisionesConObjetos() {

            for (int i = 0; i < cantidadObjetos; i++) {

                ObjetoEspacial objeto = objetosEspaciales[i];

                if (objeto == null || !objeto.isActivo()) {
                    continue;
                }

                if (naveTocaObjeto(objeto)) {

                    // Evita aplicar el efecto varias veces
                    objeto.detener();

                    aplicarEfectoObjeto(objeto);
                }
            }
        }

        private boolean naveTocaObjeto(ObjetoEspacial objeto) {

            return naveX < objeto.getPosicionX() + objeto.getAncho()
                    && naveX + anchoNave > objeto.getPosicionX()
                    && naveY < objeto.getPosicionY() + objeto.getAlto()
                    && naveY + altoNave > objeto.getPosicionY();
        }
        
        // =========================
        // EFECTOS DE OBJETOS
        // =========================

        private void aplicarEfectoObjeto(ObjetoEspacial objeto) {

            switch (objeto.getTipo()) {

                case ObjetoEspacial.SNITCH:

                    // Suma puntos y destruye los enemigos visibles
                    puntaje += 150;
                    destruirEnemigosVisibles();
                    break;

                case ObjetoEspacial.BLUDGER:

                    // Bloquea el movimiento durante dos segundos
                    bloquearNave();
                    break;

                case ObjetoEspacial.QUAFFLE:

                    // Suma diez puntos al puntaje actual
                    puntaje += 10;
                    break;
            }
        }

        private void destruirEnemigosVisibles() {

            for (int i = 0; i < cantidadEnemigos; i++) {

                if (enemigos[i] != null) {
                    enemigos[i].detener();
                }
            }
        }

        private void bloquearNave() {

            naveBloqueada = true;

            // Reinicia el tiempo si toca otro Bludger
            if (timerBloqueo != null) {
                timerBloqueo.stop();
            }

            timerBloqueo = new Timer(2000, e -> {
                naveBloqueada = false;
            });

            // Hace que el timer se ejecute solamente una vez
            timerBloqueo.setRepeats(false);
            timerBloqueo.start();
        }
        
        // =========================
        // LIMPIEZA DE OBJETOS
        // =========================

        private void eliminarObjetosInactivos() {

            for (int i = 0; i < cantidadObjetos; i++) {

                if (objetosEspaciales[i] == null
                        || !objetosEspaciales[i].isActivo()) {

                    // Cierra el espacio vacío dentro del arreglo
                    for (int j = i; j < cantidadObjetos - 1; j++) {
                        objetosEspaciales[j] = objetosEspaciales[j + 1];
                    }

                    objetosEspaciales[cantidadObjetos - 1] = null;
                    cantidadObjetos--;

                    i--;
                }
            }
        }
}