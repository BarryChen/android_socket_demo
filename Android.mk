# Copyright 2005 The Android Open Source Project
#
# Android.mk for adb
#

LOCAL_PATH:= $(call my-dir)

# =========================================================
include $(CLEAR_VARS)
LOCAL_MODULE := socket_server
#LOCAL_LDFLAGS := -llog
LOCAL_SRC_FILES := server.c
LOCAL_LDLIBS += -lpthread
LOCAL_SHARED_LIBRARIES := \
	liblog \
	libcutils 

LOCAL_MODULE_TAGS := optional
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := socket_client
#LOCAL_LDFLAGS := -llog
LOCAL_LDLIBS += -lpthread
LOCAL_SHARED_LIBRARIES := \
	liblog \
	libcutils

LOCAL_SRC_FILES := client.c
LOCAL_MODULE_TAGS := optional
include $(BUILD_EXECUTABLE)


include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_MODULE := factory
include $(BUILD_JAVA_LIBRARY)


