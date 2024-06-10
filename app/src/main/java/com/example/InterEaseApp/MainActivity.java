package com.example.InterEaseApp;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Coordinates2d;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Point.OrientationMode;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.example.InterEaseApp.common.helpers.CameraPermissionHelper;
import com.example.InterEaseApp.common.helpers.DepthSettings;
import com.example.InterEaseApp.common.helpers.DisplayRotationHelper;
import com.example.InterEaseApp.common.helpers.FullScreenHelper;
import com.example.InterEaseApp.common.helpers.GlobalClass;
import com.example.InterEaseApp.common.helpers.MyScaleGestures;
import com.example.InterEaseApp.common.helpers.RotationGestureDetector;
import com.example.InterEaseApp.common.helpers.SnackbarHelper;
import com.example.InterEaseApp.common.helpers.TapHelper;
import com.example.InterEaseApp.common.helpers.TrackingStateHelper;
import com.example.InterEaseApp.common.rendering.BackgroundRenderer;
import com.example.InterEaseApp.common.rendering.ObjectRenderer;
import com.example.InterEaseApp.common.rendering.ObjectRenderer.BlendMode;
import com.example.InterEaseApp.common.rendering.PlaneRenderer;
import com.example.InterEaseApp.common.rendering.PointCloudRenderer;
import com.example.InterEaseApp.common.rendering.Texture;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;



