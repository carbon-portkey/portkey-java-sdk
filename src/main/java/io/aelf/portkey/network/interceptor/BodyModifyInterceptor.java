package io.aelf.portkey.network.interceptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.aelf.network.interceptor.AbstractInterceptor;
import io.aelf.portkey.utils.log.GLogger;
import okhttp3.*;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class BodyModifyInterceptor extends AbstractInterceptor {

    @Override
    public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
        try {
            Request request = chain.request();
            if (!request.method().equals("GET") && (request.body() == null || request.body().contentLength() <= 0)) {
                Request.Builder builder = request.newBuilder();
                JsonObject jsonObject = new JsonObject();
                HttpUrl url = request.url();
                for (String key : url.queryParameterNames()) {
                    String value = url.queryParameter(key);
                    if (TextUtils.isEmpty(value)) {
                        jsonObject.addProperty(key, value);
                        continue;
                    }
                    try {
                        JsonElement element = new JsonParser().parse(value);
                        jsonObject.add(key, element);
                    } catch (JsonSyntaxException e) {
                        jsonObject.addProperty(key, value);
                    }
                }
                GLogger.i("BodyModifyInterceptor: Create body => " + GLogger.prettyJSON(jsonObject));
                builder.put(RequestBody.create(MediaType.parse("application/json"), jsonObject.toString()));
                return chain.proceed(builder.build());
            }
        } catch (Throwable ignored) {
        }
        return chain.proceed(chain.request());
    }
}
