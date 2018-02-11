#include "AndroidGamepadFairy.hpp"

bool AndroidGamepadFairy::isPressed(uint8_t keyIdx) {
    switch (keyIdx) {
        case A:
            return 0 != (code & MASK_A);
        case B:
            return 0 != (code & MASK_B);
        case START:
            return 0 != (code & MASK_START);
        case SELECT:
            return 0 != (code & MASK_SELECT);
        case UP:
            return 0 != (code & MASK_UP);
        case DOWN:
            return 0 != (code & MASK_DOWN);
        case LEFT:
            return 0 != (code & MASK_LEFT);
        case RIGHT:
            return 0 != (code & MASK_RIGHT);
        default:
            return false;
    }
}
