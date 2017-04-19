package apika;

import android.content.res.AXmlResourceParser;
import android.content.res.XmlResourceParser;
import apika.android.AndroidActivity;
import org.xmlpull.v1.XmlPullParserException;
import test.AXMLPrinter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by guoxing on 19/4/2017.
 */
public class ManifestParser {
    static void parseApkManifest(String apkFile) {

        InputStream manifestIS = getApkManifestInputStream(apkFile);

        AXmlResourceParser parser = new AXmlResourceParser();

        parser.open(manifestIS);
//        printXml(parser, null);
        collectDataFromXml(parser);
    }

    /**
     * @param parser , a parser initialized with an xml file
     */
    private static void collectDataFromXml(AXmlResourceParser parser) {
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                collectData(eventType, parser);
                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void collectData(int eventType, AXmlResourceParser parser) {
        switch (eventType) {
            case XmlResourceParser.START_TAG :
                String tagName = parser.getName();
                switch (tagName) {
                    case "activity":
                        addDataToStatistics(parser, Statistics.usedActivities);
                        break;
                    case "service":
                        addDataToStatistics(parser, Statistics.usedServices);
                        break;
                    case "receiver":
                        addDataToStatistics(parser, Statistics.usedReceivers);
                        break;
                    case "provider":
                        addDataToStatistics(parser, Statistics.usedProviders);
                        break;
                    case "uses-permission":
                        addDataToStatistics(parser, Statistics.usedPermissions);
                        break;
                    case "uses-feature":
                        addDataToStatistics(parser, Statistics.usedFeatures);
                        break;
                }
                break;
            default:
        }
    }

    private static void addDataToStatistics(AXmlResourceParser parser, Set set) {
        int AttrCnt = parser.getAttributeCount();
        for (int idx = 0; idx < AttrCnt; idx++) {
            String attributeName = parser.getAttributeName(idx);
            if (attributeName.equals("name")) {
                set.add(new AndroidActivity(parser.getAttributeValue(idx)));
            }
        }
    }

    /**
     * this function print the xml which the parser is reading to the PrintStream out
     * @param parser , a parser initialized with an xml file
     * @param out
     */
    private static void printXml(AXmlResourceParser parser, PrintStream out) {
        if (out == null) {
            out = System.out;
        }
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlResourceParser.START_DOCUMENT :
                        out.println("xml start\n");
                        break;
                    case XmlResourceParser.START_TAG :
                        out.format("<%s", parser.getName());
                        int AttrCnt = parser.getAttributeCount();
                        for (int idx = 0; idx < AttrCnt; idx++) {
                            out.format(" %s=\"%s\";"
                                    ,parser.getAttributeName(idx)
                                    , AXMLPrinter.getAttributeValue(parser, idx));
//                                    parser.getAttributeValue(idx)); // this can not get correct value
                        }
                        out.println(">");
                        break;
                    case XmlResourceParser.TEXT :
                        String text = parser.getText();
                        out.format("%s", text == null ? "" : text);
                        break;
                    case XmlResourceParser.END_TAG :
                        out.format("</%s>\n", parser.getName());
                        break;
                    default:
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        out.print("xml end\n");
    }

    /**
     * open the apk file
     * @param apkFile
     * @return the InputStream of "AndroidManifest.xml"
     */
    private static InputStream getApkManifestInputStream(String apkFile) {
        InputStream manifestIS;
        ZipFile archive;

        try {
            archive = new ZipFile(apkFile);
        } catch (IOException e) {
            throw new RuntimeException("Error , I/O Error in opening the apk file : " + apkFile);
        }

        ZipEntry entry =  archive.getEntry("AndroidManifest.xml");
        if (entry == null) {
            throw new RuntimeException("Error, not 'AndroidManifest.xml' file found in the apk");
        }

        try {
            manifestIS = archive.getInputStream(entry);
        } catch (IOException e) {
            throw new RuntimeException("Error, I/O exception in getInputStream for the 'AndroidManifest.xml'");
        }

        if (manifestIS == null) {
            throw new RuntimeException("Error, I/O exception in getInputStream for the 'AndroidManifest.xml'");
        }

        return manifestIS;
    }
}
