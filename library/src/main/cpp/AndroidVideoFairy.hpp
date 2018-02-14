#ifndef AndroidVideoFairy_hpp
#define AndroidVideoFairy_hpp


#include "Cycloa/src/emulator/fairy/VideoFairy.h"
#include <stdio.h>

class AndroidVideoFairy : public VideoFairy {
public:
    unsigned short bitmap565[screenWidth * screenHeight];
    bool render;
    bool skip;

    AndroidVideoFairy();

    ~AndroidVideoFairy();

    void dispatchRendering(const uint8_t (&nesBuffer)[screenHeight][screenWidth],
                           const uint8_t paletteMask);
};

#endif /* AndroidVideoFairy_hpp */
