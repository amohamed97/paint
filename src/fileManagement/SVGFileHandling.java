package fileManagement;

import org.apache.batik.dom.svg.*;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVGFileHandling implements FileHandler {
    ArrayList<Shape> shapes;

    public SVGFileHandling(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public void save(String filename, Color background) {
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

    private Color loadSVGColor(String rgba){
        final Pattern pat = Pattern.compile("rgba\\((\\d+), *(\\d+), *(\\d+), *(\\d+)\\)");
        Matcher matcher = pat.matcher(rgba);
        matcher.find();
        return new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
    }

    @Override
    public Color load(String fileName) {
        shapes.clear();
        Color background = new Color(0,0,0,0);
        try{
            Element root = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName()).createDocument(
                    Paths.get(fileName).toUri().toString()).getDocumentElement();
            background = loadSVGColor(root.getAttribute("viewport-fill"));
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
                    shape.setColor(loadSVGColor(elem.getAttribute("stroke")));
                    shape.setFillColor(loadSVGColor(elem.getAttribute("fill")));
                    shapes.add(shape);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return background;
    }
}
