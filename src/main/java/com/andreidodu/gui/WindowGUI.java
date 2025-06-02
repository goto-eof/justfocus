package com.andreidodu.gui;

import com.andreidodu.observer.CircleObserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class WindowGUI extends JFrame {
    private CirclePanel circle;
    private final CircleObserver circleObserver;
    private Point initialClick;

    public CirclePanel getCircle() {
        return circle;
    }

    public WindowGUI(CircleObserver circleObserver) {
        this.circleObserver = circleObserver;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(160, 160);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        setOpacity(0.8f);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 360.0, 360));

        RoundButton closeButton = prepareButton("close.png", circleObserver::exit, " X");
        closeButton.setBounds(getWidth() / 2 - (20 / 2), getHeight() - 35, 20, 20);
        add(closeButton);
        RoundButton upButton = prepareButton("up.png", circleObserver::increaseTimer, "↑");
        upButton.setBounds(getWidth() / 2 - (20 / 2) + 30, 25, 20, 20);
        add(upButton);
        RoundButton downButton = prepareButton("down.png", circleObserver::decreaseTimer, "↓");
        downButton.setBounds(getWidth() / 2 - (20 / 2) - 30, 25, 20, 20);
        add(downButton);

        RoundButton aboutButton = prepareButton("about.png", this::showAbout, " ?");
        aboutButton.setBounds(getWidth() / 2 - (20 / 2), 15, 20, 20);
        add(aboutButton);

        CirclePanel circlePanel = new CirclePanel();
        this.circle = circlePanel;
        circlePanel.setBounds(0, 40, 300, 260);
        add(circlePanel);
        setVisible(true);


        circlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (circlePanel.isOnBorder(e.getPoint())) {
                    initialClick = e.getPoint();
                } else {
                    initialClick = null;
                }
            }
        });


        circlePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialClick != null) {
                    int thisX = getLocation().x;
                    int thisY = getLocation().y;

                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;

                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    setLocation(X, Y);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("releasing resources");
                circleObserver.releaseResources();
                dispose();
                System.exit(0);
            }
        });

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                circleObserver.increaseTimer();
            } else if (e.getWheelRotation() > 0) {
                circleObserver.decreaseTimer();
            }
        });


    }


    private void showAbout() {
        JOptionPane.showMessageDialog(null, "<html>Developer: Andrei Dodu<br/>Project webpage: https://github.com/goto-eof/justfocus</html>", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private RoundButton prepareButton(String imageFile, Runnable runnable, String label) {
        RoundButton button = new RoundButton(buildBufferedImage("/images/" + imageFile), label);
        button.addActionListener(e -> runnable.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIsHovered(true);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIsHovered(false);
                button.repaint();
            }
        });
        return button;
    }

    private BufferedImage buildBufferedImage(String relativeFilePath) {
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(relativeFilePath);
            if (is != null) {
                return ImageIO.read(is);
            } else {
                System.err.println("File not found: " + relativeFilePath);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Unable to load image: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
