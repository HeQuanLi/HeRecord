#include <jni.h>
#include <string>
#include "lame.h"
#include <android/log.h>

//打印日志
#define LAME_TAG "Lame_Log"
#define LogE(...) __android_log_print(ANDROID_LOG_ERROR,LAME_TAG ,__VA_ARGS__)
#define LogD(...) __android_log_print(ANDROID_LOG_DEBUG,LAME_TAG ,__VA_ARGS__)

static lame_global_flags *lame = nullptr;

void errorMsg(const char *msg, va_list vaList) {
    char string[256];
    vsprintf(string, msg, vaList);
    LogE("%s", string);
}

void debugMsg(const char *msg, va_list vaList) {
    char string[256];
    vsprintf(string, msg, vaList);
    LogD("%s", string);
}

void wMsg(const char *msg, va_list vaList) {
    char string[256];
    vsprintf(string, msg, vaList);
    LogD("%s", string);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_he_lame_HeLame_getVersion(JNIEnv *env, jobject thiz) {
    return (*env).NewStringUTF(get_lame_version());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_he_lame_HeLame_init(
        JNIEnv *env,
        jobject thiz,
        jint in_sample_rate,
        jint in_channel,
        jint out_sample_rate,
        jint out_bitrate,
        jint quality,
        jint low_pass_freq,
        jint high_pass_freq,
        jboolean enable_log) {

    if (lame != nullptr) {
        lame_close(lame);
        lame = nullptr;
    }
    lame = lame_init();
    //初始化，设置参数
    lame_set_in_samplerate(lame, in_sample_rate);//输入采样率
    lame_set_out_samplerate(lame, out_sample_rate);//输出采样率
    lame_set_num_channels(lame, in_channel);//声道
    lame_set_brate(lame, out_bitrate);//比特率
    lame_set_quality(lame, quality);//质量

    lame_set_lowpassfreq(lame, low_pass_freq); //设置滤波器，-1 disabled
    lame_set_highpassfreq(lame, high_pass_freq);//设置滤波器，-1 disabled
    //设置信息输出
    if (enable_log) {
        lame_set_errorf(lame, errorMsg);
        lame_set_debugf(lame, debugMsg);
        lame_set_msgf(lame, wMsg);
        LogD("lame_init_params:inSamplerate =%d, inChannel=%d, outSamplerate=%d, outBitrate=%d, quality=%d, lowpassfreq=%d, highpassfreq=%d",
             in_sample_rate, in_channel, out_sample_rate, out_bitrate, quality, low_pass_freq,
             high_pass_freq);
    }
    lame_init_params(lame);
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_he_lame_HeLame_encode(
        JNIEnv *env,
        jobject thiz,
        jshortArray buffer_left,
        jshortArray buffer_right,
        jint samples,
        jbyteArray mp3buf) {


    jshort *j_buff_left = (*env).GetShortArrayElements(buffer_left, nullptr);
    jshort *j_buff_right = (*env).GetShortArrayElements(buffer_right, nullptr);

    const jsize mp3buf_size = (*env).GetArrayLength(mp3buf);

    jbyte *j_mp3buff = (*env).GetByteArrayElements(mp3buf, nullptr);

    int result = lame_encode_buffer(
            lame,
            j_buff_left,
            j_buff_right,
            samples,
            (unsigned char *) j_mp3buff,
            mp3buf_size
    );

    //释放参数
    (*env).ReleaseShortArrayElements(buffer_left, j_buff_left, 0);
    (*env).ReleaseShortArrayElements(buffer_right, j_buff_right, 0);
    (*env).ReleaseByteArrayElements(mp3buf, j_mp3buff, 0);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_he_lame_HeLame_encodeInterleaved(
        JNIEnv *env,
        jobject thiz,
        jshortArray pcm,
        jint samples,
        jbyteArray mp3buf
) {
    jshort *j_pcm_buffer = (*env).GetShortArrayElements(pcm, nullptr);

    const jsize mp3buf_size = (*env).GetArrayLength(mp3buf);

    jbyte *j_mp3buff = (*env).GetByteArrayElements(mp3buf, nullptr);

    int result = lame_encode_buffer_interleaved(
            lame,
            j_pcm_buffer,
            samples,
            (unsigned char *) j_mp3buff,
            mp3buf_size);

    //释放参数
    (*env).ReleaseShortArrayElements(pcm, j_pcm_buffer, 0);
    (*env).ReleaseByteArrayElements(mp3buf, j_mp3buff, 0);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_he_lame_HeLame_flush(JNIEnv *env, jobject thiz, jbyteArray mp3buf) {

    const jsize mp3buf_size = (*env).GetArrayLength(mp3buf);
    jbyte *j_mp3buff = (*env).GetByteArrayElements(mp3buf, nullptr);
    int result = lame_encode_flush(lame, (unsigned char *) j_mp3buff, mp3buf_size);
    //释放
    (*env).ReleaseByteArrayElements(mp3buf, j_mp3buff, 0);
    return result;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_he_lame_HeLame_close(JNIEnv *env, jobject thiz) {
    lame_close(lame);
    lame = nullptr;
}