package boys.indecent.com.paathshala;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Open extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Open.this,MainActivity.class));
                }
            }
        };
        timer.start();
    }
}
