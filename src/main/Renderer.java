package main;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Renderer {

    private BufferedImage img;
    private Canvas canvas;
    private static final int FPS = 1000 / 30;

    public Renderer(BufferedImage img, Canvas canvas) {
        this.img = img;
        this.canvas = canvas;
        setLoop();
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // říct plátnu, aby zobrazil aktuální img
                canvas.getGraphics().drawImage(img, 0, 0, null);
                // pro zájemce - co dělá observer - https://stackoverflow.com/a/1684476
            }
        }, 0, FPS);
    }

    public void clear() {
        // https://stackoverflow.com/a/5843470
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.clearRect(0, 0, 800, 600);
    }

    public void drawPixel(int x, int y, int color) {
        // nastavit pixel do img
        img.setRGB(x, y, color);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        float k = dy / (float) dx;
        // https://www.google.com/search?q=java+dividing+two+integers
        float q = y1 - k * x1;

        if (Math.abs(k) < 1) {
            // řídící osa X

            if (x2 < x1) {
                int x3 = x2;
                x2 = x1;
                x1 = x3;
            }

            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;
                int ry = Math.round(y);
                drawPixel(x, ry, color);
            }
        } else {
            // řídící osa Y

            if (y2 < y1) {
                int y3 = y2;
                y2 = y1;
                y1 = y3;
            }

            for (int y = y1; y <= y2; y++) {
                float x =(y-q)/k;
                int rx = Math.round(x);
                drawPixel(rx, y, color);
            }
        }
    }

    public void drawDda(int x1, int y1, int x2, int y2, int color){
        int dx = x2 - x1;
        int dy = y2 - y1;
        float k = dy / (float) dx;
        float x, y, g = 0, h = 0;

        if (dx > dy){
            g = 1;
            h = k;
        }else {
        g = 1/k;
        h = 1;
        }

        x = x1;
        y = y1;

        for(int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++){
            drawPixel(Math.round(x), Math.round(y), color);
            x += g;
            y += h;
        }
    }
}
