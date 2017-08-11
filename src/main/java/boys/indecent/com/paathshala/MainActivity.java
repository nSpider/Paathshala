package boys.indecent.com.paathshala;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import static java.lang.System.exit;


public class MainActivity extends AppCompatActivity  {
    private static View mNet1;
    private FirebaseAuth mAuth;
    private TextView lo;
    public FirebaseUser user;

    String TAG="MainActivity";

    private TextView userName;

    private BroadcastReceiver NetChange;

    static int c=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Globaly.setActivity(2);
        //Toast.makeText(MainActivity.this,Globaly.getActivity()+" is set",Toast.LENGTH_SHORT ).show();

        mNet1=findViewById(R.id.net1);


        mAuth = FirebaseAuth.getInstance();

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter1.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        NetworkChangeReceiver NetworkChangeReceiverRef;

        NetChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("BroadCastReceiver","RECIEVED");
                boolean status = NetworkUtil.getConnectivityStatus(context);
                //Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                //if (Globaly.getActivity()==1)
                netChange(status);
            }
        };




        try {
            registerReceiver(NetChange, filter1);
            Log.d(TAG, "REGISTERED");
        }catch (Exception e){
            Log.d(TAG,"ALREADY REGISTERED");
        }

        if (mAuth.getCurrentUser()== null ){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


        userName = (TextView)findViewById(R.id.username);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            Uri url=null;

                     for (UserInfo profile : user.getProviderData()) {
                    url = profile.getPhotoUrl();
                    if (!Objects.equals(name, profile.getDisplayName())) {
                        name = profile.getDisplayName();
                    }
                }

            //userName.setText(String.valueOf(url));

            ImageView imageView = (ImageView) findViewById(R.id.dp);
            new DownloadImageTask((ImageView) findViewById(R.id.dp))
                    .execute(String.valueOf(url));

            if (name == null)
                userName.setText(email);
            else
                userName.setText(name);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            Globaly.setActivity(2);
            //Toast.makeText(MainActivity.this,String.valueOf(Globaly.getActivity())+" is set",Toast.LENGTH_SHORT ).show();
        }
        checkInternet();
        c=1;
    }

    private void checkInternet() {
            boolean connected;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            //we are connected to a network
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
            netChange(connected);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            try {
                Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Log.e("Bitmap","returned");
                return myBitmap;
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public void onTextClick(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void info(View v){
        //finish();
        startActivity(new Intent(MainActivity.this, info.class));
    }

    public void cs(View v){
        //finish();
        startActivity(new Intent(MainActivity.this, cs.class));
    }

    boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(NetChange);
            Log.d(TAG, "UNREGISTERED");
        }catch (Exception e) {
            String TAG = "MainActivity";
            Log.d(TAG, "NOT REGISTERED");
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                bmImage.setImageBitmap(getCircularBitmapFrom(result));
            } else {
                bmImage.setImageBitmap(getCircularBitmapFrom(BitmapFactory.decodeResource(getResources(), R.drawable.pp)));
            }
            ImageView dpbi = (ImageView) findViewById(R.id.dpb);
            dpbi.setVisibility(View.VISIBLE);
        }
    }
    public static Bitmap getCircularBitmapFrom(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        float radius = bitmap.getWidth() > bitmap.getHeight() ? ((float) bitmap
                .getHeight()) / 2f : ((float) bitmap.getWidth()) / 2f;
        Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                radius, paint);

        return canvasBitmap;
    }
    public static void netChange(boolean i){



        if (c==1) {
            if (i) {
                mNet1.setVisibility(View.INVISIBLE);

            } else {
                mNet1.setVisibility(View.VISIBLE);
            }
        }
    }
}

