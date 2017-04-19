package apika;

import android.content.res.AXmlResourceParser;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
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
//        InitSoot.addComponentTransformer();

        ManifestParser.parseApkManifest(args[0]);

        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});

//        Statistics.createOutput();
        Statistics.printOutput();

    }


}
