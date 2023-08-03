package io.aelf.portkey.network.interceptor;

import com.google.gson.JsonObject;
import io.aelf.network.interceptor.AbstractInterceptor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class BodyModifyInterceptor extends AbstractInterceptor {

    @Override
    public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
        try {
            Request request = chain.request();
            if (Objects.requireNonNull(request.body()).contentLength() <= 0) {
                Request.Builder builder = request.newBuilder();
                JsonObject jsonObject = new JsonObject();
                HttpUrl url = request.url();
                for (String key : url.queryParameterNames()) {
                    String value = url.queryParameter(key);
                    if (!TextUtils.isEmpty(value) && isNumber(value)) {
                        jsonObject.addProperty(key, Double.parseDouble(value));
                    } else {
                        jsonObject.addProperty(key, value);
                    }
                }
                builder.put(RequestBody.create(null, jsonObject.toString()));
                return chain.proceed(builder.build());
            }
        } catch (Throwable ignored) {
        }
        return chain.proceed(chain.request());
    }


    private static boolean isNumber(@NotNull String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
