package com.comscore;

import android.util.Log;
import androidx.annotation.NonNull;
import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;
import com.comscore.UsagePropertiesAutoUpdateMode;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import java.util.Collections;
import java.util.HashMap;

public class ComScoreModule extends ReactContextBaseJavaModule {

  final String CS_FPID = "cs_fpid";
  final String CS_FPIT = "cs_fpit";
  final String CS_FPDM = "cs_fpdm";
  final String CS_FPDT = "cs_fpdt";

  public ComScoreModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return "ComScoreModule";
  }

  // Helper method to parse string representation into UsagePropertiesAutoUpdateMode enum
  private int parseUpdateMode(String modeString) {
    switch (modeString) {
      case "FOREGROUND_ONLY":
        return UsagePropertiesAutoUpdateMode.FOREGROUND_ONLY;
      case "FOREGROUND_AND_BACKGROUND":
        return UsagePropertiesAutoUpdateMode.FOREGROUND_AND_BACKGROUND;
      case "DISABLED":
        return UsagePropertiesAutoUpdateMode.DISABLED;
      default:
        return UsagePropertiesAutoUpdateMode.FOREGROUND_ONLY; // Default value
    }
  }

  @ReactMethod
  public void initializeComScore(ReadableMap params) {
    WritableMap writableMap = Arguments.createMap();
    writableMap.merge(params);

    // Extract required parameter
    String publisherId = writableMap.getString("publisherId");
    Log.i("ComScoreModule", "Setting publisherId: " + publisherId);

    // Extract optional parameters
    String applicationName = params.hasKey("applicationName")
      ? params.getString("applicationName")
      : null;
    String usagePropertiesAutoUpdateMode = params.hasKey(
        "usagePropertiesAutoUpdateMode"
      )
      ? params.getString("usagePropertiesAutoUpdateMode")
      : null;
    Integer usagePropertiesAutoUpdateInterval = params.hasKey(
        "usagePropertiesAutoUpdateInterval"
      )
      ? params.getInt("usagePropertiesAutoUpdateInterval")
      : null;
    Log.i("ComScoreModule", "Initializing ComScore");

    //1P-Data
    ReadableMap param_labels = writableMap.hasKey("data_1p")
      ? (ReadableMap) writableMap.getMap("data_1p")
      : null;
    HashMap<String, String> labels = new HashMap<String, String>();
    if (param_labels != null) {
      Log.i("ComScoreModule", "Data_1p is not null");
      if (param_labels.hasKey(CS_FPID)) {
        labels.put(CS_FPID, param_labels.getString(CS_FPID));
      }
      if (param_labels.hasKey(CS_FPIT)) {
        labels.put(CS_FPIT, param_labels.getString(CS_FPIT));
      }
      if (param_labels.hasKey(CS_FPDM)) {
        labels.put(CS_FPDM, param_labels.getString(CS_FPDM));
      }
      if (param_labels.hasKey(CS_FPDT)) {
        labels.put(CS_FPDT, param_labels.getString(CS_FPDT));
      }
    }
    try {
      // Create a PublisherConfiguration.Builder object with the provided settings
      PublisherConfiguration myPublisherConfig;
      if (!labels.isEmpty()) {
        myPublisherConfig = new PublisherConfiguration.Builder()
          .publisherId(publisherId)
          .persistentLabels(labels)
          .build();
        Log.i("ComScoreModule", "Publisher build with persistent Labels");
      } else {
        myPublisherConfig = new PublisherConfiguration.Builder()
          .publisherId(publisherId)
          .build();
        Log.i("ComScoreModule", "Publisher build without persistent Labels");
      }
      // Add the publisher configuration to the Analytics singleton
      Analytics.getConfiguration().addClient(myPublisherConfig);

      // Apply applicationName if provided
      if (applicationName != null && !applicationName.isEmpty()) {
        Log.i("ComScoreModule", "Setting applicationName: " + applicationName);
        Analytics.getConfiguration().setApplicationName(applicationName); // Correct method name is publisherName()
      }

      // Apply general configuration settings if provided
      if (
        usagePropertiesAutoUpdateMode != null &&
        !usagePropertiesAutoUpdateMode.isEmpty()
      ) {
        // Parse the update mode string into the corresponding enum value
        int autoUpdateMode = parseUpdateMode(usagePropertiesAutoUpdateMode);
        Log.i(
          "ComScoreModule",
          "Setting usagePropertiesAutoUpdateMode: " + autoUpdateMode
        );
        // Set the usagePropertiesAutoUpdateMode
        Analytics.getConfiguration()
          .setUsagePropertiesAutoUpdateMode(autoUpdateMode);
      }

      // Apply usagePropertiesAutoUpdateInterval if provided
      if (
        usagePropertiesAutoUpdateInterval != null &&
        usagePropertiesAutoUpdateInterval > 0
      ) {
        // Ensure that usagePropertiesAutoUpdateInterval is in seconds
        // Here, we assume that usagePropertiesAutoUpdateInterval is already in seconds
        Log.i(
          "ComScoreModule",
          "Setting usagePropertiesAutoUpdateInterval: " +
          usagePropertiesAutoUpdateInterval
        );
        Analytics.getConfiguration()
          .setUsagePropertiesAutoUpdateInterval(
            usagePropertiesAutoUpdateInterval
          );
      }

      Analytics.getConfiguration().enableImplementationValidationMode();
      // Start the ComScore library
      Analytics.start(getReactApplicationContext());
    } catch (Exception e) {
      // Handle exceptions with a more robust logging mechanism
      // Replace printStackTrace() with appropriate logging
      Log.e("ComScoreModule", "Error initializing ComScore", e);
    }
  }

  @ReactMethod
  public void updateConsent(String consentValue) {
    Log.i("updateConsent", consentValue);
    try {
      Analytics.getConfiguration().setPersistentLabel("cs_ucfr", consentValue);
      Analytics.notifyHiddenEvent();
    } catch (Exception e) {
      // Handle exceptions with a more robust logging mechanism
      // Replace printStackTrace() with appropriate logging
      Log.e("updateConsent", "Error in updateConsent", e);
    }
  }

  @ReactMethod
  public void trackScreen(String pageName) {
    if (pageName == null) return;
    Log.i("ComScoreModule", "trackScreen page name : " + pageName);
    try {
      Analytics.notifyViewEvent(
        Collections.singletonMap("ns_category", pageName)
      );
      Log.i("ComScoreModule", "notifyViewEvent called successfully: ");
    } catch (Exception e) {
      Log.e("ComScoreModule", "Error in trackScreen : ", e);
    }
  }

  @ReactMethod
  public void trackScreenWithData(String pageName, ReadableMap additionalParams) {
    if (pageName == null) return;
    Log.i("ComScoreModule", "trackScreen page name : " + pageName);

    Map<String, String> params = new HashMap<>();
    params.put("ns_category", pageName);

    // Merge additionalParams into params
    if (additionalParams != null) {
      ReadableMapKeySetIterator iterator = additionalParams.keySetIterator();
      while (iterator.hasNextKey()) {
        String key = iterator.nextKey();
        params.put(key, additionalParams.getString(key));
      }
    }

    try {
      Analytics.notifyViewEvent(params);
      Log.i("ComScoreModule", "notifyViewEvent called successfully: ");
    } catch (Exception e) {
      Log.e("ComScoreModule", "Error in trackScreen : ", e);
    }
  }

  @ReactMethod
  public void update1PData(ReadableMap params) {
    try {
      if (params == null || !params.hasKey("publisherId")) return;
      String publisherId = params.getString("publisherId");
      PublisherConfiguration publisherConfig = Analytics.getConfiguration()
        .getPublisherConfiguration(publisherId);

      // Loop through each key-value pair in the params map
      ReadableMapKeySetIterator iterator = params.keySetIterator();
      while (iterator.hasNextKey()) {
        String key = iterator.nextKey();
        //if key is publisher id then continue
        if ("publisherId".equals(key)) {
          continue;
        }

        String value = params.getString(key);

        // Set the persistent label for the key-value pair
        publisherConfig.setPersistentLabel(key, value);
        Log.i(
          "ComScoreModule",
          "Setting persistent label: " + key + " - " + value
        );
      }
      Analytics.notifyHiddenEvent();
      Log.i("ComScoreModule", "1p data updated : ");
    } catch (Exception e) {
      // Handle exceptions with a more robust logging mechanism
      // Replace printStackTrace() with appropriate logging
      Log.e("ComScoreModule", "Error updating 1P data", e);
    }
  }
}
