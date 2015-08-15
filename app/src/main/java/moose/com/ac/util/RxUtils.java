package moose.com.ac.util;

import android.os.Build;

import com.google.gson.Gson;

import java.util.Locale;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 12.
 */
public class RxUtils {
    public static final String UA = "acfun/1.0 (Linux; U; Android "+ Build.VERSION.RELEASE+"; "+
            Build.MODEL+"; "+ Locale.getDefault().getLanguage()+"-"+
            Locale.getDefault().getCountry().toLowerCase()+
            ") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";
    private RxUtils(){

    }
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }

    static class FarbleError implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            assert (r != null);
            switch (r.getStatus()) {
                case 401:

                    break;
                default:
                    break;
            }
            return cause;
        }
    }

    public static <T> T createApi(Class<T> c, String url) {
        RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "application/json; q=0.5");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(OkHttpClientProvider.get()))
                .setConverter(new GsonConverter(new Gson()))
                .setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);
    }
}
