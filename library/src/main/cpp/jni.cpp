#include <jni.h>
#include <string>
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

    Context() {
        vm = new VirtualMachine(video, audio, &gamepad1, &gamepad2);
    }

    ~Context() {
        delete vm;
    }
};

extern "C" JNIEXPORT jlong JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_createContext(JNIEnv *env, jobject /* this */) {
    Context *context = new Context();
    return (jlong) context;
}

extern "C" JNIEXPORT void JNICALL
Java_com_suzukiplan_emulator_nes_core_Emulator_releaseContext(JNIEnv *env, jobject /* this */,
                                                              jlong ctx) {
    Context *context = (Context *) ctx;
    delete context;
}
