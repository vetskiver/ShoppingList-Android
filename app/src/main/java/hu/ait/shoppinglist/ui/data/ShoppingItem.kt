package hu.ait.shoppinglist.ui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "estimatedprice") var estimatedPrice: Double,
    @ColumnInfo(name = "status") var status: Status,
) : Serializable

enum class Category {
    FOOD {
        override fun getIcon(): Int = R.drawable.food_icon
    },
    ELECTRONIC {
        override fun getIcon(): Int = R.drawable.electronic_icon
    },
    BOOK {
        override fun getIcon(): Int = R.drawable.book_icon
    };

    abstract fun getIcon(): Int

    fun getCategoryIcon(): Int {
        return this.getIcon()
    }
}

enum class Status {
    BOUGHT,
    NOT_BOUGHT;

    fun getStatusIcon(): Int {
        return if (this == Status.BOUGHT) R.drawable.bought else R.drawable.not_bought
    }
}
