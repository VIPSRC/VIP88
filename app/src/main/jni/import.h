#ifndef NEROMORPH_ESP_IMPORTANT_IMPORT_H
#define NEROMORPH_ESP_IMPORTANT_IMPORT_H

#include <jni.h>
#include <string>
#include <cstdlib>
#include <unistd.h>
#include <sys/mman.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <cerrno>
#include <sys/un.h>
#include <cstring>
#include <string>
#include <cmath>
#include "struct.h"

//====== Booleand =====\\

bool is360Alert = true;
bool isSkeleton = true;
bool isPlayerHead = true;
bool isPlayerUID = true;
bool isNation = true;
bool isPlayerBox = true;
bool isPlayerLine = true;
bool isPlayerHealth = true;
bool isPlayerName = true;
bool isPlayerDistance = true;
bool isPlayerWeapon = true;
bool isPlayerWeaponIcon = true;
bool isGrenadeWarning = true;
bool isVehicles = true;
bool isItems = true;
bool isLootBox = true;
//bool isVisibility = true;

#endif //NEROMORPH_ESP_IMPORTANT_IMPORT_H
