package com.example.tjgaming.finalproject.View.Home.Profile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjgaming.finalproject.Model.User;
import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

import static android.app.Activity.RESULT_OK;

/**
 * Created by TJ on 10/5/2018.
 */

public class ProfileFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mChoose;
    private TextView mUsername;
    private ImageView mImageView;
    private Button mUpload;
    private User mUser;

    private Uri mImageURI;

    private Context mContext;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private DocumentReference mDocRef;

    private String user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getContext();

        String uid = getActivity().getIntent().getStringExtra("user_id");
        mDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);

        mUsername = (TextView) v.findViewById(R.id.username);
        mChoose = (Button) v.findViewById(R.id.choose_picture);
        mImageView = (ImageView) v.findViewById(R.id.profile_picture);
        mUpload = (Button) v.findViewById(R.id.upload_picture);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("profile_pictures");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("profile_pictures");
        user = mFirebaseAuth.getCurrentUser().toString();

        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    mUser = task.getResult().toObject(User.class);
                    mUsername.setText(mUser.getUsername());
                    Log.d("ProfileFragment", mUser.toString());
                }
            }
        });



        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                //uploadFile();
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(mContext,"Upload in progress.",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
            }
        });

        return v;
    }

    private void openFileChooser(){
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageURI = data.getData();

            Picasso.with(getActivity()).load(mImageURI).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = mContext.getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "."
                + getFileExtension(mImageURI));



        mUploadTask = fileReference.putFile(mImageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Upload successful!",Toast.LENGTH_SHORT).show();

                        Upload upload = new Upload(user,
                                mStorageRef.getDownloadUrl().toString());

                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

    }
}
