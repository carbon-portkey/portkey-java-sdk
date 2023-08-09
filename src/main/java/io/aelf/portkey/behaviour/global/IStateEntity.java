package io.aelf.portkey.behaviour.global;

import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class IStateEntity<T> {
    protected T state;

    protected IStateEntity() {
        this.state = this.getInitialState();
    }

    public void setNextState(T state) {
        this.state = state;
    }

    /**
     * Get the initial state of the state entity, the subclass must implement this method.
     *
     * @return The initial state.
     */
    protected abstract @NonNull T getInitialState();
}
