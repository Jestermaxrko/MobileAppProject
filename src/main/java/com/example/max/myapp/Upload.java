package com.example.max.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Upload extends AppCompatActivity implements View.OnClickListener{

    private ImageView mIVpicture;

    private EditText name_txt;

    private Button mBTNaddPicture;

    private Button save_btn;

   String donwoldUri;

    private File mTempPhoto;

    private String mImageUri = "";

    private Uri uri;

    int max_value;
    Intent intent;
    private boolean gallery;
    private  boolean camera;

    private String mRereference = "";

    private static final int REQUEST_CODE_PERMISSION_RECEIVE_CAMERA = 102;
    private static final int REQUEST_CODE_TAKE_PHOTO = 103;

    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        intent = new Intent();
        mIVpicture = (ImageView) findViewById(R.id.iv_piture);
        mBTNaddPicture = (Button) findViewById(R.id.btn_add_picture);
        save_btn = (Button)findViewById(R.id.save_btn);
        name_txt = (EditText)findViewById(R.id.name_txt);

        max_value = getIntent().getIntExtra("max_value",0);

        //save_btn.setText(Integer.toString(max_value));

        mBTNaddPicture.setOnClickListener(this);
        save_btn.setOnClickListener(SaveToFB);

        File localFile = null;
        gallery = false;
        camera = false;

        mRereference = getIntent().getStringExtra("Reference");
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }


    private void addPhoto() {


        boolean isCameraPermissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean isWritePermissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


        if(!isCameraPermissionGranted || !isWritePermissionGranted) {

            String[] permissions;

            if (!isCameraPermissionGranted && !isWritePermissionGranted) {
                permissions = new String[] {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            } else if (!isCameraPermissionGranted) {
                permissions = new String[] {android.Manifest.permission.CAMERA};
            } else {
                permissions = new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }

            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_RECEIVE_CAMERA);
        } else {

            try {
                mTempPhoto = createTempImageFile(getExternalCacheDir());
                mImageUri = mTempPhoto.getAbsolutePath();


                List<Intent> intentList = new ArrayList<>();
                Intent chooserIntent = null;


                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                takePhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempPhoto));

                intentList = addIntentsToList(this, intentList, pickIntent);
                intentList = addIntentsToList(this, intentList, takePhotoIntent);

                if (!intentList.isEmpty()) {
                    chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),"Choose your image source");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
                }


                startActivityForResult(chooserIntent, REQUEST_CODE_TAKE_PHOTO);
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }


    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }


    public static File createTempImageFile(File storageDir) throws IOException {

        // Генерируем имя файла
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());//получаем время
        String imageFileName = "photo_" + timeStamp;//состовляем имя файла

        //Создаём файл
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    public static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_picture){
            addPhoto();
        }
    }

    View.OnClickListener SaveToFB = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(gallery)
                uploadFileInFireBaseStorage(uri);
            if(camera)
                uploadFileInFireBaseStorage(Uri.fromFile((mTempPhoto)));

            if(camera || gallery){
                intent.putExtra("name", name_txt.getText().toString());
                setResult(RESULT_OK, intent);
                //finish();
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        switch (requestCode){
            case REQUEST_CODE_TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        mImageUri = getRealPathFromURI(data.getData());
                        uri = data.getData();

                        Picasso.with(getBaseContext())
                                .load(data.getData())
                                .into(mIVpicture);
                        gallery = true;
                        camera = false;
                        //uploadFileInFireBaseStorage(uri);
                    } else if (mImageUri != null) {
                        mImageUri = Uri.fromFile(mTempPhoto).toString();

                        Picasso.with(this)
                                .load(mImageUri)
                                .into(mIVpicture);
                        gallery = false;
                        camera=true;
                        //uploadFileInFireBaseStorage(Uri.fromFile((mTempPhoto)));
                    }
                }
                break;
        }
    }
    public void uploadFileInFireBaseStorage (Uri uri){
        UploadTask uploadTask = mStorageRef.child("images/" + Integer.toString(max_value+1)+".png").putFile(uri);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred());
                Log.i("Load","Upload is " + progress + "% done");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                donwoldUri = taskSnapshot.getMetadata().getDownloadUrl().toString();
                intent.putExtra("link",donwoldUri);
                Log.i("Load" , "Uri donwlod" + donwoldUri);
                Toast.makeText(Upload.this, "Завантажено", Toast.LENGTH_SHORT).show();
            }
        });
    }
}