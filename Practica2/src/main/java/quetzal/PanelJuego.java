package quetzal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class PanelJuego extends JPanel {

    // =========================
    // ATRIBUTOS
    // =========================

    private Piloto piloto;

    private int naveX = 80;
    private int naveY = 200;

    private final int anchoNave = 60;
    private final int altoNave = 40;
    private final int movimiento = 10;

    // =========================
    // CONSTRUCTOR
    // =========================

    public PanelJuego(Piloto piloto) {

        this.piloto = piloto;

        configurarPanel();
        configurarTeclado();
    }

    // =========================
    // CONFIGURAR PANEL
    // =========================

    private void configurarPanel() {

        setBackground(Color.BLACK);

        // Permite que el panel reciba eventos del teclado
        setFocusable(true);
    }

    // =========================
    // CONFIGURAR TECLADO
    // =========================

    private void configurarTeclado() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                int tecla = e.getKeyCode();

                if (tecla == KeyEvent.VK_W
                        || tecla == KeyEvent.VK_UP) {

                    moverArriba();
                }

                if (tecla == KeyEvent.VK_S
                        || tecla == KeyEvent.VK_DOWN) {

                    moverAbajo();
                }

                if (tecla == KeyEvent.VK_A
                        || tecla == KeyEvent.VK_LEFT) {

                    moverIzquierda();
                }

                if (tecla == KeyEvent.VK_D
                        || tecla == KeyEvent.VK_RIGHT) {

                    moverDerecha();
                }

                repaint();
            }
        });
    }

    // =========================
    // MOVIMIENTO DE LA NAVE
    // =========================

    private void moverArriba() {

        naveY -= movimiento;

        if (naveY < 0) {
            naveY = 0;
        }
    }

    private void moverAbajo() {

        naveY += movimiento;

        int limiteInferior = getHeight() - altoNave;

        if (naveY > limiteInferior) {
            naveY = limiteInferior;
        }
    }

    private void moverIzquierda() {

        naveX -= movimiento;

        if (naveX < 0) {
            naveX = 0;
        }
    }

    private void moverDerecha() {

        naveX += movimiento;

        int limiteDerecho = getWidth() - anchoNave;

        if (naveX > limiteDerecho) {
            naveX = limiteDerecho;
        }
    }

    // =========================
    // DIBUJAR EL JUEGO
    // =========================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        dibujarEstrellas(g);
        dibujarNave(g);
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

        // Cambia el color dependiendo del tipo de nave
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
}