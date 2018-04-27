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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
public class JSONFileHandling implements FileHandler{

    ArrayList<Shape> shapes;
    JSONArray shapesObjectArr;

    public JSONFileHandling(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    private JSONArray colorJSONArray(Color color){
        JSONArray array = new JSONArray();
        array.add(color.getRed());
        array.add(color.getGreen());
        array.add(color.getBlue());
        array.add(color.getAlpha());
        return array;
    }

    @Override
    public void save(String fileName, Color background) {
        JSONObject rootObj = new JSONObject();
        shapesObjectArr = new JSONArray();
        try(FileWriter file = new FileWriter(fileName)){
            rootObj.put("background", colorJSONArray(background));
            for (int i = 0; i < shapes.size(); i++) {
                Shape shape = shapes.get(i);
                JSONObject obj = shape.saveJSON();

                obj.put("color",colorJSONArray(shape.getColor()));
                obj.put("fillcolor",colorJSONArray(shape.getFillColor()));

                shapesObjectArr.add(obj);
            }
            rootObj.put("shapes", shapesObjectArr);
            file.write(rootObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color loadJSONColor(Object obj){
        JSONArray arr = (JSONArray) obj;
        return new Color(Integer.parseInt(arr.get(0).toString())
                ,Integer.parseInt(arr.get(1).toString())
                ,Integer.parseInt(arr.get(2).toString())
                ,Integer.parseInt(arr.get(3).toString()));
    }

    @Override
    public Color load(String fileName) {
        shapes.clear();
        JSONParser parser = new JSONParser();
        Color background = new Color(0, 0, 0, 0);
        try {
            Object object = parser.parse(new FileReader(fileName));
            JSONObject objfile = (JSONObject) object;
            background = loadJSONColor(objfile.get("background"));
            JSONArray objArr = (JSONArray) objfile.get("shapes");
            Iterator<JSONObject> iterator = objArr.iterator();


            while (iterator.hasNext()) {
                Shape shape;

                JSONObject obj = iterator.next();
                JSONArray colorArray = (JSONArray) obj.get("color");
                JSONArray fillcolorArray = (JSONArray) obj.get("fillcolor");


                if (obj.get("type").equals("Ellipse")) {
                    int x = Integer.parseInt(obj.get("X").toString());
                    int y = Integer.parseInt(obj.get("Y").toString());
                    int width = Integer.parseInt(obj.get("Width").toString());
                    int height = Integer.parseInt(obj.get("Height").toString());
                    shape = new Ellipse(x, y, width, height);
                } else {
                    JSONArray xArr = (JSONArray) obj.get("X");
                    JSONArray yArr = (JSONArray) obj.get("Y");
                    int[] x = new int[xArr.size()];
                    int[] y = new int[yArr.size()];
                    for (int i = 0; i < xArr.size(); i++) {
                        x[i] = Integer.parseInt(xArr.get(i).toString());
                        y[i] = Integer.parseInt(yArr.get(i).toString());
                    }
                    shape = new Polyline(x, y);
                }

                shape.setColor(loadJSONColor(obj.get("color")));
                shape.setFillColor(loadJSONColor(obj.get("fillcolor")));

                shapes.add(shape);
            }
        }
        catch (FileNotFoundException e) { e.printStackTrace();}
        catch (IOException e) { e.printStackTrace();}
        catch (ParseException e) { e.printStackTrace();}
        catch (Exception e) { e.printStackTrace();}

        return background;
    }


}
