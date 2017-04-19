package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidService {
    private String name;
    public AndroidService(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Service: " + this.name;
    }
}
