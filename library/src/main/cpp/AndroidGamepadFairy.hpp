#ifndef AndroidGamepadFairy_hpp
#define AndroidGamepadFairy_hpp

#include <stdio.h>
#include "Cycloa/src/emulator/fairy/GamepadFairy.h"

class AndroidGamepadFairy : public GamepadFairy {
public:
    int code;

    AndroidGamepadFairy() {
        code = 0;
    }

    ~AndroidGamepadFairy() {
    }

    void onVBlank() {
    }

    void onUpdate() {
    }

    bool isPressed(uint8_t keyIdx);
};

#endif /* AndroidGamepadFairy_hpp */
