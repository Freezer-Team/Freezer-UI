package nep.timeline.freezerUI.ui.viewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import nep.timeline.freezerUI.GlobalVars
import nep.timeline.freezerUI.R
import nep.timeline.freezer.provide.FileBinder
import nep.timeline.freezerUI.ui.utils.AppContext
import nep.timeline.freezerUI.ui.utils.UserUtils
import nep.timeline.freezerUI.verification.Verification
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Rename
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.regex.Pattern

@Composable
fun LoginDialog(
    showDialog: MutableState<Boolean>,
    state: MutableState<Int>,
    userData: MutableState<String>
) {
    val focusManager = LocalFocusManager.current

    var username by remember(showDialog.value) { mutableStateOf("") }
    var password by remember(showDialog.value) { mutableStateOf("") }

    OverlayDialog(
        show = showDialog.value,
        title = stringResource(R.string.first_use_welcome),
        onDismissRequest = {
            showDialog.value = false
        },
        content = {
            Column {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.enter_username),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )

                var passwordVisibility by remember { mutableStateOf(false) }
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.enter_password),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.padding(end = 6.dp),
                            onClick = { passwordVisibility = !passwordVisibility },
                            content = {
                                Icon(
                                    imageVector = MiuixIcons.Rename,
                                    tint = if (passwordVisibility) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSecondaryContainer,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(
                    text = stringResource(R.string.cancel),
                    onClick = { showDialog.value = false },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = stringResource(R.string.ok),
                    onClick = {
                        showDialog.value = false

                        if (username.isEmpty()) {
                            TipDialog.show(
                                AppContext.context.resources.getString(R.string.username_empty),
                                WaitDialog.TYPE.WARNING,
                                3000
                            )
                            return@TextButton
                        }
                        if (username.length < 2) {
                            TipDialog.show(
                                AppContext.context.resources.getString(R.string.username_short),
                                WaitDialog.TYPE.WARNING,
                                3000
                            )
                            return@TextButton
                        }
                        val compile = Pattern.compile("[0-9a-zA-Z]+")
                        val matcher = compile.matcher(username)
                        val b = matcher.matches()

                        if (!b || username.contains(",")) {
                            TipDialog.show(
                                AppContext.context.resources.getString(R.string.username_only_number_and_abc),
                                WaitDialog.TYPE.WARNING,
                                3000
                            )
                            return@TextButton
                        }

                        if (password.isEmpty()) {
                            TipDialog.show(
                                AppContext.context.resources.getString(
                                    R.string.password_empty
                                ), WaitDialog.TYPE.WARNING, 3000
                            )
                            return@TextButton
                        }

                        if (password.length < 5) {
                            TipDialog.show(
                                AppContext.context.resources.getString(
                                    R.string.password_short
                                ), WaitDialog.TYPE.WARNING, 3000
                            )
                            return@TextButton
                        }

                        val compile2 = Pattern.compile("[0-9a-zA-Z~!%&@]+")
                        val matcher2 = compile2.matcher(password)
                        val b2 = matcher2.matches()

                        if (!b2 || password.contains(",")) {
                            TipDialog.show(
                                AppContext.context.resources.getString(
                                    R.string.password_only_number_abc_and_some_char
                                ), WaitDialog.TYPE.WARNING, 3000
                            )
                            return@TextButton
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
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                )
            }
        }
    )
}

@Composable
fun LoginCardView(
    modifier: Modifier = Modifier,
    state: MutableState<Int>,
    userData: MutableState<String>,
    colors: CardColors = CardDefaults.defaultColors()
) {
    val account = if (state.value == 0 || state.value == 1) userData.value.split("\n")[0] else stringResource(R.string.no_account)
    val info = if (state.value == 0) stringResource(R.string.welcome_back) else if (state.value == 1) stringResource(R.string.start_active) else stringResource(R.string.start_login)
    val icon = if (state.value == 0) Icons.Rounded.DoneAll else if (state.value == 1) Icons.Rounded.Done else Icons.Rounded.Info

    val showLoginDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 6.dp).then(modifier),
        colors = colors,
        onClick = {
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
                    showLoginDialog.value = true
                }
            }
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
                    fontWeight = FontWeight.SemiBold,
                    color = MiuixTheme.colorScheme.onSurface
                )
                Text(
                    text = info,
                    color = MiuixTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginDialog(showLoginDialog, state, userData)
        }
    }
}