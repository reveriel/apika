package apika;


import android.content.res.AXmlResourceParser;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by guoxing on 1/4/2017.
 */
public class Main {

    public static void main(String[] args) {
        InitSoot.checkArgs(args);
        InitSoot.configurationInit();
        InitSoot.initSootOptions(args);
        InitSoot.addComponentTransformer();


        parseApkManifest(args[0]);

        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});

        Statistics.createOutput();
    }

    public static void parseApkManifest(String apkFile) {
        InputStream manifestIS = null;
        ZipFile archive = null;

        try {
            archive = new ZipFile(apkFile);
        } catch (IOException e) {
            throw new RuntimeException("Error , I/O Erro in opening the apk file : " + apkFile);
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

        // Parse the AndroidManifest.xml
        AXmlResourceParser parser = new AXmlResourceParser();

        parser.open(manifestIS);

        PrintStream out = System.out;
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlResourceParser.START_DOCUMENT :  out.println("xml start");
                    case XmlResourceParser.START_TAG : out.format("<%s>", parser.getName());
                    case XmlResourceParser.TEXT : out.format("%s\n", parser.getText());
                    case XmlResourceParser.END_TAG : out.format("</%s>", parser.getName());
                    default:
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        out.print("xml end");

    }


}
