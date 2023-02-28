package com.example.c424.network;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ConverterManager extends Converter.Factory {

    public ConverterManager(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson is null");
        }
        this.gson = gson;
    }

    private final Gson gson;
    public static ConverterManager create() {
        return create(new Gson());
    }

    public static ConverterManager create(Gson gson) {
        return new ConverterManager(gson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyReqConverter<>(adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyRespConverter<>(gson, adapter);
    }
}
