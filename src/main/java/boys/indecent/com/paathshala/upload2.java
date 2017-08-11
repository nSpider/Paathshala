package boys.indecent.com.paathshala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upload2 extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    private EditText t1;
    private Button buttonUpload;
    private EditText name;
    private ImageView i;
    private StorageReference storageRef;
    private Bitmap bitmap;
    private Uri du;
    private String Name;
    public FirebaseDatabase mRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    static int c = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload2);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        t1 = (EditText) findViewById(R.id.editText2);
        buttonUpload = (Button) findViewById(R.id.button3);
        name = (EditText)findViewById(R.id.editText4);

        //mRef = new FirebaseDatabase("https://paathshala-359ec.firebaseio.com/");



            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Name = name.getText().toString();

                    if(TextUtils.isEmpty(Name)){
                        Toast.makeText(upload2.this, "Enter the name first",Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(Intent.ACTION_PICK);

                    intent.setType("*/*");

                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            final Uri uri = data.getData();

            final StorageReference file = mStorageRef.child("Computer Science").child("New File");

            buttonUpload.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {


                    final ProgressDialog progressdialog = new ProgressDialog(upload2.this);
                    progressdialog.setMessage("Uploading...");
                    progressdialog.show();
                    file.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressdialog.dismiss();
                                    Toast.makeText(upload2.this, "File Uploaded", Toast.LENGTH_LONG).show();
                                    du = taskSnapshot.getDownloadUrl();

                                    //updatingProcess();

                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressdialog.dismiss();
                                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressdialog.setMessage(((int) progress) + "% uploaded");
                                }
                            });

                }
            });
        }
    }

    void updatingProcess(){

        Toast.makeText(upload2.this,"File database Updated", Toast.LENGTH_LONG).show();

        finish();

        startActivity(new Intent(upload2.this, cs.class));

        StorageReference storageRef = storage.getReferenceFromUrl("https://paathshala-359ec.firebaseio.com/Computer Science");

        StorageReference storageRef1 = storageRef.child(Name);


        finish();



    };

}






