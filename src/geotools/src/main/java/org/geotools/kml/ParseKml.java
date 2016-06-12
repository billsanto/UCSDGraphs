package org.geotools.kml;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.xml.PullParser;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;

//import geography.GeographicPoint;


/**
 * Created by William Santo on 6/10/16.
 *
 * Ref: http://gis.stackexchange.com/a/182673
 * Ref: http://www.programcreek.com/java-api-examples/index.php?api=org.geotools.data.simple.SimpleFeatureCollection
 *
 */
public class ParseKml {

    private ArrayList<SimpleFeature> features;

    public ParseKml(String path) throws IOException, XMLStreamException, SAXException {//throws XMLStreamException, IOException, SAXException {
        File source = new File(path);
        java.io.InputStream fis = new FileInputStream(source);
        PullParser parser = new PullParser(new KMLConfiguration(), fis, KML.Placemark);
        SimpleFeature f = null;
        features = new ArrayList<SimpleFeature>();

        while ((f = (SimpleFeature) parser.parse()) != null) {
            features.add(f);
        }
    }

    private SimpleFeatureCollection getFeatures() {
        return DataUtilities.collection(features);
    }


    public static void main(String[] args) {

        String path = "data/kml/Updated rock throwing map.kml";

        try {
            ParseKml p = new ParseKml(path);

            SimpleFeatureIterator it = p.getFeatures().features();
            int i = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                Object attrs = feature.getAttributes();
//                GeographicPoint point = null;

//                if (geom instanceof Point) {
//                    point = (Point) geom;
//                } else {
//                    throw new RuntimeException("Shapefile must contain either points or polygons.");
//                }

                i += 1;
                System.out.println(attrs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
