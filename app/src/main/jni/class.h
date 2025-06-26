#ifndef KONTOL_ESP_IMPORTANT_CLASS_H
#define KONTOL_ESP_IMPORTANT_CLASS_H
#include <string>
#include <array>

class Color {
public:
    float r;
    float g;
    float b;
    float a;

    Color() {
        this->r = 0;
        this->g = 0;
        this->b = 0;
        this->a = 0;
    }

    Color(float r, float g, float b, float a) {
        this->r = r;
        this->g = g;
        this->b = b;
        this->a = a;
    }

    Color(float r, float g, float b) {
        this->r = r;
        this->g = g;
        this->b = b;
        this->a = 255;
    }

    static Color White(int alpha){
        return Color(255, 255, 255, alpha);
    }
    static Color Green(int alpha){
        return Color(0,255,0, alpha);
    }
    static Color Wood(int alpha){
        return Color(40,252,80, alpha);
    }
    static Color Black(int alpha){
        return Color(0,0,0, alpha);
    }
    static Color Red(int alpha)
    {
        return Color(255, 0, 0, alpha);
    }
    static Color Yellow(int alpha)
    {
        return Color(255, 255, 0, alpha);
    }
    static Color Orange(int alpha)
    {
        return Color(255, 165, 0, alpha);
    }
    static Color Blue(int alpha)
    {
        return Color(45, 255, 220, alpha);
    }
};

struct Vec4 {
    float x;
    float y;
    float z;
    float w;
    
    Vec4() {
        this->x = 0;
        this->y = 0;
        this->z = 0;
        this->w = 0;
    }
    
    Vec4(float x, float y, float z, float w) {
        this->x = x;
        this->y = y;
        this->z = z;
        this->w = w;
 }
};

class Vec3
{
public:
    float x;
    float y;
    float z;

    Vec3() {
        this->x = 0;
        this->y = 0;
        this->z = 0;
    }

    Vec3(float x, float y, float z) {
        this->x = x;
        this->y = y;
        this->z = z;
    }
};

class Vec2 {
        public:
        float x;
        float y;

        Vec2() {
            this->x = 0;
            this->y = 0;
        }

        Vec2(float x, float y) {
            this->x = x;
            this->y = y;
        }

        static Vec2 Zero() {
            return Vec2(0.0f, 0.0f);
        }

        bool operator!=(const Vec2 &src) const {
            return (src.x != x) || (src.y != y);
        }

        Vec2 &operator+=(const Vec2 &v) {
            x += v.x;
            y += v.y;
            return *this;
        }

        Vec2 &operator-=(const Vec2 &v) {
            x -= v.x;
            y -= v.y;
            return *this;
        }
};

class Rect {
public:
    float x;
    float y;
    float width;
    float height;

    Rect() {
        this->x = 0;
        this->y = 0;
        this->width = 0;
        this->height = 0;
    }

    Rect(float x, float y, float width, float height) {
        this->x = x;
        this->y = y;
        this->width = width;
        this->height = height;
    }

    bool operator==(const Rect &src) const {
        return (src.x == this->x && src.y == this->y && src.height == this->height &&
                src.width == this->width);
    }

    bool operator!=(const Rect &src) const {
        return (src.x != this->x && src.y != this->y && src.height != this->height &&
                src.width != this->width);
    }
};

#endif //KONTOL_ESP_IMPORTANT_CLASS_H
