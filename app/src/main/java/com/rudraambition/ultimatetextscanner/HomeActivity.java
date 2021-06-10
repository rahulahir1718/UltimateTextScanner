package com.rudraambition.ultimatetextscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.rudraambition.ultimatetextscanner.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private AdView banner_h1;
    private AdView banner_h2;
    private InterstitialAd interstitialAd;


    private CircleImageView cameraImage;
    private CircleImageView galaryImage;
    private boolean doubleBackToExitPressesdOnce = false;

    private LayoutInflater toastInflater;
    private View toastLayout;
    private TextView toastTextView;


    private Boolean CameraIntent=false;
    private Boolean GalleryIntent=false;

    private int REQUEST_CODE=18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppLovinSdk.getInstance(HomeActivity.this);
        AppLovinSdk.initializeSdk(HomeActivity.this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration config) {
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        banner_h1=(AdView)findViewById(R.id.banner_h1);
        AdRequest adRequest1=new AdRequest.Builder().build();
        banner_h1.loadAd(adRequest1);

        banner_h2=(AdView)findViewById(R.id.banner_h2);
        AdRequest adRequest2=new AdRequest.Builder().build();
        banner_h2.loadAd(adRequest2);

        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2507491428423834/3046443583");
        interstitialAd.loadAd(new AdRequest.Builder().build());


        interstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(CameraIntent)
                {
                    sendIntent(R.id.cameraImage);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else if(GalleryIntent)
                {
                    sendIntent(R.id.galaryImage);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });


        cameraImage = (CircleImageView) findViewById(R.id.cameraImage);
        galaryImage = (CircleImageView) findViewById(R.id.galaryImage);



        toastInflater = getLayoutInflater();
        toastLayout = toastInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        toastTextView = (TextView) toastLayout.findViewById(R.id.toastTextView);


        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraIntent=true;
                GalleryIntent=false;
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    sendIntent(R.id.cameraImage);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        galaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GalleryIntent=true;
                CameraIntent=false;
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    sendIntent(R.id.galaryImage);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });



        final AppUpdateManager appUpdateManager= AppUpdateManagerFactory.create(HomeActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask=appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if(result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE||result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
                {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.FLEXIBLE,HomeActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void sendIntent(int Id) {

        Intent intent = new Intent(HomeActivity.this, MainActivity2.class);
        intent.putExtra("ID", Id);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void makeMyToast(String s) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toastTextView.setText(s);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.aboutUs)
        {
            startActivity(new Intent(HomeActivity.this,About_Us.class));
            return true;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==requestCode)
        {
            makeMyToast("Download Start");
            if(resultCode!=RESULT_OK)
            {
                makeMyToast("Download Failed");
            }
        }
    }
}