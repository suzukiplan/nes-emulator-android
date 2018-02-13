#include <jni.h>
#include <android/bitmap.h>
#include <string>
#include <cstdlib>
#include "Cycloa/src/emulator/VirtualMachine.h"
#include "AndroidVideoFairy.hpp"
#include "AndroidAudioFairy.hpp"
#include "AndroidGamepadFairy.hpp"

class Context {
public:
    AndroidVideoFairy video;
    AndroidAudioFairy audio;
    AndroidGamepadFairy gamepad1;
    AndroidGamepadFairy gamepad2;
    VirtualMachine *vm;
    uint8_t *rom;
    uint32_t romSize;

    Context() : audio(AndroidAudioFairy(44100, 16, 1)) {
        vm = new VirtualMachine(video, audio, &gamepad1, &gamepad2);
        rom = NULL;
        romSize = 0;
    }

    ~Context() {
        delete vm;
        if (rom) free(rom);
    }
};

extern "C" JNIEXPORT jlong JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_createContext(JNIEnv *env,
                                                             jobject /* this */) {
    Context *context = new Context();
    return (jlong) context;
}

extern "C" JNIEXPORT void JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_releaseContext(JNIEnv *env,
                                                              jobject /* this */,
                                                              jlong ctx) {
    Context *context = (Context *) ctx;
    delete context;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_loadRom(JNIEnv *env,
                                                       jobject /* this */,
                                                       jlong ctx,
                                                       jbyteArray rom_) {
    Context *context = (Context *) ctx;
    jboolean result = JNI_FALSE;
    if (context->rom) free(context->rom);
    context->rom = NULL;
    context->romSize = 0;
    jbyte *rom = env->GetByteArrayElements(rom_, NULL);
    size_t size = (uint32_t) env->GetArrayLength(rom_);
    if (rom) {
        context->rom = (uint8_t *) malloc(size);
        if (context->rom) {
            memcpy(context->rom, rom, size);
            context->romSize = (uint32_t) size;
            context->vm->loadCartridge(context->rom, context->romSize);
            context->vm->sendHardReset();
            result = JNI_TRUE;
        }
        env->ReleaseByteArrayElements(rom_, rom, 0);
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_tick(JNIEnv *env,
                                                    jobject /* this */,
                                                    jlong ctx,
                                                    jint key1,
                                                    jint key2,
                                                    jobject bitmap) {
    Context *context = (Context *) ctx;
    if (context->rom) {
        context->gamepad1.code = key1;
        context->gamepad2.code = key2;
        context->video.render = false;

        context->audio.lock();
        while (!context->video.render) context->vm->run();
        context->audio.unlock();

        void *pixels;
        if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;
        memcpy(pixels, context->video.bitmap565, sizeof(context->video.bitmap565));
        AndroidBitmap_unlockPixels(env, bitmap);
    } else {
        void *pixels;
        if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;
        memset(pixels, 0x00, sizeof(context->video.bitmap565));
        AndroidBitmap_unlockPixels(env, bitmap);
    }
}
