package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidReceiver {
    private String name;
    public AndroidReceiver(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Receiver: " + this.name;
    }
}
