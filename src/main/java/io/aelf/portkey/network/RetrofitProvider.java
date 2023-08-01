package io.aelf.portkey.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.aelf.network.NetworkConnector;
import io.aelf.network.factories.NullOnEmptyConverterFactory;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitProvider implements IRetrofitProvider {
    private static volatile Retrofit retrofit;

    private static String hostUrl;

    /**
     * A basic way to create an API network service.
     * <p>
     *
     * @param hostUrl Portkey Peer's hostUrl,
     *                for example: <a href=
     *                "https://did-portkey-test.portkey.finance">https://did-portkey-test.portkey.finance</a>
     * @param clazz   Interface class that each method contains
     *                Retrofit's @Annotation
     * @return instance of API
     * <p>
     *
     * <p>
     * for example:
     * <p>
     * {@code interface API{
     * @GET("/api/greet") JSONElement getGreet(@Query("name") String name):
     * }
     * }}<br/>
     * {@code
     * API api = RetrofitProvider
     * .getAPIService("http://example.your-backend.com",API.class);
     * }<br/>
     * {@code
     * System.out.println(api.getGreet("Carbon").execute().body().toString());
     * }
     * </p>
     */
    public static <API> API getAPIService(Class<API> clazz) throws AElfException {
        AssertChecker.assertNotNull(retrofit, new AElfException(ResultCode.INTERNAL_ERROR
                , "retrofit is null, maybe you forgot to call resetOrInitRetrofit()?"));
        return retrofit.create(clazz);
    }

    public static void resetOrInitRetrofit(@NotNull String hostUrl) {
        AssertChecker.assertNotBlank(hostUrl);
        RetrofitProvider.hostUrl = hostUrl;
        retrofit = createRetrofit(hostUrl);
    }

    protected static Retrofit createRetrofit(@NotNull String hostUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(hostUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(NetworkConnector.getIns().getClient())
                .build();
        return retrofit;
    }

    public static String getHostUrl() {
        return hostUrl;
    }

    @Nullable
    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
