Brian Wan

Design:
I made a CameraPreview in a separate class. This class would handle the proper creation of the preview. The code is
very similar to that on the android developer site. For an unknown reason, the camera view was rotated on its side, so I
had to hard code it to rotate 90 degrees. In the MainActivity, I first began by creating a camera instance, also using
code from the developer site. I implemented auto focus for the camera, but for an unknown reason, it made face detection
even less accurate, so it was commented out.  I set up the views, and set up the button to flip between preview mode
and mask mode. Next, I set up the picture taken to detect faces. According to some sources, if the bitmap image is not
configured to RGB_565, there is no chance of detecting a face. After the configuration, I set up a face detector to
detect and count the number of faces present in the image captured. Then, I prepare another bitmap to draw on. According
to the developer site again, ARGB_8888 is recommended for quality, so I set it to that. Then, I call my drawMask function,
which would draw a solid circle on the face and draw a thin red line across the eyes.

Logcat:
To be able to use logcat, you must import android.util.Log. There are 5 versions of log messages, each one representing
a level of severity: verbose, debug, information, warning, and error. They are all used the same way though. An example
would be Log.i("x", "hello world");. The first string parameter is some sort of tag, and by convention, a string
constant TAG is declared as the name of the activity. The tag is used to identify the source of the log message. The
second string is the actual message that will show up in the logcat.

By default, Android Studio brings up Logcat at the bottom of the window when you tell it to run the app. At the top 
right of the Logcat section, there is a drop-down menu labeled "Log level". You can use this to filter only certain log
messages based on the level of the severity. To the far right of that drop-down menu is another drop-down menu. This
menu is used to filter out sources of the log messages. By choosing the correct process, you can filter out the system
messages and only get the log messages that you have programmed into the application.

Sources for code:
http://stackoverflow.com/questions/19763674/browse-image-and-face-detection
http://www.developer.com/ws/android/programming/face-detection-with-android-apis.html
http://grishma102.blogspot.com/2013/11/android-facedetection-feature-in-camera.html
http://developer.android.com/reference/android/graphics/Bitmap.Config.html
http://developer.android.com/reference/android/graphics/Bitmap.html
http://developer.android.com/guide/topics/media/camera.html
http://developer.android.com/reference/android/media/FaceDetector.html
http://developer.android.com/guide/topics/media/camera.html#access-camera
http://developer.android.com/reference/android/hardware/Camera.PictureCallback.html
http://stackoverflow.com/questions/8294868/issues-taking-picture-with-android-vertical-camera-portrait

Sources for questions:
http://developer.android.com/reference/android/util/Log.html
http://developer.android.com/tools/debugging/debugging-log.html