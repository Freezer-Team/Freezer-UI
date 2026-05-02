package nep.timeline.freezerUI.verification;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;

import java.net.SocketException;
import java.util.HashSet;
import java.util.List;

import nep.timeline.freezer.core.jni.NativeMethodsContrast;
import nep.timeline.freezerUI.ui.utils.UserUtils;

public class Verification {
    public static boolean run(String requestType, String username, String password) {
        if (requestType.equals("Reset"))
            WaitDialog.show("正在尝试修改密码");
        else
            WaitDialog.show("正在尝试验证");

        try {
            String result = NativeMethodsContrast.verification(requestType, username, password, new HashSet<>(List.of(username, UserUtils.INSTANCE.getUserData().split("\n")[0])));
            switch (result) {
                case "RESET_SUCCESS" -> {
                    TipDialog.show("你的密码已完成重置！请重新登录", WaitDialog.TYPE.SUCCESS, 3000);
                    return true;
                }
                case "RESET_FAILED_DEVICE_NOT_SAME" -> TipDialog.show("请使用注册此账户时使用的设备重置密码！", WaitDialog.TYPE.ERROR, 3000);
                case "ACTIVE_SUCCESS" -> {
                    TipDialog.show("你的设备已通过验证！请重启设备", WaitDialog.TYPE.SUCCESS, 3000);
                    return true;
                }
                case "REGISTER_SUCCESS" -> {
                    TipDialog.show("账户注册成功！", WaitDialog.TYPE.SUCCESS, 3000);
                    return true;
                }
                case "LOGIN_SUCCESS" -> {
                    TipDialog.show("账户登录成功！", WaitDialog.TYPE.SUCCESS, 3000);
                    return true;
                }
                case "PASSWORD_ERROR", "PASSWORD_MISMATCH" -> TipDialog.show("密码错误！", WaitDialog.TYPE.ERROR, 3000);
                case "DEVICE_FULL" -> TipDialog.show("用户登陆设备数量已达上限！", WaitDialog.TYPE.WARNING, 3000);
                case "WRITE_FAILED" -> TipDialog.show("文件写入失败！", WaitDialog.TYPE.ERROR, 3000);
                case "CONNECTION_FAILED" -> TipDialog.show("无法连接至验证服务器！", WaitDialog.TYPE.ERROR, 3000);
                case "REMOTE_SERVER_ERROR" -> TipDialog.show("远程验证服务器错误！", WaitDialog.TYPE.ERROR, 3000);
                case "UN_ACTIVE_ACCOUNT" -> TipDialog.show("账户未被激活, 请凭借用户名联系开发者进行激活！", WaitDialog.TYPE.ERROR, 3000);
                case "USER_NOT_FOUND" -> TipDialog.show("用户不存在！", WaitDialog.TYPE.ERROR, 3000);
                case "DEVICE_REGISTERED_ACCOUNT" -> TipDialog.show("你的设备已注册过其他账户！", WaitDialog.TYPE.ERROR, 3000);
                case "USERNAME_HAS_BEEN_USED" -> TipDialog.show("用户名已被使用！", WaitDialog.TYPE.WARNING, 3000);
                case "RISK" -> TipDialog.show("您的账户因存在风险问题已被封禁，请凭用户名联系开发者！", WaitDialog.TYPE.ERROR, 3000);
                default -> TipDialog.show("未知错误！错误码: " + result, WaitDialog.TYPE.ERROR, 3000);
            }
        } catch (SocketException ignored) {
            TipDialog.show("无法连接至验证服务器！请检查网络或重启你的设备", WaitDialog.TYPE.ERROR, 3000);
        } catch (NullPointerException ignored) {
            TipDialog.show("出现异常！第一次更新请重启设备后重试！", WaitDialog.TYPE.ERROR, 3000);
        } catch (Exception e) {
            TipDialog.show("警告: 出现异常! ", WaitDialog.TYPE.ERROR, 3000);
        }

        return false;
    }

    public static void runActivity() {
        DialogX.useHaptic = true;
        DialogX.onlyOnePopTip = true;
        System.loadLibrary("freezer");
    }
}
