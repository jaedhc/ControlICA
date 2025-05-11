package com.example.controlica.presentation.components.common.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Popup


@Composable
fun <T> CustomDropdown(
    p_items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Selecciona un valor",
    itemToText: (T) -> String,
    dropdownWidth: Dp = 250.dp,
    maxDropdownHeight: Dp = 300.dp
) {
    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val triggerSize = remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier) {

        // Trigger
        Row(
            modifier = Modifier
                .onGloballyPositioned {
                    triggerSize.value = it.size
                }
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedItem?.let { itemToText(it) } ?: placeholder,
                color = if (selectedItem == null) Color.Gray else Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expandir menú",
                tint = Color.Black
            )
        }

        // Popup
        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                offset = with(density) { IntOffset(0, triggerSize.value.height.toDp().roundToPx()) },
                onDismissRequest = { expanded = false }
            ) {
                Surface(
                    modifier = Modifier
                        .width(dropdownWidth)
                        .heightIn(max = maxDropdownHeight)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(Color.White),
                    shape = RoundedCornerShape(4.dp),
                    tonalElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        p_items.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF9F9F9))
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemSelected(item)
                                        expanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = itemToText(item),
                                    color = if (selectedItem == item) Color(0xFF4B73CC) else Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




