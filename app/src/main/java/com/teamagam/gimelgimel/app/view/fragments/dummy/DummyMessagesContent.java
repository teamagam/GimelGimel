package com.teamagam.gimelgimel.app.view.fragments.dummy;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageText;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample sender for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyMessagesContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Message> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Message> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void addItem(Message item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getMessageId(), item);
    }

    public static Message createDummyItem(int position) {
        MessageText msg = new MessageText("Sender " + position, makeDetails(position));
        msg.setCreatedAt(new Date());
        msg.setMessageId(String.valueOf(position));
        return msg;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
