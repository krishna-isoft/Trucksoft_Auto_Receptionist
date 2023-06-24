package com.isoft.trucksoft_autoreceptionist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.isoft.trucksoft_autoreceptionist.service.Constant;

import java.io.ByteArrayOutputStream;
import java.util.List;

//import android.util.Log;

public class CameraView extends Activity implements SurfaceHolder.Callback, OnClickListener {
	  private static final String TAG = "CameraTest";
     // Camera mCamera;
      static Camera mCamera = null;
      boolean mPreviewRunning = false;
      Context context;
  	private Preference pref;
  	private int i=0;
  	 boolean mboolthrad = true;
	//private int j=0;
	//private int k=0;
	//int jk=0;
	

      @SuppressWarnings("deprecation")
      public void onCreate(Bundle icicle){
          super.onCreate(icicle);
         // Log.e(TAG, "onCreatelllllllllll");
          context=this;
          pref=Preference.getInstance(context);
          getWindow().setFormat(PixelFormat.TRANSLUCENT);
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.cameraview);
        // ImageView img = (ImageView) findViewById(R.id.blankImage);
          
          /*if(checkCameraHardware(context))  {
              mCamera = getCameraInstance();
          }*/

          /*if(Home.isBlack)
              img.setBackgroundResource(android.R.color.black);
          else
              img.setBackgroundResource(android.R.color.white);
*/
          //img.setBackgroundResource(android.R.color.white);
         
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
          mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
          mSurfaceView.setOnClickListener(this);
          mSurfaceHolder = mSurfaceView.getHolder();
         mSurfaceHolder.addCallback(this);
         
       //  mCamera = getCameraInstance();
         
