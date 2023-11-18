package hu.ait.shoppinglist.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.R.string
import hu.ait.shoppinglist.ui.data.Category
import hu.ait.shoppinglist.ui.data.ShoppingItem
import hu.ait.shoppinglist.ui.data.Status
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel = hiltViewModel(),
    onNavigateToSummary: (Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var showAddShoppingDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var shoppingToEdit: ShoppingItem? by rememberSaveable {
        mutableStateOf(null)
    }
    var selectedCategory by rememberSaveable {
        mutableStateOf(Category.FOOD)
    }

    Column {

        TopAppBar(
            title = {
                Text(stringResource(string.ali_s_shopping_list))
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    shoppingViewModel.clearAllShoppings()
                }) {
                    Icon(Icons.Filled.Delete, null)
                }
                IconButton(onClick = {
                    coroutineScope.launch {
                        val allShoppings = shoppingViewModel.getAllShoppingNum()
                        val boughtShoppings = shoppingViewModel.getBoughtShoppingNum()
                        onNavigateToSummary(
                            allShoppings,
                            boughtShoppings
                        )
                    }
                }) {
                    Icon(Icons.Filled.Info, null)
                }
                IconButton(onClick = {
                    shoppingToEdit = null
                    showAddShoppingDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = category
                    }
                )
            }
        )

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddShoppingDialog) {
                AddNewShoppingForm(
                    shoppingViewModel,
                    { showAddShoppingDialog = false },
                    shoppingToEdit
                )
            }
            val sortedShoppingList by shoppingViewModel.getShoppingListSortedByCategory(selectedCategory)
                .collectAsState(emptyList())

            if (sortedShoppingList.isEmpty()) {
                Text(text = stringResource(string.no_items))
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(sortedShoppingList) { shoppingItem ->
                        ShoppingCard(
                            shoppingItem = shoppingItem,
                            onRemoveItem = { shoppingViewModel.removeShoppingItem(shoppingItem) },
                            onShoppingCheckChange = { isChecked ->
                                val newStatus = if (isChecked) Status.BOUGHT else Status.NOT_BOUGHT
                                shoppingViewModel.changeShoppingState(shoppingItem, newStatus)
                            },
                            onEditItem = { editedShoppingItem ->
                                shoppingToEdit = editedShoppingItem
                                showAddShoppingDialog = true
                            },
                            onDeleteItem = {
                                shoppingViewModel.removeShoppingItem(shoppingItem)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddNewShoppingForm(
    shoppingViewModel: ShoppingViewModel,
    onDialogDismiss: () -> Unit = {},
    shoppingToEdit: ShoppingItem? = null
) {
    var shoppingTitle by rememberSaveable {
        mutableStateOf(shoppingToEdit?.name ?: "")
    }
    var shoppingDescription by rememberSaveable {
        mutableStateOf(shoppingToEdit?.description ?: "")
    }
    var shoppingEstimatedPrice by rememberSaveable {
        mutableStateOf(shoppingToEdit?.estimatedPrice?.toString() ?: "")
    }
    var shoppingCategory by rememberSaveable {
        mutableStateOf(shoppingToEdit?.category ?: Category.FOOD)
    }
    var shoppingStatus by rememberSaveable {
        mutableStateOf(shoppingToEdit?.status ?: Status.NOT_BOUGHT)
    }
    var showError by remember { mutableStateOf(false) }

    Column(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(10.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = shoppingTitle,
            onValueChange = {
                shoppingTitle = it
            },
            label = { Text(text = stringResource(string.enter_item_name)) }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = shoppingDescription,
            onValueChange = {
                shoppingDescription = it
            },
            label = { Text(text = stringResource(string.enter_item_description)) }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = shoppingEstimatedPrice,
            onValueChange = {
                shoppingEstimatedPrice = it
            },
            label = { Text(text = stringResource(string.enter_estimated_price)) }
        )
        CategoryDropdown(
            selectedCategory = shoppingCategory,
            onCategorySelected = { category ->
                shoppingCategory = category
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = shoppingStatus == Status.BOUGHT, onCheckedChange = {
                    shoppingStatus = if (it) Status.BOUGHT else Status.NOT_BOUGHT
                })
                Text(text = stringResource(string.bought))
            }
        }

        Row {
            Button(onClick = {
                if (shoppingTitle.isNotBlank() && shoppingDescription.isNotBlank() && shoppingEstimatedPrice.isNotBlank()) {
                    val estimatedPrice = shoppingEstimatedPrice.toDoubleOrNull() ?: 0.0
                    if (shoppingToEdit == null) {
                        shoppingViewModel.addShoppingList(
                            ShoppingItem(
                                0,
                                shoppingCategory,
                                shoppingTitle,
                                shoppingDescription,
                                estimatedPrice,
                                shoppingStatus
                            )
                        )
                    } else {
                        val shoppingEdited = shoppingToEdit.copy(
                            category = shoppingCategory,
                            name = shoppingTitle,
                            description = shoppingDescription,
                            estimatedPrice = estimatedPrice,
                            status = shoppingStatus
                        )
                        shoppingViewModel.editShoppingItem(shoppingEdited)
                    }

                    onDialogDismiss()
                } else {
                    // Show error message
                    showError = true
                }
            }) {
                Text(text = stringResource(string.save))
            }
        }

        if (showError) {
            Text(
                text = stringResource(string.please_fill_in_all_fields),
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ShoppingCard(
    shoppingItem: ShoppingItem,
    onShoppingCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (ShoppingItem) -> Unit = {},
    onDeleteItem: () -> Unit = {},
    onMoreInfo: () -> Unit = {}
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val cardColor = if (shoppingItem.status == Status.BOUGHT) Color.Green else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (shoppingItem.status == Status.BOUGHT) Color.Green else MaterialTheme.colorScheme.primary

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring()
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = shoppingItem.category.getIcon()),
                    contentDescription = stringResource(string.category),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 10.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(0.2f)
                ) {
                    Text(shoppingItem.name)
                    Text(
                        text = stringResource(
                            string.dollar,
                            shoppingItem.estimatedPrice,
                            shoppingItem.description
                        ),
                        style = TextStyle(
                            fontSize = 10.sp,
                        )
                    )
                }

                Spacer(modifier = Modifier.fillMaxSize(0.35f))
                Checkbox(
                    checked = shoppingItem.status == Status.BOUGHT,
                    onCheckedChange = { isChecked ->
                        onShoppingCheckChange(isChecked)
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { onDeleteItem() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(string.delete),
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { onEditItem(shoppingItem) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(string.edit),
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            stringResource(string.less)
                        } else {
                            stringResource(string.more)
                        }
                    )
                }
            }

            Divider()

            if (expanded) {
                Text(
                    text = stringResource(string.estimated_price, shoppingItem.estimatedPrice),
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
                Text(
                    text = stringResource(string.description, shoppingItem.description),
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val categoryList = Category.values().map { it.name }
    SpinnerSample(
        list = categoryList,
        preselected = selectedCategory.name,
        onSelectionChanged = { selectedCategoryName ->
            val selectedCategory = Category.valueOf(selectedCategoryName)
            onCategorySelected(selectedCategory)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    )
}