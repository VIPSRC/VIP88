#ifndef BETA_ESP_IMPORTANT_ESP_H
#define BETA_ESP_IMPORTANT_ESP_H
#include "struct.h"

class ESP {
private:
    JNIEnv *_env;
    jobject _cvsView;
    jobject _cvs;
    jclass canvasView;
    jmethodID drawline;
    jmethodID drawrect;
    jmethodID drawfilledrect;
    jmethodID drawfilledrect2;
    jmethodID drawcurverect;


public:
    ESP() {
        _env = nullptr;
        _cvsView = nullptr;
        _cvs = nullptr;
        canvasView = nullptr;
        drawline = nullptr;
        drawrect = nullptr;
        drawfilledrect= nullptr;
        drawfilledrect2= nullptr;
        drawcurverect = nullptr;
    }

    ESP(JNIEnv *env, jobject cvsView, jobject cvs) {
        this->_env = env;
        this->_cvsView = cvsView;
        this->_cvs = cvs;
        canvasView = _env->GetObjectClass(_cvsView);
        drawline = _env->GetMethodID(canvasView, "DrawLine",
                                     "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        drawrect = _env->GetMethodID(canvasView, "DrawRect",
                                     "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        drawcurverect = _env->GetMethodID(canvasView, "DrawCurveRect",
                                          "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        drawfilledrect= _env->GetMethodID(canvasView, "DrawFilledRect",
                                          "(Landroid/graphics/Canvas;IIIIFFFF)V");
        drawfilledrect2= _env->GetMethodID(canvasView, "DrawFilledRect2",
                                           "(Landroid/graphics/Canvas;IIIIFFFF)V");
    }

    bool isValid() const {
        return (_env != nullptr && _cvsView != nullptr && _cvs != nullptr);
    }

