package com.andreidodu.gui;

import com.andreidodu.gui.themes.Theme;
import com.andreidodu.observer.CircleObserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class WindowGUI extends JFrame implements ThemeChangerListener {
    private CirclePanel circle;
    private final CircleObserver circleObserver;
    private Point initialClick;
    private JButton upButton;
    private JButton downButton;
    private JButton closeButton;
    private JButton switchButton;


    public CirclePanel getCircle() {
        return circle;
    }

    public WindowGUI(CircleObserver circleObserver) {
        this.circleObserver = circleObserver;
        setUndecorated(true);
        setSize(160, 160);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        setOpacity(0.7f);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 360.0, 360));

        closeButton = prepareButton("close.png", circleObserver::exit);
        closeButton.setBounds(getWidth() / 2 - (20 / 2), getHeight() - 35, 20, 20);
        add(closeButton);
        upButton = prepareButton("up.png", circleObserver::increaseTimer);
        upButton.setBounds(getWidth() - 35, getHeight() / 2 - 10, 20, 20);
        add(upButton);
        downButton = prepareButton("down.png", circleObserver::decreaseTimer);
        downButton.setBounds(15, getHeight() / 2 - 10, 20, 20);
        add(downButton);
        switchButton = prepareButton("switch.png", circleObserver::switchTheme);
        switchButton.setBounds(getWidth() / 2 - 10, 15, 20, 20);
        add(switchButton);

        showHideButtons(false);


        CirclePanel circlePanel = new CirclePanel();
        this.circle = circlePanel;
        circlePanel.setBounds(0, 0, getWidth(), getHeight());
        add(circlePanel);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("About");
        JMenuItem menuItem2 = new JMenuItem("Exit");
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

        menuItem1.addActionListener(e -> showAbout());
        menuItem2.addActionListener(e -> {
            circleObserver.releaseResources();
            System.exit(0);
        });

        circlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopup(e);
                }
                if (SwingUtilities.isLeftMouseButton(e) && circlePanel.isOnPanel(e.getPoint())) {
                    initialClick = e.getPoint();
                } else {
                    initialClick = null;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.circle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showHideButtons(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point mousePos = e.getPoint();
                double distance = calculateDistance(getWidth() / 2, getHeight() / 2, mousePos.x, mousePos.y);
                if (distance > 70) {
                    showHideButtons(false);
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

        setVisible(true);
    }

    private void showHideButtons(boolean aFlag) {
        upButton.setVisible(aFlag);
        downButton.setVisible(aFlag);
        closeButton.setVisible(aFlag);
        switchButton.setVisible(aFlag);
    }


    private void showAbout() {
        JOptionPane.showMessageDialog(this, "<html>Just Focus<br/>Version: 3.1.0<br/>Developer: Andrei Dodu<br/>Project webpage: https://github.com/goto-eof/justfocus<br/>License; CC BY-NC-SA 4.0</html>", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private RoundButton prepareButton(String imageFile, Runnable runnable) {
        RoundButton button = new RoundButton(buildBufferedImage("/images/" + imageFile));
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

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
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


    @Override
    public void onThemeChange(Theme theme) {
        this.circle.onThemeChange(theme);
    }
}
