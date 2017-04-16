package apika;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by guoxing on 1/4/2017.
 */
public class MainTest {

    String JAVA_HOME;
    String SOOT_CLASS_PATH;
    String ANDROID_JARS;

    @Test
    public void main() throws Exception {



//        File resourcesDir = new File("src/test/resources");

        configurationInit();

        Main.main(new String[] {
                "-cp", SOOT_CLASS_PATH,
                "-x", "android.*",   // exclude  android package , not working
                "-android-jars", ANDROID_JARS,
                "-src-prec", "apk",
                "-p", "bb", "enabled:false",
                "-allow-phantom-refs",
                "-process-dir", "src/test/resources/app-release-unsigned.apk"
        });
    }


    /**
     * init JAVA_HOME, SOOT_CLASS_PATH, ANDROID_JARS
     *
     * java properties file examples : http://www.mkyong.com/java/java-properties-file-examples/
     */
    void configurationInit() {

        JAVA_HOME = System.getenv("JAVA_HOME");
        if (JAVA_HOME == null) {
            throw new RuntimeException("JAVA_HOME undefined");
        }

        SOOT_CLASS_PATH = JAVA_HOME + "/jre/lib/rt.jar:" + JAVA_HOME + "/jre/lib/jsse.jar:.";

        // read config.properties
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");

            prop.load(input);
            ANDROID_JARS = prop.getProperty("ANDROID_JARS");
//            ANDROID_JARS = "/Users/guoxing/Library/Android/sdk/platforms";
            if (ANDROID_JARS == null) {
                System.err.println("property 'ANDROID_JARS' undefined in file config.properties");
            }


        } catch (FileNotFoundException ex) {
            System.err.println("config.properties, file not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}