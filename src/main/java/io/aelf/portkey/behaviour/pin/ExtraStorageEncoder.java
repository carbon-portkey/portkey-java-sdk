package io.aelf.portkey.behaviour.pin;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ExtraStorageEncoder {
    @Contract(pure = true)
    public static String encode(@NotNull String input, @NotNull String key) {
        assert !input.isEmpty() && !key.isEmpty();
        char[] inputChars = input.toCharArray();
        char[] keyChars = key.toCharArray();
        char[] outputChars = new char[inputChars.length];
        for (int i = 0; i < inputChars.length; i++) {
            outputChars[i] = encodeChar(inputChars[i], keyChars[i % keyChars.length]);
        }
        return new String(outputChars);
    }


    @Contract(pure = true)
    private static char encodeChar(char input, char key) {
        return (char) (input ^ key);
    }
}
