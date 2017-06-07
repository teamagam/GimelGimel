package com.teamagam.gimelgimel.data.response.rest.adapter.factory;

import com.teamagam.gimelgimel.data.response.rest.exceptions.RetrofitException;
import com.teamagam.gimelgimel.data.message.rest.exceptions.RetrofitException;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.HttpException;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
  private final RxJava2CallAdapterFactory mOriginal;

  private RxErrorHandlingCallAdapterFactory() {
    mOriginal = RxJava2CallAdapterFactory.create();
  }

  public static CallAdapter.Factory create() {
    return new RxErrorHandlingCallAdapterFactory();
  }

  @Override
  public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
    return new RxCallAdapterWrapper(retrofit, mOriginal.get(returnType, annotations, retrofit));
  }

  private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Observable<?>> {
    private final Retrofit mRetrofit;
    private final CallAdapter<R, ?> mWrapped;

    public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<R, ?> wrapped) {
      mRetrofit = retrofit;
      mWrapped = wrapped;
    }

    @Override
    public Type responseType() {
      return mWrapped.responseType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<?> adapt(Call<R> call) {
      return ((Observable) mWrapped.adapt(call)).onErrorResumeNext(
          new Function<Throwable, Observable>() {
            @Override
            public Observable apply(Throwable throwable) {
              return Observable.error(asRetrofitException(throwable));
            }
          });
    }

    private RetrofitException asRetrofitException(Throwable throwable) {
      // We had non-200 http error
      if (throwable instanceof HttpException) {
        HttpException httpException = (HttpException) throwable;
        Response response = httpException.response();
        return RetrofitException.httpError(response.raw().request().url().toString(), response,
            mRetrofit);
      }
      // A network error happened
      if (throwable instanceof IOException) {
        return RetrofitException.networkError((IOException) throwable);
      }

      // We don't know what happened. We need to simply convert to an unknown error
      return RetrofitException.unexpectedError(throwable);
    }
  }
}
