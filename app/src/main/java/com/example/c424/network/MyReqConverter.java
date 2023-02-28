package com.example.c424.network;

import com.example.c424.utils.Base64Util;
import com.example.c424.utils.LogUtil;
import com.google.gson.TypeAdapter;
import com.network.crypt.NativeLib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class MyReqConverter<T> implements Converter<T, RequestBody> {

    private final TypeAdapter<T> adapter;

    public MyReqConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        try {
            String originalJson = adapter.toJson(value);
            String sign = NativeLib.getSign();
            String encryptKey = NativeLib.getEncryptKey();
            byte[] originalByte = originalJson.getBytes("utf-8");
            byte[] signByte = sign.getBytes("utf-8");
            byte[] data = new byte[originalByte.length + signByte.length + 1];

            data[0] = (byte) signByte.length;
            System.arraycopy(signByte, 0, data, 1, signByte.length);
            System.arraycopy(originalByte, 0, data, signByte.length + 1, originalByte.length);
            byte[] compressDataByte = compress(data);
//            String encryptDataStr = encrypt(encryptKey, compressDataByte);
            byte[] encryptDataStr = encrypt(encryptKey, compressDataByte);
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), encryptDataStr);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("MyReqConverter convert Exception:" + e.getMessage());
        }
        return null;
    }

    public byte[] compress(byte[] data) {
        byte[] output = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(data);
            baos = new ByteArrayOutputStream();
            compress(bais, baos);
            output = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("compress Exception1:" + e.getMessage());
        } finally {
            try {
                baos.close();
                bais.close();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("compress Exception2: " + e.getMessage());
            }
        }
        return output;
    }

    public static void compress(InputStream is, OutputStream os) {
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(os);
            int count;
            byte data[] = new byte[1024];
            while ((count = is.read(data, 0, 1024)) != -1) {
                gos.write(data, 0, count);
            }
            gos.finish();
            gos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("compress Exception3:" + e.getMessage());
        } finally {
            try {
                gos.close();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("compress Exception4:" + e.getMessage());
            }
        }
    }

    public byte[] encrypt(String key, byte[] data) throws Exception {
        LogUtil.d("encrypt");
        SecretKeySpec skeSpect = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeSpect);
        byte[] decrypted = cipher.doFinal(data);
        return decrypted;
    }
}
