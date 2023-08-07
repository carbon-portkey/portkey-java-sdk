package io.aelf.portkey.network.interceptor;

import io.aelf.network.interceptor.AbstractInterceptor;
import io.aelf.portkey.internal.GlobalConfig;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CommonHeaderInterceptor extends AbstractInterceptor {

    @Override
    public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (isHeaderContentBlank(builder, GlobalConfig.DO_NOT_OVERRIDE_HEADERS_SYMBOL)) {
            if (chain.request().url().toString().contains(GlobalConfig.URL_SYMBOL_PORTKEY)) {
                builder = checkAndReplaceHeader(builder, "Accept", "text/plain;v=1.0");
                builder = checkAndReplaceHeader(builder, "Content-Type", "application/json");
            }
        } else {
            builder.removeHeader(GlobalConfig.DO_NOT_OVERRIDE_HEADERS_SYMBOL);
        }
        return chain.proceed(builder.build());
    }
}
