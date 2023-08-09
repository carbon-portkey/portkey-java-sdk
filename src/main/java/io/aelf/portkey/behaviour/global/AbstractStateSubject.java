package io.aelf.portkey.behaviour.global;

public abstract class AbstractStateSubject<T> {
    protected final T stub;

    protected AbstractStateSubject(T stub) {
        this.stub = stub;
    }
}
