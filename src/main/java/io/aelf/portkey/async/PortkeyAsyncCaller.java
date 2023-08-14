package io.aelf.portkey.async;

import io.aelf.internal.AbstractAsyncExecutor;
import io.aelf.internal.AsyncCaller;
import io.aelf.internal.AsyncCommand;
import io.aelf.internal.AsyncResult;
import io.aelf.network.NetworkConfig;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PortkeyAsyncCaller extends AsyncCaller {
    private static volatile PortkeyAsyncCaller caller;

    private PortkeyAsyncCaller() {
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

    public static void asyncCall(Runnable asyncFunction) {
        getInstance().asyncCall(() -> {
                    asyncFunction.run();
                    return new AsyncResult<>(null);
                },
                null,
                null
        );
    }

    public static PortkeyAsyncCaller getInstance() {
        if (caller == null) {
            synchronized (PortkeyAsyncCaller.class) {
                if (caller == null) {
                    caller = new PortkeyAsyncCaller();
                }
            }
        }
        return caller;
    }

}
