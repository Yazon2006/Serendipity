package com.example.bel.softwarefactory.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.bel.softwarefactory.utils.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private File file;
    private UploadCallbacks uploadCallbacks;
    private final String TAG = this.getClass().getSimpleName();

    public ProgressRequestBody(final File file, final UploadCallbacks uploadCallbacks) {
        this.file = file;
        this.uploadCallbacks = uploadCallbacks;
    }

    @Override
    public MediaType contentType() {
        // I want to upload only audio
        Log.d(TAG, "contentType() : " + AppConstants.MIME_TYPE);
        return MediaType.parse(AppConstants.MIME_TYPE);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Log.d(TAG, "writeTo()");

        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long uploaded = 0;

        try (FileInputStream in = new FileInputStream(file)) {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            uploadCallbacks.onProgressUpdate((int) (100 * mUploaded / mTotal));
        }
    }
}
