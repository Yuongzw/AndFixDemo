#include <jni.h>
#include <string>
#include "art_method.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_yuong_andfixdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_yuong_andfixdemo_DexManager_replaceMethod(JNIEnv *env, jobject thiz, jobject wrong_method,
                                                   jobject right_method) {
    //获取ArtMethod 结构体 Android 系统源码中
    art::mirror::ArtMethod *wrongMethod = (art::mirror::ArtMethod *)env->FromReflectedMethod(wrong_method);
    art::mirror::ArtMethod *rightMethod = (art::mirror::ArtMethod *)env->FromReflectedMethod(right_method);

    //替换
    wrongMethod->declaring_class_ = rightMethod->declaring_class_;
    wrongMethod->dex_cache_resolved_methods_ = rightMethod->dex_cache_resolved_methods_;
    wrongMethod->access_flags_ = rightMethod->access_flags_;
    wrongMethod->dex_cache_resolved_types_ = rightMethod->dex_cache_resolved_types_;
    wrongMethod->dex_code_item_offset_ = rightMethod->dex_code_item_offset_;
    wrongMethod->method_index_ = rightMethod->method_index_;
    wrongMethod->dex_method_index_ = rightMethod->dex_method_index_;
}