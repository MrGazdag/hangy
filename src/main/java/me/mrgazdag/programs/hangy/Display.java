package me.mrgazdag.programs.hangy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Display extends JFrame {
    private HangyWorld world;
    private volatile boolean running;
    public Display(HangyWorld world) {
        running = false;
        this.world = world;
        setBounds(100, 100, 512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics gr) {
                Graphics2D g = (Graphics2D) gr;
                int width = getWidth();
                int height = getHeight();

                double worldWidth = world.getXSize();
                double worldHeight = world.getYSize();
                if (width == 0 || height == 0) return;
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);

                g.setStroke(new BasicStroke(3));

                List<HangyTarget> nodes = world.getBestRouteSoFar();
                if (nodes != null) {
                    HangyTarget firstnode = world.getStartNode();
                    int firstX = (int) ((firstnode.getXPos()/worldWidth) * width);
                    int firstY = (int) ((firstnode.getYPos()/worldHeight) * height);

                    int lastX = firstX;
                    int lastY = firstY;
                    int size = nodes.size();
                    for (int i = 0; i < size; i++) {
                        HangyTarget currentNode = nodes.get(i);
                        g.setColor(huePercent(size, i));
                        int currentX = (int) ((currentNode.getXPos()/worldWidth) * width);
                        int currentY = (int) ((currentNode.getYPos()/worldHeight) * height);
                        g.drawLine(lastX, lastY, currentX, currentY);
                        lastX = currentX;
                        lastY = currentY;
                    }

                    g.setStroke(new BasicStroke(1));

                    g.setColor(huePercent(1, 1));
                    g.drawLine(lastX, lastY, firstX, firstY);
                }

                g.setStroke(new BasicStroke(1));

                g.setColor(Color.BLACK);
                for (HangyTarget target : world.getTargets()) {
                    int x = (int) ((target.getXPos()/worldWidth) * width);
                    int y = (int) ((target.getYPos()/worldHeight) * height);
                    g.fillOval(x-5, y-5, 10, 10);
                }

                g.drawString("Generation " + world.getGeneration(), 0, g.getFontMetrics().getHeight());

                g.drawString("Distance: " + world.getBestDistanceSoFar(), 0, height-g.getFontMetrics().getHeight());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!running) start();
                    else {
                        running = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    world.reset();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    world.clear();
                    running = false;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY()-16;

                double relativeX = (double)x/getWidth();
                double relativeY = (double)y/getHeight();
                world.addPoint(new HangyTarget(world.getXSize()*relativeX, world.getYSize()*relativeY, "gecigránát"));
                world.reset();
                repaint();
            }
        });
    }

    private static Color huePercent(int max, int amount) {
        return Color.getHSBColor((float)amount/max, 1, 1);
    }

    public void start() {
        running = true;
        new Thread(() -> {
            while(running) {
                try {
                    world.completeGeneration();
                } catch (Throwable e) {
                    e.printStackTrace();
                    running = false;
                }
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
