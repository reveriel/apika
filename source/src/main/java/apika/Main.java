package apika;

import java.util.Timer;

/**
 * Created by guoxing on 1/4/2017.
 */

public class Main {
    /** only support process one apk file
     *  we didn't clear statistic data. after running
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("start analysis of " + args[0]);

        Statistics.startProcessTime = System.currentTimeMillis();
        processOneFile(args);
        Statistics.endProcessTime = System.currentTimeMillis();

        Statistics.printJson();

//        System.out.println("end analysis of " + args[0]);
    }

    public static void processOneFile(String[] args) {


        Config.setApkName(args[0]);

        Config.setOutputDir("output"); //default
        if (args.length > 1) {
            System.out.println("set Outdir " + args[1]);
            Config.setOutputDir(args[1]);
        }

        InitSoot.checkArgs(args);
        InitSoot.configurationInit();
        InitSoot.initSootOptions(args);
        InitSoot.addComponentTransformer();
        InitSoot.silent();

        ManifestParser.parseApkManifest(args[0]);

        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});
    }


}
