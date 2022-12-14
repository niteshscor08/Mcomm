package com.mvine.janusclient;

import org.json.JSONObject;

/**
 * Created by ben.trent on 6/25/2015.
 */
public interface IJanusMessageObserver {
    void receivedNewMessage(JSONObject obj);

    void onClose();

    void onOpen();

    void onError(Exception ex);
}
