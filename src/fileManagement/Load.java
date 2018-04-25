package fileManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import paint.model.Ellipse;
import paint.model.Polyline;
import paint.model.Shape;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Load {
    ArrayList<Shape> shapes = new ArrayList<>();
    Shape shape;
    Color c;
    public ArrayList loadJSON(String fileName) {
        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader(fileName));
            JSONObject objfile = (JSONObject) object;
            JSONArray objArr = (JSONArray) objfile.get("shapes");
            Iterator<JSONObject> iterator = objArr.iterator();


            while (iterator.hasNext()) {

                JSONObject obj = iterator.next();
                JSONArray colorArray =(JSONArray) obj.get("color");
                JSONArray fillcolorArray =(JSONArray) obj.get("fillcolor");


                if (obj.get("type").equals("Ellipse")) {
                    int x = Integer.parseInt(obj.get("X").toString());
                    int y = Integer.parseInt(obj.get("Y").toString());
                    int width = Integer.parseInt(obj.get("Width").toString());
                    int height = Integer.parseInt(obj.get("Height").toString());
                    shape = new Ellipse(x, y,width,height);
                    Color color = new Color(Integer.parseInt(colorArray.get(0).toString())
                            ,Integer.parseInt(colorArray.get(1).toString())
                    ,Integer.parseInt(colorArray.get(2).toString())
                            ,Integer.parseInt(colorArray.get(3).toString()));

                    Color fillcolor = new Color(Integer.parseInt(fillcolorArray.get(0).toString())
                            ,Integer.parseInt(fillcolorArray.get(1).toString())
                    ,Integer.parseInt(fillcolorArray.get(2).toString())
                            ,Integer.parseInt(fillcolorArray.get(3).toString()));
                    shape.setColor(color);
                    shape.setFillColor(fillcolor);
                }
                else{
                    JSONArray xArr = (JSONArray) obj.get("X");
                    JSONArray yArr = (JSONArray) obj.get("Y");
                    int[] x = new int[xArr.size()];
                    int[] y = new int[yArr.size()];
                    for (int i = 0; i < xArr.size(); i++) {
                        x[i] = Integer.parseInt(xArr.get(i).toString());
                        y[i] = Integer.parseInt(yArr.get(i).toString());
                    }
                    shape = new Polyline(x,y);
                    Color color = new Color(Integer.parseInt(colorArray.get(0).toString())
                            ,Integer.parseInt(colorArray.get(1).toString())
                            ,Integer.parseInt(colorArray.get(2).toString())
                            ,Integer.parseInt(colorArray.get(3).toString()));

                    Color fillcolor = new Color(Integer.parseInt(fillcolorArray.get(0).toString())
                            ,Integer.parseInt(fillcolorArray.get(1).toString())
                            ,Integer.parseInt(fillcolorArray.get(2).toString())
                            ,Integer.parseInt(fillcolorArray.get(3).toString()));
                    shape.setColor(color);
                    shape.setFillColor(fillcolor);
                }

                shapes.add(shape);
            }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ParseException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
        return shapes;
    }

}
