package com.flippingutilities.model;

public class ItemViewEvent {
    private String loggingId;
    private int itemId;
    private String eventType = "item_view";

    public ItemViewEvent (String loggingId, int itemId) {
        this.itemId = itemId;
        this.loggingId = loggingId;
    }
}
