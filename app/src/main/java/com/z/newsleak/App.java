package com.z.newsleak;

import android.app.Application;
import android.util.Log;

import com.z.newsleak.di.DI;
import com.z.newsleak.service.NewsUpdateWorker;

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

public class App extends Application {

    private static final String LOG_TAG = "RxErrorHandler";

    @Override
    public void onCreate() {
        super.onCreate();

        DI.init(this);

        NewsUpdateWorker.enqueueWorker();
        setRxErrorHandler();
    }

    private void setRxErrorHandler() {
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e.getCause();
            }
            if ((e instanceof SocketException) || (e instanceof IOException)) {
                Log.d(LOG_TAG, "Irrelevant network problem or API that throws on cancellation", e);
                return;
            }
            if (e instanceof InterruptedException) {
                Log.d(LOG_TAG, "Some blocking code was interrupted by a dispose call", e);
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                Log.d(LOG_TAG, "That's likely a bug in the application", e);
                return;
            }
            if (e instanceof IllegalStateException) {
                Log.d(LOG_TAG, "That's a bug in RxJava or in a custom operator", e);
                return;
            }
            Log.d(LOG_TAG, "Undeliverable exception received, not sure what to do", e);
        });
    }
}