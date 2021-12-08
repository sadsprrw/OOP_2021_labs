import models.Device;
import models.Types;
import models.XMLElements;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import parsers.DOMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DOMParserTest {


    @Test
    void parse() throws NullPointerException {
        DOMParser parser = new DOMParser();
        File file = new File("src/main/resources/devices.xml");
        List<Device> devices = parser.parse(file);
        Device device1 = devices.get(0);

        assertEquals(device1.getTypes().getEnergyConsumption(), 6);
        assertEquals(device1.getTypes().getPort(), "USB");
        assertEquals(device1.getTypes().getGroup(), "Multimedia");
        assertTrue(device1.getTypes().isPeripherals());
        assertTrue(device1.getTypes().isCooler());

        assertEquals(XMLElements.IDOfFirstDevice, device1.getId());
        assertEquals(XMLElements.NameOfFirstDevice, device1.getName());

        assertEquals(device1.getOrigin(), "US");
        assertEquals(device1.getPrice(), 1000);
        assertTrue(device1.isCritical());

        Device device2 = devices.get(1);

        assertEquals(device2.getTypes().getEnergyConsumption(), 15);
        assertEquals(device2.getTypes().getPort(), "USB");
        assertEquals(device2.getTypes().getGroup(), "Sound");
        assertTrue(device2.getTypes().isPeripherals());
        assertFalse(device2.getTypes().isCooler());

        assertEquals(XMLElements.IDOfSecondDevice, device2.getId());
        assertEquals(XMLElements.NameOfSecondDevice, device2.getName());

        assertEquals(device2.getOrigin(), "China");
        assertEquals(device2.getPrice(), 100);
        assertTrue(device2.isCritical());
    }
}