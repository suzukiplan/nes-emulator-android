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

    bool isPressed(uint8_t keyIdx) {
        switch (keyIdx) {
            case A:
                return 0 != code & MASK_A;
            case B:
                return 0 != code & MASK_B;
            case START:
                return 0 != code & MASK_START;
            case SELECT:
                return 0 != code & MASK_SELECT;
            case UP:
                return 0 != code & MASK_UP;
            case DOWN:
                return 0 != code & MASK_DOWN;
            case LEFT:
                return 0 != code & MASK_LEFT;
            case RIGHT:
                return 0 != code & MASK_RIGHT;
            default:
                return false;
        }
    }
};

#endif /* AndroidGamepadFairy_hpp */
