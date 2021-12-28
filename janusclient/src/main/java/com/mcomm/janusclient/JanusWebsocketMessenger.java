package computician.janusclientapi;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.callback.WritableCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.WebSocket;
import org.json.JSONObject;
import timber.log.Timber;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;


/**
 * Created by ben.trent on 5/7/2015.
 */
public class JanusWebsocketMessenger implements IJanusMessenger {

    private final String uri;
    private final IJanusMessageObserver handler;
    private final JanusMessengerType type = JanusMessengerType.websocket;
    private WebSocket client = null;

    public JanusWebsocketMessenger(String uri, IJanusMessageObserver handler) {
        this.uri = uri;
        this.handler = handler;
    }

    @Override
    public JanusMessengerType getMessengerType() {
        return type;
    }

    public void connect(String cookie) {
        AsyncHttpClient asyncClient = AsyncHttpClient.getDefaultInstance();
        final AsyncHttpGet request = new AsyncHttpGet(uri.replace("ws://", "http://").replace("wss://", "https://"));
        if (cookie != null) {
            request.addHeader("Cookie", cookie);
        }

        asyncClient.websocket(request, "janus-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    handler.onError(ex);
                }
                JanusWebsocketMessenger.this.client = webSocket;
                webSocket.setWriteableCallback(new WritableCallback() {
                    @Override
                    public void onWriteable() {
                        Timber.d("On writable");
                    }
                });
                webSocket.setPongCallback(new WebSocket.PongCallback() {

                    @Override
                    public void onPongReceived(String s) {
                        Timber.d("Pong callback");
                    }
                });
                webSocket.setDataCallback(new DataCallback() {

                    @Override
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                        Timber.d("New Data");
                    }
                });
                webSocket.setEndCallback(new CompletedCallback() {

                    @Override
                    public void onCompleted(Exception ex) {
                        Timber.d("Client End");
                    }
                });
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        onMessage(s);
                    }
                });
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        Timber.d("Socket closed for some reason");
                        if (ex != null) {
                            StringWriter writer = new StringWriter();
                            PrintWriter printWriter = new PrintWriter( writer );
                            ex.printStackTrace( printWriter );
                            printWriter.flush();
                            Timber.d(ex, "webSocket.setClosedCallback");
                        }
                        if (ex != null) {
                            onError(ex);
                        } else {
                            onClose(-1, "unknown", true);
                        }
                    }
                });

                handler.onOpen();
            }
        });
    }

    private void onMessage(String message) {
        Timber.d("onMessage: \n\t%s", message);
        receivedMessage(message);
    }

    private void onClose(int code, String reason, boolean remote) {
        handler.onClose();
    }

    private void onError(Exception ex) {
        handler.onError(ex);
    }

    @Override
    public void disconnect() {
        if (client != null)
            client.close();
    }

    @Override
    public synchronized void sendMessage(String message) {
        Timber.d("sendMessage: \n\t%s", message);
        client.send(message);
        Timber.d("after send message");
    }

    @Override
    public void sendMessage(String message, BigInteger session_id) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(String message, BigInteger session_id, BigInteger handle_id) {
        sendMessage(message);
    }

    @Override
    public void receivedMessage(String msg) {
        try {
            JSONObject obj = new JSONObject(msg);
            handler.receivedNewMessage(obj);
        } catch (Exception ex) {
            handler.onError(ex);
        }
    }
}
