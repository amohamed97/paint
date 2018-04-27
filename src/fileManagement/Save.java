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

    public void saveSVG(String filename){
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        Document doc = SVGDOMImplementation.getDOMImplementation().createDocument(
                SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element root = doc.getDocumentElement();
        shapes.forEach(s -> root.appendChild(s.saveSVG(doc)));

        SVGTranscoder transcoder = new SVGTranscoder();
        TranscoderInput in = new TranscoderInput(doc);
        try{
            TranscoderOutput out = new TranscoderOutput(new FileWriter(filename));
            transcoder.transcode(in, out);
        }catch(IOException | TranscoderException e){
            e.printStackTrace();
        }
    }
}
