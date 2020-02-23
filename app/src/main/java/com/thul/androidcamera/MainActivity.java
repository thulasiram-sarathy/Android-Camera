package com.thul.androidcamera;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.divyanshu.draw.widget.DrawView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.camera.cameraview.CameraListener;
import com.camera.cameraview.CameraView;
import com.camera.cameraview.PictureResult;
import com.camera.cameraview.VideoResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final float SELECTED_COLOR_SCALE = 1.2f;

    @BindView(R.id.mainframe)
    FrameLayout mainframe;

    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.fab_video)
    FloatingActionButton fabVideo;
    @BindView(R.id.fab_front)
    FloatingActionButton fabFront;
    @BindView(R.id.pushTouches)
    PushTouchView pushTouchView;

    @BindView(R.id.draw_view)
    DrawView drawView;
    @BindView(R.id.wishText)
    TextView wishText;
    @BindView(R.id.fab_text)
    FloatingActionButton fab_text;

    @BindView(R.id.image_color_black)
    ImageView black;
    @BindView(R.id.image_color_red)
    ImageView red;
    @BindView(R.id.image_color_yellow)
    ImageView yellow;
    @BindView(R.id.image_color_green)
    ImageView green;
    @BindView(R.id.image_color_blue)
    ImageView blue;
    @BindView(R.id.image_color_pink)
    ImageView pink;
    @BindView(R.id.image_color_brown)
    ImageView brown;

    ImageView currentColor;
    @BindView(R.id.grid)
    GridView grid;

    @BindView(R.id.imgframe)
    ImageView imgframe;
    @BindView(R.id.fab_frame)
    FloatingActionButton fab_frame;

    String[] web = {
            "Frame 1",
            "Frame 2",
            "Frame 3",
            "Frame 4",
            "No Frame"

    };
    int[] imageId = {
            R.drawable.frame1,
            R.drawable.frame2,
            R.drawable.frame3,
            R.drawable.frame4,
            0

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        camera.setLifecycleOwner(this);


        camera.setVideoMaxDuration(120 * 1000); // max 2mins
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                PicturePreviewActivity.setPictureResult(result);
                Intent intent = new Intent(MainActivity.this, PicturePreviewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                VideoPreviewActivity.setVideoResult(result);
                Intent intent = new Intent(MainActivity.this, VideoPreviewActivity.class);
                startActivity(intent);

                // refresh gallery
                MediaScannerConnection.scanFile(MainActivity.this,
                        new String[]{result.getFile().toString()}, null,
                        (filePath, uri) -> {
                            Log.i("ExternalStorage", "Scanned " + filePath + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        });
            }
        });

