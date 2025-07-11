#ifndef LOGIN_H
#define LOGIN_H

#include "StrEnc.h"
#include "Includes.h"
#include "curl/curl.h"
#include "json.hpp"
#include "Log.h"
#include <jni.h>
#include <string>
#include "obfuscate.h"
#include <jni.h>
#include <string>
#include <android/log.h>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/rsa.h>
#include <openssl/err.h>
#include <openssl/md5.h>

bool check;
using json = nlohmann::ordered_json;
bool bValid = false, xEnv = false;
std::string g_Auth, g_Token,EXP;

const char *GetAndroidID(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getContentResolverMethod = env->GetMethodID(contextClass,"getContentResolver","()Landroid/content/ContentResolver;");
    jclass settingSecureClass = env->FindClass("android/provider/Settings$Secure");
    jmethodID getStringMethod = env->GetStaticMethodID(settingSecureClass,"getString", "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;");

    auto obj = env->CallObjectMethod(context, getContentResolverMethod);
    auto str = (jstring) env->CallStaticObjectMethod(settingSecureClass, getStringMethod, obj,env->NewStringUTF("android_id"));
    return env->GetStringUTFChars(str, 0);
}

const char *GetDeviceModel(JNIEnv *env) {
    jclass buildClass = env->FindClass("android/os/Build");
    jfieldID modelId = env->GetStaticFieldID(buildClass, "MODEL","Ljava/lang/String;");

    auto str = (jstring) env->GetStaticObjectField(buildClass, modelId);
    return env->GetStringUTFChars(str, 0);
}
const char *GetDeviceBrand(JNIEnv *env) {
    jclass buildClass = env->FindClass("android/os/Build");
    jfieldID modelId = env->GetStaticFieldID(buildClass, "BRAND","Ljava/lang/String;");

    auto str = (jstring) env->GetStaticObjectField(buildClass, modelId);
    return env->GetStringUTFChars(str, 0);
}

const char *GetPackageName(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getPackageNameId = env->GetMethodID(contextClass, "getPackageName","()Ljava/lang/String;");

    auto str = (jstring) env->CallObjectMethod(context, getPackageNameId);
    return env->GetStringUTFChars(str, 0);
}

const char *GetDeviceUniqueIdentifier(JNIEnv *env, const char *uuid) {
    jclass uuidClass = env->FindClass("java/util/UUID");

    auto len = strlen(uuid);

    jbyteArray myJByteArray = env->NewByteArray(len);
    env->SetByteArrayRegion(myJByteArray, 0, len, (jbyte *) uuid);

    jmethodID nameUUIDFromBytesMethod = env->GetStaticMethodID(uuidClass,"nameUUIDFromBytes","([B)Ljava/util/UUID;");
    jmethodID toStringMethod = env->GetMethodID(uuidClass, "toString","()Ljava/lang/String;");

    auto obj = env->CallStaticObjectMethod(uuidClass, nameUUIDFromBytesMethod, myJByteArray);
    auto str = (jstring) env->CallObjectMethod(obj, toStringMethod);
    return env->GetStringUTFChars(str, 0);
}

struct MemoryStruct {
    char *memory;
    size_t size;
};

static size_t WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp) {
    size_t realsize = size * nmemb;
    struct MemoryStruct *mem = (struct MemoryStruct *) userp;

    mem->memory = (char *) realloc(mem->memory, mem->size + realsize + 1);
    if (mem->memory == NULL) {
        return 0;
    }

    memcpy(&(mem->memory[mem->size]), contents, realsize);
    mem->size += realsize;
    mem->memory[mem->size] = 0;

    return realsize;
}

std::string CalcMD5(std::string s) {
    std::string result;

    unsigned char hash[MD5_DIGEST_LENGTH];
    char tmp[4];

    MD5_CTX md5;
    MD5_Init(&md5);
    MD5_Update(&md5, s.c_str(), s.length());
    MD5_Final(hash, &md5);
    for (unsigned char i : hash) {
        sprintf(tmp, "%02x", i);
        result += tmp;
    }
    return result;
}

