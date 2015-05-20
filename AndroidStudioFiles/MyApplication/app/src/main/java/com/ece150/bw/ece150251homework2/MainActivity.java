package com.ece150.bw.ece150251homework2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int MAX_FACES = 5;
    private Camera theCamera;
    public Bitmap somebmp,cvsbitmap;
    private FaceDetector.Face[] faces;
    private int num_faces;
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private PointF midPoint = new PointF();
    Canvas canvas = new Canvas();
    Matrix matrix = new Matrix();
    FrameLayout preview;
    ImageView picCaptured;
    ImageView addMask;
    TextView errorText;
    CameraPreview viewFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of Camera
        theCamera = getCameraInstance();


        //auto focus
        Camera.Parameters params = theCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        theCamera.setParameters(params);


        // Create our Preview view and set it as the content of our activity.
        viewFinder = new CameraPreview(this, theCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(viewFinder);

        //set the text of the button
        final Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setText("Take Picture");

        addMask = new ImageView(getApplicationContext());
        errorText = new TextView(getApplicationContext());
        //fix image rotation
        matrix.postRotate(90);


        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //in Preview mode, to Picture mode
                        if(captureButton.getText()=="Take Picture") {
                            // get an image from the camera
                            theCamera.takePicture(null, null, takenPicture);
                            captureButton.setText("Add Mask");
                        }
                        //in picture mode, change to mask mode
                        else if(captureButton.getText() == "Add Mask"){
                            //detect faces and count
                            FaceDetector face_detector = new FaceDetector(somebmp.getWidth(), somebmp.getHeight(),MAX_FACES);
                            faces = new FaceDetector.Face[MAX_FACES];
                            num_faces = face_detector.findFaces(somebmp, faces);
                            String count_faces = String.valueOf(num_faces);
                            Log.i("face_detection",count_faces);

                            //argb 8888 for quality
                            cvsbitmap = Bitmap.createBitmap(somebmp.getWidth(), somebmp.getHeight(),Bitmap.Config.ARGB_8888);
                            canvas = new Canvas(cvsbitmap);
                            drawMask(canvas);
                            captureButton.setText("Show Preview");
                        }
                        //in mask mode, change to picture mode
                        else if(captureButton.getText()=="Show Preview") {
                            if(num_faces>0)
                                //remove mask
                                preview.removeView(addMask);
                            else
                                //remove error
                                preview.removeView(errorText);
                            theCamera.startPreview();
                            captureButton.setText("Take Picture");
                        }
                    }
                }
        );
    }

    PictureCallback takenPicture = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            try{
                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();

                //rgb 565 bitmap required
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;

                //get picture
                somebmp = BitmapFactory.decodeByteArray(data, 0, data.length, bitmap_options);
                somebmp = Bitmap.createBitmap(somebmp, 0, 0, somebmp.getWidth(), somebmp.getHeight(), matrix, true);
                Log.i("takenPicture", somebmp.getConfig().toString());

                picCaptured.setImageBitmap(somebmp);
                preview.addView(picCaptured);

            } catch (Exception e) {}
        }
    };

    //draws a solid white circle on the face and a red line across where the eyes would be
    protected void drawMask(Canvas canvas){
        if(num_faces > 0){
            float rEye, lEye;
            for(int i = 0 ; i < num_faces ; i++){
                FaceDetector.Face face = faces[i];
                Log.i("drawMask", "drawing circle");
                paint1.setColor(Color.WHITE);
                paint1.setAlpha(100);
                face.getMidPoint(midPoint);
                canvas.drawCircle(midPoint.x, midPoint.y,face.eyesDistance()*2,paint1);
                //Log.i("drawMask", "circle drawn");
                Log.i("drawMask", "drawing line");
                paint2.setColor(Color.RED);
                paint2.setStrokeWidth(30);
                rEye = midPoint.x - (face.eyesDistance()/2);
                lEye = midPoint.x + (face.eyesDistance()/2);
                canvas.drawLine((rEye-50),(midPoint.y),(lEye+50),(midPoint.y),paint2);
                //Log.i("drawMask","line drawn");
            }
            //add the mask
            addMask.setImageBitmap(cvsbitmap);
            preview.addView(addMask);
        }
        else{
            errorText.setText("NO FACE DETECTED, TRY AGAIN!");
            errorText.setTextSize(50);
            errorText.setTextColor(Color.WHITE);
            preview.addView(errorText);
        }
    }

    @Override
    protected void onPause() {
        // release the camera immediately on pause event for other applications
        super.onPause();
        if (theCamera != null){
            theCamera.stopPreview();
            theCamera.release();
            theCamera = null;
        }
    }

    /* get an instance of the Camera object */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
