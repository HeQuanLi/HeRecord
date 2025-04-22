#include <jni.h>
#include <string>
#include <cstdlib>

#include "modules/audio_processing/legacy_ns/noise_suppression_x.h"

#if defined(__cplusplus)
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_com_he_hens_NoiseSuppressor_nsxCreate(JNIEnv *env,
                                                                         jobject obj) {
    return (long) WebRtcNsx_Create();
}


JNIEXPORT jint JNICALL
Java_com_he_hens_NoiseSuppressor_nsxInit(JNIEnv *env,
                                                                       jobject obj,
                                                                       jlong nsHandler,
                                                                       jint frequency,
                                                                       jint mode
) {
    auto *handler = (NsxHandle *) nsHandler;
    if (handler == nullptr) {
        return -3;
    }

    int init = WebRtcNsx_Init(handler, frequency);
    if (init != 0) {
        return -3;
    }

    return WebRtcNsx_set_policy(handler, mode);
}

JNIEXPORT jint JNICALL
Java_com_he_hens_NoiseSuppressor_nsxProcess(JNIEnv *env,
                                                                          jobject obj,
                                                                          jlong nsHandler,
                                                                          jshortArray speechFrame,
                                                                          jint numBands,
                                                                          jshortArray outFrame) {

    auto *handle = (NsxHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    jshort *cspeechFrame = env->GetShortArrayElements(speechFrame, nullptr);
    jshort *coutframe = env->GetShortArrayElements(outFrame, nullptr);
    WebRtcNsx_Process(handle, &cspeechFrame, numBands, &coutframe);
    env->ReleaseShortArrayElements(speechFrame, cspeechFrame, 0);
    env->ReleaseShortArrayElements(outFrame, coutframe, 0);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_he_hens_NoiseSuppressor_nsxFree(JNIEnv *env,
                                                                       jobject obj,
                                                                       jlong nsHandler) {
    auto *handle = (NsxHandle *) nsHandler;
    if (handle == nullptr) {
        return -3;
    }
    WebRtcNsx_Free(handle);
    return 0;
}

#if defined(__cplusplus)
}
#endif