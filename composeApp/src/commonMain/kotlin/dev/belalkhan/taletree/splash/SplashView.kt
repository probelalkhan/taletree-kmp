package dev.belalkhan.taletree.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import taletree.composeapp.generated.resources.Res
import taletree.composeapp.generated.resources.logo_tale_tree

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(Res.drawable.logo_tale_tree),
                contentDescription = "Tale Tree",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3-dot animated progress
            DotsLoading()
        }
    }
}

@Composable
private fun DotsLoading(
    dotCount: Int = 3,
    dotSize: Int = 12,
    animationDuration: Int = 300
) {
    var dotIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            dotIndex = (dotIndex + 1) % (dotCount + 1)
            kotlinx.coroutines.delay(animationDuration.toLong())
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until dotCount) {
            val alpha = if (i < dotIndex) 1f else 0.3f
            Box(
                modifier = Modifier
                    .size(dotSize.dp)
                    .alpha(alpha)
            ) {
                Text(
                    text = "•",
                    fontSize = dotSize.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}