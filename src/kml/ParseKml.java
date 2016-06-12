package kml;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.xml.PullParser;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.vividsolutions.jts.geom.Point;
import geography.GeographicPoint;


/**
 * Created by William Santo on 6/10/16.
 *
 * Ref: http://gis.stackexchange.com/a/182673
 * Ref: http://www.programcreek.com/java-api-examples/index.php?api=org.geotools.data.simple.SimpleFeatureCollection
 * Ref: http://www.javamex.com/tutorials/io/input_stream.shtml
 *
 * Parse a kml file from google maps with the following structure (other possible structures not tested):
 *  [10/12/2014 3:00 a.m., true, true, null, null, N. I-35 SVRD NB/E. 32nd St., null, null, null, POINT (-97.7231312 30.289148199999996)]
 *  [10/12/2014 3:00 a.m., true, true, null, null, N. I-35 SVRD NB/E. 38th 1/2 St. , null, null, null, POINT (-97.719419 30.2947946)]
 *  [10/12/2014 2:40 a.m., true, true, null, null, 3166 N. I-35 NB, null, null, null, POINT (-97.7287745 30.2802495)]
 *  [7/27/2014 4:31 a.m., true, true, null, null, 5000 N. I-35 NB, null, null, null, POINT (-97.7154547 30.301482800000002)]
 *
 * KML files can be downloaded by viewing a google map, such as https://www.google.com/maps/d/viewer?mid=1Ce1sstPM_GzUqCvIZgTcDjL3WAc
 * then selecting the "Download KML" drop down option, then choosing "Export to a .KML file instead of .KMZ (does not include custom icons)"
 */
public class ParseKml {

    private ArrayList<SimpleFeature> features;

    /**
     * Constructor reads from a FileInputStream or URL and builds a set of features
     * @param path
     * @throws IOException
     * @throws XMLStreamException
     * @throws SAXException
     */
    public ParseKml(String path) throws IOException, XMLStreamException, SAXException {
        InputStream inputStream;

        if (path.startsWith("http://") || path.startsWith("https://")) {
            // TODO Troubleshoot google 404 errors and
            URL url = null;

            try {
                url = new URL(path);

//                StringBuilder content = new StringBuilder();
//                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//                String inputLine;
//                while ((inputLine = in.readLine()) != null)
//                    System.out.println(inputLine);
//                content.append(inputLine + "\n");
//                in.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            inputStream = url.openStream();
        }
        else {
            File source = new File(path);
            inputStream = new FileInputStream(source);
        }

        PullParser parser = new PullParser(new KMLConfiguration(), inputStream, KML.Placemark);
        SimpleFeature f = null;
        features = new ArrayList<SimpleFeature>();

        while ((f = (SimpleFeature) parser.parse()) != null) {
            features.add(f);
        }
    }


    /**
     * Return a collection of features
     * @return SimpleFeatureCollection
     */
    private SimpleFeatureCollection getFeatures() {
        return DataUtilities.collection(features);
    }


    /**
     * Convert the features into a list of GeographicPoints
     * @return List<GeographicPoint>
     */
    public List<GeographicPoint> getGeographicPointsFromKml() {

        List<GeographicPoint> returnList = new ArrayList<>();

        for (SimpleFeature feature: features) {
            Coordinate coordinate = ((Point) feature.getAttributes().get(9)).getCoordinate();
            GeographicPoint geographicPoint = new GeographicPoint(coordinate.y, coordinate.x);
            returnList.add(geographicPoint);

//            System.out.println(geographicPoint);
        }
        return returnList;
    }


    /**
     * Read a kml file from a file or URL and convert to a list of GeographicPoints
     * Unfortunately, the latest tests show all URLs get a redirect to an invalid URL
     * @param args
     */
    public static void main(String[] args) {

        String path = "data/kml/Updated rock throwing map.kml";
//        String path = "http://www.google.com/maps/d/kml?forcekml=1&amp;amp;mid=1hUOCWm6PwwXCf2cyJ8vzAMvCnPw&amp;amp;lid=zlxQnUkSNutQ.kMEb6B3-htxg";

        try {
            ParseKml p = new ParseKml(path);
            List<GeographicPoint> pointList = p.getGeographicPointsFromKml();

            System.out.println(pointList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
