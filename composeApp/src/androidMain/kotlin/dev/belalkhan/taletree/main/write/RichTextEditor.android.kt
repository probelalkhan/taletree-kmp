package dev.belalkhan.taletree.main.write

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun RichTextEditor(
    modifier: Modifier,
    onInitialized: (RichEditorController) -> Unit
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val webViewHeightPx = with(density) { maxHeight.toPx() }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.allowUniversalAccessFromFileURLs = true

                    webViewClient = WebViewClient()

                    // Load your HTML editor
                    loadUrl("file:///android_asset/editor.html")

                    // Initialize controller
                    val controller = object : RichEditorController {
                        override fun setHtml(html: String) {
                            post { evaluateJavascript("setHtml(${html.quoteJsString()})", null) }
                        }

                        override fun getHtml(callback: (String) -> Unit) {
                            evaluateJavascript("getHtml()") { result ->
                                callback(result?.trim('"') ?: "")
                            }
                        }

                        override fun execCommand(command: String) {
                            post { evaluateJavascript("execCommand('$command')", null) }
                        }
                    }

                    onInitialized(controller)
                }
            },
            update = { webView ->
                // Send the measured height to JS after layout
                webView.evaluateJavascript(
                    """
                    if (typeof setEditorHeight === 'function') {
                        setEditorHeight($webViewHeightPx);
                    }
                    """.trimIndent(),
                    null
                )
            }
        )
    }
}

private fun String.quoteJsString(): String =
    replace("\"", "\\\"").replace("\n", "\\n")
