package dev.belalkhan.taletree.main.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.UIKit.UIView
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun RichTextEditor(
    modifier: Modifier,
    onInitialized: (RichEditorController) -> Unit
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration()
            val webView = WKWebView(frame = CGRectZero.readValue(), configuration = config)
            val url = NSBundle.mainBundle.URLForResource("editor", "html")
            webView.loadFileURL(url!!, allowingReadAccessToURL = url)

            val controller = object : RichEditorController {
                override fun setHtml(html: String) {
                    webView.evaluateJavaScript("setHtml(`$html`)", completionHandler = null)
                }

                override fun getHtml(callback: (String) -> Unit) {
                    webView.evaluateJavaScript("getHtml()") { result, _ ->
                        callback(result?.toString() ?: "")
                    }
                }

                override fun execCommand(command: String) {
                    webView.evaluateJavaScript("execCommand('$command')", completionHandler = null)
                }
            }

            onInitialized(controller)
            webView as UIView
        }
    )
}