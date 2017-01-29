package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import rx.Observable;

public interface VectorLayersRepository {
    void add(VectorLayer vectorLayer);

    VectorLayer get(String id);
}