    int getWidth() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getWidth", "()I");
            _env->DeleteLocalRef(canvas);
            return _env->CallIntMethod(_cvs, width);

        }
        return 0;
    }

    int getHeight() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getHeight", "()I");
            _env->DeleteLocalRef(canvas);
            return _env->CallIntMethod(_cvs, width);
        }
        return 0;
    }

    void DrawLine(Color color, float thickness, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawline, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawRect(Color color, float thickness, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawCurveRect(Color color, float thickness, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawcurverect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }



    void DrawFilledRect2(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 start.x, start.y, end.x, end.y);
        }
    }


    void DrawFilledRect(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawNation(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawNation",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawUserID(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawUserID",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawTimID(Color color, const char *txt,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTimID",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s,teamid, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }


    void DrawFilledRoundRect(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledroundrect= _env->GetMethodID(canvasView, "DrawFilledRoundRect",
                                                             "(Landroid/graphics/Canvas;IIIIFFFF)V");
            _env->CallVoidMethod(_cvsView, drawfilledroundrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawCircle(Color color, Vec2 pos, float radius,float thickness) {
        if (isValid()) {
            jmethodID drawcircle = _env->GetMethodID(canvasView, "DrawCircle",
                                                     "(Landroid/graphics/Canvas;IIIIFFFF)V");

            _env->CallVoidMethod(_cvsView, drawcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius,thickness);
        }
    }



    void DrawFilledTriangle(Color color, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawFilledTriangle = _env->GetMethodID(canvasView, "DrawFilledTriangle",
                                                             "(Landroid/graphics/Canvas;IIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawFilledTriangle, _cvs, color.a, color.r,
                                 color.g, color.b, pos.x, pos.y, size);
        }
    }

    void DrawFilledCircle(Color color, Vec2 pos, float radius) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledcircle = _env->GetMethodID(canvasView, "DrawFilledCircle",
                                                           "(Landroid/graphics/Canvas;IIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawfilledcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius);
        }
    }

    void DrawFillCircle(Color color, Vec2 pos, float radius,float thickness) {
        if (isValid()) {
            jmethodID drawfilledcircle = _env->GetMethodID(canvasView, "DrawFillCircle",
                                                           "(Landroid/graphics/Canvas;IIIIFFFF)V");

            _env->CallVoidMethod(_cvsView, drawfilledcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius,thickness);
        }
    }

    void DebugText(char * s){
        jmethodID mid = _env->GetMethodID(canvasView, "DebugText", "(Ljava/lang/String;)V");
        jstring name = _env->NewStringUTF(s);
        _env->CallVoidMethod(_cvsView, mid, name);
        _env->DeleteLocalRef(name);
    }


    void DrawName(Color color, const char *txt,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawName",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s,teamid, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }

    }
    void DrawText(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawText",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawTriangle(Color color, Vec2 point1, Vec2 point2, Vec2 point3, float thickness) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawTriangle = _env->GetMethodID(canvasView, "DrawTriangle",
                                                       "(Landroid/graphics/Canvas;IIIIFFFFFFF)V");
            _env->CallVoidMethod(_cvsView, drawTriangle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 point1.x, point1.y,
                                 point2.x, point2.y,
                                 point3.x, point3.y,
                                 thickness);
        }
    }

    void DrawTriangleFilled(Color color, Vec2 point1, Vec2 point2, Vec2 point3) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawTriangleFilled = _env->GetMethodID(canvasView, "DrawTriangleFilled",
                                                             "(Landroid/graphics/Canvas;IIIIFFFFFF)V");
            _env->CallVoidMethod(_cvsView, drawTriangleFilled, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 point1.x, point1.y,
                                 point2.x, point2.y,
                                 point3.x, point3.y);
        }
    }


    void DrawTexture(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTexture",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }
    float measureTextWidth(const char* text, float textSize) {
        jclass canvasView = _env->GetObjectClass(_cvsView);
        jmethodID measureMethod = _env->GetMethodID(canvasView, "measureTextWidth", "(Ljava/lang/String;F)F");

        jstring jText = _env->NewStringUTF(text);
        float width = _env->CallFloatMethod(_cvsView, measureMethod, jText, textSize);
        _env->DeleteLocalRef(jText);

        return width;
    }
    void DrawTextName(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTextName",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawTextMode(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTextMode",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawTextMode2(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTextMode2",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawFillRect(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledrect = _env->GetMethodID(canvasView, "DrawFillRect",
                                                         "(Landroid/graphics/Canvas;IIIIIIII)V");
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, (int) start.x, (int) start.y,
                                 (int) end.x, (int) end.y);
        }
    }

    void DrawFilledCurve(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledrect = _env->GetMethodID(canvasView, "DrawFilledCurve",
                                                         "(Landroid/graphics/Canvas;IIIIIIII)V");
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, (int) start.x, (int) start.y, (int) end.x, (int) end.y);
        }
    }

    void DrawTeamID(Color color,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTeamID",
                                                   "(Landroid/graphics/Canvas;IIIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 teamid, pos.x, pos.y, size);
        }
    }

    void DrawTriangle(Color color, float x, float y, float x2, float y2, float x3, float y3) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawwarning = _env->GetMethodID(canvasView, "DrawTriangle","(Landroid/graphics/Canvas;IIIIFFFFFF)V");
            _env->CallVoidMethod(_cvsView, drawwarning, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, x, y, x2, y2, x3, y3);
        }
    }

    void DrawPlayerName(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawPlayerName",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }



    void DrawCustom(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawCustom",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }



    void DrawItems(const char *txt, float distance, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawItems",
                                                   "(Landroid/graphics/Canvas;Ljava/lang/String;FFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs,
                                 s,distance, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawVehicles(const char *txt, float distance, float health, float fuel, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawVehicles",
                                                   "(Landroid/graphics/Canvas;Ljava/lang/String;FFFFFF)V");
            jstring s = _env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs,
                                 s, distance, health, fuel, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawWeapon(Color color, int wid,int ammo, int ammo2, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawWeapon",
                                                   "(Landroid/graphics/Canvas;IIIIIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 wid,ammo,ammo2, pos.x, pos.y, size);
        }
    }


    void DrawOTH(Vec2 pos) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawoth = _env->GetMethodID(canvasView, "DrawOTH",
                                                  "(Landroid/graphics/Canvas;FF)V");
            _env->CallVoidMethod(_cvsView, drawoth, _cvs, pos.x, pos.y);
        }
    }

    void DrawOTH2(Vec2 start, int num) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawline = _env->GetMethodID(canvasView, "DrawOTH2",
                                                   "(Landroid/graphics/Canvas;IFF)V");
            _env->CallVoidMethod(_cvsView, drawline, _cvs, num, start.x, start.y);
        }
    }

    void DrawWeaponIcon(int wid,Vec2 pos) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawoth = _env->GetMethodID(canvasView, "DrawWeaponIcon",
                                                  "(Landroid/graphics/Canvas;IFF)V");
            _env->CallVoidMethod(_cvsView, drawoth, _cvs, wid, pos.x, pos.y);
        }
    }

    void DrawPlayerID(Color color, const char *txt,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawPlayerID",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s,teamid, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawTextBot(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawTextBot",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

    void DrawName1(Color color, const char *txt,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawName1",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s,teamid, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }


    void DrawDeadBoxItems(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawDeadBoxItems",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }


    void DrawName2(Color color, const char *txt,Vec2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawName2",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }

};




#endif //BETA_ESP_IMPORTANT_ESP_H

