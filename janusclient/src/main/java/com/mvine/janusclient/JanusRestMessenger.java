package com.mvine.janusclient;

import android.net.Uri;
import com.koushikdutta.async.http.*;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.callback.HttpConnectCallback;
import org.json.JSONObject;
import timber.log.Timber;

import java.math.BigInteger;


/**
 * Created by ben.trent on 5/7/2015.
 */

//TODO big todo...it would be good to use androidasync as we already utilize that for the websocket endpoint
public class JanusRestMessenger implements IJanusMessenger {

    private final IJanusMessageObserver handler;
    private final String uri;
    private BigInteger session_id;
    private BigInteger handle_id;
    private String resturi;
    private final JanusMessengerType type = JanusMessengerType.restful;

    private int count;
    public void longPoll()
    {
        Timber.d("LONG POLL");

        if(resturi.isEmpty())
            resturi = uri;


        AsyncHttpGet get = new AsyncHttpGet(uri+"/"+session_id.toString()+"&maxev=1");

        AsyncHttpClient.getDefaultInstance().executeJSONObject(get, new AsyncHttpClient.JSONObjectCallback() {
            int x = count++;
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
                Timber.d(e, "LONGPOLL %d completed with result: %s, asyncHttpResponse: %s", x, result, source);
                if(e==null)
                    receivedMessage(result.toString());
                else
                    handler.onError(e);
            }
        });
    }

    public JanusRestMessenger(String uri, IJanusMessageObserver handler) {
        this.handler = handler;
        this.uri = uri;
        resturi = "";
    }

    @Override
    public JanusMessengerType getMessengerType() {
        return type;
    }

    @Override
    public void connect(String cookie) {
         AsyncHttpClient.getDefaultInstance().execute(uri, new HttpConnectCallback() {
             @Override
             public void onConnectCompleted(Exception ex, AsyncHttpResponse response) {
                 if(ex==null)
                    handler.onOpen();
                 else
                     handler.onError(new Exception("Failed to connect"));
             }
         });

        //todo
    }

    @Override
    public void disconnect() {

        //todo
    }

    @Override
    public void sendMessage(String message) {
        //todo
        Timber.d("sendMessage Sent: \n\t%s", message);
        if(resturi.isEmpty())
            resturi = uri;
        AsyncHttpRequest request = new AsyncHttpRequest(Uri.parse(resturi),"post");
       AsyncHttpPost post = new AsyncHttpPost(resturi);

        JSONObject obj = null;
        try {
            obj = new JSONObject(message);
        }
        catch (Exception e)
        {

        }

        post.setBody(new JSONObjectBody(obj));

        AsyncHttpClient.getDefaultInstance().executeJSONObject(post, new AsyncHttpClient.JSONObjectCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
               if(e==null)
                receivedMessage(result.toString());
                else
                   handler.onError(e);
            }
        });


    }

    @Override
    public void sendMessage(String message, BigInteger session_id) {
        //todo
        this.session_id = session_id;
        resturi = "";
        resturi = uri +"/"+ session_id.toString();
        sendMessage(message);
    }

    @Override
    public void sendMessage(String message, BigInteger session_id, BigInteger handle_id) {
        //todo
        this.session_id = session_id;
        this.handle_id = handle_id;
        resturi = "";
        resturi = uri +"/"+ session_id.toString()+"/"+ handle_id.toString();
        sendMessage(message);
    }

    //todo
    private void handleNewMessage(String message) {

    }

    @Override
    public void receivedMessage(String msg) {

        try {
            Timber.d("Receive: \n\t%s", msg);
            JSONObject obj = new JSONObject(msg);
            handler.receivedNewMessage(obj);
        } catch (Exception ex) {
            handler.onError(ex);
        }
    }
}
