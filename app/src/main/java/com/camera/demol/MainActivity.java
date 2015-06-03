package com.camera.demol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    private View mTestCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestCamera = findViewById(R.id.test_camera);
        mTestCamera.setOnClickListener(mClickListener);



    }

    final View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            startTestCamera();

        }
    };

    private void startTestCamera(){
        Intent intent = new Intent(this, TestCameraActivity.class);
        startActivity(intent);
    }

}
