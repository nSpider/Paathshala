package boys.indecent.com.paathshala;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nEW u on 30-Dec-16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NetworkChangeReceiver","RECIEVED");
        boolean status = NetworkUtil.getConnectivityStatus(context);
        //Toast.makeText(context,status,Toast.LENGTH_LONG).show();
        //if (Globaly.getActivity()==1)
        LoginActivity.netChange(status);
    }
}
