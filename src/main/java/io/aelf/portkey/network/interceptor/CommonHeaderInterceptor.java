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
        if (chain.request().url().toString().contains(GlobalConfig.URL_SYMBOL_PORTKEY)
                && isHeaderContentBlank(chain, GlobalConfig.DO_NOT_OVERRIDE_HEADERS_SYMBOL)) {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Accept", "text/plain;v=1.0");
            builder.addHeader("Content-Type", "application/json");
            return chain.proceed(builder.build());
        }
        return chain.proceed(chain.request());
    }
}
