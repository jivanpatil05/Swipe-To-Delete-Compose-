package com.example.swipetodeletecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.swipetodeletecompose.ui.theme.SwipeToDeleteComposeTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeToDeleteComposeTheme {
                val breakfastItemList = remember {
                    mutableStateListOf(
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                        BreakfastDataModel(
                            "BREAKFAST",
                            isRevealed = false),
                    )
                }
                Column {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        itemsIndexed(items = breakfastItemList) { index, item ->
                            SelectedFoodItem(
                                isReaveled = item.isRevealed,
                                icons = {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    IconButton(
                                        onClick = {
                                            breakfastItemList.removeAt(index)
                                        }, modifier = Modifier
                                            .width(74.dp)
                                            .height(63.dp)
                                            .background(Color.Gray)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_delete_png),
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                onExpanded = {

                                },
                                onCollapsed = {

                                },
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 15.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.width(17.dp))
                                        Column(verticalArrangement = Arrangement.Center) {
                                            Text(text = item.Title, color = Color.Black)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun SelectedFoodItem(
    isReaveled: Boolean,
    icons: @Composable RowScope.() -> Unit,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    content: @Composable () -> Unit,
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    var offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isReaveled, contextMenuWidth) {
        if (isReaveled) {
            offset.animateTo(
                targetValue = -contextMenuWidth,
                animationSpec = tween(durationMillis = 100)
            )
        } else {
            offset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 100)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(IntrinsicSize.Min),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            icons()
        }
        Surface(modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(offset.value.roundToInt(), 0)
            }
            .pointerInput(contextMenuWidth) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            val newOffset = offset.value + dragAmount
                            when {
                                newOffset > 0f -> {
                                    offset.snapTo(0f)
                                }
                                newOffset < -contextMenuWidth -> {
                                    offset.snapTo(-contextMenuWidth)
                                }

                                else -> {
                                    offset.snapTo(newOffset)
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        when {
                            offset.value <= -contextMenuWidth / 2 -> {
                                scope.launch {
                                    offset.animateTo(
                                        targetValue = -contextMenuWidth,
                                        animationSpec = tween(durationMillis = 100)
                                    )
                                    onExpanded()
                                }
                            }

                            else -> {
                                scope.launch {
                                    offset.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(durationMillis = 100)
                                    )
                                    onCollapsed()
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            offset.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 100)
                            )
                        }
                    }
                )
            }) {
            content.invoke()

        }
    }
}

data class BreakfastDataModel(
    val Title: String,
    val isRevealed: Boolean,
)