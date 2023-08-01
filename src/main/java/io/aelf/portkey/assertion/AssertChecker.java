package io.aelf.portkey.assertion;

import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssertChecker {

    public static void assertNotBlank(String str) throws RuntimeException {
        assertNotBlank(str, new AElfException(ResultCode.INTERNAL_ERROR, "expected str not blank"));
    }

    public static void assertNotBlank(String str, @Nullable AElfException preset) throws RuntimeException {
        innerThrow(TextUtils.isBlank(str), preset);
    }

    public static void assertNotNull(Object anything) throws RuntimeException {
        assertNotNull(anything, new AElfException(ResultCode.INTERNAL_ERROR, "expected not null but got null"));
    }

    public static void assertNotNull(Object anything, @Nullable AElfException preset) throws RuntimeException {
        innerThrow(anything == null, preset);
    }

    public static void assertNotEquals(Object anything, Object other) throws RuntimeException {
        assertNotEquals(anything, other, false);
    }

    public static void assertNotEquals(Object anything, Object other, boolean doNoExistNull) throws RuntimeException {
        if (doNoExistNull) {
            assertNotNull(anything);
            assertNotNull(other);
        }
        assertNotEquals(anything, other,
                new AElfException(ResultCode.INTERNAL_ERROR, "expected not equals but got equals"));
    }

    public static void assertNotEquals(@NotNull Object anything, @NotNull Object other, @Nullable AElfException preset)
            throws RuntimeException {
        innerThrow(anything.equals(other), preset);
    }

    public static void assertNotEmptyList(Iterable<?> list) throws RuntimeException {
        assertNotEmptyList(list, new AElfException(ResultCode.INTERNAL_ERROR, "expected not empty list"));
    }

    public static void assertNotEmptyList(Iterable<?> list, @Nullable AElfException preset) throws RuntimeException {
        innerThrow(list == null || !list.iterator().hasNext(), preset);
    }

    public static void assertNotThrow(TestFunction function) throws RuntimeException {
        assertNotThrow(function, new AElfException(ResultCode.INTERNAL_ERROR, "expected not throw exception"));
    }

    public static void assertNotThrow(TestFunction function, @Nullable AElfException preset) throws RuntimeException {
        boolean result;
        try {
            result = function.test();
        } catch (Exception e) {
            result = false;
        }
        innerThrow(result, preset);
    }

    protected static void innerThrow(boolean condition, @Nullable AElfException preset) throws RuntimeException {
        if (condition) {
            throw preset != null ? preset : new RuntimeException("assert error");
        }
    }
}
