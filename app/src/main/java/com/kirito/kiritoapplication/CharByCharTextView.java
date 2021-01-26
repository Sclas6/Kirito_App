package com.kirito.kiritoapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CharByCharTextView extends androidx.appcompat.widget.AppCompatTextView {
    String defaultText = "文字列を１文字ずつ出力するテスト";
    private static int TIMEOUT_MESSAGE = 1;
    private static int INTERVAL = 50;

    // Meta Data
    int i = 0;
    String putWord = "";
    String putText = "";

    public void startCharByCharAnim() {
        initMetaData();
        handler.sendEmptyMessage(TIMEOUT_MESSAGE);
    }

    public void setTargetText(String target) {
        this.defaultText = target;
    }

    public CharByCharTextView(Context context) {
        super(context);
    }

    public CharByCharTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CharByCharTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initMetaData() {
        i = 0;
        putWord = "";
        putText = "";
    }

    // 文字列を一文字ずつ出力するハンドラ
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            // 文字列を配列に１文字ずつセット
            char data[] = defaultText.toCharArray();

            // 配列数を取得
            int arrNum = data.length;

            if (i < arrNum) {
                if (msg.what == TIMEOUT_MESSAGE) {
                    putWord = String.valueOf(data[i]);
                    putText = putText + putWord;

                    setText(putText);
                    handler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE, INTERVAL);
                    i++;
                } else {
                    super.dispatchMessage(msg);
                }
            }
        }
    };
}