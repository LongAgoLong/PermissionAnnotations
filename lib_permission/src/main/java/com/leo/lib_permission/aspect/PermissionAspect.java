package com.leo.lib_permission.aspect;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.leo.lib_permission.activity.PermissionActivity;
import com.leo.lib_permission.annotations.PermissionApply;
import com.leo.lib_permission.annotations.PermissionRefused;
import com.leo.lib_permission.annotations.PermissionRefusedForever;
import com.leo.lib_permission.interfaces.PermissionCallback;
import com.leo.lib_permission.utils.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.leo.lib_permission.annotations.PermissionApply * * (..))")
    public void pointMethod() {
    }

    @Around("pointMethod()")
    public void permissionApply(final ProceedingJoinPoint joinPoint) {
        Context context = null;
        // 获取到切入的对象
        final Object aThis = joinPoint.getThis();
        if (aThis instanceof Context) {
            context = (Context) aThis;
        } else if (aThis instanceof Fragment) {
            context = ((Fragment) aThis).getContext();
        } else {
            // 不是在Activity/Fragment/Service中请求权限，
            // 则该类需要提供getContext()方法
            try {
                Class<?> aClass = aThis.getClass();
                Method getContext = aClass.getMethod("getContext", null);
                context = (Context) getContext.invoke(aThis, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PermissionApply permissionApply = method.getAnnotation(PermissionApply.class);

        // 不符合需要切入的条件，执行原方法
        if (context == null || permissionApply == null ||
                permissionApply.permissions() == null || permissionApply.permissions().length == 0) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        // 获取到注解中的数据 要申请的权限数组
        String[] values = permissionApply.permissions();
        // 请求码
        final int requestCode = permissionApply.requestCode();
        PermissionActivity.launchActivity(context, values, requestCode, new PermissionCallback() {
            @Override
            public void permissionApplySuccess() {
                /**
                 * 申请权限成功，执行原程序逻辑
                 * 注意这里有个坑，因为用了try-catch 如果原程序里面的异常在这里被捕获了，
                 * 不会被Thread.UncaughtExceptionHandler这个类接管到了这是第一点
                 * 第二点是如果原程序的逻辑如果本来会明显出现崩溃情况的，由于这里try-catch的原因，
                 * 不会崩溃了，可能会对测试结果产生一些影响
                 */
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionApplyRefused(String[] refusedPermissions) {
                PermissionUtil.invokeMethod(aThis, PermissionRefused.class, requestCode, refusedPermissions);
            }

            @Override
            public void permissionApplyRefusedForever(String[] refusedForeverPermissions) {
                PermissionUtil.invokeMethod(aThis, PermissionRefusedForever.class, requestCode,
                        refusedForeverPermissions);
            }
        });
    }
}
