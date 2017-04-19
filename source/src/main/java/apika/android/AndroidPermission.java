package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidPermission {
    private String name;
    public AndroidPermission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Permission: " + this.name;
    }
}
