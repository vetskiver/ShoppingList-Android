package hu.ait.shoppinglist.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.shoppinglist.ui.data.Category
import hu.ait.shoppinglist.ui.data.ShoppingDAO
import hu.ait.shoppinglist.ui.data.ShoppingItem
import hu.ait.shoppinglist.ui.data.ShoppingRepository
import hu.ait.shoppinglist.ui.data.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingDAO: ShoppingDAO,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    fun getAllShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllShoppings()
    }

    suspend fun getAllShoppingNum(): Int {
        return shoppingDAO.getShoppingsNum()
    }

    suspend fun getBoughtShoppingNum(): Int {
        return shoppingDAO.getBoughtShoppingsNum()
    }

    fun addShoppingList(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.insert(shoppingItem)
        }
    }

    fun removeShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.delete(shoppingItem)
        }
    }

    fun editShoppingItem(editedShopping: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.update(editedShopping)
        }
    }

    fun changeShoppingState(shoppingItem: ShoppingItem, status: Status) {
        val newShoppingItem = shoppingItem.copy(status = status)
        viewModelScope.launch {
            shoppingDAO.update(newShoppingItem)
        }
    }

    fun clearAllShoppings() {
        viewModelScope.launch {
            shoppingDAO.deleteAllShoppings()
        }
    }

    fun getShoppingListSortedByCategory(category: Category): Flow<List<ShoppingItem>> {
        return shoppingRepository.getShoppingListSortedByCategory(category)
    }
}