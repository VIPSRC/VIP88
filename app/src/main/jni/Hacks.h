#ifndef BETA_ESP_IMPORTANT_HACKS_H
#define BETA_ESP_IMPORTANT_HACKS_H

#include "socket.h"
#include "Color.h"
#include "items.h"
#include "rLogin/Login.h"
#include "Vector3.hpp"



Color clrEnemy, clrEdge, clrBox, clrAlert, clr, clrTeam, clrDist,  clrHealth, clrText, grenadeColor;
float h, w, x, y, z, magic_number, mx, my, top, bottom, textsize, mScale, skelSize;
//Options options {1, -1, -1, -1,1, 1,200,300};
Options options {1,-1,-1,3,false,false,1,false,200,200,200, 19,19,-1,false};
OtherFeature otherFeature{false,false,false,false};

int botCount, playerCount;
Response response;
Request request;
char extra[30];
char text[100];
int hCounter = 50;

Color colorByDistance(int distance, int alpha) {
    Color clrDistance;
    if (distance < 600)
        clrDistance = Color::Yellow(255);

    if (distance < 300)
        clrDistance = Color::Orange(255);

    if (distance < 150)
        clrDistance = Color::Red(255);

    return clrDistance;
}

bool isOutsideSafeZone(Vec2 pos, Vec2 screen) {
    if (pos.y < 0) {
        return true;
    }
    if (pos.x > screen.x) {
        return true;
    }
    if (pos.y > screen.y) {
        return true;
    }
    return pos.x < 0;
}

std::string playerstatus(int GetEnemyState) {
    switch (GetEnemyState) {
        case 520:
        case 544:
        case 656:
        case 521:
        case 528:
        case 3145736:
            return "Aiming";
            break;
        default:
            return "";
            break;
    }
}

Vec2 calculatePosition(const Vec2 &center, float radius, float angleDegrees) {
    float angleRadians = angleDegrees * (M_PI / 180.0f); // Konversi derajat ke radian
    float x = center.x + radius * cos(angleRadians);
    float y = center.y + radius * sin(angleRadians);
    return Vec2(x, y);
}

bool colorPosCenter(float sWidth, float smMx, float sHeight, float posT, float eWidth, float emMx,
                    float eHeight, float posB)
{
    if (sWidth >= smMx && sHeight >= posT && eWidth <= emMx && eHeight <= posB)
    {
        return true;
    }
    return false;
}

Vec2 pushToScreenBorder(const Vec2 &location, const Vec2 &screen, float offset, float scale = 2.0f) {
    Vec2 center(screen.x / 2, screen.y / 2);
    float angle = atan2(location.y - center.y, location.x - center.x) * (180.0f / M_PI);
    return calculatePosition(center, offset * scale, angle);
}

// TODO Draw Radar
void DrawRadar(ESP canvas, Vec2 Location, Vec2 Pos, float Size, Color clr, int TeamID) {
    // LocalPos
    canvas.DrawFilledRoundRect(Color::White(255), {Pos.x - Size / 25, Pos.y - Size / 25}, {Pos.x + Size / 25, Pos.y + Size / 25});

    // EnemyPos
    canvas.DrawFillCircle(Color(clr.r, clr.g, clr.b, 255), Location, Size / 10, 0.5);

    if (isPlayerName) {
        // TeamID
        canvas.DrawText(Color::White(255), std::to_string(TeamID).c_str(), Location, Size / 10);
    }
}
                

