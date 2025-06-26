#ifndef BETA_ESP_IMPORTANT_ITEMS_H
#define BETA_ESP_IMPORTANT_ITEMS_H

bool GetBox(int code, char **name) {
    switch (code) {
        case 101001:
            *name = "AKM";
            return true;
        case 101002:
            *name = "M16A4";
            return true;
        case 101003:
            *name = "SCAR-L";
            return true;
        case 101004:
            *name = "M416";
            return true;
        case 101005:
            *name = "Groza";
            return true;
        case 101006:
            *name = "AUG A3";
            return true;
        case 101007:
            *name = "QBZ";
            return true;
        case 101008:
            *name = "M762";
            return true;
        case 101009:
            *name = "Mk47 Mutant";
            return true;
        case 101010:
            *name = "G36C";
            return true;
        case 101100:
            *name = "FAMAS";
            return true;
        case 105001:
            *name = "M249";
            return true;
        case 105002:
            *name = "DP-28";
            return true;
        case 102001:
            *name = "UZI";
            return true;
        case 102002:
            *name = "UMP45";
            return true;
        case 102003:
            *name = "Vector";
            return true;
        case 102004:
            *name = "Tompson SMG";
            return true;
        case 102005:
            *name = "PP-19 Bizon";
            return true;
        case 102007:
            *name = "Skorpion";
            return true;
        case 103001:
            *name = "Kar98K";
            return true;
        case 103002:
            *name = "M24";
            return true;
        case 103003:
            *name = "AWM";
            return true;
        case 103004:
            *name = "SKS";
            return true;
        case 103005:
            *name = "VSS";
            return true;
        case 103006:
            *name = "Mini14";
            return true;
        case 103007:
            *name = "Mk14";
            return true;
        case 103008:
            *name = "Win94";
            return true;
        case 103009:
            *name = "SLR";
            return true;
        case 103010:
            *name = "QBU";
            return true;
        case 103011:
            *name = "Mosin Nagant";
            return true;
        case 103100:
            *name = "Mk12";
            return true;
        case 104001:
            *name = "S686";
            return true;
        case 104002:
            *name = "S1897";
            return true;
        case 104003:
            *name = "S12K";
            return true;
        case 104004:
            *name = "DP12";
            return true;
        case 104101:
            *name = "M1014";
            return true;
        case 106006:
            *name = "Sawed-off";
            return true;
        case 106001:
            *name = "P92";
            return true;
        case 106002:
            *name = "P1911";
            return true;
        case 106003:
            *name = "R1895";
            return true;
        case 106004:
            *name = "P18C";
            return true;
        case 106005:
            *name = "R45";
            return true;
        case 106008:
            *name = "Vz61";
            return true;
        case 106010:
            *name = "Desert Eagle";
            return true;
        case 107001:
            *name = "Crossbow";
            return true;
        case 108001:
            *name = "Machete";
            return true;
        case 108002:
            *name = "Crowbar";
            return true;
        case 108003:
            *name = "Sickle";
            return true;
        case 108004:
            *name = "Pan";
            return true;
        case 201001:
            *name = "Choke";
            return true;
        case 201002:
            *name = "Compensator (SMG)";
            return true;
        case 201003:
            *name = "Compensator (Snipers)";
            return true;
        case 201004:
            *name = "Flash Hider (SMG)";
            return true;
        case 201005:
            *name = "Flash Hider (Snipers)";
            return true;
        case 201006:
            *name = "Suppressor (SMG)";
            return true;
        case 201007:
            *name = "Suppressor (Snipers)";
            return true;
        case 201009:
            *name = "Compensator (AR)";
            return true;
        case 201010:
            *name = "Flash Hider (AR)";
            return true;
        case 201011:
            *name = "Suppressor (AR)";
            return true;
        case 201012:
            *name = "Duckbill";
            return true;
        case 202001:
            *name = "Angled Foregrip";
            return true;
        case 202002:
            *name = "Vertical Foregrip";
            return true;
        case 202004:
            *name = "Light Grip";
            return true;
        case 202005:
            *name = "Half Grip";
            return true;
        case 202006:
            *name = "Thumb Grip";
            return true;
        case 202007:
            *name = "Laser Sight";
            return true;
        case 203001:
            *name = "Red Dot Sight";
            return true;
        case 203002:
            *name = "Holographic Sight";
            return true;
        case 203003:
            *name = "2x Scope";
            return true;
        case 203004:
            *name = "4x Scope";
            return true;
        case 203005:
            *name = "8x Scope";
            return true;
        case 203014:
            *name = "3x Scope";
            return true;
        case 203015:
            *name = "6x Scope";
            return true;
        case 203018:
            *name = "Canted Sight";
            return true;
        case 204014:
            *name = "Bullet Loop";
            return true;
        case 205001:
            *name = "Stock (Micro UZI)";
            return true;
        case 205002:
            *name = "Tactical Stock";
            return true;
        case 205003:
            *name = "Cheek Pad (Snipers)";
            return true;
        case 205004:
            *name = "Quiver (Crossbow)";
            return true;
        case 204004:
            *name = "Extend Mag (SMG, Pistols)";
            return true;
        case 204005:
            *name = "QuickD Mag (SMG, Pistols)";
            return true;
        case 204006:
            *name = "ExtendQ Mag (SMG, Pistols)";
            return true;
        case 204007:
            *name = "Extend Mag (Snipers)";
            return true;
        case 204008:
            *name = "QuickD Mag (Snipers)";
            return true;
        case 204009:
            *name = "ExtendedQ Mag (Snipers)";
            return true;
        case 204011:
            *name = "ExtendQ Mag (AR)";
            return true;
        case 204012:
            *name = "QuickD Mag (AR)";
            return true;
        case 204013:
            *name = "ExtendQ Mag (AR)";
            return true;
        case 301001:
            *name = "9mm";
            return true;
        case 302001:
            *name = "7.62mm";
            return true;
        case 303001:
            *name = "5.56mm";
            return true;
        case 304001:
            *name = "12 Gauge";
            return true;
        case 305001:
            *name = "45 ACP";
            return true;
        case 306001:
            *name = "300 Magnum";
            return true;
        case 307001:
            *name = "Bolt";
            return true;
        case 501001:
            *name = "Backpack (Lv. 1)";
            return true;
        case 501002:
            *name = "Backpack (Lv. 2)";
            return true;
        case 501006:
            *name = "Backpack (Lv. 3)";
            return true;
        case 502001:
            *name = "Helmet (Lv. 1)";
            return true;
        case 502002:
            *name = "Helmet (Lv. 2)";
            return true;
        case 502003:
            *name = "Helmet (Lv. 3)";
            return true;
        case 503001:
            *name = "Vest (Lv. 1)";
            return true;
        case 503002:
            *name = "Vest (Lv. 2)";
            return true;
        case 503003:
            *name = "Vest (Lv. 3)";
            return true;
        case 601001:
            *name = "Energy Drink";
            return true;
        case 601002:
            *name = "Adrenaline";
            return true;
        case 601003:
            *name = "Painkiller";
            return true;
        case 601004:
            *name = "Bandage";
            return true;
        case 601005:
            *name = "First Aid Kit";
            return true;
        case 601006:
            *name = "Med Kit";
            return true;
        default:
        return false;
    }
}


#endif //BETA_ESP_IMPORTANT_ITEMS_H


