package apika.android;

/**
 * Created by guoxing on 19/4/2017.
 */

public class AndroidActivity {
    private String name;
    public AndroidActivity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Actvity: " + this.name;
    }
}

