package com.andreidodu.gui;

import com.andreidodu.observer.CircleObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

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

        setOpacity(0.7f);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 360.0, 360));

        JButton closeButton = new JButton("X");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setBounds(getWidth() / 2 - (20 / 2), 15, 20, 20);
        closeButton.setFocusable(false);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        closeButton.setBackground(new Color(255, 0, 0, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> System.exit(0));

        add(closeButton);

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
    }


}
