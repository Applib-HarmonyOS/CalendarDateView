package com.yyx.library.utils;

import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.IOException;

public class ResUtil {

    private ResUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Color getColor(Context context, int id) {
        String LOG_KKK = "LOG_KKK";
        Color result = null;
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = new Color(manager.getElement(id).getColor());
        } catch (IOException e) {
            HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201, LOG_KKK), "getPathById -> IOException");
        } catch (NotExistException e) {
            HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201, LOG_KKK), "getPathById -> NotExistException");
        } catch (WrongTypeException e) {
            HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201, LOG_KKK), "getPathById -> WrongTypeException");
        }
        return result;
    }
}
