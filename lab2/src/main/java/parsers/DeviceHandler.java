package parsers;

import models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceHandler extends DefaultHandler {

    private String elementValue;
    private List<Device> listOfDevices = new ArrayList<>() ;
    public List<Device> getDevices() {
        return listOfDevices;
    }

    @Override
    public void startDocument() throws SAXException {
        listOfDevices = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);
    }

    public String getName() {
        return XMLElements.DEVICE;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case XMLElements.DEVICE:
                Device device = new Device();
                listOfDevices.add(device);
                break;
            case XMLElements.TYPES:
                Types types = new Types();
                getLastDevice().setTypes(types);
                break;
        }
    }

    private Device getLastDevice() {
        return listOfDevices.get(listOfDevices.size() - 1);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case XMLElements.ID -> getLastDevice().setId(elementValue);
            case XMLElements.NAME -> getLastDevice().setName(elementValue);
            case XMLElements.ORIGIN -> getLastDevice().setOrigin(elementValue);
            case XMLElements.PRICE -> getLastDevice().setPrice(Integer.valueOf(elementValue));
            case XMLElements.CRITICAL -> getLastDevice().setCritical(Boolean.parseBoolean(elementValue));
            case XMLElements.PERIPHERAL -> getLastDevice().getTypes().setPeripherals(Boolean.parseBoolean(elementValue));
            case XMLElements.ENERGYCONSUMPTION -> getLastDevice().getTypes().setEnergyConsumption(Short.parseShort(elementValue));
            case XMLElements.COOLER -> getLastDevice().getTypes().setCooler(Boolean.parseBoolean(elementValue));
            case XMLElements.GROUP -> getLastDevice().getTypes().setGroup(elementValue);
            case XMLElements.PORT -> getLastDevice().getTypes().setPort(elementValue);
        }
    }

    public void setField(String qName, String content, Map<String ,String> attributes) {
        switch (qName) {
            case XMLElements.DEVICE -> {
                Device device = new Device();
                listOfDevices.add(device);
            }
            case XMLElements.NAME -> getLastDevice().setName(content);
            case XMLElements.ID -> getLastDevice().setId(content);
            case XMLElements.ORIGIN -> getLastDevice().setOrigin(content);
            case XMLElements.PRICE -> getLastDevice().setPrice(Integer.valueOf(content));
            case XMLElements.CRITICAL -> getLastDevice().setCritical(Boolean.parseBoolean(content));
            case XMLElements.TYPES -> {
                Types types = new Types();
                getLastDevice().setTypes(types);
            }
            case XMLElements.PERIPHERAL -> getLastDevice().getTypes().setPeripherals(Boolean.parseBoolean(content));
            case XMLElements.ENERGYCONSUMPTION -> getLastDevice().getTypes().setEnergyConsumption(Short.parseShort(content));
            case XMLElements.COOLER -> getLastDevice().getTypes().setCooler(Boolean.parseBoolean(content));
            case XMLElements.PORT -> getLastDevice().getTypes().setPort(content);
            case XMLElements.GROUP -> getLastDevice().getTypes().setGroup(content);
        }
    }
}