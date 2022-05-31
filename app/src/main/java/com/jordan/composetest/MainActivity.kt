package com.jordan.composetest

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.jordan.composetest.ui.theme.ComposeTestTheme
import moe.tlaster.zoomable.Zoomable
import moe.tlaster.zoomable.rememberZoomableState
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Test()
                }
            }
        }
    }
}
val Int.Dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


@Composable
fun Greeting(name: String) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        var scale by remember { mutableStateOf(1f) }
        val transformState = rememberTransformableState { zoomChange, _, _ ->
            scale = (zoomChange * scale).coerceAtLeast(1f)
        }

        val x = 5.Dp

        Text(text = x.toString())
        /*Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX = when {
                            offsetX < 0f -> 0f
                            else -> offsetX + dragAmount.x
                        }
                        offsetY = when {
                            offsetY < 0f -> 0f
                            else -> offsetY + dragAmount.y
                        }
                    }
                }
        )*/
        Image(
            painter = painterResource(id = R.drawable.taeyeon),
            contentDescription = "news image",
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, zoom, _ ->
                            scale = when {
                                scale < 1f -> 1f
                                scale > 3f -> 3f
                                else -> scale * zoom
                            }
                            offsetX = when {
                                pan.x > 30f -> 30f
                                else -> offsetX + pan.x
                            }
                            offsetY = when {
                                pan.y > 30f -> 30f
                                else -> offsetY + pan.y
                            }
                        }
                    )
                }
        )
    }
}

@Composable
fun Test() {
    Text(text = "Experimental branch")
}