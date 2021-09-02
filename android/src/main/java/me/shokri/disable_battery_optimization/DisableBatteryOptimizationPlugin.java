package me.shokri.disable_battery_optimization;

import android.os.Build;
import android.content.Context;
import androidx.annotation.NonNull;
import static android.content.Context.POWER_SERVICE;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.net.Uri;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** DisableBatteryOptimizationPlugin */
public class DisableBatteryOptimizationPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context applicationContext;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    this.applicationContext = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "disable_battery_optimization");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    applicationContext = null;
    channel = null;
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }     else if (call.method.equals("isIgnoringBatteryOptimizations")) {
      boolean reading = isIgnoringBatteryOptimizations();
      result.success(reading);
      return;
    }
    else if (call.method.equals("openBatteryOptimizationSettings")) {
      String reading = openBatteryOptimizationSettings();
      result.success(reading);
      return;
    }
    else if (call.method.equals("stopOptimizingBatteryUsage")) {
      String reading = stopOptimizingBatteryUsage();
      result.success(reading);
      return;
    }
    else {
      result.notImplemented();
    }
  }
  private boolean isIgnoringBatteryOptimizations() {
    String packageName = applicationContext.getPackageName();
    PowerManager mPowerManager = (PowerManager) (applicationContext.getSystemService(POWER_SERVICE));

    if(mPowerManager !=null && mPowerManager.isIgnoringBatteryOptimizations(packageName)) {
      return true;
    } else {
      return false;
    }
  }

  private String openBatteryOptimizationSettings() {
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
    applicationContext.startActivity(intent);
    return "Success";
  }

  private String stopOptimizingBatteryUsage() {
    Intent intent = new Intent();
    String packageName = applicationContext.getPackageName();
    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
    intent.setData(Uri.parse("package:" + packageName));
    applicationContext.startActivity(intent);
    return "Success";
  }

}
