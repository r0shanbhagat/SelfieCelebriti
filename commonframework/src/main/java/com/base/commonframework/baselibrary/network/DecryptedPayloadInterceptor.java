package com.base.commonframework.baselibrary.network;

import android.text.TextUtils;

import com.base.commonframework.utility.CommonFrameworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DecryptedPayloadInterceptor implements Interceptor {
    public static final String EXCLUDE_TYPE_ENCRYPTION = "pdf~jpg~jpeg~png~mpeg3~audio";
    private String TAG = "DecryptedPayloadInterceptor";
    //private final DecryptionStrategy mDecryptionStrategy;


    public DecryptedPayloadInterceptor() {
        //this.mDecryptionStrategy = mDecryptionStrategy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.isSuccessful()) {
            Response.Builder newResponse = response.newBuilder();
            String contentType = response.header("Content-Type");
            if (TextUtils.isEmpty(contentType)) contentType = "application/json";
            InputStream cryptedStream = response.body().byteStream();
            String decrypted = null;
            String headerValue = response.header("encrypt");
            if (!TextUtils.isEmpty(headerValue) && headerValue.equalsIgnoreCase("y")) {
                if (checkValidUrls(contentType)) {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(cryptedStream, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            if (line.length() > 0) {
                                sb.append(line);
                                sb.append("\n");
                            }
                        }
                        decrypted = sb.toString();
                       /* if (!TextUtils.isEmpty(decrypted)) {
                            decrypted = AESSecurity.decrypt(decrypted);
                        }*/
                        if (null != reader) {
                            reader.close();
                        }
                    } catch (Exception e) {

                    }
                    try {
                        newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted));
                    } catch (Exception e) {
                        CommonFrameworkUtil.showException(TAG, e);
                    }
                    if (null != cryptedStream) {
                        cryptedStream.close();
                    }
                    return newResponse.build();
                }
            }

        }


        return response;
    }

    private boolean checkValidUrls(String contentType) {
        String[] temp = EXCLUDE_TYPE_ENCRYPTION.split("~");
        if (null != temp) {
            for (int i = 0; i < temp.length; i++) {
                if (null != contentType && contentType.contains(temp[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public interface DecryptionStrategy {
        //String decrypt(InputStream stream);
    }
}
