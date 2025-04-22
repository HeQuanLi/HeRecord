#include <jni.h>
#include <string>
#include <cstdlib>

#include "modules/audio_processing/agc/legacy/gain_control.h"

#if defined(__cplusplus)
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_com_he_hens_AutomaticGainControl_agcCreate(JNIEnv *env,
                                                                              jobject obj) {
    return (long) webrtc::WebRtcAgc_Create();
}

JNIEXPORT jint JNICALL
Java_com_he_hens_AutomaticGainControl_agcInit(JNIEnv *env,
                                                                            jobject obj,
                                                                            jlong agcInst,
                                                                            jint minLevel,
                                                                            jint maxLevel,
                                                                            jint agcMode,
                                                                            jint fs,
                                                                            jshort targetLevelDbfs,
                                                                            jshort compressionGaindB,
                                                                            jboolean limiterEnable
) {
    void *_agcInst = (void *) agcInst;
    if (_agcInst == nullptr) {
        return -3;
    }
    int init_state = webrtc::WebRtcAgc_Init(_agcInst, minLevel, maxLevel, agcMode, fs);
    if (init_state != 0) {
        return init_state;
    }
    webrtc::WebRtcAgcConfig setConfig;
    setConfig.targetLevelDbfs = targetLevelDbfs;
    setConfig.compressionGaindB = compressionGaindB;
    setConfig.limiterEnable = limiterEnable;
    return WebRtcAgc_set_config(_agcInst, setConfig);
}

JNIEXPORT jint JNICALL
Java_com_he_hens_AutomaticGainControl_agcProcess(JNIEnv *env,
                                                                               jobject obj,
                                                                               jlong agcInst,
                                                                               jshortArray inNear,
                                                                               jint num_bands,
                                                                               jint samples,
                                                                               jshortArray out,
                                                                               jint inMicLevel,
                                                                               jint outMicLevel,
                                                                               jint echo,
                                                                               jboolean saturationWarning) {
    void *_agcInst = (void *) agcInst;
    if (_agcInst == nullptr)
        return -3;
    jshort *cinNear = env->GetShortArrayElements(inNear, nullptr);
    jshort *cout = env->GetShortArrayElements(out, nullptr);

    int32_t gains[11] = {};
    jint ret = webrtc::WebRtcAgc_Analyze(_agcInst, &cinNear, num_bands, samples, inMicLevel,
                                         &outMicLevel,
                                         echo, &saturationWarning, gains);
    if (ret == 0) {
        ret = webrtc::WebRtcAgc_Process(_agcInst, gains, &cinNear, num_bands, &cout);
    }
    env->ReleaseShortArrayElements(inNear, cinNear, 0);
    env->ReleaseShortArrayElements(out, cout, 0);
    return ret;
}

JNIEXPORT jint JNICALL
Java_com_he_hens_AutomaticGainControl_agcFree(JNIEnv *env,
                                                                            jobject obj,
                                                                            jlong agcInst) {
    void *_agcInst = (void *) agcInst;
    if (_agcInst == nullptr)
        return -3;
    webrtc::WebRtcAgc_Free(_agcInst);
    return 0;
}

#if defined(__cplusplus)
}
#endif


