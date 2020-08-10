package com.zoothii.parseproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetFileCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class Tab3Profile extends Fragment {

    public static final int ADD_PHOTO_REQUEST_CODE = 111;
    public static final int ADD_PHOTO_INTENT_REQUEST_CODE = 222;
    public static final int TAKE_PHOTO_REQUEST_CODE = 333;
    public static final int TAKE_PHOTO_INTENT_REQUEST_CODE = 444;


    View view;
    TextView textViewUserName, textViewEmailAddress;
    ImageView imageViewProfile;
    Button buttonEditProfile, buttonSaveProfile, buttonLogOutProfile;
    Uri imageUri;
    String currentPhotoPath;
    ParseFile imageProfile;

    Context myContext;
    Activity myActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab3_profile, container, false);
        view.setBackgroundColor(Color.WHITE);
        textViewUserName = view.findViewById(R.id.textViewUserNameProfile);
        textViewEmailAddress = view.findViewById(R.id.textViewEmailAddressProfile);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);
        buttonLogOutProfile = view.findViewById(R.id.buttonLogOutProfile);

        myContext = getContext();
        myActivity = getActivity();

        imageViewProfile.setEnabled(false);
        buttonSaveProfile.setVisibility(View.INVISIBLE);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
        buttonLogOutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });
        imageViewProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                takePhoto();
                return false;
            }
        });

        getCurrentUser();

        return view;
    }

    public void getCurrentUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        textViewUserName.setText(currentUser.getUsername());
        textViewEmailAddress.setText(currentUser.getEmail());
        ParseFile parseFile = currentUser.getParseFile("imageProfile");
        parseFile.getFileInBackground(new GetFileCallback() {
            @Override
            public void done(File file, ParseException e) {
                imageUri = Uri.fromFile(file);
                imageViewProfile.setImageURI(imageUri);
            }
        });

    }

    public void editProfile(){
        if(!imageViewProfile.isEnabled()){
            imageViewProfile.setEnabled(true);
        }
        buttonEditProfile.setVisibility(View.INVISIBLE);
        buttonSaveProfile.setVisibility(View.VISIBLE);
    }

    public void saveProfile(){

        UUID uuid = UUID.randomUUID();
        final String imageName = uuid + ".jpg";

        InputStream iStream;
        if (imageUri != null){
            try {
                iStream =   myActivity.getContentResolver().openInputStream(imageUri);
                byte[] inputData = getBytes(iStream);
                imageProfile = new ParseFile(imageName,inputData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (currentPhotoPath != null){
            File file = new File(currentPhotoPath);
            imageUri = Uri.fromFile(file);
            try {
                iStream =   myActivity.getContentResolver().openInputStream(imageUri);
                byte[] inputData = getBytes(iStream);
                imageProfile = new ParseFile(imageName,inputData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("imageProfile", imageProfile);

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(myContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    ParseFile parseFile = currentUser.getParseFile("imageProfile");
                    parseFile.getFileInBackground(new GetFileCallback() {
                        @Override
                        public void done(File file, ParseException e) {
                            imageUri = Uri.fromFile(file);
                            imageViewProfile.setImageURI(imageUri);
                        }
                    });

                }
            }
        });

        if(imageViewProfile.isEnabled()){
            imageViewProfile.setEnabled(false);
        }
        buttonEditProfile.setVisibility(View.VISIBLE);
        buttonSaveProfile.setVisibility(View.INVISIBLE);
    }

    public void logOut(){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(myContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentToSign = new Intent(myContext, SignInActivity.class);
                    startActivity(intentToSign);
                    myActivity.finish();
                }
            }
        });
    }


    // ADD PHOTO ADD PHOTO ADD PHOTO ADD PHOTO ADD PHOTO ADD PHOTO ADD PHOTO

    public void addPhoto(){
        if(ContextCompat.checkSelfPermission(myContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ADD_PHOTO_REQUEST_CODE);
        } else {
            Intent addPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(addPhotoIntent, ADD_PHOTO_INTENT_REQUEST_CODE);
        }
    }


    // TAKE PHOTO TAKE PHOTO TAKE PHOTO TAKE PHOTO TAKE PHOTO TAKE PHOTO

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = myActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(/* prefix */ imageFileName,/* suffix */".jpg",/* directory */storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(myActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //file to uri // manifest içerisinde authority verilmesi gerekiyor file işlemleri için
                Uri photoURI = FileProvider.getUriForFile(myContext, "com.zoothii.parseproject.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_INTENT_REQUEST_CODE);
            }
        }
    }

    public void takePhoto(){
        if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    // PERMISSION PERMISSION PERMISSION PERMISSION PERMISSION PERMISSION PERMISSION

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ADD_PHOTO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent addPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(addPhotoIntent, ADD_PHOTO_INTENT_REQUEST_CODE);
            }
        } else if (requestCode == TAKE_PHOTO_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PHOTO_INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri);
        } else if (requestCode == TAKE_PHOTO_INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            File file = new File(currentPhotoPath);
            imageViewProfile.setImageURI(Uri.fromFile(file));
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



}
