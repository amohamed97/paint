package fileManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import paint.model.Shape;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;

public class Save {
    ArrayList<Shape> shapes;
    JSONArray objArr;

    public Save(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public void saveJSON( String fileName) {
        JSONObject obj1 = new JSONObject();
        objArr = new JSONArray();
        try(FileWriter file = new FileWriter(fileName)){
            for (int i = 0; i < shapes.size(); i++) {
                Shape shape = shapes.get(i);
                JSONObject obj = new JSONObject();
                JSONArray colorArr = new JSONArray();
                JSONArray fillColorArr = new JSONArray();
                obj = shape.saveJSON();

                Color color = shape.getColor();
                colorArr.add(color.getRed());
                colorArr.add(color.getBlue());
                colorArr.add(color.getGreen());
                colorArr.add(color.getAlpha());
                obj.put("color",colorArr);

                Color fillcolor = shape.getFillColor();
                fillColorArr.add(fillcolor.getRed());
                fillColorArr.add(fillcolor.getBlue());
                fillColorArr.add(fillcolor.getGreen());
                fillColorArr.add(fillcolor.getAlpha());
                obj.put("fillcolor",fillColorArr);

                objArr.add(obj);
            }
            obj1.put("shapes",objArr);
            file.write(obj1.toJSONString());
        } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
