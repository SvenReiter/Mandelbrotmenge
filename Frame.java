package mandelbrot2312221055;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame {
    final float LIMIT= 4f;                  //2f;
    final int ITERATIONS= 50;              //200;
    final int FACTOR_OF_MAX_ZOOM_INTO= 5;   //5;
    int absPosHor=0, absPosVer=0, zoomFactor=250;
    int scopeHorMin, scopeVerMin, scopeHorMax, scopeVerMax;
    /*todo
        color the pixels depending on its iterations
        only draw the visible area
     */

    class ImaginaryPart {
        final float realPart, imaginaryPart;
        public ImaginaryPart(float realPartArg, float imaginaryPartArg) {
            realPart= realPartArg;
            imaginaryPart= imaginaryPartArg;
        }
        public ImaginaryPart add(ImaginaryPart iArg) {
            float r= this.realPart+ iArg.realPart;
            float i= this.imaginaryPart+ iArg.imaginaryPart;
            return new ImaginaryPart(r, i);
        }
        public ImaginaryPart square() {
            float r= realPart* realPart- imaginaryPart*imaginaryPart;
            float i= realPart* imaginaryPart* 2;
            return new ImaginaryPart(r,i);
        }
        public float amount() {
            return (float)(Math.sqrt(realPart* realPart+ imaginaryPart* imaginaryPart));
        }
    }

    public Frame() {
        this.setSize(1280*FACTOR_OF_MAX_ZOOM_INTO, 720*FACTOR_OF_MAX_ZOOM_INTO);
//        this.setVisible(true);      // setVisible() here changes this resolution
        scopeHorMin= this.getWidth()/-1;
        scopeVerMin= this.getHeight()/-1;
        scopeHorMax= this.getWidth()/ 1;
        scopeVerMax= this.getHeight()/ 1;
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP: absPosVer-= 10; break;
                    case KeyEvent.VK_RIGHT: absPosHor+= 10; break;
                    case KeyEvent.VK_DOWN: absPosVer+= 10; break;
                    case KeyEvent.VK_LEFT: absPosHor-= 10; break;
                    case KeyEvent.VK_PLUS: zoomFactor+= 10; break;
                    case KeyEvent.VK_MINUS: zoomFactor-= 10; break;
                }
                repaint();
            }
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.BLACK);
        g.translate(this.getWidth()/2, this.getHeight()/ 2);
        int greyScale=0;
        for(int y= scopeVerMin; y< scopeVerMax; ++y) {
            for(int x= scopeHorMin; x< scopeHorMax; ++x) {
                if((greyScale=isInMandelbrotSet((float)x/ zoomFactor,(float)y/ zoomFactor))!=0) {
                    g.setColor(new Color(255-greyScale, 127, greyScale));
                    g.drawLine(absPosHor+ x, absPosVer+ y, absPosHor+ x+ 1, absPosVer+ y+ 1);
                }
            }
        }
    }

    private int isInMandelbrotSet(float x, float y) {
        ImaginaryPart C= new ImaginaryPart(x,y);
        ImaginaryPart Z= new ImaginaryPart(0,0);
        int i=0;
        for(i=0; i<ITERATIONS; ++i) {
            Z= Z.square().add(C);
            if(Z.amount()> LIMIT) {     //Auf Konvergenz(Grenze durch <LIMIT> gesetzt) prüfen und im Falle dessen Rekursion abbrechen
                return (int)(i*((double)(255/ITERATIONS)));                  //Abbruch -> Der Betrag liegt somit nicht innerhalb der Mandelbrotmenge
            }
        }
        return i;
    }

    public static void main(String[] args) {
        new Frame();
    }
}
