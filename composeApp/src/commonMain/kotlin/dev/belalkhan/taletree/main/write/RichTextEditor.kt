package dev.belalkhan.taletree.main.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface RichEditorController {
    fun setHtml(html: String)
    fun getHtml(callback: (String) -> Unit)
    fun execCommand(command: String) // e.g. bold, italic
}

@Composable
expect fun RichTextEditor(
    modifier: Modifier = Modifier,
    onInitialized: (RichEditorController) -> Unit = {}
)
