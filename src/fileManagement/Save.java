package fileManagement;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import paint.model.Shape;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Save {
    ArrayList<Shape> shapes;
    JSONArray shapesObjectArr;

    public Save(ArrayList<Shape> shapes) {
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

    public void saveJSON(String fileName, Color background) {
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

    public void saveSVG(String filename, Color background){
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        Document doc = SVGDOMImplementation.getDOMImplementation().createDocument(
                SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element root = doc.getDocumentElement();
        root.setAttribute("viewport-fill", Shape.colorRGBA(background));

        shapes.forEach(s -> root.appendChild(s.saveSVG(doc)));
        SVGTranscoder transcoder = new SVGTranscoder();
        TranscoderInput in = new TranscoderInput(doc);
        try(FileWriter file = new FileWriter(filename)){
            TranscoderOutput out = new TranscoderOutput(file);
            transcoder.transcode(in, out);
        }catch(IOException | TranscoderException e){
            e.printStackTrace();
        }
    }
}
