package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidFeature {
    private String name;
    public AndroidFeature(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Feature: " + this.name;
    }
}
