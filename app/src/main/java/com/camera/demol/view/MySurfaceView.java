package com.camera.demol.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.camera.demol.util.LogM;

import java.io.IOException;

/**
 * Created by luxu on 15-6-2.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        printLog(" surfaceCreated ");
        try {
            mCamera.setPreviewDisplay(holder);
            startPreView();
        } catch (IOException e) {
            printLogE("Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        printLog(" surfaceDestroyed ");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        printLog(" surfaceChanged ");
        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            stopPreview();
        } catch (Exception e) {
            printLogE("surfaceChanged: " + e.getMessage());
        }

        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                    printLog(" pcb onPreviewFrame " + bytes + ", " + camera + "," + mCamera);
                }
            });
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                    printLog(" pcbbuffer onPreviewFrame " + bytes + ", " + camera + "," + mCamera);
                }
            });


            startPreView();


            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    printLog(" onAutoFocus " + b + ", " + camera + "," + mCamera);
                }
            });
            mCamera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int i, Camera camera) {
                    printLog(" onError " + i + ", " + camera + "," + mCamera);
                }
            });
            mCamera.setZoomChangeListener(new Camera.OnZoomChangeListener() {
                @Override
                public void onZoomChange(int i, boolean b, Camera camera) {
                    printLog(" onZoomChange " + i + "," + b + ", " + camera + "," + mCamera);
                }
            });

        } catch (Exception e) {
            printLogE("Error starting camera preview: " + e.getMessage());
        }
    }

    public void stopPreview() {
        mCamera.stopPreview();
    }

    public void startPreView() {
        mCamera.startPreview();
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    public void takePicture(Camera.PictureCallback pictureCallback) {
        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                printLog(" takePicture onShutter");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                printLog(" takePicture onPictureTaken 1, " + bytes + "," + camera);
            }
        }, pictureCallback);
    }

    private void printLog(String log) {
        LogM.i(getClass().getSimpleName(), log);
    }

    private void printLogE(String log) {
        LogM.e(getClass().getSimpleName(), log);
    }

}
