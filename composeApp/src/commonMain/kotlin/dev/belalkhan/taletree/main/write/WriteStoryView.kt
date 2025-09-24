package dev.belalkhan.taletree.main.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WriteStoryView(
    onSaveDraft: (String, String) -> Unit,
    onPublish: (String, String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var editorController: RichEditorController? by remember { mutableStateOf(null) }

    Scaffold(
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
            ) {
                OutlinedButton(
                    onClick = {
                        editorController?.getHtml { html ->
                            onSaveDraft(title, html)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save Draft") }

                Button(
                    onClick = {
                        editorController?.getHtml { html ->
                            onPublish(title, html)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Publish") }
            }
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter title…") },
                textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            RichTextEditor(
                modifier = Modifier.weight(1f),
                onInitialized = { controller -> editorController = controller }
            )
        }
    }
}
