package com.leo.lib_permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.leo.lib_permission.annotations.PermissionRefused;
import com.leo.lib_permission.annotations.PermissionRefusedForever;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PermissionUtil {
    private PermissionUtil() {
    }

    /**
     * 是否所有权限均已被申请
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissionRequest(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否所有权限都申请成功了
     *
     * @param result
     * @return
     */
    public static boolean hasPermissionSuccess(int... result) {
        if (result == null || result.length == 0) {
            return false;
        }
        for (int i : result) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取被永久拒绝的权限
     *
     * @param activity
     * @param permissions
     */
    public static ArrayList<String> shouldShowRequestPermissionRationale(Activity activity, String[] permissions,
                                                                         int[] grantResults) {
        ArrayList<String> unRationalePermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                // i位的权限是申请通过的权限  shouldShowRequestPermissionRationale会返回false
                continue;
            }
            // 被永久拒绝拒绝了的权限 看下是被永久拒绝
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                unRationalePermissions.add(permissions[i]);
            }
        }
        return unRationalePermissions;
    }

    /**
     * 获取所有被拒绝的权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static ArrayList<String> getUnauthorizedPermissions(Context context, String[] permissions) {
        ArrayList<String> unauthorizedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                unauthorizedPermissions.add(permissions[i]);
            }
        }
        return unauthorizedPermissions;
    }

    /**
     * 将结果回调到被annotationClass这个注解的requestCode这个方法
     *
     * @param o
     * @param annotationClass
     * @param requestCode
     */
    public static void invokeMethod(Object o, Class annotationClass, int requestCode, String[] refusedPermissions) {
        Class<?> aClass = o.getClass();
        // 获取到所有的方法
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            // 方法是否被annotation所注解
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            if (annotationPresent) {
                Annotation annotation = method.getAnnotation(annotationClass);

                int code = -9999;
                if (annotationClass == PermissionRefused.class) {
                    code = ((PermissionRefused) annotation).requestCode();
                } else if (annotationClass == PermissionRefusedForever.class) {
                    code = ((PermissionRefusedForever) annotation).requestCode();
                }
                if (code == requestCode) {
                    try {
                        method.invoke(o, new Object[]{refusedPermissions});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
