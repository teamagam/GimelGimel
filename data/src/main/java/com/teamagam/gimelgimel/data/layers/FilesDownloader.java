package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class FilesDownloader {

    private static final Logger sLogger = LoggerFactory.create(
            FilesDownloader.class.getSimpleName());

    private static final String FAKE_VALID_URL = "http://lies";

    private static FilesDownloaderAPI sFilesDownloaderAPI = initializeDownloaderAPI();

    private static FilesDownloaderAPI initializeDownloaderAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FAKE_VALID_URL)
                .client(new OkHttpClient())
                .build();
        return retrofit.create(FilesDownloaderAPI.class);
    }

    @Inject
    FilesDownloader() {
    }

    public void download(URL downloadURL, URI targetURI) {
        try {
            Response<ResponseBody> responseBody = sFilesDownloaderAPI.downloadFile(
                    downloadURL.toString()).execute();
            writeResponseBodyToDisk(responseBody.body(), targetURI);
        } catch (IOException e) {
            throw new RuntimeException("Failed downloading file " + downloadURL, e);
        }
    }

    private static void writeResponseBodyToDisk(ResponseBody body, URI targetURI)
            throws IOException {
        File targetFile = new File(targetURI.getPath());

        createParentDirectoryIfNecessary(targetFile);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(targetFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;

                sLogger.d("file download: " + fileSizeDownloaded + " of " + fileSize);
            }

            outputStream.flush();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private static void createParentDirectoryIfNecessary(File targetFile) {
        File parentFile = targetFile.getParentFile();
        if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
            throw new RuntimeException(
                    "Couldn't create parent directory for " + targetFile.getPath());
        }
    }

    interface FilesDownloaderAPI {
        @Streaming
        @GET
        Call<ResponseBody> downloadFile(@Url String fileUrl);
    }
}
