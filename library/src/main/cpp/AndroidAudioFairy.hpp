#ifndef AndroidAudioFairy_hpp
#define AndroidAudioFairy_hpp

#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include "Cycloa/src/emulator/fairy/AudioFairy.h"

struct SL {
    SLObjectItf slEngObj;
    SLEngineItf slEng;
    SLObjectItf slMixObj;
    SLObjectItf slPlayObj;
    SLPlayItf slPlay;
    SLAndroidSimpleBufferQueueItf slBufQ;
};

class AndroidAudioFairy : public AudioFairy {
public:
    AndroidAudioFairy(int sampling, int bit, int channel);

    ~AndroidAudioFairy();

    int16_t buffer[4096];
    char emptyBuffer[1024];
    int skip;

    void lock();

    void unlock();

    static void callback(SLAndroidSimpleBufferQueueItf bq, void *c);

    bool isEnded() {
        return NULL == sl.slBufQ;
    }

private:
    pthread_mutex_t mutex;
    struct SL sl;
    int sampling;
    int bit;
    SLuint32 channel;

    int init_sl();

    int init_sl2();
};

#endif /* AndroidAudioFairy_hpp */
