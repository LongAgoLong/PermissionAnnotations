# PermissionAnnotations

------

## 基于AOP实现通过注解来完成权限动态申请

[![](https://jitpack.io/v/LongAgoLong/PermissionAnnotations.svg)](https://jitpack.io/#LongAgoLong/PermissionAnnotations)

## 1.依赖

### project的gradle依赖

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### build.gradle依赖

```
plugins {
    //AOP插件
    id 'android-aspectjx'
}
```

```gradle
dependencies {
	def version = "1.0.0"
	implementation "com.github.LongAgoLong:PermissionAnnotations:$version"
}
```

### maven依赖

```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```xml
	<dependency>
	    <groupId>com.github.LongAgoLong</groupId>
	    <artifactId>PermissionAnnotations</artifactId>
	    <version>$version</version>
	</dependency>
```

## 2.注解

### 2.1 PermissionApply-申请权限注解

```java
    @PermissionApply(
            permissions = {Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO},
            requestCode = 1001)
    public void requestPermission() {
        resultTv.setText("权限申请成功");
    }
```

PermissionApply(permissions ={},requestCode = 1001)

- permissions：申请的权限列表
- requestCode：请求码，失败回调判断的依据

### 2.2 PermissionRefused-申请权限失败的回调注解

```java
    @PermissionRefused(requestCode = 1001)
    public void permissionFailed(String[] refusedPermissions) {
        resultTv.setText("权限申请失败:\n");
        for (String s : refusedPermissions) {
            resultTv.append(s);
            resultTv.append("\n");
        }
    }
```

PermissionRefused(requestCode = 1001)

- requestCode：请求码，失败回调判断的依据

PS：方法修饰必须是public！

### 2.3 PermissionRefusedForever-申请权限被永久拒绝的回调

```java
    @PermissionRefusedForever(requestCode = 1001)
    public void permissionForever(String[] refusedPermissions) {
        resultTv.setText("权限申请失败,被永久拒绝了:\n");
        for (String s : refusedPermissions) {
            resultTv.append(s);
            resultTv.append("\n");
        }
    }
```

PermissionRefusedForever(requestCode = 1001)

- requestCode：请求码，失败回调判断的依据

PS：方法修饰必须是public！