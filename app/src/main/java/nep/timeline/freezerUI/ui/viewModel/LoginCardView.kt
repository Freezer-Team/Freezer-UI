package nep.timeline.freezerUI.ui.viewModel

import android.view.View
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezer.provide.FileBinder
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.UserUtils
import nep.timeline.freezerUI.verification.Verification
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.regex.Pattern

@Composable
fun LoginCardView(
    state: MutableState<Int>,
    userData: MutableState<String>
) {
    val account = if (state.value == 0 || state.value == 1) userData.value.split("\n")[0] else stringResource(R.string.no_account)
    val info = if (state.value == 0) stringResource(R.string.welcome_back) else if (state.value == 1) stringResource(R.string.start_active) else stringResource(R.string.start_login)
    val icon = if (state.value == 0) Icons.Rounded.DoneAll else if (state.value == 1) Icons.Rounded.Done else Icons.Rounded.Info

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (state.value != 0) {
                            if (state.value == 1)
                                Thread {
                                    val username = userData.value.split("\n")[0]
                                    val password = userData.value.split("\n")[1]
                                    if (Verification.run("Active", username, password)) {
                                        state.value = 0
                                        UserUtils.state = 0
                                    }
                                }.start()
                            else {
                                val inputDialog = InputDialog(
                                    AppContext.context.resources.getString(R.string.first_use_welcome),
                                    AppContext.context.resources.getString(R.string.enter_username),
                                    AppContext.context.resources.getString(R.string.ok),
                                    AppContext.context.resources.getString(R.string.cancel),
                                    ""
                                )
                                inputDialog.setCancelable(false)
                                inputDialog.setOkButton(object :
                                    OnInputDialogButtonClickListener<InputDialog> {
                                    override fun onClick(
                                        baseDialog: InputDialog?,
                                        v: View?,
                                        username: String?
                                    ): Boolean {
                                        if (username.isNullOrEmpty()) {
                                            TipDialog.show(
                                                AppContext.context.resources.getString(R.string.username_empty),
                                                WaitDialog.TYPE.WARNING,
                                                3000
                                            )
                                            return false
                                        }
                                        if (username.length < 2) {
                                            TipDialog.show(
                                                AppContext.context.resources.getString(R.string.username_short),
                                                WaitDialog.TYPE.WARNING,
                                                3000
                                            )
                                            return false
                                        }
                                        val compile = Pattern.compile("[0-9a-zA-z]+")
                                        val matcher = compile.matcher(username)
                                        val b = matcher.matches()

                                        if (!b) {
                                            TipDialog.show(
                                                AppContext.context.resources.getString(R.string.username_only_number_and_abc),
                                                WaitDialog.TYPE.WARNING,
                                                3000
                                            )
                                            return false
                                        }

                                        val passwordInputDialog = InputDialog(
                                            AppContext.context.resources.getString(R.string.next_step),
                                            AppContext.context.resources.getString(R.string.enter_password),
                                            AppContext.context.resources.getString(R.string.ok),
                                            AppContext.context.resources.getString(R.string.cancel),
                                            AppContext.context.resources.getString(R.string.reset_password),
                                            ""
                                        )
                                        passwordInputDialog.setCancelable(false)
                                        passwordInputDialog.setOkButton(object :
                                            OnInputDialogButtonClickListener<InputDialog> {
                                            override fun onClick(
                                                baseDialog: InputDialog?,
                                                v: View?,
                                                password: String?
                                            ): Boolean {
                                                if (password.isNullOrEmpty()) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_empty
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                if (password.length < 5) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_short
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                val compile2 = Pattern.compile("[0-9a-zA-z~!%&@]+")
                                                val matcher2 = compile2.matcher(password)
                                                val b2 = matcher2.matches()

                                                if (!b2) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_only_number_abc_and_some_char
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                Thread {
                                                    if (Verification.run(
                                                            "Login",
                                                            username,
                                                            password
                                                        )
                                                    ) {
                                                        val udata = FileBinder.getInstance()
                                                            .readString(GlobalVars.CONFIG_DIR + "/user.data")
                                                            .trim()
                                                        userData.value = udata
                                                        state.value = 1

                                                        UserUtils.userData = udata
                                                        UserUtils.state = 1
                                                    }
                                                }.start()
                                                return false
                                            }
                                        })
                                        passwordInputDialog.setOtherButton(object :
                                            OnInputDialogButtonClickListener<InputDialog> {
                                            override fun onClick(
                                                baseDialog: InputDialog?,
                                                v: View?,
                                                password: String?
                                            ): Boolean {
                                                if (password.isNullOrEmpty()) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_empty
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                if (password.length < 5) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_short
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                val compile2 = Pattern.compile("[0-9a-zA-z~!%&@]+")
                                                val matcher2 = compile2.matcher(password)
                                                val b2 = matcher2.matches()

                                                if (!b2) {
                                                    TipDialog.show(
                                                        AppContext.context.resources.getString(
                                                            R.string.password_only_number_abc_and_some_char
                                                        ), WaitDialog.TYPE.WARNING, 3000
                                                    )
                                                    return false
                                                }

                                                Thread {
                                                    Verification.run("Reset", username, password)
                                                }.start()
                                                return false
                                            }
                                        })
                                        passwordInputDialog.show()
                                        inputDialog.dismiss()
                                        return true
                                    }
                                })
                                inputDialog.show()
                            }
                        }
                    }
                )
            },
        insideMargin = PaddingValues(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 6.dp),
                imageVector = icon,
                tint = MiuixTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(start = 24.dp)) {
                Text(
                    text = account,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = info
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            //if (!isWeb()) LoginDialog(isLogin)
        }
    }
}