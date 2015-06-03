package com.camera.demol;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.camera.demol.exception.CameraException;
import com.camera.demol.util.BitmapUtil;
import com.camera.demol.util.CameraUtil;
import com.camera.demol.util.FileUtil;
import com.camera.demol.util.LogM;
import com.camera.demol.view.MySurfaceView;

import java.io.IOException;


public class TestCameraActivity extends Activity {

    private MySurfaceView mSurfaceView;
    private View mTakePicture;
    private View mStopCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);

        mSurfaceView = (MySurfaceView) findViewById(R.id.test_surfaceview);
        mTakePicture = findViewById(R.id.test_takePicture);
        mStopCamera = findViewById(R.id.test_stop);

        CameraUtil cameraUtil = CameraUtil.getUtil();
        int frontCameraId = cameraUtil.getFrontId();
        Camera camera = null;
        try {
            camera = cameraUtil.open(frontCameraId);
            mSurfaceView.setCamera(camera);
        } catch (CameraException e) {
            e.printStackTrace();
            exit();
        }

        mTakePicture.setOnClickListener(mClickListener);
        mStopCamera.setOnClickListener(mClickListener);

    }

    final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.test_takePicture:
                    takePicture();
                    break;
                case R.id.test_stop:
                    exit();
                    break;
            }
        }
    };

    private void takePicture() {
        mSurfaceView.takePicture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                final byte[] datas = bytes;
                String path = Environment.getExternalStorageDirectory().getPath() + "/testpic.jpg";
                printLog(" takePicture onPictureTaken 2, " + bytes.length + "," + path);

                Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
                Bitmap destBmp = BitmapUtil.getRotateBitmap(bitmap, 90);
                BitmapUtil.recyle(bitmap);
                try {
                    FileUtil.saveBitmap(path, destBmp, 80);
                    //FileUtil.saveFile(datas, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSurfaceView.stopPreview();
                mSurfaceView.startPreView();
            }
        });
    }

    private void exit() {
        CameraUtil.getUtil().close();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }

    private void printLog(String log) {
        LogM.i(getClass().getSimpleName(), log);
    }

    private void printLogE(String log) {
        LogM.e(getClass().getSimpleName(), log);
    }

}