public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer, RotationGestureDetector.OnRotationGestureListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static int plane_check=0;

    private GLSurfaceView surfaceView;

    private boolean installRequested;
    private String selectedProductName;


    private Session session;
    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();
    private DisplayRotationHelper displayRotationHelper;
    private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this);
    private TapHelper tapHelper;

    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private final ObjectRenderer virtualObject = new ObjectRenderer();
    private final PlaneRenderer planeRenderer = new PlaneRenderer();
    private final PointCloudRenderer pointCloudRenderer = new PointCloudRenderer();
    private final Texture depthTexture = new Texture();
    private boolean calculateUVTransform = true;

    private final DepthSettings depthSettings = new DepthSettings();
    private boolean[] settingsMenuDialogCheckboxes;

    private final float[] anchorMatrix = new float[16];
    private static final float[] DEFAULT_COLOR = new float[]{0f, 0f, 0f, 0f};

    private static final String SEARCHING_PLANE_MESSAGE = "Searching for surfaces...";

    private static class ColoredAnchor {
        public final Anchor anchor;
        public final float[] color;

        public ColoredAnchor(Anchor anchor, float[] color) {
            this.anchor = anchor;
            this.color = color;
        }


    }


    public static RotationGestureDetector mRotationDetector;
    public static MyScaleGestures scaleGestureDetector;
    public static MotionEvent motionEvent;

    public static String obj_file = "";
    public static String png_file = "";

    public static int cnt = 0;
    public static boolean isObjectReplaced;


    private final ArrayList<ColoredAnchor> anchors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        surfaceView = findViewById(R.id.surfaceview);
        displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);
        mRotationDetector = new RotationGestureDetector(this);
        scaleGestureDetector = new MyScaleGestures(this);

        tapHelper = new TapHelper(/*context=*/ this);
        surfaceView.setOnTouchListener(tapHelper);


        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(this);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        surfaceView.setWillNotDraw(false);

        installRequested = false;
        calculateUVTransform = true;

        depthSettings.onCreate(this);
        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this::launchSettingsMenuDialog);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }


                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this);
                    return;
                }

                session = new Session(/* context= */ this);
                Config config = session.getConfig();
                if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    config.setDepthMode(Config.DepthMode.AUTOMATIC);
                } else {
                    config.setDepthMode(Config.DepthMode.DISABLED);
                }
                session.configure(config);
            } catch (UnavailableArcoreNotInstalledException
                     | UnavailableUserDeclinedInstallationException e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (UnavailableDeviceNotCompatibleException e) {
                message = "This device does not support AR";
                exception = e;
            } catch (Exception e) {
                message = "Failed to create AR session";
                exception = e;
            }

            if (message != null) {
                messageSnackbarHelper.showError(this, message);
                Log.e(TAG, "Exception creating session", exception);
                return;
            }
        }

        try {
            session.resume();
        } catch (CameraNotAvailableException e) {
            messageSnackbarHelper.showError(this, "Camera not available. Try restarting the app.");
            session = null;
            return;
        }
        surfaceView.onResume();
        displayRotationHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (session != null) {

            displayRotationHelper.onPause();
            surfaceView.onPause();
            session.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        try {
            depthTexture.createOnGlThread();
            backgroundRenderer.createOnGlThread(/*context=*/ this, depthTexture.getTextureId());
            planeRenderer.createOnGlThread(/*context=*/ this, "models/trigrid.png");
            pointCloudRenderer.createOnGlThread(/*context=*/ this);
            virtualObject.createOnGlThread(/*context=*/ this, obj_file, png_file);
            virtualObject.setBlendMode(BlendMode.AlphaBlending);
            virtualObject.setDepthTexture(
                    depthTexture.getTextureId(), depthTexture.getWidth(), depthTexture.getHeight());
            virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);

        } catch (IOException e) {
            Log.e(TAG, "Failed to read an asset file", e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        displayRotationHelper.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (session == null) {
            return;
        }

        displayRotationHelper.updateSessionIfNeeded(session);
        try {
            session.setCameraTextureName(backgroundRenderer.getTextureId());

            Frame frame = session.update();
            Camera camera = frame.getCamera();

            if (frame.hasDisplayGeometryChanged() || calculateUVTransform) {

                calculateUVTransform = false;
                float[] transform = getTextureTransformMatrix(frame);
                virtualObject.setUvTransformMatrix(transform);
            }

            if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                depthTexture.updateWithDepthImageOnGlThread(frame);
            }

            handleTap(frame, camera);

            backgroundRenderer.draw(frame, depthSettings.depthColorVisualizationEnabled());

            trackingStateHelper.updateKeepScreenOnFlag(camera.getTrackingState());

            if (camera.getTrackingState() == TrackingState.PAUSED) {
                messageSnackbarHelper.showMessage(
                        this, TrackingStateHelper.getTrackingFailureReasonString(camera));
                return;
            }

            float[] projmtx = new float[16];
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            float[] viewmtx = new float[16];
            camera.getViewMatrix(viewmtx, 0);


            final float[] colorCorrectionRgba = new float[4];
            frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);


            try (PointCloud pointCloud = frame.acquirePointCloud()) {
                pointCloudRenderer.update(pointCloud);

            }

            if (hasTrackingPlane()) {
                messageSnackbarHelper.hide(this);
            } else {
                messageSnackbarHelper.showMessage(this, SEARCHING_PLANE_MESSAGE);

            }

            planeRenderer.drawPlanes(
                    session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projmtx);

            float scaleFactor = 1.0f;
            virtualObject.setUseDepthForOcclusion(this, depthSettings.useDepthForOcclusion());

            for (ColoredAnchor coloredAnchor : anchors) {

                if (coloredAnchor.anchor.getTrackingState() != TrackingState.TRACKING) {
                    continue;
                }

                coloredAnchor.anchor.getPose().toMatrix(anchorMatrix, 0);

                virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
                virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color);
            }
        } catch (Throwable t) {

            Log.e(TAG, "Exception on the OpenGL thread", t);
        }
        if (isObjectReplaced) {
            isObjectReplaced = false;
            try {
                virtualObject.createOnGlThread(this, obj_file, png_file);
                virtualObject.setBlendMode(BlendMode.AlphaBlending);
                virtualObject.setDepthTexture(
                        depthTexture.getTextureId(), depthTexture.getWidth(), depthTexture.getHeight());
                virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    private void handleTap(Frame frame, Camera camera) {
        MotionEvent tap = tapHelper.poll();
        if (tap != null && camera.getTrackingState() == TrackingState.TRACKING) {
            for (HitResult hit : frame.hitTest(tap)) {
                Trackable trackable = hit.getTrackable();
                if ((trackable instanceof Plane
                        && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())
                        && (PlaneRenderer.calculateDistanceToPlane(hit.getHitPose(), camera.getPose()) > 0))
                        || (trackable instanceof Point
                        && ((Point) trackable).getOrientationMode()
                        == OrientationMode.ESTIMATED_SURFACE_NORMAL)) {

                    if (anchors.size() >= 1) {
                        anchors.get(0).anchor.detach();
                        anchors.remove(0);
                    }


                    float[] objColor;
                    if (trackable instanceof Point) {
                        objColor = new float[]{66.0f, 133.0f, 244.0f, 255.0f};
                    } else if (trackable instanceof Plane) {
                        objColor = new float[]{255.0f, 255.0f, 255.0f, 255.0f};
                    } else {
                        objColor = DEFAULT_COLOR;
                    }


                    anchors.add(new ColoredAnchor(hit.createAnchor(), objColor));



                    this.runOnUiThread(this::showOcclusionDialogIfNeeded);
                    break;
                }
            }
        }


    }


    /**
     * Shows a pop-up dialog on the first call, determining whether the user wants to enable
     * depth-based occlusion. The result of this dialog can be retrieved with useDepthForOcclusion().
     */
    private void showOcclusionDialogIfNeeded() {
        boolean isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC);
        if (!depthSettings.shouldShowDepthEnableDialog() || !isDepthSupported) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.options_title_with_depth)
                .setMessage(R.string.depth_use_explanation)
                .setPositiveButton(
                        R.string.button_text_enable_depth,
                        (DialogInterface dialog, int which) -> {
                            depthSettings.setUseDepthForOcclusion(true);
                        })
                .setNegativeButton(
                        R.string.button_text_disable_depth,
                        (DialogInterface dialog, int which) -> {
                            depthSettings.setUseDepthForOcclusion(false);
                        })
                .show();
    }

    /**
     * Shows checkboxes to the user to facilitate toggling of depth-based effects.
     */
    private void launchSettingsMenuDialog(View view) {
        // Retrieves the current settings to show in the checkboxes.
        resetSettingsMenuDialogCheckboxes();

        // Shows the dialog to the user.
        Resources resources = getResources();
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            // With depth support, the user can select visualization options.
            new AlertDialog.Builder(this)
                    .setTitle(R.string.options_title_with_depth)
                    .setMultiChoiceItems(
                            resources.getStringArray(R.array.depth_options_array),
                            settingsMenuDialogCheckboxes,
                            (DialogInterface dialog, int which, boolean isChecked) ->
                                    settingsMenuDialogCheckboxes[which] = isChecked)
                    .setPositiveButton(
                            R.string.done,
                            (DialogInterface dialogInterface, int which) -> applySettingsMenuDialogCheckboxes())
                    .setNegativeButton(
                            android.R.string.cancel,
                            (DialogInterface dialog, int which) -> resetSettingsMenuDialogCheckboxes())
                    .show();
        } else {
            // Without depth support, no settings are available.
            new AlertDialog.Builder(this)
                    .setTitle(R.string.options_title_without_depth)
                    .setPositiveButton(
                            R.string.done,
                            (DialogInterface dialogInterface, int which) -> applySettingsMenuDialogCheckboxes())
                    .show();
        }
    }

    private void applySettingsMenuDialogCheckboxes() {
        depthSettings.setUseDepthForOcclusion(settingsMenuDialogCheckboxes[0]);
        depthSettings.setDepthColorVisualizationEnabled(settingsMenuDialogCheckboxes[1]);
    }

    private void resetSettingsMenuDialogCheckboxes() {
        settingsMenuDialogCheckboxes = new boolean[2];
        settingsMenuDialogCheckboxes[0] = depthSettings.useDepthForOcclusion();
        settingsMenuDialogCheckboxes[1] = depthSettings.depthColorVisualizationEnabled();
    }

    /**
     * Checks if we detected at least one plane.
     */
    private boolean hasTrackingPlane() {
        for (Plane plane : session.getAllTrackables(Plane.class)) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a transformation matrix that when applied to screen space uvs makes them match
     * correctly with the quad texture coords used to render the camera feed. It takes into account
     * device orientation.
     */
    private static float[] getTextureTransformMatrix(Frame frame) {
        float[] frameTransform = new float[6];
        float[] uvTransform = new float[9];
        // XY pairs of coordinates in NDC space that constitute the origin and points along the two
        // principal axes.
        float[] ndcBasis = {0, 0, 1, 0, 0, 1};

        // Temporarily store the transformed points into outputTransform.
        frame.transformCoordinates2d(
                Coordinates2d.OPENGL_NORMALIZED_DEVICE_COORDINATES,
                ndcBasis,
                Coordinates2d.TEXTURE_NORMALIZED,
                frameTransform);

        // Convert the transformed points into an affine transform and transpose it.
        float ndcOriginX = frameTransform[0];
        float ndcOriginY = frameTransform[1];
        uvTransform[0] = frameTransform[2] - ndcOriginX;
        uvTransform[1] = frameTransform[3] - ndcOriginY;
        uvTransform[2] = 0;
        uvTransform[3] = frameTransform[4] - ndcOriginX;
        uvTransform[4] = frameTransform[5] - ndcOriginY;
        uvTransform[5] = 0;
        uvTransform[6] = ndcOriginX;
        uvTransform[7] = ndcOriginY;
        uvTransform[8] = 1;

        return uvTransform;
    }

    //region=======================================Screen Capture======================================//
    private Bitmap snapshotBitmap;

    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    // supporting methods
    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                EGL10 egl = (EGL10) EGLContext.getEGL();
                GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
                snapshotBitmap = createBitmapFromGLSurface(0, 0, surfaceView.getWidth(), surfaceView.getHeight(), gl);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                    }
                });

            }
        });

    }

    public void captureMethod(View v) {


        captureBitmap(new BitmapReadyCallbacks() {

            @Override
            public void onBitmapReady(Bitmap bitmap) {
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "DARI", null);
            }
        });


        Toast.makeText(getApplicationContext(), "Image Captured!", Toast.LENGTH_SHORT).show();
    }

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }


    public void deleteMethod(View v) {
        if (anchors.size() >= 1) {
            anchors.get(anchors.size() - 1).anchor.detach();
            anchors.remove(anchors.size() - 1);
        }
        Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
    }





    @Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
        GlobalClass.rotateF = GlobalClass.rotateF + rotationDetector.getAngle() / 10;
    }

    public void plane_check_Method(View v) {
        if(plane_check==0){
            Toast.makeText(getApplicationContext(), "Plane Renderer has been removed", Toast.LENGTH_SHORT).show();
            plane_check=1;
        }else if(plane_check==1){
            Toast.makeText(getApplicationContext(), "Plane Renderer has been enabled", Toast.LENGTH_SHORT).show();
            plane_check=0;
        }

    }
}
