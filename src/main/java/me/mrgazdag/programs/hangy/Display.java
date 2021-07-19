package me.mrgazdag.programs.hangy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Display extends JFrame {
    private HangyWorld world;
    private volatile boolean running;
    private int transparent;
    public Display(HangyWorld world) {
        running = false;
        transparent = 0;
        this.world = world;
        setBounds(100, 100, 512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics gr) {
                Graphics2D g = (Graphics2D) gr;
                int width = getWidth();
                int height = getHeight();

                double worldWidth = getWorld().getXSize();
                double worldHeight = getWorld().getYSize();
                if (width == 0 || height == 0) return;
                if (transparent == 1) {
                    g.setColor(new Color(255, 255 ,255, 100));
                } else if (transparent == 2) {
                    g.setColor(new Color(255, 255 ,255, 0));
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(0, 0, width, height);

                g.setStroke(new BasicStroke(3));

                g.setColor(new Color(128, 128, 128, 40));
                HangyTarget firstnode = getWorld().getStartNode();
                if (firstnode != null) for (Hangy ant : getWorld().getAnts()) {
                    List<HangyTarget> nodes = ant.getRoute();
                    if (nodes == null) continue;
                    int firstX = (int) ((firstnode.getXPos()/worldWidth) * width);
                    int firstY = (int) ((firstnode.getYPos()/worldHeight) * height);

                    int lastX = firstX;
                    int lastY = firstY;
                    int size = nodes.size();
                    for (int i = 0; i < size; i++) {
                        HangyTarget currentNode = nodes.get(i);

                        int currentX = (int) ((currentNode.getXPos()/worldWidth) * width);
                        int currentY = (int) ((currentNode.getYPos()/worldHeight) * height);
                        g.drawLine(lastX, lastY, currentX, currentY);
                        lastX = currentX;
                        lastY = currentY;
                    }

                    g.drawLine(lastX, lastY, firstX, firstY);
                }

                g.setStroke(new BasicStroke(3));
                List<HangyTarget> nodes2 = getWorld().getBestRouteSoFar();
                if (nodes2 != null) {
                    int firstX = (int) ((firstnode.getXPos()/worldWidth) * width);
                    int firstY = (int) ((firstnode.getYPos()/worldHeight) * height);

                    int lastX = firstX;
                    int lastY = firstY;
                    int size = nodes2.size();
                    for (int i = 0; i < size; i++) {
                        HangyTarget currentNode = nodes2.get(i);
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
                for (HangyTarget target : getWorld().getTargets()) {
                    int x = (int) ((target.getXPos()/worldWidth) * width);
                    int y = (int) ((target.getYPos()/worldHeight) * height);
                    g.fillOval(x-5, y-5, 10, 10);
                }

                g.drawString("Generation " + getWorld().getGeneration(), 0, g.getFontMetrics().getHeight());

                g.drawString("Distance: " + getWorld().getBestDistanceSoFar(), 0, height-g.getFontMetrics().getHeight());
            }
        };
        setContentPane(panel);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!running) start();
                    else {
                        running = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    transparent = (transparent+1)%3;
                    if (transparent > 0) {
                        setVisible(false);
                        //try {Thread.sleep(200);} catch (InterruptedException eh) {eh.printStackTrace();}
                        dispose();
                        setUndecorated(true);
                        setBackground(new Color(0,0,0,0));
                        //try {Thread.sleep(200);} catch (InterruptedException eh) {eh.printStackTrace();}
                        setVisible(true);
                    } else {
                        setVisible(false);
                        //try {Thread.sleep(200);} catch (InterruptedException eh) {eh.printStackTrace();}
                        dispose();
                        setBackground(new Color(0,0,0,255));
                        setUndecorated(false);
                        //try {Thread.sleep(200);} catch (InterruptedException eh) {eh.printStackTrace();}
                        setVisible(true);
                    }
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    getWorld().reset();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    String contents = getWorld().toJSON().toString(4);
                    try {
                        File f = new File("save.json").getAbsoluteFile();
                        if (!f.exists()) {
                            f.getParentFile().mkdirs();
                            f.createNewFile();
                        }
                        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                        bw.write(contents);
                        bw.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    getWorld().clear();
                    running = false;
                    try {
                        setWorld(HangyMain.loadFromJSON(HangyMain.getJSONFromFile(new File("lague_profile.json"))));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    getWorld().clear();
                    running = false;
                    repaint();
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                double relativeX = (double)x/getContentPane().getWidth();
                double relativeY = (double)y/getContentPane().getHeight();
                getWorld().addPoint(new HangyTarget(getWorld().getXSize()*relativeX, getWorld().getYSize()*relativeY, "gecigránát"));
                getWorld().reset();
                repaint();
            }
        });
    }

    public HangyWorld getWorld() {
        return world;
    }

    private void setWorld(HangyWorld world) {
        this.world = world;
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
