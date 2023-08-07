package io.aelf.portkey.network.retrofit;

import okhttp3.OkHttpClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface ClientInterceptorFactory {
    @Contract(pure = true)
    OkHttpClient.@NonNull Builder addInterceptors(OkHttpClient.@NonNull Builder builder);
}
