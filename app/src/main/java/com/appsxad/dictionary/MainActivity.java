package com.appsxad.dictionary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.appsxad.dictionary.Adapters.MeaningAdpater;
import com.appsxad.dictionary.Adapters.PhoneticsAdapter;
import com.appsxad.dictionary.Models.APIResponse;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

public class MainActivity extends AppCompatActivity {

    SearchView search_view;
    TextView textView_word;
    RecyclerView recycle_phonetics, recycle_meanings;
    ProgressDialog progressDialog;
    PhoneticsAdapter phoneticsAdapter;
    MeaningAdpater meaningAdpater;

    //unity inter 1
    private String GameID = "4739223";
    private String interPlacement="Interstitial_Android";
    private boolean testMode = false;
    //unity inter 1



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//unity inter 2
        UnityAds.initialize(this, GameID, testMode);
        IUnityAdsListener interListner = new IUnityAdsListener() {
            @Override
            public void onUnityAdsReady(String s) {

            }

            @Override
            public void onUnityAdsStart(String s) {

            }

            @Override
            public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

            }

            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

            }
        };
        UnityAds.setListener(interListner);
        UnityAds.load(interPlacement);
        //unity inter 2
        //intenet dialog box 1

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        //intenet dialog box 1

        search_view = findViewById(R.id.search_view);
        textView_word = findViewById(R.id.textView_word);
        recycle_phonetics = findViewById(R.id.recycle_phonetics);
        recycle_meanings = findViewById(R.id.recycle_meanings);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading...");
        progressDialog.show();
        RequestManager manager = new RequestManager(MainActivity.this);
        manager.getWordMeaning(listener, "Hello");

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {



            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching response for "+query);
                progressDialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getWordMeaning(listener, query);
                //unity inter 3

                if (UnityAds.isReady(interPlacement))
                {
                    UnityAds.show(MainActivity.this,interPlacement);
                }

                //unity inter 3
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //intenet dialog box 2

    private boolean isConnected(MainActivity mainActivity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo moblieConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() || (moblieConn != null && moblieConn.isConnected())))
        {
            return true;
        }
        else
        {
            return false;
        }


    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //intenet dialog box 2

    private final OnFetchDataListener listener = new OnFetchDataListener() {
        @Override
        public void onFetchData(APIResponse apiResponse, String message) {
            progressDialog.dismiss();
            if (apiResponse==null){
                Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
                return;
            }
            showData(apiResponse);
        }

        @Override
        public void onError(String message) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(APIResponse apiResponse) {
        textView_word.setText("Word: "+apiResponse.getWord());
        recycle_phonetics.setHasFixedSize(true);
        recycle_phonetics.setLayoutManager(new GridLayoutManager(this, 1));
        phoneticsAdapter = new PhoneticsAdapter(this, apiResponse.getPhonetics());
        recycle_phonetics.setAdapter(phoneticsAdapter);

        recycle_meanings.setHasFixedSize(true);
        recycle_meanings.setLayoutManager(new GridLayoutManager(this, 1));
        meaningAdpater = new MeaningAdpater(this, apiResponse.getMeanings());
        recycle_meanings.setAdapter(meaningAdpater);
    }
}