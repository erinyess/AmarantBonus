// CustomNumberPicker.kt
package com.example.amarantbonuscompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun CustomNumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = value - range.first)

    Box(
        modifier = Modifier
            .height(100.dp)
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 30.dp)
        ) {
            itemsIndexed(range.toList()) { index, item ->
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                            onValueChange(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString(),
                        fontSize = 20.sp,
                        color = if (item == value) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }

        // Полупрозрачные области сверху и снизу
        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f))
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f))
                .align(Alignment.BottomCenter)
        )

        // Линии или индикаторы для выделения текущего элемента (опционально)
        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
                .background(Color.Transparent)
        )
    }
}
