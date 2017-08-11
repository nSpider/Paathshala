package boys.indecent.com.paathshala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class cs extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private List<FileUpload> fileList;
    private ListView lv;
    private FileListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);
        fileList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewFile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Database. Please wait!!");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(upl.FB_DATABASE_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FileUpload file = snapshot.getValue(FileUpload.class);
                    fileList.add(file);
                }

                adapter = new FileListAdapter(cs.this, R.layout.file_item, fileList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    public void upload(View v){
        //finish();
        int p = 1;
        startActivity(new Intent(cs.this,upl.class));
    }
}
