package com.teamagam.gimelgimel.domain.images;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class SendImageMessageInteractor extends AbstractInteractor {

    private ImagesRepository mImagesRepository;
    private MessageImage mMessageImage;
    private String mImagePath;

    public SendImageMessageInteractor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                      ImagesRepository repository) {
        super(threadExecutor, postExecutionThread);

        mImagesRepository = repository;
    }

    public void sendImageMessage(Subscriber subscriber, MessageImage messageImage, String imagePath) {
        if(isValid(subscriber, messageImage, imagePath)) {
            mMessageImage = messageImage;
            mImagePath = imagePath;

            execute(subscriber);
        } else {
            throw new IllegalArgumentException("None of the arguments can be null");
        }
    }

    private boolean isValid(Subscriber subscriber, MessageImage messageImage, String imagePath) {
        return !(subscriber == null || messageImage == null || imagePath.isEmpty());
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.just(mImagePath)
                .map(imagePath -> {
                    try {
                        byte[] imageBytes = mImagesRepository.getImageBytes(imagePath);

                        return compressImage(imageBytes);
                    } catch (Throwable e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .doOnNext(bytes ->
                        mImagesRepository.uploadImage(mMessageImage, getName(mImagePath), bytes));
    }

    private String getName(String imagePath) {
        File file = new File(imagePath);

        return file.getName();
    }

    private byte[] compressImage(byte[] imageBytes) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);

            int height = image.getHeight();
            int width = image.getWidth();
            int scale = getScaleSize(height, width);

            image = transformImage(image, scale);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, Constants.IMAGE_COMPRESS_TYPE, outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private BufferedImage transformImage(BufferedImage image, int scale) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();
        transform.scale(scale, scale);

        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        transformOp.filter(image, result);

        return result;
    }

    private int getScaleSize(int imageHeight, int imageWidth) {
        int scale = 1;

        if (imageHeight > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS ||
                imageWidth > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS) {
            scale = (int) Math.pow(2,
                    (int) Math.ceil(
                            Math.log(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS /
                                    (double) Math.max(imageHeight, imageWidth)) / Math.log(0.5)));
        }

        return scale;
    }
}
