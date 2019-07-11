/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theta360.iftttsample;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.theta360.iftttsample.task.TakePictureTask;
import com.theta360.iftttsample.task.TakePictureTask.Callback;
import com.theta360.iftttsample.task.WebhookTask;
import com.theta360.pluginlibrary.activity.PluginActivity;
import com.theta360.pluginlibrary.callback.KeyCallback;
import com.theta360.pluginlibrary.receiver.KeyReceiver;
import com.theta360.pluginlibrary.values.LedColor;
import com.theta360.pluginlibrary.values.LedTarget;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends PluginActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String WEBHOOK_KEY = "write ifttt webhook key here";
    private static final String WEBHOOK_EVENT_NAME = "write ifttt webhook event name here";
    private static final String WEBHOOK_URL = "https://maker.ifttt.com/trigger/" + WEBHOOK_EVENT_NAME + "/with/key/" + WEBHOOK_KEY;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AtomicBoolean isProceeding = new AtomicBoolean();
    private WebhookTask webhookTask = new WebhookTask(WEBHOOK_URL);

    private TakePictureTask.Callback mTakePictureTaskCallback = new Callback() {
        @Override
        public void onTakePicture(String fileUrl) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set enable to close by pluginlibrary, If you set false, please call close() after finishing your end processing.
        setAutoClose(true);
        // Set a callback when a button operation event is acquired.
        setKeyCallback(new KeyCallback() {
            @Override
            public void onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyReceiver.KEYCODE_CAMERA) {
                    /*
                     * To take a static picture, use the takePicture method.
                     * You can receive a fileUrl of the static picture in the callback.
                     */
//                    new TakePictureTask(mTakePictureTaskCallback).execute();
                    if (isProceeding.get()) {
                        Log.d(TAG, "Skip to post webhook.");
                        return;
                    }
                    executor.submit(() -> {
                        isProceeding.set(true);
                        notificationAudioOpen();
                        try {
                            if (webhookTask.request()) {
                                Log.d(TAG, "Succeeded to post webhook.");
                                notificationAudioClose();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to post webhook.", e);
                            notificationAudioWarning();
                        } finally {
                            isProceeding.set(false);
                        }
                    });
                }
            }

            @Override
            public void onKeyUp(int keyCode, KeyEvent event) {
                /**
                 * You can control the LED of the camera.
                 * It is possible to change the way of lighting, the cycle of blinking, the color of light emission.
                 * Light emitting color can be changed only LED3.
                 */
                notificationLedBlink(LedTarget.LED3, LedColor.BLUE, 1000);
            }

            @Override
            public void onKeyLongPress(int keyCode, KeyEvent event) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isApConnected()) {

        }
    }

    @Override
    protected void onPause() {
        // Do end processing
        //close();

        super.onPause();
    }
}
