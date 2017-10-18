package smp.clipboard;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import smp.components.Values;

/**
 * Rubber band is drawn by the mouse. Click and drag to create a rectangle that
 * will overlay a region on top of the composition. This rectangular region will
 * grab all notes in that region.
 *
 * Adapted from https://github.com/varren/JavaFX-Resizable-Draggable-Node
 */
public class RubberBand extends Rectangle {

    private static final Color FILL_TRANSPARENT = new Color(0.5, 0.5, 0.5, 0.5);

    public interface OnDragResizeEventListener {

        void onResize(Node node, double x, double y, double h, double w);
    }

    private static final OnDragResizeEventListener defaultListener = new OnDragResizeEventListener() {

        @Override
        public void onResize(Node node, double x, double y, double h, double w) {
            /*
            // TODO find generic way to get parent width and height of any node
            // can perform out of bounds check here if you know your parent size
            if (w > width - x) w = width - x;
            if (h > height - y) h = height - y;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
             */
            setNodeSize(node, x, y, h, w);
        }

        private void setNodeSize(Node node, double x, double y, double h, double w) {
            node.setLayoutX(x);
            node.setLayoutY(y);
            // TODO find generic way to set width and height of any node
            // here we cant set height and width to node directly.
            // or i just cant find how to do it,
            // so you have to wright resize code anyway for your Nodes...
            //something like this
            if (node instanceof Canvas) {
                ((Canvas) node).setWidth(w);
                ((Canvas) node).setHeight(h);
            } else if (node instanceof Rectangle) {
                ((Rectangle) node).setWidth(w);
                ((Rectangle) node).setHeight(h);
            }
        }
    };

    public static enum S {
        DEFAULT,
        NW_RESIZE,
        SW_RESIZE,
        NE_RESIZE,
        SE_RESIZE,
        E_RESIZE,
        W_RESIZE,
        N_RESIZE,
        S_RESIZE;
    }

    private double clickX, clickY, nodeX, nodeY, nodeH, nodeW;

    private S state = S.DEFAULT;

    private Node node;
    private OnDragResizeEventListener listener = defaultListener;

    private static final int MARGIN = 8;
    private static final double MIN_W = 1;
    private static final double MIN_H = 1;

    //use this until the rubberBandLayer's dimensions are linked
	private static final int HEIGHT_DEFAULT = 360;
	private static final int WIDTH_DEFAULT = 798;

    private double xOrigin;
    private double yOrigin;

    /* Allow resizing by dragging the edges of the rectangle, will prevent conventional resizing */
    private boolean postResizable;

    public RubberBand() {
        super();
        this.setFill(FILL_TRANSPARENT);
//        makeResizable(this);
        postResizable = false;
    }