			 mCamera = Camera.open();

        
         int cameraCount = 0;
         
         Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
         cameraCount = Camera.getNumberOfCameras();
         for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
             Camera.getCameraInfo( camIdx, cameraInfo );
             if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
                 try {
                     mCamera = Camera.open( camIdx );
                     
                    // Log.e("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", ""+camIdx);
                 } catch (RuntimeException e) {
                     Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                 }
             }
         }

         
         // Create our Preview view and set it as the content of our activity.
         
     
        
         
         if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
         {
        	 
        	 mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      }
         mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

      }
       @Override
      protected void onRestoreInstanceState(Bundle savedInstanceState){
          super.onRestoreInstanceState(savedInstanceState);
      }


      Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

          public void onPictureTaken(byte[] data, Camera camera) {
              // TODO Auto-generated method stub
              if (data != null){
                  //Intent mIntent = new Intent();
                  //mIntent.putExtra("image",imageData);
            	  /*mCamera.setPreviewCallback(null);
                 mCamera.stopPreview();
                  mPreviewRunning = false;
                  mCamera.release();*/
            	//  mPreviewRunning = false;
            	 // if (mPreviewRunning){
            	//  Log.e("43", "t"+mPreviewRunning);
            	  
            	  if (mPreviewRunning){
    	              mCamera.stopPreview();
    	              mPreviewRunning = false;
    	          }
            	  
            		  releaseCamera();
            	
                   try{
                       BitmapFactory.Options opts = new BitmapFactory.Options();
                       Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length,opts);
                       bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                       int width = bitmap.getWidth();
                       int height = bitmap.getHeight();
                       int newWidth = 300;
                       int newHeight = 300;

                       // calculate the scale - in this case = 0.4f
                       float scaleWidth = ((float) newWidth) / width;
                       float scaleHeight = ((float) newHeight) / height;

                       // createa matrix for the manipulation
                       Matrix matrix = new Matrix();
                       // resize the bit map
                       matrix.postScale(scaleWidth, scaleHeight);
                       // rotate the Bitmap
                       matrix.postRotate(-90);
                       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                               width, height, matrix, true);
                     //  Log.e("resizedBitmap", "*********"+resizedBitmap.toString());
                      // Home.pphoto.setImageBitmap(resizedBitmap);
                       ByteArrayOutputStream bos = new ByteArrayOutputStream();

                       resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


       				Home_page.pikimg= bos.toByteArray();


       			//	Log.e("bosfxbfgdfhfd", ""+bos.size());
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                  //StoreByteImage(mContext, imageData, 50,"ImageName");
                  //setResult(FOTO_MODE, mIntent);
                  setResult(585);
                  
                  finish();
              }  else
              {
            	  
            	//  Log.e("no bosssssssssssssss", "nobossssssssssssssssss");
              }
          }
      };

      protected void onResume(){
         // Log.e(TAG, "onResume");
          super.onResume();
      }

      protected void onSaveInstanceState(Bundle outState){
          super.onSaveInstanceState(outState);
      }

      protected void onStop(){
         // Log.e(TAG, "onStopeeeeeeeeee");
          super.onStop();
      }

      @TargetApi(9)
      public void surfaceCreated(SurfaceHolder holder){
        //  Log.e(TAG, "surfaceCreated");
         // mCamera = Camera.open(Home.cameraID);
          if (mCamera != null){
          mCamera.release();
          }
          i++;
         // Log.e("s created", "s created" +i);
        //  mCamera = Camera.open(-1);
          int cameraCount = 0;
         
          Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
          cameraCount = Camera.getNumberOfCameras();
          for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
              Camera.getCameraInfo( camIdx, cameraInfo );
              if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
                  try {
                      mCamera = Camera.open( camIdx );
                      
                      //Log.e("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", ""+camIdx);
                  } catch (RuntimeException e) {
                      Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                  }
              }
          }
          //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

          if (mCamera != null){
              Camera.Parameters params = mCamera.getParameters();
              mCamera.setParameters(params);
          }
          else {
              Toast.makeText(getApplicationContext(), "Camera error.", Toast.LENGTH_LONG).show();
              finish();
          }
      }

      public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	  //k++;

         // Log.e(TAG, "surfaceChanged"+k);

          // XXX stopPreview() will crash if preview is not running
          if (mPreviewRunning){
        	  mPreviewRunning=false;
              mCamera.stopPreview();
          }


        //  if (mCamera != null){
          Camera.Parameters p = mCamera.getParameters();

          //check for supported sizes to avoid exceptions
          Size size = getBestPreviewSize(300, 300, p);
          p.setPreviewSize(size.width, size.height);
          //move ahead
         // p.setPreviewFormat(ImageFormat.JPEG);


            if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
          {

if (p.getSupportedFlashModes() != null) {
          if (p.getSupportedFlashModes().contains(
        		  Parameters.FLASH_MODE_TORCH))
        		  {
        		  p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        		  }
          }
          }
            if (p.getSupportedFocusModes() != null) {
        		 if (p.getSupportedFocusModes().contains(
        		  Parameters.FOCUS_MODE_AUTO))
        		  {
        		  p.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        		  }
            }


            if (p.getSupportedFocusModes() != null) {
            	if (p.isZoomSupported()) {
               	 p.setZoom(0);
               	 }
           }




          mCamera.setParameters(p);
        //  }






          try{
              mCamera.setPreviewDisplay(holder);

          }catch (Exception e){
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
          mCamera.startPreview();
          mPreviewRunning = true;
          if(mPreviewRunning)
          {
        	//  jk++;
        	 // Log.e("cL", ""+mPreviewRunning+jk);
        	  if(mboolthrad)
        	  {
        		 // Log.e("cLk", ""+mPreviewRunning+jk+mboolthrad);
        		  mboolthrad=false;
          boolthread();
        	  }
          }
         // mCamera.takePicture(null, mPictureCallback, mPictureCallback);

      }

      public void surfaceDestroyed(SurfaceHolder holder) {
    	 // j++;
         // Log.e("fthrtrt", "surfaceDestroyed"+"  "+j);
          //mCamera.stopPreview();
          if(mPreviewRunning)
          {
        	  mCamera.stopPreview();
        	 mPreviewRunning = false;
          }




		 if (mCamera != null) {
		        mCamera.release();
		        mCamera = null;
		    }
      }

      private SurfaceView mSurfaceView;
      private SurfaceHolder mSurfaceHolder;

      public void onClick(View v) {/*
          // TODO Auto-generated method stub


    	  Thread th = new Thread() {
				public void run() {
					try {

						sleep(2000);


					} catch (Exception e) {
						e.printStackTrace();
					} finally {

						 mCamera.takePicture(null, mPictureCallback, mPictureCallback);

					}
				}
			};
			th.start();

      */}











      private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
          Size bestSize = null;
          List<Size> sizeList = parameters.getSupportedPreviewSizes();

          bestSize = sizeList.get(0);

          for(int i = 1; i < sizeList.size(); i++){
           if((sizeList.get(i).width * sizeList.get(i).height) >
             (bestSize.width * bestSize.height)){
            bestSize = sizeList.get(i);
           }
          }

          return bestSize;
         }
      private boolean checkCameraHardware(Context context) {
          if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
              return true;
          } else {
              return false;
          }
      }

     /* public static Camera getCameraInstance(){
          Camera c = null;
          try {
              c = Camera.open();
          }
          catch (Exception e){
          }
          return c;
      }*/

      public void boolthread()
      {
    		  Thread th = new Thread() {
			public void run() {
				try {

					sleep(2000);


				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// mPreviewRunning = true;
					takepicturess();


				}
			}
		};
		th.start();



      }
      private void takepicturess()
      {
    	 // Log.e("0", ""+mPreviewRunning);

    	  if (mPreviewRunning){
    	  pref.putString(Constant.signok, "1");
			 mCamera.takePicture(null, mPictureCallback, mPictureCallback);
      }
    	  /*
    	  Log.e("1", ""+mPreviewRunning);
			 if (mPreviewRunning){
				 Log.e("2", ""+mPreviewRunning);
	              mCamera.stopPreview();
	              mPreviewRunning=false;
	          }
			// mCamera.release();

			 int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			 Log.e("currentapiVersion", ""+currentapiVersion);
			 if (currentapiVersion >= 23){
			     // Do something for lollipop and above versions

				 if (mCamera != null) {
				        mCamera.release();
				        mCamera = null;
				    }


			 } else{
				 if (mCamera != null) {
				        mCamera.release();
				        mCamera = null;
				    }
			 }*/

			/*if (mCamera != null) {
			        mCamera.release();
			        mCamera = null;
			    }*/

      }
      @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	if (mPreviewRunning){
            mCamera.stopPreview();
            mPreviewRunning = false;
           // Log.e("pause", ""+mPreviewRunning);
        }
    	if(!mPreviewRunning)
    	{
		 if (mCamera != null) {
		        mCamera.release();
		        mCamera = null;
		    }
    	}
    }

      private void releaseCamera() {
    	    // check if Camera instance exists
    	  /*  if (mCamera != null) {
    	    	mPreviewRunning = false;
    	        // first stop preview
    	        mCamera.stopPreview();
    	        // then cancel its preview callback
    	        mCamera.setPreviewCallback(null);
    	        // and finally release it
    	        mCamera.release();
    	        // sanitize you Camera object holder
    	        mCamera = null;
    	    }*/


			 int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			// Log.e("currentapiVersion", ""+currentapiVersion);
			 if (currentapiVersion >= 23){
			     // Do something for lollipop and above versions
				
				 if (mCamera != null) {
				        mCamera.release();
				        mCamera = null;
				    }
				
				
			 } else{
				 if (mCamera != null) {
				        mCamera.release();
				        mCamera = null;
				    }
			 }
    	}
   
      }