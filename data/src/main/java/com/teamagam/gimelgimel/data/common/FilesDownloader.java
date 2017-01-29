package com.teamagam.gimelgimel.data.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class FilesDownloader {

    private final FilesDownloaderAPI mFilesDownloaderAPI;

    @Inject
    FilesDownloader(FilesDownloaderAPI filesDownloaderAPI) {
        mFilesDownloaderAPI = filesDownloaderAPI;
    }

    public void download(URL downloadURL, File targetFile) {
        try {
            writeStreamToDisk(getDownloadStream(downloadURL), targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed downloading file " + downloadURL, e);
        }
    }

    private InputStream getDownloadStream(URL downloadURL) throws IOException {
        return mFilesDownloaderAPI.downloadFile(
                downloadURL.toString()).execute().body().byteStream();
    }

    private static void writeStreamToDisk(InputStream downloadStream, File targetFile)
            throws IOException {
        createParentDirectoryIfNecessary(targetFile);
        StreamsUtils.writeStream(downloadStream, new FileOutputStream(targetFile));
    }

    private static void createParentDirectoryIfNecessary(File targetFile) {
        File parentFile = targetFile.getParentFile();
        if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
            throw new RuntimeException(
                    "Couldn't create parent directory for " + targetFile.getPath());
        }
    }

    public interface FilesDownloaderAPI {
        @Streaming
        @GET
        Call<ResponseBody> downloadFile(@Url String fileUrl);
    }
}
