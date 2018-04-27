package fileManagement;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMEllipseElement;
import org.apache.batik.dom.svg.SVGOMPolygonElement;
import org.apache.batik.util.XMLResourceDescriptor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;
import paint.model.Ellipse;
import paint.model.Polyline;
import paint.model.Shape;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

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

    private Color getColor(String rgba){
        final Pattern pat = Pattern.compile("rgba\\((\\d+), *(\\d+), *(\\d+), *(\\d+)\\)");
        Matcher matcher = pat.matcher(rgba);
        matcher.find();
        return new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
    }

    public ArrayList loadSVG(String filename){
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        try{
            Element root = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName()).createDocument(
                    Paths.get(filename).toUri().toString()).getDocumentElement();
            NodeList nodes = root.getChildNodes();
            final int nodesSize = nodes.getLength();
            for(int i = 0; i < nodesSize; ++i){
                Node node = nodes.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    SVGOMElement elem = (SVGOMElement) node;
                    Shape shape;
                    final String elemName = elem.getTagName();
                    if (elemName.equals("ellipse")) {
                        SVGOMEllipseElement ellipseElement = (SVGOMEllipseElement) elem;
                        int rx = (int) ellipseElement.getRx().getBaseVal().getValue();
                        int ry = (int) ellipseElement.getRy().getBaseVal().getValue();
                        shape = new Ellipse((int) ellipseElement.getCx().getBaseVal().getValue() - rx,
                                (int) ellipseElement.getCy().getBaseVal().getValue() - ry, rx*2, ry*2);
                    } else if (elemName.equals("polygon")) {
                        SVGOMPolygonElement polygonElement = (SVGOMPolygonElement) elem;
                        SVGPointList points = polygonElement.getPoints();
                        final int pointsSize = points.getNumberOfItems();
                        int[] x = new int[pointsSize];
                        int[] y = new int[pointsSize];
                        for(int j = 0; j < pointsSize; ++j){
                            SVGPoint point = points.getItem(j);
                            x[j] = (int) point.getX();
                            y[j] = (int) point.getY();
                        }
                        shape = new Polyline(x, y);
                    }else continue;
                    shape.setColor(getColor(elem.getAttribute("stroke")));
                    shape.setFillColor(getColor(elem.getAttribute("fill")));
                    shapes.add(shape);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return shapes;
    }
}
