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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLHelper {
    public Integer default1 = -31321439;
    public static final String basekey = "systemui_color";
    public static final String swtag = "XMLHelper: ";
    String filename = "userData.xml";
    File file = new File(Environment.getExternalStorageDirectory(), filename);
    final ArrayList<Integer> Theme = new ArrayList<>();

    public void WriteToXML(String keyname, Integer theme1) {
        Logger.d(swtag + "Trying to set " + keyname + " to " + theme1);
        checkXMLExists();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            assert docBuilder != null;
            doc = docBuilder.parse(file);
        } catch (SAXException | NullPointerException | IOException e) {
            Logger.e(swtag + "" + e);
        }
        // Change the content of node
        assert doc != null;
        Node nodes = doc.getElementsByTagName(keyname).item(0);
        nodes.setTextContent(Integer.toString(theme1));
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        assert transformer != null;
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(file);
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
            Logger.d(swtag + "Key set.");
        } catch (TransformerException e) {
            Logger.e(swtag + "" + e);
        }


    }

    public Integer readFromXML(Integer theme) throws IOException {
        XmlPullParserFactory factory = null;
        Logger.i(swtag + "Starting readfromfile for integer " + theme);
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Logger.e(swtag + "XPPException " + e);
        }
        assert factory != null;
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Logger.e(swtag + "XPPException " + e);
        }

        try {
            // create an input stream to be read by the stream reader.
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Logger.e(swtag + "FilenotFound " + e);
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
                    Logger.i(swtag + "Start tag, name: " + currentTag);
                } else if (eventType == XmlPullParser.TEXT) {
                    if (currentTag != null) {
                        Logger.i(swtag + "Start text read of " + currentTag);
                        if (currentTag.contains("systemui_color")) {
                            if (isInteger(xpp.getText())) {
                                color = Integer.valueOf(xpp.getText());
                                Logger.i(swtag + "Set color " + currentTag + color);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    Logger.i(swtag + "End tag, name: " + xpp.getName());
                    if (xpp.getName().contains("1")) {
                        Logger.i(swtag + "Add color to array - index 0" + color);
                        Theme.add(0, color);
                    }
                    if (xpp.getName().contains("2")) {
                        Logger.i(swtag + "Add color to array - index 1" + color);
                        Theme.add(1, color);
                    }
                    if (xpp.getName().contains("3")) {
                        Logger.i(swtag + "Add color to array - index 2" + color);
                        Theme.add(2, color);
                    }
                    if (xpp.getName().contains("4")) {
                        Logger.i(swtag + "Add color to array - index 3" + color);
                        Theme.add(3, color);
                    }
                }
                Logger.i(swtag + "moving to next tag event");
                eventType = xpp.next();
            }

            switch (theme) {
                case 1:
                    Logger.i(swtag + "trying to return for int 1, color " + Theme.get(0));
                    return Theme.get(0);
                case 2:
                    Logger.i(swtag + "trying to return for int 2, color " + Theme.get(1));
                    return Theme.get(1);
                case 3:
                    Logger.i(swtag + "trying to return for int 3, color " + Theme.get(2));
                    return Theme.get(2);
                case 4:
                    Logger.i(swtag + "trying to return for int 4, color " + Theme.get(3));
                    return Theme.get(3);
                default:
                    Logger.i(swtag + "trying to return for default, color " + Theme.get(1));
                    return Theme.get(0);
            }
        } catch (XmlPullParserException e) {
            Logger.e(swtag + "Error" + e);
        }

        Logger.e(swtag + "Error, returning null.");
        return null;
    }


    public void checkXMLExists() {
        if (!file.exists()) {
            try {
                Logger.d("Sensify: Creating file " + file.getPath());
                FileOutputStream fileos = new FileOutputStream(file);
                XmlSerializer xmlSerializer = Xml.newSerializer();
                xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.startTag(null, "theme");
                for (int x=1; x<5; x++) {
                    xmlSerializer.startTag(null, basekey + x);
                    xmlSerializer.text(Integer.toString(default1));
                    xmlSerializer.endTag(null, basekey + 1);
                }
                xmlSerializer.endTag(null, "theme");
                xmlSerializer.endDocument();
                xmlSerializer.flush();
                String dataWrite = writer.toString();
                fileos.write(dataWrite.getBytes());
                fileos.close();


            } catch (IOException e) {
                Logger.e(swtag + "error " + e);

            }
        } else {
            Logger.d(swtag + "file " + file.getPath() + " already exists.");

        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

}
