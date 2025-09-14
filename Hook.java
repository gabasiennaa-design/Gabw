package com.example.calltimerhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.phone")) return;
        XposedBridge.log("CallTimerHook loaded in " + lpparam.packageName);

        try {
            Class<?> gsmCdmaConn = lpparam.classLoader.loadClass("com.android.internal.telephony.GsmCdmaCallTracker");
            XposedBridge.hookAllMethods(gsmCdmaConn, "obtainMessage", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // Example: reduce timers (t305, t310...)
                    XposedBridge.log("CallTimerHook: Modified call timer");
                }
            });
        } catch (Throwable t) {
            XposedBridge.log("CallTimerHook error: " + t);
        }
    }
}
