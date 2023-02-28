package com.example.c424.network;

import com.example.c424.utils.Base64Util;
import com.example.c424.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.network.crypt.NativeLib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MyRespConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public MyRespConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            byte[] bytes = value.bytes();
//            byte[] newData = Base64Util.decode(new String(bytes));
            String decryptKey = NativeLib.getDecryptKey();
            byte[] decryptByte = decrypt(decryptKey, bytes);
            byte[] decompressByte = decompress(decryptByte);
            LogUtil.d("MyRespConverter response:" + new String(decompressByte));
            Reader reader = new StringReader(new String(decompressByte));
            JsonReader jsonReader = gson.newJsonReader(reader);
            try {
                return adapter.read(jsonReader);
            } finally {
                reader.close();
                jsonReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("MyRespConverter convert Exception:" + e.getMessage());
        }
        return null;
    }

    public byte[] decrypt(String key, byte[] data) throws Exception {
        SecretKeySpec skeSpect = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeSpect);
        byte[] decrypted = cipher.doFinal(data);
        return decrypted;
    }

    public byte[] decompress(byte[] data) {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(data);
            baos = new ByteArrayOutputStream();
            // 解压缩
            boolean unzipResult = decompress(bais, baos);
            data = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            LogUtil.d("解压异常 :" + data.toString());
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != bais) {
                    bais.close();
                }
            } catch (Exception e) {
                LogUtil.d("decompress Exception:" + e.getMessage());
            }
        }
        return data;
    }

    public static boolean decompress(InputStream is, OutputStream os) {
        GZIPInputStream gis = null;
        boolean unzipResult = true;
        try {
            gis = new GZIPInputStream(is);

            int count;
            byte data[] = new byte[1024];
            while ((count = gis.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }

            //gis.close();
        } catch (Exception e) {
            unzipResult = false;
            LogUtil.d("解压异常 : " + e.getMessage());
        } finally {
            if (null != gis) {
                try {
                    gis.close();
                } catch (Exception e) {
                    LogUtil.d("关闭流异常 : " + e.getMessage());
                }
            }
        }
        return unzipResult;
    }
}
