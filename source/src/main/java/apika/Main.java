package apika;

/**
 * Created by guoxing on 1/4/2017.
 */

public class Main {
    public static void main(String[] args) {
        processOneFile(args);
    }

    public static void processOneFile(String[] args) {

        Config.apkName = args[0];

        InitSoot.checkArgs(args);
        InitSoot.configurationInit();
        InitSoot.initSootOptions(args);
//        InitSoot.addComponentTransformer();

        ManifestParser.parseApkManifest(args[0]);

//        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});

        Statistics.createOutput();
        Statistics.printOutput();
    }


}
