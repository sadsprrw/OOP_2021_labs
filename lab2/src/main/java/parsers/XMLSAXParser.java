package parsers;

import models.Device;
import org.xml.sax.SAXException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class XMLSAXParser {
    public List<Device> parse(File xml) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        saxParser = factory.newSAXParser();
        DeviceHandler userHandler = new DeviceHandler();
        saxParser.parse(xml, userHandler);
        return userHandler.getDevices();
    }

}