std::string CalcSHA256(std::string s) {
    std::string result;

    unsigned char hash[SHA256_DIGEST_LENGTH];
    char tmp[4];SHA256_CTX sha256;
    SHA256_Init(&sha256);
    SHA256_Update(&sha256, s.c_str(), s.length());
    SHA256_Final(hash, &sha256);
    for (unsigned char i : hash) {
        sprintf(tmp, "%02x", i);
        result += tmp;
    }
    return result;

}
extern "C" JNIEXPORT jstring JNICALL native_Check(JNIEnv *env, jclass clazz, jobject mContext, jstring mUserKey) {
    const char* user_key = env->GetStringUTFChars(mUserKey, 0);
    std::string hwid = user_key;
    hwid += GetAndroidID(env, mContext);StrEnc("lakyWbF&oaI*n86C`nK<mwLp*0EeCIO=Zvaw", "\x04\x15\x1F\x09\x24\x58\x69\x09\x02\x0E\x2D\x04\x1E\x59\x58\x26\x0C\x1D\x3F\x53\x1F\x12\x62\x03\x5A\x51\x26\x00\x6C\x2A\x20\x53\x34\x13\x02\x03", 36).c_str();/*https://mod.panelstore.space/connect*/ StrEnc("lakyWbF&oaI*n86C`nK<mwLp*0EeCIO=Zvaw", "\x04\x15\x1F\x09\x24\x58\x69\x09\x02\x0E\x2D\x04\x1E\x59\x58\x26\x0C\x1D\x3F\x53\x1F\x12\x62\x03\x5A\x51\x26\x00\x6C\x2A\x20\x53\x34\x13\x02\x03", 36).c_str();/*https://mod.panelstore.space/connect*/ StrEnc("lakyWbF&oaI*n86C`nK<mwLp*0EeCIO=Zvaw", "\x04\x15\x1F\x09\x24\x58\x69\x09\x02\x0E\x2D\x04\x1E\x59\x58\x26\x0C\x1D\x3F\x53\x1F\x12\x62\x03\x5A\x51\x26\x00\x6C\x2A\x20\x53\x34\x13\x02\x03", 36).c_str();/*https://mod.panelstore.space/connect*/ StrEnc("lakyWbF&oaI*n86C`nK<mwLp*0EeCIO=Zvaw", "\x04\x15\x1F\x09\x24\x58\x69\x09\x02\x0E\x2D\x04\x1E\x59\x58\x26\x0C\x1D\x3F\x53\x1F\x12\x62\x03\x5A\x51\x26\x00\x6C\x2A\x20\x53\x34\x13\x02\x03", 36).c_str();
    hwid += GetDeviceModel(env);
    hwid += GetDeviceBrand(env);
    std::string UUID = GetDeviceUniqueIdentifier(env, hwid.c_str());
    std::string errMsg;
    struct MemoryStruct chunk{};
    chunk.memory = (char *) malloc(1);
    chunk.size = 0;

    CURL *curl;
    CURLcode res;
    curl = curl_easy_init();
    if (curl) {
        char lol[1000];
        sprintf(lol,OBFUSCATE("https://api.keysgen.site/ArraBxAbood/connect"));
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_easy_setopt(curl, CURLOPT_URL, lol);
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
        curl_easy_setopt(curl, CURLOPT_DEFAULT_PROTOCOL, "https");
        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Accept: application/json");
        headers = curl_slist_append(headers,"Content-Type: application/x-www-form-urlencoded");
        headers = curl_slist_append(headers, "Charset: UTF-8");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
        char data[4096];
        sprintf(data, "game=PUBG&user_key=%s&serial=%s", user_key, UUID.c_str());
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *) &chunk);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYSTATUS, 0);
        curl_easy_setopt(curl, CURLOPT_USERAGENT, "");

        res = curl_easy_perform(curl);

        if (res == CURLE_OK) {
            try {
                json result = json::parse(chunk.memory);
                auto STATUS = std::string{"status"};
                if (result[STATUS] == true) {
                    std::string token = result["data"]["token"].get<std::string>();
                    EXP = result["data"]["EXP"].get<std::string>();
                    time_t rng = result["data"]["rng"].get<time_t>();
                    if (rng + 30 > time(0)) {
                        std::string auth = "PUBG";
                        auth += "-";
                        auth += user_key;
                        auth += "-";
                        auth += UUID;
                        auth += "-";
                        auth += "Vm8Lk7Uj2JmsjCPVPVjrLa7zgfx3uz9E";
                        std::string outputAuth = CalcMD5(auth);
                        g_Token = token;
                        g_Auth = outputAuth;
                        bValid = g_Token == g_Auth;xEnv = true;
                        if (bValid && xEnv) {
                           // printf(OBFUSCATE("Login Success \n"));
                        }
                    }
                } else {
                    auto REASON = std::string{"reason"};
                    errMsg = result[REASON].get<std::string>();
                }
            } catch (json::exception &e) {
                errMsg = e.what();
            }
        } else {
            errMsg = curl_easy_strerror(res);
        }
    }
    curl_easy_cleanup(curl);
    return bValid ? env->NewStringUTF("OK") : env->NewStringUTF(errMsg.c_str());
}

bool signValid = false;

int REI_HttpsCanay_Closed() {
    JavaVM *vm;
    jclass clazz;
    JavaVM* java_vm = vm;
    JNIEnv* java_env = NULL;
    jint jni_return = java_vm->GetEnv((void**)&java_env, JNI_VERSION_1_6);
    if (jni_return == JNI_ERR)
        return -1;
    jni_return = java_vm->AttachCurrentThread(&java_env, NULL);
    if (jni_return != JNI_OK)
        return -2;
    jclass native_activity_clazz = java_env->GetObjectClass(clazz);
    if (native_activity_clazz == NULL)
        return -3;
    jmethodID method_id = java_env->GetMethodID(native_activity_clazz, OBFUSCATE("AndroidThunkJava_RestartGame"),/*Yaser New Restart*/OBFUSCATE("()V"));
    if (method_id == NULL)
        return -4;
    java_env->CallVoidMethod(clazz, method_id);
    jni_return = java_vm->DetachCurrentThread();
    if (jni_return != JNI_OK)
        return -5;
    return 0;
}


bool SecherREI(const std::string& folderPath) {
    return (access(folderPath.c_str(), F_OK) == 0);
}

void Detected_REIHttpCanary() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.guoshi.httpcanary");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}
void Detected_REIHttpCanary1() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.guoshi.httpcanary.PUBG");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}
void Detected_REIHttpCanary2() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.sniffer");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}

void Detected_REIHttpCanary3() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.httpcanary.pro");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    } else {

    }
}
#endif