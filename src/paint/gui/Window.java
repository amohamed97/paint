package paint.gui;

import paint.controller.Engine;
import paint.model.Ellipse;
import paint.model.Polyline;
import paint.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window {
	private JToggleButton lineToggleButton;
	private JToggleButton rectangleToggleButton;
	private JToggleButton ellipseToggleButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	public JPanel contentPane;
    private Engine engine = new Engine();
    private Canvas canvasPanel;
    private JToggleButton selectToggleButton;
    private JButton deleteButton;
    private JButton fillColorButton;
    private String actionCommand;
    private int startX, startY;


    private Color brushColor = Color.black;
    private Color fillColor = new Color(0,0,0,0);

	public Window() {

		backgroundColorButton.addActionListener(e -> {
			Color color = ColorDialog.getColor();
			if(color != null)
				canvasPanel.setBackground(color);
		});
		brushColorButton.addActionListener(e -> {
			Color color = ColorDialog.getColor();
			if(color != null)
				brushColor = color;
		});
        fillColorButton.addActionListener(e -> {
            Color color = ColorDialog.getColor();
            if(color != null)
                fillColor = color;
        });

        canvasPanel.setEngine(engine);

        MouseAdapter shaper = new MouseAdapter() {
            int startX, startY;
            paint.model.Shape newShape;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX = e.getX();
                startY = e.getY();
                newShape = null;
            };

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int endX = e.getX();
                int endY = e.getY();
                if(newShape != null)
                    engine.removeLastShape();
                if(actionCommand.equals("Line"))
                    newShape = new Polyline(new int[]{startX, endX}, new int[]{startY, endY});
                else if(actionCommand.equals("Ellipse")) {
                    int leftX = startX;
                    int topY = startY;
                    if(endX < leftX){
                        int tmp = leftX;
                        leftX = endX;
                        endX = tmp;
                    }
                    if(endY < topY){
                        int tmp = topY;
                        topY = endY;
                        endY = tmp;
                    }
                    newShape = new Ellipse(leftX, topY, endX - leftX, endY - topY);
                }else if(actionCommand.equals("Rectangle")) {
                    newShape = new Polyline(new int[]{startX, endX, endX, startX},
                            new int[]{startY, startY, endY, endY});
                }else
                    throw new UnsupportedOperationException();
                newShape.setColor(brushColor);
                newShape.setFillColor(fillColor);
                engine.addShape(newShape);
                canvasPanel.repaint();
            }
        };

        MouseAdapter selector = new MouseAdapter() {
            int x, y;
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                engine.selectShape(e.getPoint());
                canvasPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int newX,newY,yDiff,xDiff;
                super.mouseDragged(mouseEvent);
                if(engine.containsSelected(mouseEvent.getPoint())) {
                    newX = mouseEvent.getX();
                    newY = mouseEvent.getY();
                    xDiff = newX - x;
                    yDiff = newY - y;
                    x = newX;
                    y = newY;
                    engine.moveShape(xDiff, yDiff);
                    canvasPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e){
                super.mouseEntered(e);
                canvasPanel.setCursor(engine.containsSelected(e.getPoint())?
                        Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR) :
                        Cursor.getDefaultCursor());
            }
        };

        ActionListener modeChanged = e -> {
            actionCommand = e.getActionCommand();
            if(actionCommand.equals("Select")){
                canvasPanel.setCursor(Cursor.getDefaultCursor());
                canvasPanel.addMouseListener(selector);
                canvasPanel.addMouseMotionListener(selector);
                canvasPanel.removeMouseMotionListener(shaper);
                canvasPanel.removeMouseListener(shaper);
            } else {
                canvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                canvasPanel.addMouseMotionListener(shaper);
                canvasPanel.addMouseListener(shaper);
                canvasPanel.removeMouseListener(selector);
                canvasPanel.removeMouseMotionListener(selector);
            }
        };
        selectToggleButton.addActionListener(modeChanged);
        lineToggleButton.addActionListener(modeChanged);
        ellipseToggleButton.addActionListener(modeChanged);
        rectangleToggleButton.addActionListener(modeChanged);
        deleteButton.addActionListener(e -> {
                engine.deleteShape();
                canvasPanel.repaint();
        });
    }
}
