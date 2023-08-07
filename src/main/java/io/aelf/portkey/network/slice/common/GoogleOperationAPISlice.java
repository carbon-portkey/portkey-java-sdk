package io.aelf.portkey.network.slice.common;

import org.jetbrains.annotations.NotNull;

public interface GoogleOperationAPISlice {
    String getGoogleAccessToken(@NotNull String authorization);
}
