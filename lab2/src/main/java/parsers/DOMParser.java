package parsers;

import models.Device;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOMParser {

    public List<Device> parse(File xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(xml);

            document.getDocumentElement().normalize();

            Element rootNode = document.getDocumentElement();
            DeviceHandler deviceHandler = new DeviceHandler();

            NodeList devicesNodesList = rootNode.getElementsByTagName(deviceHandler.getName());
            for (int devicesNode = 0; devicesNode < devicesNodesList.getLength(); devicesNode++) {
                Element deviceElement = (Element) devicesNodesList.item(devicesNode);
                traverseNodes(deviceElement,deviceHandler);
            }
            return deviceHandler.getDevices();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }
    private void traverseNodes(Node node, DeviceHandler deviceHandler) {
        if(node.getNodeType() == Node.ELEMENT_NODE) {
            Map<String, String> attributes = new HashMap<>();
            if(node.getAttributes() != null) {
                for (int i = 0; i < node.getAttributes().getLength(); i++) {
                    attributes.put(node.getAttributes().item(i).getNodeName(),
                            node.getAttributes().item(i).getTextContent());
                }
            }
            deviceHandler.setField(node.getNodeName(), node.getTextContent(), attributes);
            if(node.getChildNodes() != null) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    traverseNodes(node.getChildNodes().item(i), deviceHandler);
                }
            }
        }
    }

}