package com.example.smart_phone;

import android.util.Log;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;

class MyUrlRequestCallback extends UrlRequest.Callback {
    private static final String TAG = "MyUrlRequestCallback";

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
        Log.i(TAG, "onRedirectReceived method called.");
        // You should call the request.followRedirect() method to continue
        // processing the request.
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onResponseStarted method called.");
        // You should call the request.read() method before the request can be
        // further processed. The following instruction provides a ByteBuffer object
        // with a capacity of 102400 bytes for the read() method. The same buffer
        // with data is passed to the onReadCompleted() method.
        request.read(ByteBuffer.allocateDirect(102400));
        int httpStatusCode = info.getHttpStatusCode();
        Log.d("TEST",request.toString());
        if (httpStatusCode == 200) {
            // The request was fulfilled. Start reading the response.
            //request.read(myBuffer);
        } else if (httpStatusCode == 503) {
            // The service is unavailable. You should still check if the request
            // contains some data.
            //request.read(myBuffer);
        }
        //responseHeaders = info.getAllHeaders();
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
        Log.i(TAG, "onReadCompleted method called.");
        // You should keep reading the request until there's no more data.
        byteBuffer.clear();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onSucceeded method called.");
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

    }
}