//        forwardTouchesView.setForwardTo(drawView);
//        forwardTouchesView.setForwardTo(imgframe);

        mainframe.setOnDragListener(new ChoiceDragListener());
        colorRed();
        View v = (View) findViewById(R.id.colorpalette);
        v.setVisibility(View.INVISIBLE);


        grid = (GridView) findViewById(R.id.grid);


        PermissionUtils.requestReadWriteAppPermissions(this);
    }

    @OnClick(R.id.fab_frame)
    void openFrame() {
        grid.setVisibility(View.VISIBLE);
        CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position <= 3) {
                    Drawable drawable = getResources().getDrawable(imageId[position]);
                    imgframe.setImageDrawable(drawable);
                    grid.setVisibility(View.INVISIBLE);
                } else {
                    imgframe.setImageDrawable(null);
                    grid.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    @OnClick(R.id.fab_video)
    void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            camera.stopVideo();
            fabVideo.setImageResource(R.drawable.ic_videocam_black_24dp);
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US);
        String currentTimeStamp = dateFormat.format(new Date());

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "CameraViewFreeDrawing";
        File outputDir = new File(path);
        outputDir.mkdirs();
        File saveTo = new File(path + File.separator + currentTimeStamp + ".mp4");
        camera.takeVideoSnapshot(saveTo);

        fabVideo.setImageResource(R.drawable.ic_stop_black_24dp);
    }

    @OnClick(R.id.fab_text)
    void addTextOverlay() {

        pushTouchView.setForwardTo(wishText);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.open_edit_dialog, null);
        dialogBuilder.setCancelable(false);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        if (!wishText.getText().toString().equals("")) {
            editText.setText(wishText.getText().toString());
        }
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishText.setVisibility(View.INVISIBLE);
                wishText.setText("");
                pushTouchView.setForwardTo(drawView);
                dialogBuilder.dismiss();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishText.setVisibility(View.VISIBLE);
                wishText.setText(editText.getText().toString());
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }


    @OnClick(R.id.fab_picture)
    void capturePictureSnapshot() {
        if (camera.isTakingVideo()) {
            Toast.makeText(this, "Already taking video.", Toast.LENGTH_SHORT).show();
            return;
        }
        camera.takePictureSnapshot();
    }

    @OnClick(R.id.fab_front)
    void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                fabFront.setImageResource(R.drawable.ic_camera_front_black_24dp);
                break;

            case FRONT:
                fabFront.setImageResource(R.drawable.ic_camera_rear_black_24dp);
                break;
        }
    }

    @OnClick(R.id.image_clear)
    void clearCanvas() {
        drawView.clearCanvas();
    }

    private void setColor(ImageView colorView, int color) {
        if (currentColor == colorView) {
            return;
        }

        drawView.setColor(color);

        colorView.setScaleX(SELECTED_COLOR_SCALE);
        colorView.setScaleY(SELECTED_COLOR_SCALE);

        if (currentColor != null) {
            currentColor.setScaleX(1.0f);
            currentColor.setScaleY(1.0f);
        }

        currentColor = colorView;
    }

    @OnClick(R.id.image_color_black)
    void colorBlack() {
        setColor(black, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_black, null));
    }

    @OnClick(R.id.image_color_red)
    void colorRed() {
        setColor(red, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_red, null));
    }

    @OnClick(R.id.image_color_yellow)
    void colorYellow() {
        setColor(yellow, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_yellow, null));
    }

    @OnClick(R.id.image_color_green)
    void colorGreen() {
        setColor(green, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_green, null));
    }

    @OnClick(R.id.image_color_blue)
    void colorBlue() {
        setColor(blue, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_blue, null));
    }

    @OnClick(R.id.image_color_pink)
    void colorPink() {
        setColor(pink, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_pink, null));
    }

    @OnClick(R.id.image_color_brown)
    void colorBrown() {
        setColor(brown, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_brown, null));
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {
        @SuppressLint("NewApi")
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                /*
                 * Drag details: we only need default behavior
                 * - clip data could be set to pass data as part of drag
                 * - shadow can be tailored
                 */
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                //start dragging the item touched
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    @SuppressLint("NewApi")
    private class ChoiceDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    Log.v("ACTION_DRAG_STARTED", " " + view.getX() + " " + view.getY());
                    Log.v("ACTION_DRAG_STARTED", " " + event.getX() + " " + event.getY());
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
//                    Log.v("ACTION_DRAG_ENTERED", " "+ event.getX() +" "+ event.getY());
//                    Toast.makeText(MainActivity.this, "ACTION_DRAG_ENTERED", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
//                    Toast.makeText(MainActivity.this, "ACTION_DRAG_EXITED", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DROP:
//                    Log.v("ACTION_DROP", " "+ view.getX() + " "+ view.getY());
//                    Log.v("ACTION_DROP", " "+ event.getX() + " "+ event.getY());
//                    Toast.makeText(CameraActivity.this, "ACTION_DROP", Toast.LENGTH_SHORT).show();
                    //handle the dragged view being dropped over a drop view
//                    view = (View) event.getLocalState();
                    ViewGroup parent = (ViewGroup) view.getParent();
                    parent.removeView(view);
                    //stop displaying the view where it was before it was dragged
                    view.setVisibility(View.INVISIBLE);
                    //view dragged item is being dropped on
                    FrameLayout dropTarget = (FrameLayout) v;

                    view.setX(event.getX());
                    view.setY(event.getY());
                    //view being dragged and dropped
                    TextView dropped = (TextView) view;
                    //update the text in the target view to reflect the data being dropped
//                    dropTarget.setText(dropped.getText());
//                    dropTarget.addView(dropped);
//                    view.setLayoutParams(params);
                    parent.addView(dropped);
//                    mainframe.invalidate();
                    view.invalidate();
//                    img1.setVisibility(View.VISIBLE);
                    //make it bold to highlight the fact that an item has been dropped
//                    dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
                    //if an item has already been dropped here, there will be a tag
                    Object tag = dropTarget.getTag();
                    //if there is already an item here, set it back visible in its original place
                    if (tag != null) {
                        //the tag is the view id already dropped here
                        int existingID = (Integer) tag;
                        //set the original view visible again
                        findViewById(existingID).setVisibility(View.VISIBLE);
                    }
                    //set the tag in the target view being dropped on - to the ID of the view being dropped
                    dropTarget.setTag(dropped.getId());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
//                    view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
//                    Toast.makeText(MainActivity.this, "ACTION_DRAG_ENDED", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        }
    }


}
