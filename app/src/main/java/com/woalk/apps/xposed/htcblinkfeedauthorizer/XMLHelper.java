package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.os.Environment;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLHelper {
    public Integer default1 = -31321439;
    public static final String basekey = "systemui_color";
    public static final String xhtag = "XMLHelper: ";
    String filename = "/themeColors.xml";
    String path = "/Sensify";
    File directory = new File(Environment.getExternalStorageDirectory().toString() + path);
    File file = new File(directory + filename);


    public XMLHelper() {
        checkXMLExists();
    }

    final ArrayList<Integer> Theme = new ArrayList<>();

    public void WriteToXML(String keyname, Integer theme1) {
        Logger.d(xhtag + "Trying to set " + keyname + " to " + theme1);
           try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // Change the content of node
            Node nodes = doc.getElementsByTagName(keyname).item(0);
            // I changed the below line form nodes.setNodeValue to nodes.setTextContent
            nodes.setTextContent(Integer.toString(theme1));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(file);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
               Logger.d("XMLHelper: Writing seems to have completed.");
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            Logger.e("XMLHelper: Exception writing tag " + e);
        }
    }

    public Integer readFromXML(Integer theme) throws IOException {
        XmlPullParserFactory factory = null;
        Logger.i(xhtag + "Starting readfromXML for integer " + theme);
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Logger.e(xhtag + "XPPException " + e);
        }
        assert factory != null;
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Logger.e(xhtag + "XPPException " + e);
        }

        try {
            // create an input stream to be read by the stream reader.
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Logger.e(xhtag + "FilenotFound " + e);
            }

            // set the input for the parser using an InputStreamReader
            assert xpp != null;
            assert fis != null;
            xpp.setInput(new InputStreamReader(fis));

            int eventType = xpp.getEventType();
            String currentTag = null;
            Integer color = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    currentTag = xpp.getName();

                } else if (eventType == XmlPullParser.TEXT) {
                    if (currentTag != null) {
                        if (currentTag.contains("systemui_color")) {
                            if (Common.isInteger(xpp.getText())) {
                                color = Integer.valueOf(xpp.getText());
                                Logger.i(xhtag + "Found color " + currentTag + color);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().contains("1")) {
                        Theme.add(0, color);
                    }
                    if (xpp.getName().contains("2")) {
                        Theme.add(1, color);
                    }
                    if (xpp.getName().contains("3")) {
                        Theme.add(2, color);
                    }
                    if (xpp.getName().contains("4")) {
                        Theme.add(3, color);
                    }
                }
                eventType = xpp.next();
            }

            switch (theme) {
                case 0:
                    Logger.i(xhtag + "trying to return for int 1, color " + Theme.get(0));
                    return Theme.get(0);
                case 1:
                    Logger.i(xhtag + "trying to return for int 2, color " + Theme.get(1));
                    return Theme.get(1);
                case 2:
                    Logger.i(xhtag + "trying to return for int 3, color " + Theme.get(2));
                    return Theme.get(2);
                case 3:
                    Logger.i(xhtag + "trying to return for int 4, color " + Theme.get(3));
                    return Theme.get(3);
                default:
                    Logger.i(xhtag + "trying to return for default, color " + Theme.get(1));
                    return Theme.get(0);
            }
        } catch (XmlPullParserException e) {
            Logger.e(xhtag + "Error" + e);
        }

        Logger.e(xhtag + "Error, returning null.");
        return null;
    }


    public void checkXMLExists() {
        if (!file.exists()) try {
            Logger.d("Sensify: Creating file " + file.getPath());
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    Logger.d(xhtag + "Directory Successfully created.");
                    FileOutputStream fileos = new FileOutputStream(file);
                    XmlSerializer xmlSerializer = Xml.newSerializer();
                    xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    StringWriter writer = new StringWriter();
                    xmlSerializer.setOutput(writer);
                    xmlSerializer.startDocument("UTF-8", true);
                    xmlSerializer.startTag(null, "theme");
                    for (int x = 1; x < 5; x++) {
                        xmlSerializer.startTag(null, basekey + x);
                        xmlSerializer.text(Integer.toString(default1));
                        xmlSerializer.endTag(null, basekey + x);
                    }
                    xmlSerializer.endTag(null, "theme");
                    xmlSerializer.endDocument();
                    xmlSerializer.flush();
                    String dataWrite = writer.toString();
                    fileos.write(dataWrite.getBytes());
                    fileos.close();
                } else {
                    Logger.e(xhtag + "Problem creating directory");
                    throw new FileNotFoundException();
                }
            }

        } catch (IOException e) {
            Logger.e(xhtag + "error " + e);

        }
        else {
            Logger.d(xhtag + "file " + file.getPath() + " already exists.");

        }
    }

    public static int mixThemeColor(Integer p, Integer s, Float f) {
        if (p.intValue() == s.intValue()) {
            Logger.i(xhtag + "Theme color not unique, mixing.");
            return Common.enlightColor(p, f);
        } else {
            Logger.i(xhtag + "Theme color is unique, returning.");
            return s;
        }
    }
}
