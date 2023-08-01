package io.aelf.portkey.async;

import io.aelf.internal.AsyncCaller;
import io.aelf.internal.DefaultAsyncExecutor;

public class AsyncTaskCaller extends AsyncCaller {
    private AsyncTaskCaller() {
        super(new DefaultAsyncExecutor());
    }

    private static volatile AsyncTaskCaller caller;

    public static AsyncTaskCaller getInstance() {
        if (caller == null) {
            synchronized (AsyncTaskCaller.class) {
                if (caller == null) {
                    caller = new AsyncTaskCaller();
                }
            }
        }
        return caller;
    }

}
