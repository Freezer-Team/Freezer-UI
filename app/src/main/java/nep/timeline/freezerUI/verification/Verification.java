package nep.timeline.freezerUI.verification;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;

import nep.timeline.freezer.binders.VerificationInterface;
import nep.timeline.freezer.provide.VerificationBinder;
import nep.timeline.freezerUI.ui.utils.AppContext;
import nep.timeline.freezerUI.ui.utils.UserUtils;

public class Verification {
    public static boolean run(String requestType, String username, String password) {
        if (requestType.equals("Reset"))
            WaitDialog.show("正在尝试修改密码");
        else
            WaitDialog.show("正在尝试验证");

        try {
            VerificationInterface verificationInterface = VerificationBinder.getInstance();
            if (verificationInterface == null) {
                TipDialog.show("无法与系统框架通信，请重启设备后重试！", WaitDialog.TYPE.ERROR, 3000);
                return false;
            }

            String result = verificationInterface.verification(requestType, username, password);
            if (result == null) {
                TipDialog.show("验证服务器无响应，请重启设备后重试！", WaitDialog.TYPE.ERROR, 3000);
                return false;
            }
            switch (result) {
                case "RESET_SUCCESS" -> {
                    TipDialog.show("你的密码已完成重置！请重新登录", WaitDialog.TYPE.SUCCESS, 3000);
                    return true;
                }
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
                case "RESET_FAILED_DEVICE_NOT_SAME" -> TipDialog.show("请使用注册此账户时使用的设备重置密码！", WaitDialog.TYPE.ERROR, 3000);
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
                case "HARDWARE_ID_MISMATCH" -> TipDialog.show("硬件ID不匹配，这不是我们的问题，请联系开发者！", WaitDialog.TYPE.ERROR, 3000);
                case "HARDWARE_INVALID" -> TipDialog.show("硬件ID不可用，这不是我们的问题，请联系开发者！", WaitDialog.TYPE.ERROR, 3000);
                case "FAILED_TO_CONNECT_SYSTEM_FRAMEWORK" -> TipDialog.show("无法与系统框架通信，请重启设备后重试！", WaitDialog.TYPE.ERROR, 3000);
                case "SocketException" -> TipDialog.show("无法连接至验证服务器！请检查网络或重启你的设备", WaitDialog.TYPE.ERROR, 3000);
                default -> TipDialog.show("未知错误！错误码: " + result, WaitDialog.TYPE.ERROR, 3000);
            }
        } catch (NullPointerException e) {
            ClipData clip = ClipData.newPlainText("text", android.util.Log.getStackTraceString(e));
            ClipboardManager manager = ((ClipboardManager) AppContext.context.getSystemService(CLIPBOARD_SERVICE));
            manager.setPrimaryClip(clip);
            TipDialog.show("出现异常！第一次更新请重启设备后重试！异常已复制至剪贴板", WaitDialog.TYPE.ERROR, 3000);
        } catch (Exception e) {
            TipDialog.show("警告: 出现异常! ", WaitDialog.TYPE.ERROR, 3000);
        }

        return false;
    }

    public static void runActivity() {
        DialogX.useHaptic = true;
        DialogX.onlyOnePopTip = true;
    }
}
