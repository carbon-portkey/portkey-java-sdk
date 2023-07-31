package io.aelf.portkey.assertion;

@FunctionalInterface
public interface TestFunction {
    boolean test() throws Exception;
}
