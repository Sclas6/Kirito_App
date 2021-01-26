package com.kirito.kiritoapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    int mode = 0;
    int step = 0;
    int tap_count = 0;
    long p_touch;
    long s_touch;
    long touch_diff = 50000;
    final int default_percent = 280;
    int increase_percent;
    final int default_increase_percent[][] = {{40,5,30,-20},{10,30,20,5},{20,30,5,10},{10,40,20,30},{20,30,10,40}};
    boolean start = false;
    final long data_installed = System.currentTimeMillis();
    //final long data_start = 1593907200000;
    long start_data;
    private ProgressBar progressBar0;
    private ProgressBar progressBar1;
    private int percent_0 = 450;
    private int percent_1;
    private static final String TAG = "MainActivity";
    CharByCharTextView mCustomTextView;

    private GLSurfaceView _glSurfaceView;
    private GLRenderer _glRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glRenderer = new GLRenderer();
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(_glSurfaceView);
        View view = this.getLayoutInflater().inflate(R.layout.activity_ui,null);
        addContentView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        /*getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
                        ? View.SYSTEM_UI_FLAG_LOW_PROFILE
                        : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        );*/

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(openFileInput("data_start.txt")));
            String str = null;
            while((str = reader.readLine()) != null) {
                start = true;
                start_data = Long.parseLong(str);
                Log.d(TAG, str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!start){
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("data_start.txt", Context.MODE_PRIVATE)));
                writer.write(Long.toString(data_installed));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "saved dir: " + getFilesDir());
        }

        Log.d("START TIME_MILLIS",Long.toString(start_data));
        Log.d("NOW TIME_MILLIS",Long.toString(System.currentTimeMillis()));
        Log.d("DIFF",Long.toString((System.currentTimeMillis()-start_data)/(long)(3600000*24)));



        percent_1 = default_percent;
        progressBar0 = (ProgressBar)findViewById(R.id.progressBar0);
        progressBar0.setMax(1000);
        progressBar0.setProgress(450);

        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar1.setMax(1000);
        progressBar1.setProgress(percent_1);
        //progressBar.setMin(0);
        ImageView bar = findViewById(R.id.bar);
        barAnimations(bar);

        final Button button1 = this.findViewById(R.id.button1);
        final Button button2 = this.findViewById(R.id.button2);
        final Button button3 = this.findViewById(R.id.button3);
        final Button button4 = this.findViewById(R.id.button5);
        final Button test_button = this.findViewById(R.id.testbutton);

        /*test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tap_count += 1;
                s_touch=System.currentTimeMillis();
                touch_diff = s_touch - p_touch;
                setStep(percent);
                setIncreasePercent(step,mode);
                //multipleEffectOnIncrease(step);
                touchNipple(percent,increase_percent,step);

                p_touch = System.currentTimeMillis();

                Log.d("STEP:MODE",Integer.toString(step)+" "+Integer.toString(mode));
                Log.d("INCREASE_PERCENT",Integer.toString(increase_percent));
                Log.d("TOUCH_DIFF",Long.toString(touch_diff));

                long touch_diff = s_touch - p_touch;
            }
        });*/

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 0;
                button1.setText(">hand1<");
                button2.setText("hand2");
                button3.setText("hand3");
                button4.setText("hand4");
                tap_count = 0;
                Log.d("debug", "now mode "+mode+" is available!");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                button1.setText("hand1");
                button2.setText(">hand2<");
                button3.setText("hand3");
                button4.setText("hand4");
                tap_count = 0;
                Log.d("debug", "now mode "+mode+" is available!");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 2;
                button1.setText("hand1");
                button2.setText("hand2");
                button3.setText(">hand3<");
                button4.setText("hand4");
                tap_count = 0;
                Log.d("debug", "now mode "+mode+" is available!");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 3;
                button1.setText("hand1");
                button2.setText("hand2");
                button3.setText("hand3");
                button4.setText(">hand4<");
                tap_count = 0;
                Log.d("debug", "now mode "+mode+" is available!");
            }
        });
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _glSurfaceView.queueEvent(
                        new Runnable() {
                            // This method will be called on the rendering
                            // thread:
                            public void run() {
                                JniBridgeJava.nativeOnTouchesGear();
                            }
                        });
            }
        });
        mCustomTextView = (CharByCharTextView) findViewById(R.id.text_window_view);
        mCustomTextView.setTargetText("表示させる文字表示させる文字表示させる文字表示させる文字");
        mCustomTextView.startCharByCharAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        long data_now = System.currentTimeMillis();
        long data_diff = data_now - start_data;
        data_diff /= 1000;
        data_diff /= 3600;
        data_diff /= 24;
        setTitle("  キリトさん  ("+data_diff+"日目)");
        JniBridgeJava.nativeOnStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _glSurfaceView.onPause();
        JniBridgeJava.nativeOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JniBridgeJava.nativeOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JniBridgeJava.nativeOnDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float pointX = event.getX();
        final float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                JniBridgeJava.nativeOnTouchesBegan(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                JniBridgeJava.nativeOnTouchesEnded(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                JniBridgeJava.nativeOnTouchesMoved(pointX, pointY);
                break;
        }
        s_touch=System.currentTimeMillis();
        touch_diff = s_touch - p_touch;
        if(pointX>400&&pointX<700 && touch_diff>120){
            touchNippleAction();
            Log.d("Position",Float.toString(pointX)+" "+Float.toString(pointY));
        }
        p_touch = System.currentTimeMillis();
        return super.onTouchEvent(event);
    }

    public void onProgressChanged(ProgressBar p_b , int percentage){
        Animator animation = ObjectAnimator.ofInt(p_b,"progress",percentage);
        animation.setDuration(100);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
    public void setIncreasePercent(int s,int m) {
        //increase_percent = (s!=4)?(s!=3)?(s!=2)?(s!=1)?40:30:10:10:10;
        increase_percent = default_increase_percent[step][mode];
    }
    private void barAnimations(ImageView target) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "translationY", -15f, 12f);
        objectAnimator.setDuration(280);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(target,"translationY",12f,-15f);
        objectAnimator2.setDuration(420);

        final AnimatorSet animationSet = new AnimatorSet();
        animationSet.playSequentially(objectAnimator,objectAnimator2);
        //objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                animationSet.start();
            }
        });
        animationSet.start();
        //objectAnimator.start();
    }
    public void setStep(int p){
        step=(p>=500)?(p>=750)?(p>=880)?(p>=940)?4:3:2:1:0;
    }

    public int getStep(){
        return step;
    }

    public void touchNippleAction(){
        tap_count += 1;
        //s_touch=System.currentTimeMillis();
        //touch_diff = s_touch - p_touch;
        setStep(percent_1);
        if(tap_count>3){
            increase_percent = 5;
        }
        else{
            setIncreasePercent(step,mode);
        }
        if((Math.random()*100<55 && tap_count%3==0)||tap_count==1){
            mCustomTextView.setTargetText("ぁぁ、あっ、あ゛♡ぁ……っいく、イクからっも、やだぁっや");
            mCustomTextView.startCharByCharAnim();
        }
        //multipleEffectOnIncrease(step);
        if(progressBar0.getProgress()<1000){
            percent_0 += increase_percent;
            onProgressChanged(progressBar0,percent_0);
            touchNipple(percent_1,(mode == 1||mode==3)?increase_percent/4:increase_percent/8,step);
        }
        else{
            touchNipple(percent_1,increase_percent,step);
        }

        //p_touch = System.currentTimeMillis();

        Log.d("STEP:MODE",Integer.toString(step)+" "+Integer.toString(mode));
        Log.d("INCREASE_PERCENT",Integer.toString(increase_percent));
        Log.d("TOUCH_DIFF",Long.toString(touch_diff));

        long touch_diff = s_touch - p_touch;
    }

    public void touchNipple(int p, int i_p, int s){
        if(touch_diff<250 && p >=400){
            increase_percent = -20;
            percent_1 -=20;
        }
        else if(p + i_p >= 500 && s == 0){
            percent_1 = 500;
        }
        else if(p + i_p >= 750 && s == 1){
            percent_1 = 750;
        }
        else if(p + i_p >= 880 && s == 2){
            percent_1 = 880;
        }
        else if(p + i_p >= 940 && s == 3){
            percent_1 = 940;
        }
        else if(p >= 1000){
            percent_1 = default_percent;
        }
        else{
            percent_1 += i_p;
        }
        onProgressChanged(progressBar1, percent_1);
    }

}