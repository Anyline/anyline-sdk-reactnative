package com.anyline.reactnative;


import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import at.nineyards.anyline.core.LicenseException;
import io.anyline.AnylineSDK;

class AnylineSdk extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "AnylineSdk";
    private final ReactApplicationContext reactContext;

    public AnylineSdk(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void initSdk(String license, Callback errorCallback, Callback successCallback) {
        try {
            AnylineSDK.init(license, reactContext);
            successCallback.invoke();
        } catch (LicenseException e) {
            errorCallback.invoke(e.getMessage());
        }
    }
}