package com.starsailor.util;

import com.starsailor.model.items.MapItem;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class TMXParser {

  private File file;

  public TMXParser(File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
  }

  public boolean contains(MapItem gameData) {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    FileReader fileReader = null;
    XMLEventReader eventReader = null;
    try {
      fileReader = new FileReader(file.getAbsolutePath());
      eventReader = factory.createXMLEventReader(fileReader);
      while(eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        switch(event.getEventType()) {
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String qName = startElement.getName().getLocalPart();

            if(qName.equalsIgnoreCase("object")) {
              Iterator<Attribute> attributes = startElement.getAttributes();
              while(attributes.hasNext()) {
                Attribute next = attributes.next();
                String attributeName = next.getName().getLocalPart();

                if(attributeName.equals("name")) {
                  String value = next.getValue();
                  if(value.toLowerCase().startsWith(gameData.getName().toLowerCase())) {
                    return true;
                  }
                  if(value.startsWith(String.valueOf(gameData.getId()))) {
                    return true;
                  }
                }
              }
            }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        eventReader.close();
        fileReader.close();
      } catch (XMLStreamException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public void findRoutes(List<String> routeNames) {
    FileReader fileReader = null;
    XMLEventReader eventReader = null;
    try {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      fileReader = new FileReader(file.getAbsolutePath());
      eventReader = factory.createXMLEventReader(fileReader);

      while(eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        switch(event.getEventType()) {
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String qName = startElement.getName().getLocalPart();

            if(qName.equalsIgnoreCase("object")) {
              Iterator<Attribute> attributes = startElement.getAttributes();
              String name = null;
              while(attributes.hasNext()) {
                Attribute next = attributes.next();
                String attributeName = next.getName().getLocalPart();

                if(attributeName.equals("name")) {
                  name = next.getValue();
                }
                else if(attributeName.equals("type")) {
                  String value = next.getValue();
                  if(value.toLowerCase().equals("route") && !routeNames.contains(name)) {
                    routeNames.add(name);
                  }
                }
              }
            }
        }
      }
      eventReader.close();
      fileReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        eventReader.close();
        fileReader.close();
      } catch (XMLStreamException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