    /**
     * First, call this to begin drawing the rubber band. Sets rubber band
     * visible.
     *
     * @param x x-coord to begin rectangle shape at
     * @param y y-coord to begin rectangle shape at
     */
    public void begin(double x, double y) {
//        if (postResizable) {
//            return;
//        }

//        if (x > Constants.WIDTH_DEFAULT || y > Constants.HEIGHT_DEFAULT) {
//            return;
//        }

        this.setWidth(0);
        this.setHeight(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setVisible(true);

        xOrigin = x;
        yOrigin = y;
        
    }

    /**
     * Second call this to redraw the rubber band to a new size.
     *
     * @param x x-coord to resize to
     * @param y y-coord to resize to
     */
    public void resize(double x, double y) {
//        if (postResizable) {
//            return;
//        }
        if (x > WIDTH_DEFAULT) {
            x = WIDTH_DEFAULT;
        } else if (x < 0) {
            x = 0;
        }
        if (y > HEIGHT_DEFAULT) {
            y = HEIGHT_DEFAULT;
        } else if (y < 0) {
            y = 0;
        }

        if (x >= xOrigin) {
            this.setTranslateX(xOrigin);
            this.setWidth(x - xOrigin);
        } else {
            this.setTranslateX(x);
            this.setWidth(xOrigin - x);
        }

        if (y >= yOrigin) {
            this.setTranslateY(yOrigin);
            this.setHeight(y - yOrigin);
        } else {
            this.setTranslateY(y);
            this.setHeight(yOrigin - y);
        }
    }
    
    /**
     * Second call this to redraw the rubber band to a new size. Alters only x 
     * dimension.
     *
     * @param x x-coord to resize to
     */
    @Deprecated
    public void resizeX(double x){
        
//        if (x > Constants.WIDTH_DEFAULT) {
//            x = Constants.WIDTH_DEFAULT;
//        } else if (x < 0) {
//            x = 0;
//        }

        if (x >= xOrigin) {
            this.setTranslateX(xOrigin);
            this.setWidth(x - xOrigin);
        } else {
            this.setTranslateX(x);
            this.setWidth(xOrigin - x);
        }
    }
    
    /**
     * Second call this to redraw the rubber band to a new size. Alters only y 
     * dimension.
     *
     * @param y y-coord to resize to
     */
    @Deprecated
    public void resizeY(double y){

//        if (y > Constants.HEIGHT_DEFAULT) {
//            y = Constants.HEIGHT_DEFAULT;
//        } else if (y < 0) {
//            y = 0;
//        }
        
        if (y >= yOrigin) {
            this.setTranslateY(yOrigin);
            this.setHeight(y - yOrigin);
        } else {
            this.setTranslateY(y);
            this.setHeight(yOrigin - y);
        }
        
    }
    
    /**
     * Finally call this to end drawing the rubber band and hide it. Sets rubber
     * band invisible.
     */
    public void end() {
        this.setVisible(false);
    }

    /**
     * Finally call this to end drawing the rubber band BUT keep the rubber band
     * visible and allow resizing by dragging the edges of the rectangle.
     */
    public void endButKeepUsable() {
        setPostResizable(true);
    }

    //hard coded for now
    public int getLineBegin() {
        //x, 32px is exactly at the line
        /* lineOffsetX is indices 0-31 of the row */
        int lineOffsetX = 0;
        if (this.getTranslateX() < 128 + 32) {
            lineOffsetX = 0;
        } else if (this.getTranslateX() > 784 - 32) {
            lineOffsetX = Values.NOTELINES_IN_THE_WINDOW;
        } else {
            lineOffsetX = ((int)this.getTranslateX() - (128 + 32)) / 64 + 1;
        }
        
        return lineOffsetX;
    }

    public int getLineEnd() {

        int lineOffsetX = 0;
        if ((this.getTranslateX() + this.getWidth()) < 128 + 32) {
            lineOffsetX = -1;
        } else if ((this.getTranslateX() + this.getWidth()) > 784 - 32) {
            lineOffsetX = Values.NOTELINES_IN_THE_WINDOW - 1;
        } else {
            lineOffsetX = ((int)(this.getTranslateX() + this.getWidth()) - (128 + 32)) / 64;
        }
        
        return lineOffsetX;
    }

    public int getPositionBegin() {  
    	
        if ((this.getTranslateY() + this.getHeight()) < 0 + 16) {
            return Values.NOTES_IN_A_LINE - 1;
        } else if ((this.getTranslateY()) > 304 - 16) {
            return 0;//-1;
        } else {
            return Values.NOTES_IN_A_LINE - (((int)(this.getTranslateY() + this.getHeight()) - (0 + 16)) / 16);
        }
    }

	public int getPositionEnd() {
		
		if ((this.getTranslateY()) < 0 + 16) {
			return Values.NOTES_IN_A_LINE;
		} else if ((this.getTranslateY()) > 304 - 16) {
			return 1;//0;
		} else {
			return Values.NOTES_IN_A_LINE - (((int) (this.getTranslateY()) - (0 + 16)) / 16 + 1);
		}
	}
    
    private void setPostResizable(boolean postResizable) {
        this.postResizable = postResizable;
    }

    private RubberBand(Node node, OnDragResizeEventListener listener) {
        this.node = node;
        if (listener != null) {
            this.listener = listener;
        }
    }

    public static void makeResizable(Node node) {
        makeResizable(node, null);
    }

    public static void makeResizable(Node node, OnDragResizeEventListener listener) {
        final RubberBand resizer = new RubberBand(node, listener);

        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }
        });
        node.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }
        });
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    protected void mouseReleased(MouseEvent event) {
        node.setCursor(Cursor.DEFAULT);
        state = S.DEFAULT;
    }

    protected void mouseOver(MouseEvent event) {
        S state = currentMouseState(event);
        Cursor cursor = getCursorForState(state);
        node.setCursor(cursor);
    }

    private S currentMouseState(MouseEvent event) {
        S state = S.DEFAULT;
        boolean left = isLeftResizeZone(event);
        boolean right = isRightResizeZone(event);
        boolean top = isTopResizeZone(event);
        boolean bottom = isBottomResizeZone(event);

        if (left && top) {
            state = S.NW_RESIZE;
        } else if (left && bottom) {
            state = S.SW_RESIZE;
        } else if (right && top) {
            state = S.NE_RESIZE;
        } else if (right && bottom) {
            state = S.SE_RESIZE;
        } else if (right) {
            state = S.E_RESIZE;
        } else if (left) {
            state = S.W_RESIZE;
        } else if (top) {
            state = S.N_RESIZE;
        } else if (bottom) {
            state = S.S_RESIZE;
        }

        return state;
    }

    private static Cursor getCursorForState(S state) {
        switch (state) {
            case NW_RESIZE:
                return Cursor.NW_RESIZE;
            case SW_RESIZE:
                return Cursor.SW_RESIZE;
            case NE_RESIZE:
                return Cursor.NE_RESIZE;
            case SE_RESIZE:
                return Cursor.SE_RESIZE;
            case E_RESIZE:
                return Cursor.E_RESIZE;
            case W_RESIZE:
                return Cursor.W_RESIZE;
            case N_RESIZE:
                return Cursor.N_RESIZE;
            case S_RESIZE:
                return Cursor.S_RESIZE;
            default:
                return Cursor.DEFAULT;
        }
    }

    protected void mouseDragged(MouseEvent event) {

        if (listener != null) {
            double mouseX = parentX(event.getX());
            double mouseY = parentY(event.getY());

            if (state != S.DEFAULT) {
                //resizing
                double newX = nodeX;
                double newY = nodeY;
                double newH = nodeH;
                double newW = nodeW;

                // Right Resize
                if (state == S.E_RESIZE || state == S.NE_RESIZE || state == S.SE_RESIZE) {
                    newW = mouseX - nodeX;
                }
                // Left Resize
                if (state == S.W_RESIZE || state == S.NW_RESIZE || state == S.SW_RESIZE) {
                    newX = mouseX;
                    newW = nodeW + nodeX - newX;
                }

                // Bottom Resize
                if (state == S.S_RESIZE || state == S.SE_RESIZE || state == S.SW_RESIZE) {
                    newH = mouseY - nodeY;
                }
                // Top Resize
                if (state == S.N_RESIZE || state == S.NW_RESIZE || state == S.NE_RESIZE) {
                    newY = mouseY;
                    newH = nodeH + nodeY - newY;
                }

                //min valid rect Size Check
                if (newW < MIN_W) {
                    if (state == S.W_RESIZE || state == S.NW_RESIZE || state == S.SW_RESIZE) {
                        newX = newX - MIN_W + newW;
                    }
                    newW = MIN_W;
                }

                if (newH < MIN_H) {
                    if (state == S.N_RESIZE || state == S.NW_RESIZE || state == S.NE_RESIZE) {
                        newY = newY + newH - MIN_H;
                    }
                    newH = MIN_H;
                }

                listener.onResize(node, newX, newY, newH, newW);
            }
        }
    }

    protected void mousePressed(MouseEvent event) {

        if (isInResizeZone(event)) {
            setNewInitialEventCoordinates(event);
            state = currentMouseState(event);
        } else {
            state = S.DEFAULT;
        }
    }

    private void setNewInitialEventCoordinates(MouseEvent event) {
        nodeX = nodeX();
        nodeY = nodeY();
        nodeH = nodeH();
        nodeW = nodeW();
        clickX = event.getX();
        clickY = event.getY();
    }

    private boolean isInResizeZone(MouseEvent event) {
        return isLeftResizeZone(event) || isRightResizeZone(event)
                || isBottomResizeZone(event) || isTopResizeZone(event);
    }

    private boolean isLeftResizeZone(MouseEvent event) {
        return intersect(0, event.getX());
    }

    private boolean isRightResizeZone(MouseEvent event) {
        return intersect(nodeW(), event.getX());
    }

    private boolean isTopResizeZone(MouseEvent event) {
        return intersect(0, event.getY());
    }

    private boolean isBottomResizeZone(MouseEvent event) {
        return intersect(nodeH(), event.getY());
    }

    private boolean intersect(double side, double point) {
        return side + MARGIN > point && side - MARGIN < point;
    }

    private double parentX(double localX) {
        return nodeX() + localX;
    }

    private double parentY(double localY) {
        return nodeY() + localY;
    }

    private double nodeX() {
//        return node.getBoundsInParent().getMinX();
        return this.getBoundsInParent().getMinX();
    }

    private double nodeY() {
//        return node.getBoundsInParent().getMinY();
        return this.getBoundsInParent().getMinY();
    }

    private double nodeW() {
        return node.getBoundsInParent().getWidth();
    }

    private double nodeH() {
        return node.getBoundsInParent().getHeight();
    }
    
    /* The following helper functions are also in StaffInstrumentEventHandler. */
    
