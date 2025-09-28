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
            // 1. Create the web view configuration and the web view
            val config = WKWebViewConfiguration()
            val webView = WKWebView(frame = CGRectZero.readValue(), configuration = config)

            // --- FIX IS APPLIED BELOW ---

            // 2. Get the URL for your local HTML file from the main app bundle.
            //    Ensure 'editor.html' is added to your Xcode project's "Copy Bundle Resources" build phase.
            val url = NSBundle.mainBundle.URLForResource("editor", withExtension = "html")

            // 3. Load the local HTML file using loadFileURL for better security and resource access.
            //    Give it read access to the directory containing the file.
            if (url != null) {
                webView.loadFileURL(url, allowingReadAccessToURL = url.URLByDeletingLastPathComponent()!!)
            } else {
                // Fallback or error logging if editor.html is not found.
                // For now, load a simple string with a dummy base URL.
                println("Warning: 'editor.html' not found in bundle. Loading a fallback string.")
                val fallbackHtml = "<h1>Hello, Fallback!</h1><p>editor.html was not loaded.</p>"
                webView.loadHTMLString(fallbackHtml, baseURL = null)
            }

            // --- END OF FIX ---

            // The controller logic remains the same
            val controller = object : RichEditorController {
                override fun setHtml(html: String) {
                    // Important: Make sure your JS functions like `setHtml` and `getHtml`
                    // are defined inside your editor.html file.
                    val sanitizedHtml = html.replace("`", "\\`").replace("$", "\\$")
                    webView.evaluateJavaScript("setHtml(`$sanitizedHtml`)", completionHandler = null)
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
            webView // Return the configured webView
        }
    )
}
