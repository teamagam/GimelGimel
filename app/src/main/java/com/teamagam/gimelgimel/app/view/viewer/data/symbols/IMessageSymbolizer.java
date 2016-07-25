package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * interface to return symbol for each message.
 */
public interface IMessageSymbolizer {
    Symbol symbolize(Message message);
}
