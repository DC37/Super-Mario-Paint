package smp.clipboard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import smp.components.Values;
import smp.stateMachine.StateMachine;

/**
 * Rubber band is drawn by the mouse. Click and drag to create a rectangle that
 * will overlay a region on top of the composition. This rectangular region will
 * grab all notes in that region.
 *
 * Adapted from https://github.com/varren/JavaFX-Resizable-Draggable-Node
 */
public class RubberBand extends Rectangle {

    private static final Color FILL_TRANSPARENT = new Color(0.5, 0.5, 0.5, 0.5);

    private double xOrigin;
    private double yOrigin;

    /* Allow resizing by dragging the edges of the rectangle, will prevent conventional resizing */
    private boolean postResizable = false;
 
	private double lineMinBound = 0;
	private double lineMaxBound = 0;
	private double positionMinBound = 0;
	private double positionMaxBound = 0;
	private double volumeYMaxCoord = 0;
    private double lineSpacing = 0;
    private double positionSpacing = 0;
	private double marginVertical = 0;
	private double marginHorizontal = 0;
	private int scrollOffset = 0;
	private int originLine = 0;
	
	private Text outsideBoundText = new Text();

    public RubberBand() {
        super();
        this.setFill(FILL_TRANSPARENT);
//        makeResizable(this);
        outsideBoundText.setFill(Color.RED);
    }
    
