package com.anyline.reactnative;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import io.anyline.view.ScanView;

public class ScanViewViewManager extends SimpleViewManager<ReactScanView> {

    private static final String REACT_NATIVE_CLASS_NAME = "ScanView";
    private static final int COMMAND_START = 1;
    private static final int COMMAND_STOP = 2;

    private ReactApplicationContext reactApplicationContext;

    public ScanViewViewManager(ReactApplicationContext reactApplicationContext) {
        this.reactApplicationContext = reactApplicationContext;
    }

    @Override
    public ReactScanView createViewInstance(ThemedReactContext reactContext) {
        return new ReactScanView(reactContext);
    }

    @Override
    public String getName() {
        return REACT_NATIVE_CLASS_NAME;
    }

    @ReactProp(name = "config")
    public void setConfig(ReactScanView scanView, String config) {
        scanView.init(config);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(
                        "onResult",
                        MapBuilder.of(
                                "registrationName",
                                "onResult"
                        )
                )
                .put(
                        "onContinuousResult",
                        MapBuilder.of(
                                "registrationName",
                                "onContinuousResult"
                        )
                )
                .build();
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "start",
                COMMAND_START,
                "stop",
                COMMAND_STOP
        );
    }

    @Override
    public void receiveCommand(ReactScanView scanView, int commandType, @Nullable ReadableArray args) {
        switch (commandType) {
            case COMMAND_START:
                start(scanView);
                break;
            case COMMAND_STOP:
                stop(scanView);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Unsupported command %d received by %s.",
                                commandType,
                                getClass().getSimpleName()
                        )
                );
        }
    }

    private void start(ScanView scanView) {
        scanView.start();
    }

    private void stop(ScanView scanView) {
        scanView.stop();
        scanView.releaseCameraInBackground();
    }
}