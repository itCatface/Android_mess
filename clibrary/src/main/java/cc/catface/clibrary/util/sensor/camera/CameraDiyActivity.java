package cc.catface.clibrary.util.sensor.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cc.catface.clibrary.R;

/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */

public class CameraDiyActivity extends Activity implements View.OnClickListener{

    private CameraSurfaceView mCameraSurfaceView;
    private Button takePicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_diy);
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        takePicBtn= (Button) findViewById(R.id.takePic);
        takePicBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.takePic) {
            mCameraSurfaceView.takePicture();
        }
    }
}

