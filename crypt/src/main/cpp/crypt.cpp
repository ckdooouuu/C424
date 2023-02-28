#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_network_crypt_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_network_crypt_NativeLib_getEncryptKey(JNIEnv *env, jclass clazz) {
    char *clientKey = "5km3WLGZ1Zplt6Ci";
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(clientKey));
    env->SetByteArrayRegion(bytes, 0, strlen(clientKey), (jbyte *) clientKey);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring) env->NewObject(strClass, ctorID, bytes, encoding);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_network_crypt_NativeLib_getDecryptKey(JNIEnv *env, jclass clazz) {
    char *clientKey = "SNegeygZqrrbQemX";
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(clientKey));
    env->SetByteArrayRegion(bytes, 0, strlen(clientKey), (jbyte *) clientKey);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring) env->NewObject(strClass, ctorID, bytes, encoding);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_network_crypt_NativeLib_getSign(JNIEnv *env, jclass clazz) {
    char *clientKey = "6j406fdts42n0s75wn35mjok0t5r7tto";
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(clientKey));
    env->SetByteArrayRegion(bytes, 0, strlen(clientKey), (jbyte *) clientKey);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring) env->NewObject(strClass, ctorID, bytes, encoding);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_network_crypt_NativeLib_getLocalConfig(JNIEnv *env, jclass clazz) {
    char *clientKey = "{\"StStart_rocketspeedup_Ad\":true,\"Max_rocketspeedup_Connect\":3,\"StartAnimation_rocketspeedup_Upper\":11,\"configAd\":[{\"adLocation\":\"start\",\"innerAd\":[{\"nativeBtn\":0,\"adId\":\"ca-app-pub-3940256099942544/3419835294\",\"adv_scale\":1,\"advFormat\":\"admob_start\"}],\"closeAdv\":true},{\"adLocation\":\"finish\",\"innerAd\":[{\"nativeBtn\":0,\"adId\":\"ca-app-pub-3940256099942544/1033173712\",\"adv_scale\":1,\"advFormat\":\"admob_int\"}],\"closeAdv\":true},{\"adLocation\":\"home\",\"innerAd\":[{\"nativeBtn\":0,\"adId\":\"ca-app-pub-3940256099942544/2247696110\",\"adv_scale\":1,\"advFormat\":\"admob_nav\"}],\"closeAdv\":true},{\"adLocation\":\"report\",\"innerAd\":[{\"nativeBtn\":0,\"adId\":\"ca-app-pub-3940256099942544/2247696110\",\"adv_scale\":0,\"advFormat\":\"admob_nav\"}],\"closeAdv\":true}],\"strategyList\":[{\"tacticsConfig\":[{\"timeouts\":15,\"strategyType\":2},{\"timeouts\":15,\"strategyType\":2},{\"timeouts\":15,\"strategyType\":1}],\"strategy_code\":1}],\"Native_rocketspeedup_ClickLimit\":2,\"forceConfig\":{\"finishApp\":0,\"dialogMsg\":\"Forced update, your app is too old, please update it (* ^ ▽ ^ *)\",\"minApp\":2,\"upgradett\":\"Forced update* ^ ▽ ^ *\",\"mustForce\":false,\"upgradeName\":\"com.example.c424\"},\"Config_rocketspeedup_Interval\":2,\"rocketspeedup_Guide\":true,\"Report_rocketspeedup_BackAd\":true,\"List_rocketspeedup_BackAd\":true,\"rocketspeedup_BlackLists\":\"1\",\"Priority_rocketspeedup_Connect\":\"US,SG\"}";
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(clientKey));
    env->SetByteArrayRegion(bytes, 0, strlen(clientKey), (jbyte *) clientKey);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring) env->NewObject(strClass, ctorID, bytes, encoding);
}