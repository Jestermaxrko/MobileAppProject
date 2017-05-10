
package com.example.max.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static com.example.max.myapp.Upload.createTempImageFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference myRefName;
    private DatabaseReference myRefRating;
    private DatabaseReference myRefMaxValue;
    private  DatabaseReference myRefURL;
    private static final String TAG = "MainActivity";


    private StorageReference mStorageRef;

    //FirebaseUser user = mAuth.getInstance().getCurrentUser();
    int indx;
    Integer max_value;
    int last_id;
    String max_val;
    boolean in;
    List<String> url;
    List<String> names;
    List<Integer> ratings;
    String load_name;
    LinearLayout line;
    ImageButton upload_btn;
    ImageButton logout_btn;
    Vector<View> separator;
    Vector<TextView> name_pic;
    Vector<TextView> rating;
    Vector<ImageView> image;
    Vector<LinearLayout> layout;
    Vector<LinearLayout> but_layout;
    Vector<ImageButton> like;
    Vector<ImageButton> dislike;
    Vector<ImageButton>download;
    int rate[];
    int len;


    public static final String IMAGE_URL = "http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        line = (LinearLayout) findViewById(R.id.line);
        upload_btn =(ImageButton)findViewById(R.id.upload);
        logout_btn = (ImageButton)findViewById(R.id.logout);
        upload_btn.setOnClickListener(add_to);
        logout_btn.setOnClickListener(exit);
        mAuth = FirebaseAuth.getInstance();


        indx=0;
        in = false;
        len=20;

        //
        //
        //add(0,);
        //len=10;
        //Firabase




        myRefName = FirebaseDatabase.getInstance().getReference();
        myRefRating = FirebaseDatabase.getInstance().getReference();
        myRefMaxValue = FirebaseDatabase.getInstance().getReference("max_value");
        myRefURL = FirebaseDatabase.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();



        myRefURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> text = new GenericTypeIndicator<List<String>>(){};
                url=dataSnapshot.child("URL").getValue(text);
                //max_value = dataSnapshot.getValue(Integer.class);
                UpdateUI();
            }

            private void UpdateUI() {
                for(int i=0;i<len;i++) {
                    Glide.with(MainActivity.this /* context */).using(new FirebaseImageLoader())
                            .load(mStorageRef = FirebaseStorage.getInstance().getReference(("images/" + Integer.toString(i)+".png")))
                            .into(image.elementAt(i));
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> text = new GenericTypeIndicator<List<String>>(){};
                names=dataSnapshot.child("Name").getValue(text);
                //max_value = dataSnapshot.getValue(Integer.class);
                UpdateUI();
            }

            private void UpdateUI() {
                for(int i=0;i<len;i++) {
                    name_pic.elementAt(i).setText(names.get(i));
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRefRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Integer>> value = new GenericTypeIndicator<List<Integer>>(){};
                ratings = dataSnapshot.child("Rating").getValue(value);
                //max_value = dataSnapshot.getValue(Integer.class);
                UpdateUI();
            }

            private void UpdateUI() {

                for(int i=0;i<len;i++) {
                    rate[i]=ratings.get(i);
                    rating.elementAt(i).setText("Rating : "+ Integer.toString(rate[i]));
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRefMaxValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                max_value = dataSnapshot.getValue(Integer.class);
                UpdateUI();

            }


            private void UpdateUI() {
                len=max_value+1;
                fill_data();
                add_to_layout(indx,len);
                indx=len;
                //recreate();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //
    }

    public void fill_data(){

        separator = new Vector<View>();
        layout = new Vector<LinearLayout>();
        but_layout = new Vector<LinearLayout>();
        like = new Vector<ImageButton>();
        dislike = new Vector<ImageButton>();
        download=new Vector<ImageButton>();
        name_pic = new Vector<TextView>();
        rating = new Vector<TextView>();
        image = new Vector<ImageView>();
        rate = new int[len];


        for (int i = 0; i < len; i++) {

            separator.add(new View(this));
            layout.add(new LinearLayout(this));
            but_layout.add(new LinearLayout(this));
            like.add(new ImageButton(this));
            dislike.add(new ImageButton(this));
            name_pic.add(new TextView(this));
            rating.add(new TextView(this));
            image.add(new ImageView(this));
            download.add(new ImageButton(this));

            //Picasso.with(this).load(IMAGE_URL).into(image.elementAt(i));
            //get_image(i);

            layout.elementAt(i).setOrientation(LinearLayout.VERTICAL);
            but_layout.elementAt(i).setOrientation(LinearLayout.HORIZONTAL);

            separator.elementAt(i).setBackgroundColor(Color.BLACK);

            name_pic.elementAt(i).setId(i);
            name_pic.elementAt(i).setTypeface(Typeface.DEFAULT_BOLD);
            name_pic.elementAt(i).setTextColor(Color.parseColor("#303030"));
            name_pic.elementAt(i).setTextSize(16);
            rating.elementAt(i).setTextSize(16);
            rating.elementAt(i).setGravity(Gravity.CENTER_VERTICAL);

            download.elementAt(i).setId(i);
            image.elementAt(i).setId(i);
            like.elementAt(i).setId(i);
            dislike.elementAt(i).setId(i);
            rating.elementAt(i).setId(i);
            download.elementAt(i).setBackgroundColor(Color.TRANSPARENT);
            like.elementAt(i).setBackgroundColor(Color.TRANSPARENT);
            dislike.elementAt(i).setBackgroundColor(Color.TRANSPARENT);
            like.elementAt(i).setImageResource(R.drawable.like);
            dislike.elementAt(i).setImageResource(R.drawable.dislike);
            download.elementAt(i).setImageResource(R.drawable.download);

            //like.elementAt(i).setText("Like");
            //dislike.elementAt(i).setText("Dislike");
            //name_pic.elementAt(i).setText(s);
            //rating.elementAt(i).setText("Rating : "+Integer.toString(rate[i]));
        }
    }

    public void add_to_layout(int start, int end){
        RelativeLayout.LayoutParams rlParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams dislikeParam = new RelativeLayout.LayoutParams(
                100, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams likeParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams imgParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams nameParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams ratingParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams sepParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, 1);
        RelativeLayout.LayoutParams downparams = new RelativeLayout.LayoutParams(
                100, RelativeLayout.LayoutParams.WRAP_CONTENT);

        downparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        for(int i=start;i<end;i++){
            layout.elementAt(i).setLayoutParams(rlParam);
            like.elementAt(i).setOnClickListener(this);
            dislike.elementAt(i).setOnClickListener(Dislike);
            download.elementAt(i).setOnClickListener(Down);

            but_layout.elementAt(i).addView(like.elementAt(i), likeParam);
            but_layout.elementAt(i).addView(dislike.elementAt(i), dislikeParam);
            but_layout.elementAt(i).addView(download.elementAt(i),downparams);
            but_layout.elementAt(i).addView(rating.elementAt(i), ratingParam);

            layout.elementAt(i).addView(name_pic.elementAt(i), nameParam);
            layout.elementAt(i).addView(image.elementAt(i), imgParam);
            layout.elementAt(i).addView(but_layout.elementAt(i));
            layout.elementAt(i).addView(separator.elementAt(i), sepParam);
            line.addView(layout.elementAt(i));
        }

    }

    public void onClick(View v) {
        int id = v.getId();

        rate[id]++;
        rating.elementAt(id).setText("Rating : "+Integer.toString(rate[id]));
        myRefRating.child("Rating").child(Integer.toString(id)).setValue(rate[id]);
        //rating.elementAt(id).setText("Rating : " + Integer.toString(rate[id]));
    }

    OnClickListener Dislike = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            rate[id]--;
            rating.elementAt(id).setText("Rating : "+Integer.toString(rate[id]));
            myRefRating.child("Rating").child(Integer.toString(id)).setValue(rate[id]);

        }
    };

    OnClickListener Down = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id  =v.getId();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();;
            File localFile = null;

            try {
                localFile = createTempImageFile(getExternalCacheDir());
                final File finalLocalFile = localFile;

                mStorageRef.child("images/" +Integer.toString(id)+".png").getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(MainActivity.this, "Збережено", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    OnClickListener add_to = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),Upload.class);
            intent.putExtra("max_value",max_value);
            startActivityForResult(intent, 1);
            //len=0;
            //myRef.child("Name").child(Integer.toString(max_value+1)).setValue("TEst");
            //myRef.child("Rating").child(Integer.toString(max_value+1)).setValue(1000);
            //myRefMaxValue.setValue(max_value+1);

        }
    };


    OnClickListener exit = new OnClickListener() {
        @Override
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            finish();

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String name = data.getStringExtra("name");
        String url = data.getStringExtra("link");
        //upload_btn.setText(url);
        myRef.child("Name").child(Integer.toString(max_value+1)).setValue(name);
        myRef.child("Rating").child(Integer.toString(max_value+1)).setValue(0);
        myRef.child("URL").child(Integer.toString(max_value+1)).setValue(url);
        myRefMaxValue.setValue(max_value+1);

        // /fill_data();

    }

}


