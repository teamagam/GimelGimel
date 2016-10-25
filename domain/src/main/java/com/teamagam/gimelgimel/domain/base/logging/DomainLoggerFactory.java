package com.teamagam.gimelgimel.domain.base.logging;

public interface DomainLoggerFactory {

    DomainLogger create(String tag);
}
