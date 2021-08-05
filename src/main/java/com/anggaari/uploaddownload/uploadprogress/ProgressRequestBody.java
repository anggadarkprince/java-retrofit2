package com.anggaari.uploaddownload.uploadprogress;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProgressRequestBody extends RequestBody {
    private final File file;
    private final UploadCallbacks listener;
    private final String contentType;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public ProgressRequestBody(File file, String contentType, UploadCallbacks listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) {
        long fileLength = file.length();
        long uploaded = 0;
        double percentage = 0;
        boolean isFinished = false;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        try (FileInputStream inputStream = new FileInputStream(file)) {
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                // update progress on UI thread (android)
                // System.out.println(uploaded + "/" + fileLength);

                uploaded += read;
                bufferedSink.write(buffer, 0, read);

                double progress = (double) uploaded / fileLength * 100;
                percentage = (double) Math.round(progress * 100) / 100;
                listener.onProgressUpdate(percentage, uploaded, fileLength);

                if (percentage >= 100 && !isFinished) {
                    isFinished = true;
                    listener.onFinish(percentage, uploaded, fileLength);
                }
            }
        } catch (IOException e) {
            listener.onError(e, percentage);
        }
    }

    public interface UploadCallbacks {
        /**
         * Update progress in percent.
         *
         * @param percentage progress of upload file
         * @param uploaded total of uploaded byte
         * @param fileLength total of length of byte
         */
        void onProgressUpdate(double percentage, long uploaded, long fileLength);

        /**
         * When error occurred in progress.
         *
         * @param e exception
         * @param percentage total percentage when error occurred
         */
        void onError(Exception e, double percentage);

        /**
         * When file is uploaded and reach 100%, it does mean the request is success.
         *
         * @param percentage progress when finished
         * @param uploaded total of uploaded byte
         * @param fileLength total of length of byte
         */
        void onFinish(double percentage, long uploaded, long fileLength);
    }
}
