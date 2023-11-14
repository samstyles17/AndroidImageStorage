package com.example.imagestorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity
{

    ImageView imageView;
    Button button;
    EditText image;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.firebaseimage);
        image = findViewById(R.id.editTextImageID) ;
        button = findViewById(R.id.readimage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity2.this);
                progressDialog.setMessage("Fetching Image...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String imageId = image.getText().toString().trim();
                storageReference = FirebaseStorage.getInstance().getReference("images/"+imageId+".jpg");

                try {
                    File localfile = File.createTempFile("tempfile",".jpg");
                    storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(MainActivity2.this,"Failed to retrieve Image",Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}