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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("ALL")
public class RetrofitProvider {
    private static volatile Retrofit retrofit;

    private static String mainHostUrl;

    private static OkHttpClient client;

    private static Map<String, Retrofit> foreignRetrofitList = new ConcurrentHashMap<>();

    private static final ClientInterceptorFactory factory = builder -> {
        getInterceptors().forEach(builder::addInterceptor);
        return builder;
    };

    /**
     * A basic way to create an API network service.
     *
     * @param clazz Interface class that each method contains
     *              Retrofit's @Annotation
     * @return instance of API
     * <p>
     * example:
     * {@snippet :
     *  interface API{
     *     @GET("anything")
     *     Response<JsonElement> getGreet(@Query("name") String name);
     * }
     *  RetrofitProvider.resetOrInitMainRetrofit("http://www.example.com");
     *  API api = RetrofitProvider.getAPIService(API.class);
     *  api.getGreet("test");
     *}
     */
    public static <API> API getAPIService(@NotNull Class<API> clazz) throws AElfException {
        AssertChecker.assertNotNull(retrofit, new AElfException(ResultCode.INTERNAL_ERROR
                , "retrofit is null, maybe you forgot to call resetOrInitMainRetrofit(String)?"));
        return retrofit.create(clazz);
    }

    public static <API> API getAPIService(@NotNull Class<API> clazz,
                                          @Nullable String alternativeHost) throws AElfException {
        return getAPIService(clazz, alternativeHost, builder -> builder);
    }

    public static <API> API getAPIService(@NotNull Class<API> clazz,
                                          @Nullable String alternativeHost,
                                          @NotNull ClientInterceptorFactory factory) throws AElfException {
        if (retrofit.baseUrl().toString().equals(alternativeHost)) {
            AssertChecker.assertNotNull(retrofit, new AElfException(ResultCode.INTERNAL_ERROR
                    , "retrofit is null, maybe you forgot to call resetOrInitMainRetrofit(String)?"));
            return retrofit.create(clazz);
        } else {
            AssertChecker.assertNotBlank(alternativeHost);
            return getCachedOrCreateForeignRetrofit(alternativeHost, factory).create(clazz);
        }
    }

    public static void resetOrInitMainRetrofit(@NotNull String hostUrl) {
        AssertChecker.assertNotBlank(hostUrl);
        RetrofitProvider.mainHostUrl = hostUrl;
        retrofit = generateRetrofit(hostUrl, false, factory);
    }

    protected static synchronized Retrofit generateRetrofit(@NotNull String hostUrl,
                                                            boolean usePureOkHttpClient,
                                                            @NotNull ClientInterceptorFactory factory) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(hostUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getOkHttpClient(usePureOkHttpClient, factory))
                .build();
        return retrofit;
    }

    public static synchronized Retrofit getCachedOrCreateForeignRetrofit(String host,
                                                                         @NotNull ClientInterceptorFactory factory) {
        Optional<Retrofit> retrofitOptional = Optional.ofNullable(foreignRetrofitList.get(host));
        if (retrofitOptional.isPresent()) {
            return retrofitOptional.get();
        } else {
            Retrofit generatedRetrofit = generateRetrofit(host, true, factory);
            foreignRetrofitList.put(host, generatedRetrofit);
            return generatedRetrofit;
        }
    }

    protected static synchronized OkHttpClient getOkHttpClient(boolean usePureClient,
                                                               @NotNull ClientInterceptorFactory factory) {
        if (usePureClient) {
            return factory.addInterceptors(generateOkHttpClient()).build();
        }
        if (client == null) {
            client = factory.addInterceptors(generateOkHttpClient()).build();
        }
        return client;
    }

    protected static OkHttpClient.Builder generateOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder;
    }

    protected static List<Interceptor> getInterceptors() {
        List<Interceptor> interceptors = new LinkedList<>();
        // Keep the order of interceptors, since CommonHeaderInterceptor need to get the length of the body.
        interceptors.add(new BodyModifyInterceptor());
        // Therefore, CommonHeaderInterceptor should be the last interceptor to be added.
        interceptors.add(new CommonHeaderInterceptor());
        return interceptors;
    }

    public static String getMainHostUrl() {
        return mainHostUrl;
    }

}
