package paint.gui;

import paint.controller.Engine;
import paint.controller.ShapeFactory;
import paint.model.Ellipse;
import paint.model.Polyline;
import paint.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

public class Window {
	private JToggleButton lineToggleButton;
	private JToggleButton rectangleToggleButton;
	private JToggleButton ellipseToggleButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	public JPanel contentPane;
    static private Engine engine = new Engine();
    private Canvas canvasPanel;
    private JToggleButton selectToggleButton;
    private JButton deleteButton;
    private JButton fillColorButton;
    private JButton cloneButton;
    private JButton undoButton;
    private JButton redoButton;
    private JToggleButton squareToggleButton;
    private JToggleButton circleToggleButton;
    private JToggleButton triangleToggleButton;
    private JButton saveButton;
    private JButton loadButton;
    private String actionCommand;
    private int startX, startY;
    String fileName;
    ShapeFactory shapeFactory = new ShapeFactory();


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
            paint.model.Shape newShape = null;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX = e.getX();
                startY = e.getY();
            };

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int endX = e.getX();
                int endY = e.getY();
                if(newShape != null)
                    engine.removeLastShape();
                if(actionCommand.equals("Line"))
                    newShape = shapeFactory.getPolyline(new int[]{startX, endX}, new int[]{startY, endY});
                else if(actionCommand.equals("Ellipse") || actionCommand.equals("Circle") ) {
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
                    if(actionCommand.equals("Circle")) {
                        int width = endX - leftX;
                        int height = endY - topY;
                        if(width > height)
                            endX = (endY - topY) + leftX;
                        else
                            endY = (endX - leftX) + topY;

                    }
                    newShape = shapeFactory.getEllipse(leftX, topY, endX-leftX, endY - topY);
                }else if(actionCommand.equals("Rectangle")) {
                    newShape = shapeFactory.getPolyline(new int[]{startX, endX, endX, startX},
                            new int[]{startY, startY, endY, endY});
                }else if(actionCommand.equals("Square")) {

                    if(endX - startX < endY - startY){
                        endY = (endX - startX) + startY;
                    }else if(endX - startX > endY - startY){
                        endX = (endY - startY) + startX;
                    }
                    newShape = shapeFactory.getPolyline(new int[]{startX, endX, endX, startX},
                            new int[]{startY, startY, endY, endY});
                }else if(actionCommand.equals("Triangle")){
                    newShape = shapeFactory.getPolyline(new int[]{startX,endX , startX},
                            new int[]{startY, endY, endY});
                }else
                    throw new UnsupportedOperationException();
                newShape.setColor(brushColor);
                newShape.setFillColor(fillColor);
                engine.addShape(newShape);
                canvasPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                if(newShape != null){
                    engine.checkpointAdd();
                }
                undoButton.setEnabled(true);
                redoButton.setEnabled(false);
                newShape = null;
            }
        };

        MouseAdapter selector = new MouseAdapter() {
            int oldX, oldY, tmpX, tmpY, cursorMode;
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                engine.selectShape(e.getPoint());
                boolean selectionExists = engine.selectionExists();
                deleteButton.setEnabled(selectionExists);
                cloneButton.setEnabled(selectionExists);
                canvasPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                oldX = e.getX();
                oldY = e.getY();
                tmpX = oldX;
                tmpY = oldY;
                cursorMode = engine.cursorMode(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                if(engine.selectionExists()) {
                    if (cursorMode == 1) {
                        int newX, newY, yDiff, xDiff;
                        newX = mouseEvent.getX();
                        newY = mouseEvent.getY();
                        xDiff = newX - tmpX;
                        yDiff = newY - tmpY;
                        tmpX = newX;
                        tmpY = newY;
                        engine.moveShape(xDiff, yDiff, mouseEvent.getPoint());
                        canvasPanel.repaint();
                    } else {
                        engine.resize(mouseEvent.getPoint());
                        canvasPanel.repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e){
                super.mouseEntered(e);
                Cursor cursor;
                switch(engine.cursorMode(e.getPoint())){
                    case 1:
                        cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
                        break;
                    case 2:
                        cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                        break;
                    default:
                        cursor = Cursor.getDefaultCursor();
                }
                canvasPanel.setCursor(cursor);
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(cursorMode == 2) {
                    engine.checkpointResize(e.getX(), e.getY(), oldX, oldY);
                }else if(cursorMode == 1){
                    engine.checkpointMove(e.getX() - oldX, e.getY() - oldY);
                }
            }
        };

        ActionListener modeChanged = e -> {
            actionCommand = e.getActionCommand();
            if(actionCommand.equals("Select")){
                canvasPanel.setCursor(Cursor.getDefaultCursor());
                canvasPanel.removeMouseListener(selector);
                canvasPanel.addMouseListener(selector);
                canvasPanel.removeMouseMotionListener(selector);
                canvasPanel.addMouseMotionListener(selector);
                canvasPanel.removeMouseMotionListener(shaper);
                canvasPanel.removeMouseListener(shaper);
            } else {
                canvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                canvasPanel.removeMouseMotionListener(shaper);
                canvasPanel.addMouseMotionListener(shaper);
                canvasPanel.removeMouseListener(shaper);
                canvasPanel.addMouseListener(shaper);
                canvasPanel.removeMouseListener(selector);
                canvasPanel.removeMouseMotionListener(selector);
                engine.unselect();
                canvasPanel.repaint();
            }
        };
        selectToggleButton.addActionListener(modeChanged);
        lineToggleButton.addActionListener(modeChanged);
        ellipseToggleButton.addActionListener(modeChanged);
        rectangleToggleButton.addActionListener(modeChanged);
        squareToggleButton.addActionListener(modeChanged);
        circleToggleButton.addActionListener(modeChanged);
        triangleToggleButton.addActionListener(modeChanged);
        deleteButton.addActionListener(e -> {
                engine.deleteShape();
                canvasPanel.repaint();
                deleteButton.setEnabled(false);
                cloneButton.setEnabled(false);
        });
        cloneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Shape shape = engine.cloneShape();
                if(shape != null) {
                    shape.setColor(brushColor);
                    shape.setFillColor(fillColor);
                }
                canvasPanel.repaint();
            }
        });
        canvasPanel.addMouseListener(selector);
        canvasPanel.addMouseMotionListener(selector);

        undoButton.addActionListener(e -> {
            engine.undo();
            undoButton.setEnabled(engine.hasUndo());
            redoButton.setEnabled(true);
            canvasPanel.repaint();
        });

        redoButton.addActionListener(e -> {
            engine.redo();
            redoButton.setEnabled(engine.hasRedo());
            canvasPanel.repaint();
            undoButton.setEnabled(true);
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                FileDialog fd = new FileDialog(frame,"choose file",FileDialog.SAVE);
                fd.setFilenameFilter((file, s) -> s.endsWith(".svg") || s.endsWith(".json"));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(200,200);
                fd.setVisible(true);
                if(fd.getFile() != null) {
                    fileName = Paths.get(fd.getDirectory(), fd.getFile()).toString();
                    engine.save(fileName, canvasPanel.getBackground());
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                FileDialog fd = new FileDialog(frame,"choose file",FileDialog.LOAD);
                fd.setFilenameFilter((file, s) -> s.endsWith(".svg") || s.endsWith(".json"));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(200,200);
                fd.setVisible(true);
                if(fd.getFile() != null) {
                    fileName = Paths.get(fd.getDirectory(), fd.getFile()).toString();
                    canvasPanel.setBackground(engine.load(fileName));
                    redoButton.setEnabled(false);
                    undoButton.setEnabled(false);
                    canvasPanel.repaint();
                }
            }
        });


        KeyAdapter delKeyAdapter = new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(engine.selectionExists())
                    if (keyEvent.getKeyChar() == '\u007F')
                        deleteButton.doClick();
            }
        };

        selectToggleButton.addKeyListener(delKeyAdapter);
    }
}
