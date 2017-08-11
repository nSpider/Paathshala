package boys.indecent.com.paathshala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class upload extends AppCompatActivity implements View.OnClickListener {


    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    private EditText t1;
    private Button buttonUpload;
    private ImageView i;
    private StorageReference storageRef;
    private Bitmap bitmap;


    private Uri file;

    static int c = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        t1 = (EditText) findViewById(R.id.editText2);
        buttonUpload = (Button)findViewById(R.id.button3);

        if (c == 0){
            fc();
        }

        t1.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }


    private void fc(){
        int c = 1;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void uploadFile(){

        if (file!= null){

            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setMessage("Uploading...");
            progressdialog.show();

            StorageReference riversRef = storageRef.child("notes/rivers.jpg");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Uploaded", Toast.LENGTH_LONG).show();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_LONG).show();

                            // Handle unsuccessful uploads
                            // ...
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressdialog.setMessage(((int)progress)+ "% uploaded");
                        }
                    });
            ;
        }else{
            //display a toast
            Toast.makeText(getApplicationContext(),"Choose a file first", Toast.LENGTH_LONG).show();


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null){
            file=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file);
                i.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //t1.setText((CharSequence) filepath);

        /*try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file);
                i.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onClick(View view) {
        if (view == t1){
            //open file chooser
            fc();
        }else if(view == buttonUpload){
            //upload file
            uploadFile();
        }
    }
}
