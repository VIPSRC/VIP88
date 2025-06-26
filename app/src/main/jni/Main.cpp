#include <jni.h>
#include <string>
#include "ESP.h"
#include "Hacks.h"
#include "Oxorany/oxorany.h"

ESP espOverlay;
int type = 1, utype = 2;

/* ================ SERVER FUNCTION =========================*/

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_material_LoginActivity_GetKey(JNIEnv *env, jclass thiz) {
    return env->NewStringUTF(OBFUSCATE("https://t.me/AhmedUn5"));
}

/* ================ ESP FUNCTION =========================*/

extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_Overlay_DrawOn(JNIEnv *env, jclass , jobject espView, jobject canvas) {
    espOverlay = ESP(env, espView, canvas);
    if (espOverlay.isValid()){
        DrawESP(espOverlay, espOverlay.getWidth(), espOverlay.getHeight());
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_EXP(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(EXP.c_str());
}


int Register1(JNIEnv *env) {
    JNINativeMethod methods[] = {
            {"native_Check", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) native_Check}
    };
    jclass clazz = env->FindClass("pubgm/loader/activity/LoginActivity");
    if (!clazz)
        return -1;

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return -1;

    return 0;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (Register1(env) != 0)
        return -1;
    Detected_REIHttpCanary();
    Detected_REIHttpCanary1();
    Detected_REIHttpCanary2();
    Detected_REIHttpCanary3();
    Detected_REIHttpCanary();
    return JNI_VERSION_1_6;
}


extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_Overlay_Close(JNIEnv *, jobject) {
    Close();
    options.openState = -1;
    options.aimBullet = -1;
    options.aimT = -1;
}


extern "C" JNIEXPORT jboolean JNICALL
Java_pubgm_loader_floating_Overlay_getReady(JNIEnv *, jobject thiz) {
    int sockCheck = 1;

    if (!Create()) {
        perror("Creation failed");
        return false;
    }
    setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &sockCheck, sizeof(int));
    if (!Bind()) {
        perror("Bind failed");
        return false;
    }

    if (!Listen()) {
        perror("Listen failed");
        return false;
    }
    if (Accept()) {
        return true;
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_ToggleAim_ToggleAim(JNIEnv *, jobject thiz, jboolean value) {
    if (value)
        options.openState = 0;
    else
        options.openState = -1;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_ToggleBullet_ToggleBullet(JNIEnv *, jobject thiz, jboolean value) {
    if (value)
        options.aimBullet = 0;
    else
        options.aimBullet = -1;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_ToggleSimulation_ToggleSimulation(JNIEnv *, jobject thiz, jboolean value) {
    if (value)
        options.aimT = 0;
    else
        options.aimT = -1;
}

extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_SettingValue(JNIEnv *, jobject, jint code, jboolean jboolean1) {

    switch ((int) code) {
        case 2:
            isPlayerLine = jboolean1;
            break;
        case 3:
            isPlayerBox = jboolean1;
            break;
        case 4:
            isSkeleton = jboolean1;
            break;
        case 5:
            isPlayerDistance = jboolean1;
            break;
        case 6:
            isPlayerHealth = jboolean1;
            break;
        case 7:
            isPlayerName = jboolean1;
            break;
        case 8:
            isPlayerHead = jboolean1;
            break;
        case 9:
            is360Alert = jboolean1;
            break;
        case 10:
            isPlayerWeapon = jboolean1;
            break;
        case 11:
            isGrenadeWarning = jboolean1;
            break;
        case 12:
            isVehicles = jboolean1;
            break;
        case 13:
            isItems = jboolean1;
            break;
        case 14:
            isLootBox = jboolean1;
            break;
        case 15:
            options.ignoreAi = jboolean1;
            break;
        case 16:
            isPlayerWeaponIcon = jboolean1;
            break;
        case 222:
            isNation = jboolean1;
            break;
        case 111:
            isPlayerUID = jboolean1;
            break;
        
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_SettingAim(JNIEnv *env, jobject thiz, jint setting_code, jboolean value) {
    switch ((int) setting_code) {
        case 1:
            options.openState = -1;
            break;
        case 2:
            options.aimBullet = -1;
            break;
        case 3:
            options.pour = value;
            break;
        case 4:
            options.ignoreBot = value;
            break;
        case 5:
            options.InputInversion = value;
            break;
        case 6:
            options.tracingStatus = value;
            break;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_SettingMemory(JNIEnv *env, jobject thiz, jint setting_code, jboolean value) {
    switch ((int) setting_code) {
        case 1:
            otherFeature.HitX = value;
            break;
        case 2:
            otherFeature.SmallCrosshair = value;
            break;
        case 3:
            otherFeature.Aimbot = value;
            break;
        case 4:
            otherFeature.WideView = value;
            break;
        case 5:
            otherFeature.HitX = value;
            break;

    }
}

extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_Range(JNIEnv *, jobject, jint range) {
    options.aimingRange = 1 + range;
}

extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_distances(JNIEnv *, jobject, jint distances) {
    options.aimingDist = distances;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_recoil(JNIEnv *env, jobject thiz, jint recoil) {
    options.recCompe = recoil;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_recoil2(JNIEnv *env, jobject thiz, jint recoil) {
    options.recCompe1 = recoil;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_recoil3(JNIEnv *env, jobject thiz, jint recoil) {
    options.recCompe2 = recoil;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_Bulletspeed(JNIEnv *env, jobject thiz, jint bulletspeed) {
    options.aimingSpeed = bulletspeed;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_AimingSpeed(JNIEnv *env, jobject thiz, jint aimingspeed) {
    options.touchSpeed = aimingspeed;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_Smoothness(JNIEnv *env, jobject thiz, jint smoothness) {
    options.Smoothing = smoothness;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_TouchSize(JNIEnv *env, jobject thiz, jint touchsize) {
    options.touchSize = touchsize;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_TouchPosX(JNIEnv *env, jobject thiz, jint touchposx) {
    options.touchX = touchposx;
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_TouchPosY(JNIEnv *env, jobject thiz, jint touchposy) {
    options.touchY = touchposy;
}


extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_WideView(JNIEnv *env, jobject thiz, jint wideview) {
    otherFeature.WideView = wideview;
}

extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_Target(JNIEnv *, jobject, jint target) {
    options.aimbotmode = target;
}
extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_AimWhen(JNIEnv *, jobject, jint state) {
    options.aimingState = state;
}
extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_AimBy(JNIEnv *, jobject, jint aimby) {
    options.priority = aimby;
}
extern "C" JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_RadarSize(JNIEnv *, jobject, jint size) {
    request.radarSize = size;
}

// ====================== Main Activity ====================== //

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_material_MainActivity_Telegram(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(OBFUSCATE(""));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_getOwner(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(OBFUSCATE("https://t.me/AhmedUn5"));   // TELEGRAM LINK
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_getTelegram(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(OBFUSCATE("https://t.me/AhmedUn5"));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_getGrup(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(OBFUSCATE("https://t.me/AhmedUn5"));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_FixCrash(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(
            OBFUSCATE("https://www.mhack.store/turbovip/Bypass/loader.zip")); //  LIB LOAD LINK
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_activity(JNIEnv *env, jclass clazz) {
    if (!xEnv){
        return env->NewStringUTF(
                OBFUSCATE("pubgm.loader.activity.LoginActivity"));
    }else{
        return env->NewStringUTF(
                OBFUSCATE("pubgm.loader.activity.MainActivity"));
    }
}
extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_mainURL(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(
            OBFUSCATE("https://www.mhack.store/vip/Bypass/assets.zip"));  //BYPASS LINK

}
extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_Component_DownloadZip_pw(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(
            OBFUSCATE("1212"));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_URLJSON(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(
            OBFUSCATE("https://www.mhack.store/turbovip/Bypass/update.json"));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_server_ApiServer_ApiKeyBox(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(
            OBFUSCATE("2IMQ7RHH2GR51B5UPT4M"));
}
extern "C" JNIEXPORT jstring JNICALL
Java_pubgm_loader_activity_MainActivity_ONOFFBGMI(JNIEnv *env, jobject activityObject) {
    return env->NewStringUTF("JEOE9JQM18EHBFIE");
}

/*SAAD*/
extern "C"
JNIEXPORT jstring JNICALL
Java_pubgm_loader_activity_LoginActivity_URLJSON(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(
            OBFUSCATE("https://www.mhack.store/turbovip/Bypass/update.json"));
}

extern "C"
JNIEXPORT void JNICALL
Java_pubgm_loader_floating_FloatService_SkinHack(JNIEnv *env, jobject thiz, jint setting_code) {
    switch ((int) setting_code) {
        case 1:
            otherFeature.clothes = 1;
            break;
        case 2:
            otherFeature.clothes = 2;
            break;
        case 3:
            otherFeature.clothes = 3;
            break;
        case 4:
            otherFeature.clothes = 4;
            break;
        case 5:
            otherFeature.clothes = 5;
            break;
        case 6:
            otherFeature.clothes = 6;
            break;
        case 7:
            otherFeature.clothes = 7;
            break;
    }
}