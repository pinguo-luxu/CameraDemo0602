package com.camera.demol.util;

import android.hardware.Camera;

import com.camera.demol.exception.CameraException;

/**
 * Created by luxu on 15-6-2.
 */
public class CameraUtil {

    private static CameraUtil mUtil = null;

    private Camera mCamera;

    private int mFrontCameraId = -1;
    private int mBackCameraId = -1;
    private int mCurrCamearId = -1;

    public static synchronized CameraUtil getUtil() {
        if (mUtil == null) {
            mUtil = new CameraUtil();
        }
        return mUtil;
    }

    private CameraUtil() {
        init();
    }

    private void init() {
        int num = getNumOfCameras();

        for (int i = 0; i < num; i++) {
            int cameraId = i;
            Camera.CameraInfo cInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cInfo);

            if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = cInfo.facing;
            } else if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = cInfo.facing;
            }
        }
    }

    private int getNumOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public int getBackId(){
        return mBackCameraId;
    }

    public int getFrontId(){
        return mFrontCameraId;
    }

    public Camera open(int cameraId) throws CameraException{
        if(cameraId < 0){
            throw new CameraException(" paramater cameraId  ");
        }
        printLog(" camera open 1 ");
        mCamera = Camera.open(cameraId);
        printLog(" camera open 2 ");
        mCurrCamearId = cameraId;
        return mCamera;
    }

    public void close(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        mCurrCamearId = -1;
    }

    public void startPreView(){
        if(mCamera == null){
            printLogE(" mCamera is null ");
            return;
        }
        mCamera.startPreview();
    }

    public void stopPreView(){
        if(mCamera == null){
            printLogE(" mCamera is null ");
            return;
        }
        mCamera.stopPreview();
    }

    private void printLog(String log){
        LogM.i(getClass().getSimpleName(), log);
    }

    private void printLogE(String log){
        LogM.e(getClass().getSimpleName(), log);
    }

}