//    /**
//     * Line will be toward the inside of the rubber band.
//     * 
//     * @param x mouse pos for entire scene
//     * @param y mouse pos for entire scene
//     * @return line based on x and y coord
//     */
//    private int getLine(double x, double y){
//
//    	if(x < 122 || x > Constants.WIDTH_DEFAULT - 48 || !zby(y))//122 is arbitrary, 48 is arbitrary
//            return -1;
//    	return (((int)x - 122) / Constants.LINE_SPACING) + 
//                ((int)y / Constants.ROW_HEIGHT_TOTAL) * Constants.LINES_IN_A_ROW;
//    }
//    
//    /**
//     * 
//     * @param y mouse pos for entire scene
//     * @return note position based on y coord
//     */
//    private int getPosition(double y){
//    	if(!zby(y))
//            return -1;
//    	return (((int)y % Constants.ROW_HEIGHT_TOTAL - 10) / 16);//10 is arbitrary
//    }    
//    
//    /* If valid y */
//    private boolean zby(double y){
//        return y % Constants.ROW_HEIGHT_TOTAL <= Constants.ROW_HEIGHT_NOTES;
//    }
//    
//    /**
//     * Like getLine() but does not check zby().
//     */
//    private int getLineSimple(double x, double y){
//
//    	if(x < 122 || x > Constants.WIDTH_DEFAULT - 48)//122 is arbitrary, 48 is arbitrary
//            return -1;
//    	return (((int)x - 122) / Constants.LINE_SPACING) + 
//                ((int)y / Constants.ROW_HEIGHT_TOTAL) * Constants.LINES_IN_A_ROW;
//    }
//    
//    public int getLineBeginVol() {
//        if (zby(this.getTranslateY())) {
//            return getLineBegin();
//        } else {
//            return getLineBegin() - Constants.LINES_IN_A_ROW;
//        }
//    }
//
//    public int getLineEndVol() {
//        if (zby(this.getTranslateY() + this.getHeight())) {
//            return getLineEnd() - Constants.LINES_IN_A_ROW;
//        } else {
//            return getLineEnd();
//        }
//    }
}
