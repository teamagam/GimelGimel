package com.teamagam.gimelgimel.app.map.model.symbols;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * interface to return symbol for each message.
 */
public interface IMessageSymbolizer {
    SymbolApp symbolize(MessageApp message);
}
