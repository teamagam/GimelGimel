package com.teamagam.gimelgimel.app.model.ViewsModels;

interface IMessageVisitable {
    void accept(IMessageVisitor visitor);
}
