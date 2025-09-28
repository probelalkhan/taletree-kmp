package dev.belalkhan.taletree.main.write

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun RichTextEditor(
    modifier: Modifier,
    onInitialized: (RichEditorController) -> Unit
) {
    UIKitView(
        modifier = modifier.fillMaxSize(), // Use fillMaxSize or a specific size for debugging
        factory = {
            val config = WKWebViewConfiguration()
            val webView = WKWebView(frame = CGRectZero.readValue(), configuration = config)
            val url = NSBundle.mainBundle.URLForResource("editor", withExtension = "html")
            if (url != null) {
                webView.loadFileURL(
                    url,
                    allowingReadAccessToURL = url.URLByDeletingLastPathComponent()!!
                )
            } else {
                println("Warning: 'editor.html' not found in bundle. Loading a fallback string.")
                val fallbackHtml = "<h1>Hello, Fallback!</h1><p>editor.html was not loaded.</p>"
                webView.loadHTMLString(fallbackHtml, baseURL = null)
            }

            val controller = object : RichEditorController {
                override fun setHtml(html: String) {
                    val sanitizedHtml = html.replace("`", "\\`").replace("$", "\\$")
                    webView.evaluateJavaScript(
                        "setHtml(`$sanitizedHtml`)",
                        completionHandler = null
                    )
                }

                override fun getHtml(callback: (String) -> Unit) {
                    webView.evaluateJavaScript("getHtml()") { result, error ->
                        if (error != null) {
                            println("getHtml JS error: ${error.localizedDescription}")
                            callback("")
                        } else {
                            callback(result as? String ?: "")
                        }
                    }
                }

                override fun execCommand(command: String) {
                    webView.evaluateJavaScript("execCommand('$command')", completionHandler = null)
                }
            }

            onInitialized(controller)
            webView
        }
    )
}
