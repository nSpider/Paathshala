package boys.indecent.com.paathshala;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upl extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Button buttonUpload;
    private EditText name;
    private ImageView i;
    private StorageReference storageRef;
    private Bitmap bitmap;
    private Uri fileUri;


    public static final String FB_STORAGE_PATH = "cs/";
    public static final String FB_DATABASE_PATH = "cs";
    static int c = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        Button t1 = (Button) findViewById(R.id.editText2);
        buttonUpload = (Button) findViewById(R.id.button3);
        name = (EditText)findViewById(R.id.editText4);



        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("*/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    public String getFileExt(Uri uri1){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri1));
    }

    @SuppressWarnings("VisibleForTests")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            final Uri uri = data.getData();




            buttonUpload.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    //final StorageReference file = mStorageRef.child("Computer Science").child(uri.getLastPathSegment());
                    final StorageReference file = mStorageRef.child(FB_STORAGE_PATH + name.getText().toString()+"."+getFileExt(uri)) ;
                    final ProgressDialog progressdialog = new ProgressDialog(upl.this);
                    progressdialog.setMessage("Uploading...");
                    progressdialog.show();
                    file.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressdialog.dismiss();
                                    Toast.makeText(upl.this, "Upload done...", Toast.LENGTH_LONG).show();
                                    FileUpload fileUpload = new FileUpload(name.getText().toString()+"."+getFileExt(uri),taskSnapshot.getDownloadUrl().toString() );
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(fileUpload);
                                    Uri uri = null;
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
        } else {
            Toast.makeText(upl.this, "Choose a file first", Toast.LENGTH_LONG).show();
        }
    }
}






