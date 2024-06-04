package com.example.hear;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this,101);
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        Toast.makeText(this, "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }


    // define variables
    TextView text1, text2;
    SensorManager manager;
    SensorListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connect to xml
        text1 = findViewById(R.id.accelerometerTextView);
        text2 = findViewById(R.id.gyroscopeTextView); // 추가된 TextView

        manager = (SensorManager) getSystemService(SENSOR_SERVICE); // 센서관리객체설정
        listener = new SensorListener(); // 아래 만든 리스너 객체설정
    }

    // sensor start method -> onclick도 설정
    public void startSensor(View view) {
        // 객체설정
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 아래 만들었던 리스너 연결
        boolean chk = manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        if (chk == false) {
            text1.setText("가속도 센서 지원하지 않음");
        }

        // 자이로스코프 센서 시작
        Sensor gyroSensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        boolean gyroChk = manager.registerListener(listener, gyroSensor, SensorManager.SENSOR_DELAY_UI);
        if (gyroChk == false) {
            text2.setText("자이로스코프 센서 지원하지 않음");
        }
    }// finish

    // sensor stop method -> onclick 도 설정
    public void stopSensor(View view) {
        manager.unregisterListener(listener);
    }

    // sensor data 받아오기 listener 설정
    class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {    // 센서변화
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {    // 가속도센서 라면
                text1.setText("X축 : " + event.values[0] + "\n");
                text1.append("Y축 : " + event.values[1] + "\n");
                text1.append("Z축 : " + event.values[2]);
            }
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {    // 자이로스코프 센서 라면
                text2.setText("X축 각속도 : " + event.values[0] + "\n");
                text2.append("Y축 각속도 : " + event.values[1] + "\n");
                text2.append("Z축 각속도 : " + event.values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {    // 센서감도변화

        }
    } // listener finish
}