void DrawESP(ESP esp, int screenWidth, int screenHeight) {

    if (!g_Token.empty()) {

        if (!g_Auth.empty()) {

            if (g_Token == g_Auth) {

esp.DrawTextName(Color::White(255), "FPS : ", Vec2(screenWidth / 10, screenHeight / 13.5), screenHeight / 30);

esp.DrawText(Color(252, 164, 64), "AHMED VIP", Vec2(screenWidth / 3, screenHeight / 79.55f + 50), 30);

request.radarPos = {screenWidth / 4, screenHeight / 4};


                request.ScreenHeight = screenHeight;
                request.ScreenWidth = screenWidth;
                request.options = options;
                request.otherFeature = otherFeature;
                request.Mode = InitMode;

                botCount = 0, playerCount = 0;
                send((void *) &request, sizeof(request));
                receive((void *) &response);
                float mScaleY = screenHeight / (float) 1080;
                mScale = screenHeight / (float) 1080;
                mScale = screenHeight / (float) 1080;
                skelSize = (mScale * 1.5f);
                float BoxSize = (mScaleY * 2.0f);
                textsize = screenHeight / 50;
                Vec2 screen(screenWidth, screenHeight);
                if (!signValid) {
                    int *p = nullptr;
                    *p = 0;
                }


                if (response.Success) {
                
                   esp.DrawFillCircle(Color::Black(35), request.radarPos, request.radarSize + (request.radarSize / 10), 0.5);
                    esp.DrawCircle(Color::White(255), request.radarPos, request.radarSize + (request.radarSize / 10), 1);
                    
                    for (int i = 0; i < response.PlayerCount; i++) {

                        PlayerData Player = response.Players[i];
                        x = Player.HeadLocation.x;
                        y = Player.HeadLocation.y;

                        sprintf(extra, "%0.0fM", Player.Distance);
                        float magic_number = (response.Players[i].Distance * response.fov);
                        float namewidht = (screenWidth / 6) / magic_number;
                        float pp2 = namewidht / 2;
                        float mx = (screenWidth / 4) / magic_number;
                        float my = (screenWidth / 1.38) / magic_number;
                        float top = y - my + (screenWidth / 1.7) / magic_number;
                        float bottom = response.Players[i].Bone.lAn.y + my -
                                       (screenWidth / 1.7) / magic_number;
                        clrDist = colorByDistance((int) Player.Distance, 255);
                        clrAlert = _clrID((int) Player.TeamID, 80);
                        clrTeam = _clrID((int) Player.TeamID, 150);
                        clr = _clrID((int) Player.TeamID, 200);
                        Vec2 location(x,y);
                        float textsize = screenHeight / 50;

                        if (Player.isBot)
                        {
                            botCount++;
                            clrEnemy = Color::White(255);
                            clrEdge = Color::White(80);
                            clrBox = Color::White(255);
                            clrText = Color::Black(255);
                        }
                        else
                        {
                            playerCount++;
                            clrEnemy = clrDist;
                            clrEdge = clrAlert;
                            clrBox = Color::Red(255);
                            clrText = Color::White(255);
                        }
                        
                        DrawRadar(esp, Player.RadarLocation, request.radarPos, request.radarSize, clr, Player.TeamID);


                        if (response.Players[i].HeadLocation.z != 1) {
                            // On Screen
                            if (x > -50 && x < screenWidth + 50) {

                                if (isSkeleton && Player.Bone.isBone) {
                                    float skelSize = (mScaleY * 2.0f);
                                    float headsize = (mScaleY * 7.0f);
                                   float distanceFromCamera = Player.Distance;
                                    float minHeadSize = (mScaleY * 2.0f);

                                  //  playerCount++;


  if (!response.Players[i].isVisible) {
                                              clr = Color::Red(255);
                                              clrEdge = Color::Red(255);
                           clrBox = Color::Red(255);
                                              esp.DrawText(Color().Red(255),"", Vec2(x, top - 130),
                                                 textsize);
                                         } else {
                                            clr = Color::Green(255);
                                            clrEdge = Color::Green(255);
                          clrBox = Color::Green(255);
                                            esp.DrawText(Color().Green(255),"", Vec2(x, top - 130),
                                                 textsize);
                                        }
                            
                             
                                    
                                    headsize = std::max(minHeadSize, headsize - std::min((distanceFromCamera - 10.0f) * 0.1f, 0.1f));
                                    esp.DrawFilledCircle(clrEdge, Player.Bone.head, static_cast<int>(headsize));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.neck.x,
                                                      response.Players[i].Bone.neck.y),
                                                 Vec2(response.Players[i].Bone.cheast.x,
                                                      response.Players[i].Bone.cheast.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.cheast.x,
                                                      response.Players[i].Bone.cheast.y),
                                                 Vec2(response.Players[i].Bone.pelvis.x,
                                                      response.Players[i].Bone.pelvis.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.neck.x,
                                                      response.Players[i].Bone.neck.y),
                                                 Vec2(response.Players[i].Bone.lSh.x,
                                                      response.Players[i].Bone.lSh.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.neck.x,
                                                      response.Players[i].Bone.neck.y),
                                                 Vec2(response.Players[i].Bone.rSh.x,
                                                      response.Players[i].Bone.rSh.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.lSh.x,
                                                      response.Players[i].Bone.lSh.y),
                                                 Vec2(response.Players[i].Bone.lElb.x,
                                                      response.Players[i].Bone.lElb.y));
                                    esp.DrawFilledCircle(clrEdge,
                                                         Vec2(response.Players[i].Bone.lWr.x,
                                                              response.Players[i].Bone.lWr.y),
                                                         screenHeight / 20 / magic_number);
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.rSh.x,
                                                      response.Players[i].Bone.rSh.y),
                                                 Vec2(response.Players[i].Bone.rElb.x,
                                                      response.Players[i].Bone.rElb.y));
                                    esp.DrawFilledCircle(clrEdge,
                                                         Vec2(response.Players[i].Bone.rWr.x,
                                                              response.Players[i].Bone.rWr.y),
                                                         screenHeight / 20 / magic_number);
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.lElb.x,
                                                      response.Players[i].Bone.lElb.y),
                                                 Vec2(response.Players[i].Bone.lWr.x,
                                                      response.Players[i].Bone.lWr.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.rElb.x,
                                                      response.Players[i].Bone.rElb.y),
                                                 Vec2(response.Players[i].Bone.rWr.x,
                                                      response.Players[i].Bone.rWr.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.pelvis.x,
                                                      response.Players[i].Bone.pelvis.y),
                                                 Vec2(response.Players[i].Bone.lTh.x,
                                                      response.Players[i].Bone.lTh.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.pelvis.x,
                                                      response.Players[i].Bone.pelvis.y),
                                                 Vec2(response.Players[i].Bone.rTh.x,
                                                      response.Players[i].Bone.rTh.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.lTh.x,
                                                      response.Players[i].Bone.lTh.y),
                                                 Vec2(response.Players[i].Bone.lKn.x,
                                                      response.Players[i].Bone.lKn.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.rTh.x,
                                                      response.Players[i].Bone.rTh.y),
                                                 Vec2(response.Players[i].Bone.rKn.x,
                                                      response.Players[i].Bone.rKn.y));
                                    esp.DrawFilledCircle(clrEdge,
                                                         Vec2(response.Players[i].Bone.lAn.x,
                                                              response.Players[i].Bone.lAn.y),
                                                         screenHeight / 20 / magic_number);
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.lKn.x,
                                                      response.Players[i].Bone.lKn.y),
                                                 Vec2(response.Players[i].Bone.lAn.x,
                                                      response.Players[i].Bone.lAn.y));
                                    esp.DrawLine(Color().White(255), skelSize,
                                                 Vec2(response.Players[i].Bone.rKn.x,
                                                      response.Players[i].Bone.rKn.y),
                                                 Vec2(response.Players[i].Bone.rAn.x,
                                                      response.Players[i].Bone.rAn.y));
                                    esp.DrawFilledCircle(clrEdge,
                                                         Vec2(response.Players[i].Bone.rAn.x,
                                                              response.Players[i].Bone.rAn.y),
                                                         screenHeight / 20 / magic_number);
                                }

                                // Player Box
                                if (isPlayerBox) {
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x + pp2, top),
                                                 Vec2(x + namewidht, top));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x - pp2, top),
                                                 Vec2(x - namewidht, top));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x + namewidht, top),
                                                 Vec2(x + namewidht, top + pp2));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x - namewidht, top),
                                                 Vec2(x - namewidht, top + pp2));
                                    // bottom
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x + pp2, bottom),
                                                 Vec2(x + namewidht, bottom));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x - pp2, bottom),
                                                 Vec2(x - namewidht, bottom));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x - namewidht, bottom),
                                                 Vec2(x - namewidht, bottom - pp2));
                                    esp.DrawLine(clrBox, BoxSize,
                                                 Vec2(x + namewidht, bottom),
                                                 Vec2(x + namewidht, bottom - pp2));

                                }
                     
                                if (isPlayerLine){
                                    esp.DrawLine(clrBox, screenHeight / 500,
                                                  Vec2(screenWidth / 2, screenHeight / 10.5),
                                                 Vec2(x, top - screenHeight / 32));
                                                 
                                }

                         if (isPlayerHealth) {
                                    float healthLength = screenWidth / 24;
                                    if (healthLength < mx)
                                        healthLength = mx;
                                    if (response.Players[i].Health < 25)
                                        clrHealth = Color(255, 0, 0,185);
                                    else if (response.Players[i].Health < 50)
                                        clrHealth = Color(255, 204, 0,185);
                                    else if (response.Players[i].Health < 75)
                                        clrHealth = Color(255, 255, 0, 185);
                                    else
                                        clrHealth = Color(34, 214, 97, 225);
                                    if (response.Players[i].Health == 0)
                                        esp.DrawText(Color(255, 0, 0), "Knock",
                                                     Vec2(x, top - screenHeight / 200), textsize),
                                                screenHeight / 27;
                                    else {
                                        esp.DrawFilledRect(clrTeam,
                                                           Vec2(x - healthLength, top - screenHeight / 30),
                                                           Vec2(x - healthLength +
                                                                (2 * healthLength) *
                                                                response.Players[i].Health /
                                                                100, top - screenHeight / 225));
                                        esp.DrawRect(Color(0, 0, 0), screenHeight / 640,
                                                     Vec2(x - healthLength, top - screenHeight / 30),
                                                     Vec2(x + healthLength, top - screenHeight / 255));
                                    }
                                }                                

                                                                if (isNation) {
                                     if (response.Players[i].isBot) {
                                     } else {
                                         esp.DrawNation(Color(255, 255, 255, 255),
                                                   response.Players[i].PlayerNation,
                                                   Vec2(x - -10, top - 0), 28);
                                     }
                                 }

                                 if (isPlayerUID) {
                                     esp.DrawUserID(Color().White(255), response.Players[i].PlayerUID,
                                                    Vec2(response.Players[i].HeadLocation.x - 30,
                                               top - screenHeight / 23),
                                       screenHeight / 60);
                                 }

                                //Player Head
                                if (isPlayerHead){
                                    esp.DrawFilledCircle(clrEdge,Vec2(response.Players[i].HeadLocation.x,response.Players[i].HeadLocation.y),screenHeight /12 /magic_number);
                                }

                                //Player Names
                                if (isPlayerName && response.Players[i].isBot) {
                                    sprintf(extra, "B O T");
                                    esp.DrawText(Color(255, 255, 255), extra,
                                                 Vec2(x, top - 18),
                                                 textsize);
                                } else if (isPlayerName) {
                                    esp.DrawName(Color().White(255),
                                                 response.Players[i].PlayerNameByte,
                                                 response.Players[i].TeamID,
                                                 Vec2(response.Players[i].HeadLocation.x,
                                                      top - 18),
                                                 textsize);
                                }

                                if (isPlayerDistance) {
                                    sprintf(extra, "%0.0f M", response.Players[i].Distance);
                                    esp.DrawText(Color(247, 175, 63,255), extra,
                                                 Vec2(x, bottom + screenHeight / 45),
                                                 textsize);
                                                
                                                 
                                }


                                // weapon text only
                                if (isPlayerWeapon && response.Players[i].Weapon.isWeapon) {
                                     /*esp.DrawWeapon(Color(247, 175, 63), response.Players[i].Weapon.id,
                                                    response.Players[i].Weapon.ammo,response.Players[i].Weapon.ammo2,
                                                    Vec2(x, top - 65), 20.0f);*/
                                    esp.DrawWeapon(Color(247, 244, 200), response.Players[i].Weapon.id,  response.Players[i].Weapon.ammo,response.Players[i].Weapon.ammo,
                                                   Vec2(x, bottom + screenHeight / 23), textsize);
                                }


                                if (isPlayerWeaponIcon && response.Players[i].Weapon.isWeapon) {
                                    esp.DrawWeaponIcon(response.Players[i].Weapon.id,
                                                       Vec2(x - 45, top - 60));
                                }
                                
                               //  if (Player.isVisible) {
                                if(playerstatus(Player.StatusPlayer) == "Aiming") {
                                    esp.DrawTexture(Color::Yellow(255), " ⚠\uFE0F Player Aiming at you ⚠\uFE0F", Vec2(screenWidth / 2, screenHeight / 4.3), screenHeight / 30);
                                }
                            // }



                            } //OnScreen
                            
                            if (response.Players[i].HeadLocation.z == 1.0f)
{
    if (!is360Alert)
        continue;

    if (x > screenWidth - screenWidth / 12)
        x = screenWidth - screenWidth / 120;
    else if (x < screenWidth / 120)
        x = screenWidth / 12;

    if (y < screenHeight / 1)
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(screenWidth - x - 100, screenHeight - 48),
                     Vec2(screenWidth - x + 100, screenHeight + 2));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(screenWidth - x - 100, screenHeight - 48),
                           Vec2(screenWidth - x + 100, screenHeight + 2));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(screenWidth - x, screenHeight - 20),
                     textsize);
    }
    else
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(screenWidth - x - 100, 48),
                     Vec2(screenWidth - x + 100, -2));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(screenWidth - x - 100, 48),
                           Vec2(screenWidth - x + 100, -2));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(screenWidth - x, 25), textsize);
    }
}
else if (x < -screenWidth / 10 || x > screenWidth + screenWidth / 10)
{
    if (!is360Alert)
        continue;

    if (y > screenHeight - screenHeight / 12)
        y = screenHeight - screenHeight / 120;
    else if (y < screenHeight / 120)
        y = screenHeight / 12;

    if (x > screenWidth / 2)
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(screenWidth - 88, y - 35),
                     Vec2(screenWidth + 2, y + 35));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(screenWidth - 88, y - 35),
                           Vec2(screenWidth + 2, y + 35));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(screenWidth - screenWidth / 80, y + 10),
                     textsize);
    }
    else
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(0 + 88, y - 35), Vec2(0 - 2, y + 35));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(0 + 88, y - 35), Vec2(0 - 2, y + 35));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(screenWidth / 80, y + 10), textsize);
    }
}
else if (y < -screenHeight / 10 || y > screenHeight + screenHeight / 10)
{
    if (!is360Alert)
        continue;

    if (x > screenWidth - screenWidth / 12)
        x = screenWidth - screenWidth / 120;
    else if (x < screenWidth / 120)
        x = screenWidth / 12;

    if (y > screenHeight / 2.5)
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(x - 100, screenHeight - 48), Vec2(x + 100,
                                                           screenHeight + 2));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(x - 100, screenHeight - 48),
                           Vec2(x + 100, screenHeight + 2));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(x, screenHeight - 20), textsize);
    }
    else
    {
        esp.DrawRect(Color(255, 255, 255), 2,
                     Vec2(x - 100, 48), Vec2(x + 100, -2));
        esp.DrawFilledRect(Color(255, 0, 0, 140),
                           Vec2(x - 100, 48), Vec2(x + 100, -2));
        sprintf(extra, "%0.0f m", response.Players[i].Distance);
        esp.DrawText(Color(255, 255, 255, 255), extra,
                     Vec2(x, 25), textsize);
    }

}
                                                                  


                        } //Player.HeadLocation.z
                    } //response.PlayerCount
                    
                    


                    for (int i = 0; i < response.GrenadeCount; i++) {
                        GrenadeData grenade = response.Grenade[i];
                        if (!isGrenadeWarning || grenade.Location.z == 1.0f) {
                            continue;
                        }
                        const char *grenadeTypeText;
                        switch (grenade.type) {
                            case 1:
                                grenadeColor = Color::Red(255);
                                grenadeTypeText = "Grenade";
                                break;
                            case 2:
                                grenadeColor = Color::Orange(255);
                                grenadeTypeText = "Molotov";
                                break;
                            case 3:
                                grenadeColor = Color::Yellow(255);
                                grenadeTypeText = "Stun";
                                break;
                            default:
                                grenadeColor = Color::White(255);
                                grenadeTypeText = "Smoke";
                        }
                        sprintf(extra, "%s (%0.0f m)", grenadeTypeText, grenade.Distance);
                        esp.DrawText(grenadeColor, extra, Vec2(grenade.Location.x, grenade.Location.y + (screenHeight / 50)), textsize);
                        int WARNING = 4;
                        esp.DrawOTH(Vec2(screenWidth / 2 - 160, 120), WARNING);
                        esp.DrawText(Color(255, 255, 255), OBFUSCATE("Warning : Throwable"),Vec2(screenWidth / 2 + 20, 145), 21);
                        esp.DrawText(grenadeColor, "〇", Vec2(grenade.Location.x, grenade.Location.y), textsize);
                    } //response.GrenadeCount


                    for (int i = 0; i < response.VehicleCount; i++) {
                        if (isVehicles) {
                            VehicleData vehicle = response.Vehicles[i];
                            if (vehicle.Location.z != 1.0f) {
                                esp.DrawVehicles(vehicle.VehicleName, vehicle.Distance, vehicle.Health, vehicle.Fuel, Vec2(vehicle.Location.x, vehicle.Location.y), screenHeight / 47);
                            }
                        }
                    } //response.VehicleCount

                    for (int i = 0; i < response.ItemsCount; i++) {
                        if (isItems) {
                            ItemData currentItem = response.Items[i];
                            if (currentItem.Location.z != 1.0f) {
                                esp.DrawItems(currentItem.ItemName, currentItem.Distance, Vec2(currentItem.Location.x, currentItem.Location.y), screenHeight / 50);
                            }
                        }
                    } //response.ItemsCount

                } //response.Success

               
						int ENEM_ICON = 2;
						int BOT_ICON = 3;

						if (playerCount == 0)
						{
							ENEM_ICON = 0;
						}
						if (botCount == 0)
						{
							BOT_ICON = 1;
						}
						char cn[10];
						sprintf(cn, "%d", playerCount);

						char bt[10];
						sprintf(bt, "%d", botCount);

						esp.DrawOTH(Vec2(screenWidth / 2 - (80), 60), ENEM_ICON);
						esp.DrawOTH(Vec2(screenWidth / 2, 60), BOT_ICON);
						esp.DrawText(Color(255, 255, 255, 255), cn,
									 Vec2(screenWidth / 2 - (20), 87), 23);
						esp.DrawText(Color(255, 255, 255, 255), bt,
									 Vec2(screenWidth / 2 + (50), 87), 23);
						
                        

                if (options.tracingStatus) {
                    float py = screenHeight / 2;
                    float px = screenWidth / 2;
                    esp.DrawFilledRect(Color::Green(50),
                                       Vec2(options.touchY - options.touchSize / 2,
                                            py * 2 - options.touchX + options.touchSize / 2),
                                       Vec2(options.touchY + options.touchSize / 2,
                                            py * 2 - options.touchX - options.touchSize / 2));
                }

                if (options.openState == 0 || options.aimBullet == 0 || options.aimT == 0) {
                    const Color textColor = (options.openState == 0) ? Color::Red(255) :(options.aimT == 0 ? Color::Blue(255) : Color::Blue(255));
                    esp.DrawCircle(textColor, Vec2(screenWidth / 2, screenHeight / 2), options.aimingRange, 1.5);
                }
                
                  if (isLootBox) {
                        for (int i = 0; i < response.BoxItemsCount; i++) {
                            if (response.BoxItems[i].Location.z != 1.0f) {
                                BoxItemData *boxData = &response.BoxItems[i];
                                char *itemname;
                                int BoxCount = 0;
                                for (int ij = 0; ij < boxData->itemCount; ij++) {
                                    if (GetBox((int) boxData->itemID[ij], &itemname)) {
                                        BoxCount++;
                                        esp.DrawDeadBoxItems(Color(), itemname, Vec2(boxData->Location.x, boxData->Location.y - (float) BoxCount * (screenHeight / 50)), textsize);
                                    }
                                }
                            }
                        }
                    }

            } //g_Token == g_Auth
        } //g_Auth
    } //g_Token
} //OnDraw

#endif // BETA_ESP_IMPORTANT_HACKS_H
