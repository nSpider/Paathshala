package boys.indecent.com.paathshala;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by nEW u on 04-Jan-17.
 */

public class Globaly extends Application {
    private static int activity;

    public static int getActivity() {
        return activity;
    }

    public static void setActivity(int activity) {
        Globaly.activity = activity;
    }
}
