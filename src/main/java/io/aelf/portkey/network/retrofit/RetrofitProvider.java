package io.aelf.portkey.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.aelf.network.factories.NullOnEmptyConverterFactory;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.network.interceptor.CommonHeaderInterceptor;
import io.aelf.portkey.network.interceptor.BodyModifyInterceptor;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ALL")
public class RetrofitProvider {
    private static volatile Retrofit retrofit;

    private static String hostUrl;

    /**
     * A basic way to create an API network service.
     * <p>
     *
     * @param clazz Interface class that each method contains
     *              Retrofit's @Annotation
     * @return instance of API
     * <p/>
     * example:
     * {@snippet :
     *  interface API{
     *     @GET("anything")
     *     Response<JsonElement> getGreet(@Query("name") String name);
     * }
     *  RetrofitProvider.resetOrInitRetrofit("http://www.example.com");
     *  API api = RetrofitProvider.getAPIService(API.class);
     *  api.getGreet("test");
     *}
     */
    public static <API> API getAPIService(Class<API> clazz) throws AElfException {
        AssertChecker.assertNotNull(retrofit, new AElfException(ResultCode.INTERNAL_ERROR
                , "retrofit is null, maybe you forgot to call resetOrInitRetrofit(String)?"));
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
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }

    protected static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        for (Interceptor interceptor : getInterceptors()) {
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    protected static List<Interceptor> getInterceptors() {
        List<Interceptor> interceptors = new LinkedList<>();
        // Keep the order of interceptors, since CommonHeaderInterceptor need to get the length of the body.
        interceptors.add(new BodyModifyInterceptor());
        // Therefore, CommonHeaderInterceptor should be the last interceptor to be added.
        interceptors.add(new CommonHeaderInterceptor());
        return interceptors;
    }

    public static String getHostUrl() {
        return hostUrl;
    }

    @Nullable
    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
