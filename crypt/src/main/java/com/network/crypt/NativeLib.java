package com.network.crypt;

public class NativeLib {

    // Used to load the 'crypt' library on application startup.
    static {
        System.loadLibrary("crypt");
    }

    /**
     * A native method that is implemented by the 'crypt' native library,
     * which is packaged with this application.
     */
    public static native String getEncryptKey();

    public static native String getDecryptKey();

    public static native String getSign();

    public static native String getLocalConfig();
}