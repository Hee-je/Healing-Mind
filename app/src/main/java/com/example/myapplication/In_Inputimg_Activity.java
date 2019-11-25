package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.sql.Types.NULL;

public class In_Inputimg_Activity extends Activity {
    static final int camera = 2001;
    static final int gallery = 2002;
    LinearLayout linear;
    Button camerapopBtn, gallerypopBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_inputimg_activity);
        camerapopBtn = (Button) findViewById(R.id.camerapopBtn);
        gallerypopBtn = (Button) findViewById(R.id.gallerypopBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(01);
                setResult(NULL);
                finish();
            }
        });
        camerapopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, camera);
            }
        });
        gallerypopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, gallery);
            }
        });
    }

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        Bitmap bm;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case camera:
                    bm = (Bitmap) data.getExtras().get("data");
                    bm = resize(bm);
                    intent.putExtra("bitmap", bm);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case gallery:
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        bm = resize(bm);
                        intent.putExtra("bitmap", bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;
            }
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
