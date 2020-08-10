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
import androidx.viewpager.widget.ViewPager;


import com.parse.GetFileCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class Tab2Upload extends Fragment {

    public static final int ADD_PHOTO_REQUEST_CODE = 111;
    public static final int ADD_PHOTO_INTENT_REQUEST_CODE = 222;
    public static final int TAKE_PHOTO_REQUEST_CODE = 333;
    public static final int TAKE_PHOTO_INTENT_REQUEST_CODE = 444;
    UUID uuid = UUID.randomUUID();
    final String imageName = uuid + ".jpg";


    View view;
    Button button;
    TextView infoText;
    ImageView imageViewUpload;
    ImageView imageViewProfile;
    Uri imageUri;
    String currentPhotoPath;
    InputStream iStream;
    ParseFile imagePost;
    ParseFile imageProfile;
    ParseObject posts;

    Context myContext;
    Activity myActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab2_upload, container, false);
        view.setBackgroundColor(Color.WHITE);
        button = view.findViewById(R.id.buttonUpload);
        infoText = view.findViewById(R.id.infoText);
        imageViewUpload = view.findViewById(R.id.imageViewUpload);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        myContext = getContext();
        myActivity = getActivity();
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });

        imageViewUpload.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                takePhoto();
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void uploadData(){
        button.setVisibility(View.INVISIBLE);
        if (imageUri != null){
            try {
                iStream =   myActivity.getContentResolver().openInputStream(imageUri);
                byte[] inputData = getBytes(iStream);
                imagePost = new ParseFile(imageName,inputData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (currentPhotoPath != null){
            File file = new File(currentPhotoPath);
            imageUri = Uri.fromFile(file);
            try {
                iStream =   myActivity.getContentResolver().openInputStream(imageUri);
                byte[] inputData = getBytes(iStream);
                imagePost = new ParseFile(imageName,inputData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        posts = new ParseObject("Posts");

        ParseUser.getCurrentUser().getParseFile("imageProfile").getFileInBackground(new GetFileCallback() {
            @Override
            public void done(File file, ParseException e) {
                if (e != null) {
                    Toast.makeText(myContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    imageUri = Uri.fromFile(file);
                    try {
                        iStream =   myActivity.getContentResolver().openInputStream(imageUri);
                        byte[] inputData = getBytes(iStream);
                        imageProfile = new ParseFile(imageName,inputData);
                        posts.put("imageProfile",imageProfile);
                        posts.put("imagePost", imagePost);
                        posts.put("username", ParseUser.getCurrentUser().getUsername());
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy" +
                                "\n      HH:mm");
                        LocalDateTime now = LocalDateTime.now();

                        posts.put("date", dtf.format(now));

                        Date date = new Date();
                        posts.put("dateDate", date);

                        posts.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    Toast.makeText(myContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(myContext, "Shared", Toast.LENGTH_SHORT).show();
                                    imageViewUpload.setImageResource(R.drawable.imagebutton);
                                    infoText.setText("Long click to take selfie");
                                }
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
                ex.printStackTrace();
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
            infoText.setText("");
            imageViewUpload.setImageURI(imageUri);
            button.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_PHOTO_INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            File file = new File(currentPhotoPath);
            infoText.setText("");
            imageViewUpload.setImageURI(Uri.fromFile(file));
            button.setVisibility(View.VISIBLE);
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
