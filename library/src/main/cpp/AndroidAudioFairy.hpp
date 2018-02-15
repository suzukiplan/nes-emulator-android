#ifndef AndroidAudioFairy_hpp
#define AndroidAudioFairy_hpp

#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include "Cycloa/src/emulator/fairy/AudioFairy.h"
#include "buffer-queue/src/BufferQueue.h"

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
    int16_t skipBuffer[2048];
    bool buffered;
    int skip;
    BufferQueue* captureQueue;

    void lock();

    void unlock();

    int startPlaying();

    static void callback(SLAndroidSimpleBufferQueueItf bq, void *c);

    bool isEnded() {
        return NULL == sl.slBufQ;
    }

    SLAndroidSimpleBufferQueueItf getBufferQueueItf() {
        return sl.slBufQ;
    }

    bool beginCapture() {
        if (captureQueue) return false;
        lock();
        try {
            captureQueue = new BufferQueue(16384);
        } catch(...) {
            captureQueue = NULL;
        }
        if (NULL == captureQueue) {
            unlock();
            return false;
        }
        unlock();
        return true;
    }

    void* getCapture(size_t* size, size_t limit) {
        if (!captureQueue) {
            *size = 0;
            return NULL;
        }
        void* result;
        captureQueue->dequeue(&result, size, limit);
        return result;
    }

    void endCapture() {
        if (captureQueue) {
            lock();
            delete captureQueue;
            captureQueue = NULL;
            unlock();
        }
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
