#ifndef AndroidAudioFairy_hpp
#define AndroidAudioFairy_hpp

#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include "Cycloa/src/emulator/fairy/AudioFairy.h"

class AndroidAudioFairy : public AudioFairy
{
public: // 以下はsound_threadから参照するのでスコープを public にしている (外部からは触ってはならない)

private:

public:
    AndroidAudioFairy();
    ~AndroidAudioFairy();
};

#endif /* AndroidAudioFairy_hpp */
