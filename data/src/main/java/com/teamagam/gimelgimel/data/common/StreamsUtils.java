package com.teamagam.gimelgimel.data.common;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamsUtils {

  private static final int COPY_BUFFER_SIZE_BYTES = 4096;

  public static void writeStream(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    try {
      copy(inputStream, outputStream);
    } finally {
      closeStreams(inputStream, outputStream);
    }
  }

  public static void closeStreams(Closeable... closeables) throws IOException {
    for (Closeable closeable : closeables) {
      closeStream(closeable);
    }
  }

  public static void closeStream(Closeable inputStream) throws IOException {
    if (inputStream != null) {
      inputStream.close();
    }
  }

  private static void copy(InputStream from, OutputStream to) throws IOException {
    byte[] buf = new byte[COPY_BUFFER_SIZE_BYTES];

    while (true) {
      int r = from.read(buf);
      if (r == -1) {
        return;
      }

      to.write(buf, 0, r);
    }
  }
}