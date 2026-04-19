#include <windows.h>
#include <jni.h>
#include <string.h>

#define ID_JAR_FILE1 101

HMODULE GetCurrentModule() {
    HMODULE h = NULL;
    GetModuleHandleExA(GET_MODULE_HANDLE_EX_FLAG_FROM_ADDRESS | GET_MODULE_HANDLE_EX_FLAG_UNCHANGED_REFCOUNT, (LPCSTR)GetCurrentModule, &h);
    return h;
}

void LoadAndStartJar(JNIEnv* env) {
    HMODULE hMod = GetCurrentModule();
    HRSRC hRes = FindResourceA(hMod, MAKEINTRESOURCEA(ID_JAR_FILE1), "JAR_FILE");
    if (!hRes) hRes = FindResourceA(hMod, MAKEINTRESOURCEA(ID_JAR_FILE1), (LPCSTR)RT_RCDATA);
    if (!hRes) return;

    HGLOBAL hData = LoadResource(hMod, hRes);
    void* pJarData = LockResource(hData);
    DWORD jarSize = SizeofResource(hMod, hRes);

    char tempPath[MAX_PATH];
    GetTempPathA(MAX_PATH, tempPath);
    strcat_s(tempPath, MAX_PATH, "clover_cache.jar");

    HANDLE hFile = CreateFileA(tempPath, GENERIC_WRITE, 0, NULL, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, NULL);
    if (hFile != INVALID_HANDLE_VALUE) {
        DWORD bytesWritten;
        WriteFile(hFile, pJarData, jarSize, &bytesWritten, NULL);
        CloseHandle(hFile);
    }

    jclass threadClass = env->FindClass("java/lang/Thread");
    jobject threadsMap = env->CallStaticObjectMethod(threadClass, env->GetStaticMethodID(threadClass, "getAllStackTraces", "()Ljava/util/Map;"));
    jclass mapClass = env->FindClass("java/util/Map");
    jobject keySet = env->CallObjectMethod(threadsMap, env->GetMethodID(mapClass, "keySet", "()Ljava/util/Set;"));
    jclass setClass = env->FindClass("java/util/Set");
    jobject iterator = env->CallObjectMethod(keySet, env->GetMethodID(setClass, "iterator", "()Ljava/util/Iterator;"));
    jclass iterClass = env->FindClass("java/util/Iterator");
    jmethodID hasNext = env->GetMethodID(iterClass, "hasNext", "()Z");
    jmethodID next = env->GetMethodID(iterClass, "next", "()Ljava/lang/Object;");

    jobject classLoader = nullptr;
    while (env->CallBooleanMethod(iterator, hasNext)) {
        jobject thread = env->CallObjectMethod(iterator, next);
        jobject loader = env->CallObjectMethod(thread, env->GetMethodID(threadClass, "getContextClassLoader", "()Ljava/lang/ClassLoader;"));
        if (loader) {
            jstring s = (jstring)env->CallObjectMethod(loader, env->GetMethodID(env->FindClass("java/lang/Object"), "toString", "()Ljava/lang/String;"));
            const char* str = env->GetStringUTFChars(s, nullptr);
            if (strstr(str, "LaunchClassLoader") || strstr(str, "AppClassLoader")) {
                classLoader = loader;
                env->ReleaseStringUTFChars(s, str);
                break;
            }
            env->ReleaseStringUTFChars(s, str);
        }
    }

    if (classLoader) {
        jclass fileClass = env->FindClass("java/io/File");
        jobject fileObj = env->NewObject(fileClass, env->GetMethodID(fileClass, "<init>", "(Ljava/lang/String;)V"), env->NewStringUTF(tempPath));
        jobject urlObj = env->CallObjectMethod(fileObj, env->GetMethodID(fileClass, "toURL", "()Ljava/net/URL;"));
        jclass loaderClass = env->GetObjectClass(classLoader);
        jmethodID addURL = env->GetMethodID(loaderClass, "addURL", "(Ljava/net/URL;)V");

        if (addURL) {
            env->CallVoidMethod(classLoader, addURL, urlObj);
            jclass mainClass = (jclass)env->CallObjectMethod(classLoader, env->GetMethodID(loaderClass, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;"), env->NewStringUTF("com.itelcan3.cloverclient.Main"));
            if (mainClass) {
                jmethodID inject = env->GetStaticMethodID(mainClass, "inject", "()V");
                if (inject) env->CallStaticVoidMethod(mainClass, inject);
            }
        }
    }
}

DWORD WINAPI MainThread(LPVOID lpParam) {
    JavaVM* jvm = nullptr;
    JNIEnv* env = nullptr;
    jsize nVMs;
    if (JNI_GetCreatedJavaVMs(&jvm, 1, &nVMs) == JNI_OK && nVMs > 0) {
        if (jvm->AttachCurrentThread((void**)&env, NULL) == JNI_OK) {
            LoadAndStartJar(env);
            jvm->DetachCurrentThread();
        }
    }
    return 0;
}

BOOL WINAPI DllMain(HINSTANCE hinstDLL, DWORD fdwReason, LPVOID lpvReserved) {
    if (fdwReason == DLL_PROCESS_ATTACH) {
        DisableThreadLibraryCalls(hinstDLL);
        CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)MainThread, NULL, 0, NULL);
    }
    return TRUE;
}