    public void setScrollBarResizable(Slider slider) {
    	
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {		
				
//	        	Pane rubberBandLayer = (Pane) getParent();
//	        	if(rubberBandLayer == null)
//	        		return;
				
				//only xOrigin will move so we need to get the other x bound that will remain the same	
			    double sameEndX = 0;
				if (getTranslateX() < xOrigin) {
					sameEndX = getTranslateX();
				} else {// if (rbRef.getTranslateX() == xOrigin)
					sameEndX = getTranslateX() + getWidth();
				}
        		
				int change = newVal.intValue() - oldVal.intValue();
				applyScroll(change);

//				resizeBand(sameEndX, getTranslateY()); doesn't work for some reason...
				//do this instead
		        if (sameEndX >= xOrigin) {
		            setTranslateX(xOrigin);
		            setWidth(sameEndX - xOrigin);
		        } else {
		            setTranslateX(sameEndX);
		            setWidth(xOrigin - sameEndX);
		        }
		        
		        applyBoundsText();
		        
			}

        });
    }

	/**
	 * add the out of bounds text in the rubberBandLayer if bound goes off the
	 * window. remove the out of bounds text if in the window. this tells where
	 * the line bound is.
	 */
	public void applyBoundsText() {
		Pane rubberBandLayer = (Pane) getParent();
    	if(rubberBandLayer == null)
    		return;
    	
        //out of bounds line text
        if(scrollOffset + originLine < 0 || Values.NOTELINES_IN_THE_WINDOW < scrollOffset + originLine){
			if (!rubberBandLayer.getChildren().contains(outsideBoundText)) {
				rubberBandLayer.getChildren().add(outsideBoundText);

				int outsideBoundLineNum = scrollOffset < 0 ? (StateMachine.getMeasureLineNum() - 1) / 4 + 1
						: (StateMachine.getMeasureLineNum() + Values.NOTELINES_IN_THE_WINDOW) / 4 + 1;
				outsideBoundText.setText("" + outsideBoundLineNum);
				
				double transX = scrollOffset + originLine < 0 ? getTranslateX() + 10 : getTranslateX() + getWidth() - 10;
				outsideBoundText.setTranslateX(transX);
				outsideBoundText.setTranslateY(getTranslateY() + getHeight());
			}

        } else {
        	rubberBandLayer.getChildren().remove(outsideBoundText);
        }
    }
    /**
     * Update scrollOffset with change. Then update xOrigin.
     * 
     * @param change scroll delta
     */
    public void applyScroll(int change) {
		//note -change because xOrigin moves other way
		int begPos = scrollOffset + originLine;
		int endPos = scrollOffset - change + originLine;
		int xOriginChange = Math.max(0, Math.min(endPos, Values.NOTELINES_IN_THE_WINDOW))
				- Math.max(0, Math.min(begPos, Values.NOTELINES_IN_THE_WINDOW));
		
		xOrigin = Math.max(lineMinBound, Math.min(xOrigin + lineSpacing * xOriginChange, lineMaxBound));
		
		//if xOrigin moves out of bounds still, set min and max bound positioning
		if(xOriginChange == 0) {
			if(Values.NOTELINES_IN_THE_WINDOW < endPos)
				xOrigin = lineMaxBound;
			else if(endPos < 0)
				xOrigin = lineMinBound;
		}
		
		scrollOffset -= change;
    }
    
    /**
	 * First, call this to begin drawing the rubber band. Sets rubber band
	 * visible.
	 *
	 * @param x
	 *            x-coord to begin rectangle shape at
	 * @param y
	 *            y-coord to begin rectangle shape at
	 */
	public void begin(double x, double y) {
		// if (postResizable) {
		// return;
		// }

//		System.out.println("rb:" + lineMinBound);
//		System.out.println(lineMaxBound);
//		System.out.println(positionMinBound);
//		System.out.println(positionMaxBound);
//		System.out.println(marginVertical);
//		System.out.println(marginHorizontal);

		if (x < lineMinBound - marginHorizontal || x > lineMaxBound + marginHorizontal
				|| y < positionMinBound - marginVertical
				|| y > Math.max(volumeYMaxCoord, positionMaxBound + marginVertical)) {
			return;
		}  
		
        this.setWidth(0);
        this.setHeight(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setVisible(true);
        
        xOrigin = x;
        yOrigin = y;

        scrollOffset = 0;    
        originLine = getLineBegin();
    }

	/**
	 * Second call this to redraw the rubber band to a new size.
	 * 
	 * Note: this method isn't called resize because the JavaFX API uses a
	 * function of that name.
	 *
	 * @param x
	 *            x-coord to resize to
	 * @param y
	 *            y-coord to resize to
	 */
	public void resizeBand(double x, double y) {
		// if (postResizable) {
//            return;
//        }
        if (x > lineMaxBound + marginHorizontal) {
            x = lineMaxBound + marginHorizontal;
        } else if (x < lineMinBound - marginHorizontal) {
            x = lineMinBound - marginHorizontal;
        }
        if (y > Math.max(volumeYMaxCoord, positionMaxBound + marginVertical)) {
            y = Math.max(volumeYMaxCoord, positionMaxBound + marginVertical);
        } else if (y < positionMinBound - marginVertical) {
            y = positionMinBound - marginVertical;
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

        Pane rubberBandLayer = (Pane)this.getParent();
        rubberBandLayer.getChildren().remove(outsideBoundText);
    }

//    /**
//     * Finally call this to end drawing the rubber band BUT keep the rubber band
//     * visible and allow resizing by dragging the edges of the rectangle.
//     */
//    public void endButKeepUsable() {
//        setPostResizable(true);
//    }

    /**
     * 
     * @return beginning line of rubberband relative to the current frame of lines
     */
	public int getLineBegin() {

		return scrollOffset < 0 ? getLineBeginRaw() + scrollOffset + originLine : getLineBeginRaw();
	}

	private int getLineBeginRaw() {

		double bandMinX = this.getTranslateX();
		
		if (bandMinX < lineMinBound + lineSpacing / 2) {
			return 0;
		} else if (bandMinX > lineMaxBound - lineSpacing / 2) {
			return Values.NOTELINES_IN_THE_WINDOW;
		} else {

			double firstLineX = (lineMinBound + lineSpacing / 2);
			return (int) ((bandMinX - firstLineX) / lineSpacing) + 1;
		}
	}

    /**
     * 
     * @return ending line of rubberband relative to the current frame of lines
     */
	public int getLineEnd() {
		return scrollOffset > 0 ? getLineEndRaw() + scrollOffset + originLine : getLineEndRaw();
	}
	
	private int getLineEndRaw() {

		double bandMaxX = this.getTranslateX() + this.getWidth();
		if (bandMaxX < lineMinBound + lineSpacing / 2) {
			return -1;
		} else if (bandMaxX > lineMaxBound - lineSpacing / 2) {
			return Values.NOTELINES_IN_THE_WINDOW - 1;
		} else {

			double firstLineX = (lineMinBound + lineSpacing / 2);
			return (int) ((bandMaxX - firstLineX) / lineSpacing);
		}
	}

    public int getPositionBegin() {  
    	
    	double bandBottomPosY = this.getTranslateY() + this.getHeight();
        if (bandBottomPosY < positionMinBound + positionSpacing / 2) {
            return Values.NOTES_IN_A_LINE - 1;
        } else if (bandBottomPosY > positionMaxBound - positionSpacing / 2) {
            return 0;//-1;
        } else {
        	
        	double firstPosY = positionMinBound;// + positionSpacing / 2;
        	//this is half because positions overlap one another
        	double positionHalfSpacing = positionSpacing / 2;
            return Values.NOTES_IN_A_LINE - (int)((bandBottomPosY - firstPosY) / positionHalfSpacing);// - 1;
        }
    }

	public int getPositionEnd() {
		
		double bandTopPosY = this.getTranslateY();
		if (bandTopPosY <  positionMinBound + positionSpacing / 2) {
			return Values.NOTES_IN_A_LINE - 1;
		} else if (bandTopPosY > positionMaxBound - positionSpacing / 2) {
			return 0;//0;
		} else {
        	
        	double firstPosY = positionMinBound;// + positionSpacing / 2;
        	//this is half because positions overlap one another
        	double positionHalfSpacing = positionSpacing / 2;
            return Values.NOTES_IN_A_LINE - (int)((bandTopPosY - firstPosY) / positionHalfSpacing) - 1;
		}
	}
	
	public void setLineMinBound(double x) {
		lineMinBound = x;
	}

	public void setLineMaxBound(double x) {
		lineMaxBound = x;
	}

	public void setPositionMinBound(double y) {
		positionMinBound = y;
	}

	public void setPositionMaxBound(double y) {
		positionMaxBound = y;
	}

	public void setVolumeYMaxCoord(double y) {
		volumeYMaxCoord = y;
	}

	public void setLineSpacing(double dx) {
		lineSpacing = dx;
	}

	public void setPositionSpacing(double dy) {
		positionSpacing = dy;
	}

	public void setMarginVertical(double m) {
		marginVertical = m;
	}
	
	public void setMarginHorizontal(double m) {
		marginHorizontal = m;
	}
}
