#ifndef ZEROPC_DEV_IMPORTANT_STRUCT_H
#define ZEROPC_DEV_IMPORTANT_STRUCT_H
#include "class.h"
#define maxplayerCount 100
#define maxvehicleCount 50
#define maxitemsCount 400
#define maxgrenadeCount 10
#define maxzonesCount 10
#define maxboxitemsCount 10

struct PlayerBone {
    bool isBone=false;
    Vec2 neck;
    Vec2 cheast;
    Vec2 pelvis;
    Vec2 lSh;
    Vec2 rSh;
    Vec2 lElb;
    Vec2 rElb;
    Vec2 lWr;
    Vec2 rWr;
    Vec2 lTh;
    Vec2 rTh;
    Vec2 lKn;
    Vec2 rKn;
    Vec2 lAn;
    Vec2 rAn;
    Vec2 head;
    //Vec2 ids[100];
};

struct PlayerWeapon {
    bool isWeapon=false;
    int id;
    int ammo;
};

enum Mode {
    InitMode = 1,
    ESPMode = 2,
    HackMode = 3,
    StopMode = 4,
};

struct Options {
    int aimbotmode;
    int openState;
    int aimT;
    int aimingState;
    bool ignoreBot;
    bool tracingStatus;
    int priority;
    bool pour;
    int aimingRange;
    int aimingDist;
    int aimingSpeed;
    int touchSpeed;
    int recCompe;
    int aimBullet;
    bool ignoreAi;
    float Smoothing;
    bool InputInversion;
    int touchSize;
    int touchX;
    int touchY;
    int recCompe1;
    int recCompe2;
    float recScope[9];
    bool customScope;
};

struct OtherFeature {
    bool LessRecoil;
    bool ZeroRecoil;
    bool InstantHit;
    bool FastShootInterval;
    bool HitX;
    bool SmallCrosshair;
    bool NoShake;
    bool Aimbot;
    bool FastSwitchWeapon;
    int WideView;
};

struct Request {
    int Mode;
    Options options;
    OtherFeature otherFeature;
    int ScreenWidth;
    int ScreenHeight;
    Vec2 radarPos;
    float radarSize;
};

struct VehicleData {
    char VehicleName[50];
    float Distance;
    float Fuel;
    float Health;
    Vec3 Location;
};

struct ItemData {
    char ItemName[50];
    float Distance;
    Vec3 Location;
};

struct GrenadeData {
    int type;
    float Distance;
    Vec3 Location;
};

struct PlayerData {
    char PlayerNameByte[100];
    char PlayerNation[100];
    char PlayerUID[100];
    int TeamID;
    float Health;
    float Healthy;
    float Distance;
    bool isBot;
    bool isVisible;
    Vec3 HeadLocation;
    Vec2 RadarLocation;
    PlayerWeapon Weapon;
    PlayerBone Bone;
    int StatusPlayer;
};

struct BoxItemData {
    int itemCount;
    int itemID[50];
    float Distance;
    Vec3 Location;
};


struct Response {
    bool Success;
    bool InLobby;
    int PlayerCount;
    int VehicleCount;
    int ItemsCount;
    int BoxItemsCount;
    int GrenadeCount;
    float fov;
    PlayerData Players[maxplayerCount];
    VehicleData Vehicles[maxvehicleCount];
    ItemData Items[maxitemsCount];
    GrenadeData Grenade[maxgrenadeCount];
    BoxItemData BoxItems[maxboxitemsCount];

};

#endif //ZEROPC_DEV_IMPORTANT_STRUCT_H
