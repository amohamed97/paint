package paint.controller;

import paint.model.Ellipse;
import paint.model.Polyline;
import paint.model.Shape;

import java.util.ArrayList;

public class ShapeFactory {

    public ShapeFactory(){
        return;
    }

    public Polyline getPolyline(int[] x, int[] y){
        return (new Polyline(x,y));
    }

    public Ellipse getEllipse(int x, int y,int width ,int height){
        return (new Ellipse(x,y,width,height));
    }

}
