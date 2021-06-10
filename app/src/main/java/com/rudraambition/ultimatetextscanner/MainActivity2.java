package com.rudraambition.ultimatetextscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.rudraambition.ultimatetextscanner.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.Delayed;

public class MainActivity2 extends AppCompatActivity {


    private AdView adView;
    private AdView banner_m1;
    private InterstitialAd interstitialAd;

    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=300;
    private static final int IMAGE_PICK_GALLARY_CODE=400;
    private static final int IMAGE_PICK_CAMERA_CODE=500;

    String [] cameraPermossion;
    String [] storagePermission;


    Uri imageUri;

     private  EditText editText;
    private ImageView imageView;
    private ImageView copyImageView;
    private ImageView speaker;
    private Button clearButton;
    private Button saveButton;
    private Button shareButton;
    private Button changeButton1;
    private Button changeButton2;
    private Boolean joinText;
    private Uri resultUri;
    private Context context;

    private EditText filename;
    private Button Cancel;
    private Button Save;

    private LayoutInflater toastInflater;
    private View toastLayout;
    private TextView toastTextView;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textToSpeech=new TextToSpeech(MainActivity2.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                   int result= textToSpeech.setLanguage(Locale.US);

                   if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                   {
                       makeMyToast("Language Not Supported");
                   }
                }
                else
                {
                    makeMyToast("Intialization failed");
                }
            }
        });




        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toastInflater=getLayoutInflater();
        toastLayout=toastInflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_layout_id));
        toastTextView=(TextView)toastLayout.findViewById(R.id.toastTextView);


        joinText=false;
        context=this;
        editText=(EditText)findViewById(R.id.editText);
        copyImageView=(ImageView)findViewById(R.id.copyImageView);
        imageView=(ImageView)findViewById(R.id.imageView);
        speaker=(ImageView)findViewById(R.id.speaker);


        clearButton=(Button)findViewById(R.id.clearButton);
        saveButton=(Button)findViewById(R.id.saveButton);
        shareButton=(Button)findViewById(R.id.shareButton);
        changeButton1=(Button)findViewById(R.id.changeButton1);
        changeButton2=(Button)findViewById(R.id.changeButton2);
        cameraPermossion=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        if(savedInstanceState!=null)
        {
            Uri saveUri=savedInstanceState.getParcelable("Uri");
            imageView.setImageURI(saveUri);
            editText.setText(savedInstanceState.getString("Result"));
        }
        else
        {
        int id=getIntent().getExtras().getInt("ID");
        switch(id)
        {
            case R.id.cameraImage:
                if(!checkCameraPermission())
                {
                requestCameraPermission();
                }
            else
                {
                pickCamera();
                }
            break;
            case R.id.galaryImage:
                if(!checkStoragePermission())
                {
                    requestStoragePermission();
                }
                else{
                    pickgallary();
                }
            break;
        }

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                    makeMyToast("Complete");
                }
            });
            adView=(AdView)findViewById(R.id.adView);
            AdRequest adRequest1=new AdRequest.Builder().build();
            adView.loadAd(adRequest1);

            banner_m1=(AdView)findViewById(R.id.banner_m1);
            AdRequest adRequest2=new AdRequest.Builder().build();
            banner_m1.loadAd(adRequest2);


            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });
            interstitialAd=new InterstitialAd(this);
            interstitialAd.setAdUnitId("ca-app-pub-2507491428423834/7958314325");
            interstitialAd.loadAd(new AdRequest.Builder().build());


            copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                {
                    makeMyToast("Please detect some text and then after try to copy");
                }
                else
                {
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("",editText.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                makeMyToast("Copied on clipboard");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(null);
                imageView.setImageBitmap(null);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String linkText = "Download Application From Playstore:\n\n";
                String link = "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n\n";
                String text = editText.getText().toString();
                final String message = linkText + link + text;

                interstitialAd.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        sendIntent(message);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                });

                if (text.isEmpty()) {
                    makeMyToast("Nothing To Share,Please Detect Some Text");
                } else {

                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        sendIntent(message);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().isEmpty()) {
                    makeMyToast("Nothing To Save ,Please Detect Some Text First");
                } else {
                    try {
                        LayoutInflater li = LayoutInflater.from(MainActivity2.this);
                        View promptView = li.inflate(R.layout.prompt_layout, null);

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity2.this);
                        alert.setView(promptView);

                        filename = (EditText) promptView.findViewById(R.id.promptEditText);
                        Cancel = (Button) promptView.findViewById(R.id.promptCancleButton);
                        Save = (Button) promptView.findViewById(R.id.promptSaveButton);

                        alert.setCancelable(false);

                        final AlertDialog alertDialog = alert.create();

                        Cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        Save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fileName = filename.getText().toString() + ".txt";
                                if (fileName.isEmpty()) {
                                    makeMyToast("Please Enter File Name");
                                } else {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UTC-Ultimate Text Scanner/";
                                    try {
                                        File root = new File(path);
                                        if (!root.exists()) {
                                            root.mkdirs();
                                        }
                                        File newfile = new File(path + fileName);
                                        newfile.createNewFile();
                                        if (newfile.exists()) {
                                            OutputStream out=new FileOutputStream(newfile);
                                            out.write(editText.getText().toString().getBytes());
                                            out.close();
                                            makeMyToast(newfile.getAbsolutePath());
                                        }
                                        alertDialog.dismiss();
                                    } catch (Exception e) {
                                        makeMyToast("" + e);
                                    }
                                }
                            }
                        });
                        alertDialog.show();
                    } catch (Throwable t) {
                        makeMyToast("Exception:" + t.toString());
                    }
                }
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty())
                {
                    makeMyToast("Please Detect Some Text First");
                }
                else
                {
                    LayoutInflater inflater=LayoutInflater.from(MainActivity2.this);
                    View myView=inflater.inflate(R.layout.speech_layout,null);

                    AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity2.this);
                    alert1.setView(myView);


                    alert1.setCancelable(false);

                    final Button cancel=(Button)myView.findViewById(R.id.cancelButton);
                    ImageView micImage=(ImageView)myView.findViewById(R.id.mic_image);

                    final AnimationDrawable animationDrawable=new AnimationDrawable();
                    animationDrawable.addFrame(getResources().getDrawable(R.drawable.v1),1000);
                    animationDrawable.addFrame(getResources().getDrawable(R.drawable.v2),1000);
                    animationDrawable.addFrame(getResources().getDrawable(R.drawable.speaker),1000);
                    micImage.setImageDrawable(animationDrawable);
                    animationDrawable.start();

                    final AlertDialog alertDialog1 = alert1.create();
                    alertDialog1.setCancelable(true);
                    alertDialog1.show();

                    textToSpeech.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
                    final Handler handler=new Handler();
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            if(!textToSpeech.isSpeaking())
                            {
                               textToSpeech.stop();
                               alertDialog1.dismiss();
                            }
                            handler.postDelayed(this,1000);
                        }
                    };
                    handler.postDelayed(runnable,1000);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog1.dismiss();
                            textToSpeech.stop();
                        }
                    });
                    alertDialog1.setOnDismissListener(new AlertDialog.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            textToSpeech.stop();
                        }
                    });
                }
            }
        });

        changeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinText=false;
                String [] s={"Camera","Gallery"};
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity2.this);
                alert.setTitle("Select Image");
                alert.setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            if(!checkCameraPermission())
                            {
                                requestCameraPermission();
                            }
                            else
                            {
                                pickCamera();
                            }
                        }
                        if(which==1)
                        {
                            if(!checkStoragePermission())
                            {
                                requestStoragePermission();
                            }
                            else{
                                pickgallary();
                            }
                        }
                    }
                });
                alert.create().show();
            }
        });

            changeButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinText=true;
                    String [] s={"Camera","Gallery"};
                    AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity2.this);
                    alert.setTitle("Select Image");
                    alert.setItems(s, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0)
                            {
                                if(!checkCameraPermission())
                                {
                                    requestCameraPermission();
                                }
                                else
                                {
                                    pickCamera();
                                }
                            }
                            if(which==1)
                            {
                                if(!checkStoragePermission())
                                {
                                    requestStoragePermission();
                                }
                                else{
                                    pickgallary();
                                }
                            }
                        }
                    });
                    alert.create().show();
                }
            });
        }
    }

    private void sendIntent(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private void makeMyToast(String s) {
        Toast toast=new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toastTextView.setText(s);
        toast.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Result",editText.getText().toString());
        outState.putParcelable("Uri",resultUri);
    }

    private void pickgallary() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLARY_CODE);
    }

    private void pickCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");
        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermossion,CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result&&result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && cameraStorageAccepted) {
                        pickCamera();
                    } else {
                        makeMyToast("Permission Denied");
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0) {
                    boolean cwriteStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cwriteStorageAccepted) {
                        pickgallary();
                    } else {
                        makeMyToast("Permission Denied");
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            if (requestCode == IMAGE_PICK_GALLARY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imageView.setImageURI(resultUri);

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!textRecognizer.isOperational()) {
                    makeMyToast("Error");
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < sparseArray.size(); i++) {
                        TextBlock item = sparseArray.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    if(joinText==false) {
                        editText.setText(stringBuilder.toString());
                    }
                    else{
                        String oldText=editText.getText().toString()+"\n";
                        editText.setText(oldText+stringBuilder.toString());
                    }
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                makeMyToast("" + error);
            }
        }
    }
}