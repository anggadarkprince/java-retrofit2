package com.anggaari.uploaddownload.downloadprogress;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final DownloadCallbacks listener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, DownloadCallbacks listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @NotNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            double percentage = 0;
            boolean isStarted = false;
            boolean isFinished = false;

            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);

                double progress = (double) totalBytesRead / contentLength() * 100;
                percentage = (double) Math.round((bytesRead == -1 ? 100L : progress) * 100) / 100;

                if (listener != null) {
                    if (!isStarted) {
                        isStarted = true;
                        listener.onStart(0, 0, contentLength());
                    }

                    if (!isFinished) {
                        listener.onProgressUpdate(percentage, totalBytesRead, contentLength());
                    }

                    if (percentage >= 100 && totalBytesRead >= contentLength() && !isFinished) {
                        isFinished = true;
                        listener.onFinish(percentage, totalBytesRead, contentLength());
                    }
                }

                return bytesRead;
            }
        };
    }

    public interface DownloadCallbacks {
        /**
         * Start to download.
         */
        void onStart(double percentage, long downloaded, long fileLength);

        /**
         * Update progress in percent.
         *
         * @param percentage progress of upload file
         * @param downloaded total of downloaded byte
         * @param fileLength total of length of byte
         */
        void onProgressUpdate(double percentage, long downloaded, long fileLength);

        /**
         * When file is downloaded and reach 100%, it does mean the request is success.
         *
         * @param percentage progress when finished
         * @param downloaded total of downloaded byte
         * @param fileLength total of length of byte
         */
        void onFinish(double percentage, long downloaded, long fileLength);
    }
}
