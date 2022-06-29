package com.anyline.reactnative;

import android.content.Context;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import io.anyline.models.AnylineImage;
import io.anyline.plugin.ScanResult;
import io.anyline.plugin.barcode.Barcode;
import io.anyline.plugin.barcode.BarcodeScanPlugin;
import io.anyline.plugin.barcode.BarcodeScanResult;
import io.anyline.plugin.barcode.BarcodeScanViewPlugin;
import io.anyline.util.TempFileUtil;
import io.anyline.view.ScanView;

public class ReactScanView extends ScanView {

    public ReactScanView(@NonNull Context context) {
        super(context);
    }

    @ReactMethod
    @Override
    public void init(@NonNull String configFileName) {
        super.init(configFileName);
        subscribeForResult();
    }

    private void subscribeForResult() {
        Context context = getContext();
        if (context instanceof ReactContext) {
            RCTEventEmitter rctEventEmitter = ((ReactContext) context).getJSModule(RCTEventEmitter.class);

            getScanViewPlugin().addScanResultListener(scanResult -> {
                rctEventEmitter.receiveEvent(
                        getId(),
                        "onResult",
                        getResultMap(scanResult, true)
                );
            });

            ((BarcodeScanPlugin) ((BarcodeScanViewPlugin) getScanViewPlugin()).getScanPlugin())
                    .addScannedBarcodesListener(scanResult -> {
                        rctEventEmitter.receiveEvent(
                                getId(),
                                "onContinuousResult",
                                getResultMap(scanResult, false)
                        );
                    });
        }
    }

    @NonNull
    private WritableMap getResultMap(@NonNull ScanResult scanResult, boolean shouldSaveImage) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("result", getResultJson(scanResult, shouldSaveImage));
        writableMap.putInt("confidence", scanResult.getConfidence() != null ? scanResult.getConfidence() : -1);
        return writableMap;
    }

    @NonNull
    private String getResultJson(@NonNull ScanResult scanResult, boolean shouldSaveImage) {
        JSONObject jsonObject = new JSONObject();

        if (scanResult instanceof BarcodeScanResult) {
            BarcodeScanResult barcodeScanResult = (BarcodeScanResult) scanResult;
            try {
                JSONArray barcodes = new JSONArray();

                for (Barcode barcode : barcodeScanResult.getResult()) {
                    barcodes.put(barcode.toJSONObject());
                }

                jsonObject.put("result", barcodes);

                if (scanResult.getCutoutImage() != null && shouldSaveImage) {
                    jsonObject.put("cutoutImage", saveImage(scanResult.getCutoutImage()));
                }

                if (scanResult.getFullImage() != null && shouldSaveImage) {
                    jsonObject.put("fullImage", saveImage(scanResult.getFullImage()));
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    private String saveImage(@NonNull AnylineImage image) throws IOException {
        File imageFile = TempFileUtil.createTempFileCheckCache(getContext(), UUID.randomUUID().toString(), ".jpg");
        image.save(imageFile, 90);
        return imageFile.getAbsolutePath();
    }
}