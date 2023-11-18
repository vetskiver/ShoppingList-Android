package hu.ait.shoppinglist.ui.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRepository @Inject constructor(
    private val shoppingDAO: ShoppingDAO
) {

    fun getAllShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllShoppings()
    }

    suspend fun getAllShoppingNum(): Int {
        return shoppingDAO.getShoppingsNum()
    }

    suspend fun getBoughtShoppingNum(): Int {
        return shoppingDAO.getBoughtShoppingsNum()
    }

    suspend fun addShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.insert(shoppingItem)
    }

    suspend fun removeShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.delete(shoppingItem)
    }

    suspend fun editShoppingItem(editedShopping: ShoppingItem) {
        shoppingDAO.update(editedShopping)
    }

    suspend fun changeShoppingState(shoppingItem: ShoppingItem, status: Status) {
        val newShoppingItem = shoppingItem.copy(status = status)
        shoppingDAO.update(newShoppingItem)
    }

    suspend fun clearAllShoppings() {
        shoppingDAO.deleteAllShoppings()
    }

    fun getShoppingListSortedByCategory(category: Category): Flow<List<ShoppingItem>> {
        return shoppingDAO.getShoppingListSortedByCategory(category)
    }
}
