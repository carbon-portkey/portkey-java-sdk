package io.aelf.portkey.async;

import io.aelf.internal.AbstractAsyncExecutor;
import io.aelf.internal.AsyncCaller;
import io.aelf.internal.AsyncCommand;
import io.aelf.network.NetworkConfig;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncTaskCaller extends AsyncCaller {
    private static volatile AsyncTaskCaller caller;

    private AsyncTaskCaller() {
        super(
                new AbstractAsyncExecutor() {
                    private final ThreadPoolExecutor service =
                            new ThreadPoolExecutor(3, 100,
                                    NetworkConfig.TIME_OUT_LIMIT * 2, TimeUnit.MILLISECONDS, new SynchronousQueue<>());

                    @Override
                    protected <T> void executeRequest(AsyncCommand<T> asyncCommand) {
                        this.service.execute(() -> this.onNewRequest(asyncCommand));
                    }
                });
    }

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
