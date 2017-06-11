package com.bigdj.emergetransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class KairosActivity extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private TextureView textureView;
    boolean currentlyCapturing = false;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    int trueCount = 0;

    Map<String, Class<?>> activities;

    Handler handler;
    Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities = new HashMap<>();
        activities.put("HomeActivityMat", HomeActivityMat.class);
        activities.put("HomeActivitySimple", HomeActivitySimple.class);
        activities.put("HomeActivitySimplest", HomeActivitySimplest.class);
        activities.put("SuccessActivityMat", SuccessActivityMat.class);
        activities.put("SuccessActivitySimple", SuccessActivitySimple.class);
        activities.put("SuccessActivitySimplest", SuccessActivitySimplest.class);
        activities.put("TransactionActivityMat", TransactionActivityMat.class);
        activities.put("TransactionActivitySimple", TransactionActivitySimple.class);
        activities.put("TransactionActivitySimplest", TransactionActivitySimplest.class);
    }

    Timer timer;

    public void initializeCamera(boolean repeat) {
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        handler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                Log.d("running", String.valueOf(currentlyCapturing));
                if (currentlyCapturing && trueCount > 3) {
                    currentlyCapturing = false;
                    trueCount = 0;
                }

                if(!currentlyCapturing) {
                    currentlyCapturing = true;
                    takePicture();
                    trueCount = 0;
                } else {
                    trueCount++;
                }
            }
        };

        reinitTimer();
    }

    public void reinitTimer(){
        handler = new Handler();
        mRunnable = new Runnable() {
            public void run() {
                Log.d("running", String.valueOf(currentlyCapturing));
                if(!currentlyCapturing) {
                    currentlyCapturing = true;
                    takePicture();
                }
            }
        };

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(mRunnable);
            }
        };
        timer = new Timer();
        timer.schedule(timertask, 800, 800);
    }


    public void changeActivity(int id) {
        Class<?> classs = this.getClass();
        if(classs != null) {
            //Toast.makeText(this, classs.getSimpleName(), Toast.LENGTH_LONG).show();
            String oldname = classs.getSimpleName();
            String name = oldname.replace("Mat", "");
            name = name.replace("Simplest", "");
            //great catch 2k17 david
            name = name.replace("Simple", "");
            switch(id) {
                case 1:
                    name += "Simplest";
                    break;
                case 2:
                    name += "Simple";
                    break;
                case 3:
                    name += "Mat";
                    break;
                case 4:
                    name += "Simple";
                    break;
                case 5:
                    name += "Mat";
                    break;
            }
            if(!oldname.equals(name)) {
                Log.e("NAME:MEMES", name);
                Intent intent = new Intent(this, activities.get(name));
                startActivity(intent);
                finish();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("TextureMemes", String.valueOf(surface));
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            Log.d("CameraMemes", String.valueOf(cameraDevice));
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            try {
                cameraCaptureSessions.abortCaptures();
            } catch(CameraAccessException e) {
                e.printStackTrace();
            }
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession
            .CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                       TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void takePicture() {
        if(null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            currentlyCapturing = false;
            return;
        }
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            final CameraCharacteristics characteristics = manager.getCameraCharacteristics
                    (cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics
                        .SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if(jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            if(textureView.getSurfaceTexture() != null) {
                ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputSurfaces = new ArrayList<Surface>(2);
                outputSurfaces.add(reader.getSurface());
                outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
                final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest
                        (CameraDevice.TEMPLATE_PREVIEW);
                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                // Orientation
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
                ImageReader.OnImageAvailableListener readerListener = new ImageReader
                        .OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = null;
                        try {
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            save(bytes);
                        } catch(FileNotFoundException e) {
                            currentlyCapturing = false;
                            e.printStackTrace();
                        } catch(IOException e) {
                            currentlyCapturing = false;
                            e.printStackTrace();
                        } finally {
                            if(image != null) {
                                image.close();
                            }
                        }
                    }

                    public Bitmap rotateImage(Bitmap source, float angle) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(angle);
                        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source
                                        .getHeight(),
                                matrix, true);
                    }

                    private void save(byte[] bytes) throws IOException {
                        OutputStream output = null;
                        try {
                            Bitmap x = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            x = Bitmap.createScaledBitmap(x, 200, 320, false);
                            x = rotateImage(x, 180);
                            Log.d("rotation", String.valueOf(characteristics.get
                                    (CameraCharacteristics.SENSOR_ORIENTATION)));
                            String app_id = "f6af176b";
                            String app_key = "eef8088104d012f2949a435f1be1c159";
                            URL url = new URL("https://api.kairos.com/recognize");
                            ByteArrayOutputStream blob = new ByteArrayOutputStream();
                            x.compress(Bitmap.CompressFormat.PNG, 100, blob);
                            byte[] byteArray = blob.toByteArray();
                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            HttpURLConnection client;
                            try {
                                client = (HttpURLConnection) url.openConnection();
                                client.setRequestMethod("POST");
                                client.setRequestProperty("app_id", app_id);
                                client.setRequestProperty("app_key", app_key);
                                client.setDoOutput(true);
                                OutputStream os = client.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(os, "UTF-8"));
                                writer.write("{" + "\"image\"" + ":" + "\"" + encoded + "\"," +
                                        "\"gallery_name\":\"gallerytest4\"" + "}");
                                writer.flush();
                                writer.close();
                                os.close();
                                int responseCode = client.getResponseCode();
                                String response = "";
                                if(responseCode == HttpsURLConnection.HTTP_OK) {
                                    String line;
                                    BufferedReader br = new BufferedReader(new InputStreamReader
                                            (client.getInputStream()));
                                    while((line = br.readLine()) != null) {
                                        response += line;
                                    }
                                } else {
                                    response = "";
                                }
                                Log.d("Response: ", response);
                                JSONObject res = new JSONObject(response);
                                JSONArray images = res.getJSONArray("images");
                                JSONObject image = images.getJSONObject(0);
                                JSONObject transaction = image.getJSONObject("transaction");
                                Log.d("Transaction: ", String.valueOf(transaction));
                                String status = (String) transaction.get("subject_id");
                                //Toast.makeText(getApplicationContext(), status, Toast
                                //        .LENGTH_SHORT).show();
                                changeActivity(Integer.valueOf(status));
                            } catch(JSONException e) {
                               // Toast.makeText(getApplicationContext(), "ERR0R", Toast
                               //         .LENGTH_SHORT).show();
                            } catch(MalformedURLException error) {
                            } catch(SocketTimeoutException error) {
                            } catch(IOException error) {
                            } finally {
                                currentlyCapturing = false;
                                //if (null != output) {
                                //    output.close();
                                //}
                            }
                        } catch(MalformedURLException e) {
                            e.printStackTrace();
                        } catch(IOException e) {
                            currentlyCapturing = false;
                            e.printStackTrace();
                        }
                    }
                };
                reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureListener = new
                        CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(CameraCaptureSession session,
                                                           CaptureRequest
                                                                   request, TotalCaptureResult
                                                                   result) {
                                super.onCaptureCompleted(session, request, result);
                                //Toast.makeText(tempactivity.this, "Saved:" + file, Toast
                                // .LENGTH_SHORT)
                                // .show();
                                createCameraPreview();
                            }
                        };
                cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession
                        .StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        try {
                            session.capture(captureBuilder.build(), captureListener,
                                    mBackgroundHandler);
                        } catch(CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {
                    }
                }, mBackgroundHandler);
            } else {
                Log.d("Error", "Stopped");
                textureView = (TextureView) findViewById(R.id.texture);
                currentlyCapturing = false;
            }
        } catch(CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice
                    .TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession
                    .StateCallback() {
                @Override
                public synchronized void onConfigured(@NonNull CameraCaptureSession
                                                              cameraCaptureSession) {
                    if(null == cameraDevice) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;
                    if(!currentlyCapturing) {
                        updatePreview();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                }
            }, null);
        } catch(CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics
                    .SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                    .PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(KairosActivity.this, new String[]{Manifest
                                .permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch(CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    protected synchronized void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.capture(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch(CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() throws CameraAccessException {
        if(null != cameraDevice) {
            cameraCaptureSessions.abortCaptures();
            cameraDevice.close();
            cameraDevice = null;
        }
        if(null != imageReader) {
            cameraCaptureSessions.abortCaptures();
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                //Toast.makeText(AndroidCameraApi.this, "Sorry!!!, you can't use this app without
                // granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        textureView = (TextureView) findViewById(R.id.texture);
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if(textureView != null)
            if(textureView.isAvailable()) {
                openCamera();
            } else {
                textureView.setSurfaceTextureListener(textureListener);
            }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
        //timer.cancel();

        /*try {
            killCam();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if(timer != null) {
            timer.cancel();
            timer.purge();
        } else if(handler != null) {
            if(mRunnable != null)
                handler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(timer != null) {
            timer.cancel();
            timer.purge();
        } else if(handler != null) {
            if(mRunnable != null)
                handler.removeCallbacks(mRunnable);
        }
    }

    public void killCam() throws CameraAccessException {
        if(cameraCaptureSessions != null)
            cameraCaptureSessions.abortCaptures();

        if(cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
}