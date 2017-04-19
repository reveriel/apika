package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidProvider {
    private String name;
    public AndroidProvider(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Provider: " + this.name;
    }
}
