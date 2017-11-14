package com.example.aliihsangokalp.casepool;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadCaseActivity extends AppCompatActivity {

    EditText name;
    EditText comment;
    EditText analyzername;

    ImageView upload;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Uri selected;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case);

        name = (EditText) findViewById(R.id.name);
        analyzername = (EditText) findViewById(R.id.analyzer );
        comment = (EditText) findViewById(R.id.comment);
        upload = (ImageView) findViewById(R.id.upload);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void send(View view){


        UUID uuidImage = UUID.randomUUID();

        String imageName = "images/" + uuidImage+".jpg";

       StorageReference storageReference =  mStorageRef.child(imageName);
       storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               String downloadURL = taskSnapshot.getDownloadUrl().toString();


               FirebaseUser user = mAuth.getCurrentUser();
               String userEmail = user.getEmail();
               String username = name.getText().toString();
               String usercomment = comment.getText().toString();
               String analyzer = analyzername.getText().toString();



               UUID uuıd = UUID.randomUUID();
               String uuidString = uuıd.toString();

               myRef.child("Cases").child(uuidString).child("useremail").setValue(userEmail);
               myRef.child("Cases").child(uuidString).child("names").setValue(username);
               myRef.child("Cases").child(uuidString).child("comment").setValue(usercomment);
               myRef.child("Cases").child(uuidString).child("devices").setValue(analyzer);

               myRef.child("Cases").child(uuidString).child("downloadrul").setValue(downloadURL);

               Toast.makeText(getApplicationContext(),"CASE HAS BEEN SEND",Toast.LENGTH_LONG).show();




               Intent intent = new Intent(getApplicationContext(),CaseListActivity.class);
               startActivity(intent);






           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

           }
       });




    }

    public void uploadPhoto (View view){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);


            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 2 && requestCode == RESULT_OK && data != null) {
            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected);
                upload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

