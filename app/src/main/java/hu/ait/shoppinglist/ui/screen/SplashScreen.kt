package hu.ait.shoppinglist.ui.screen

import android.os.Handler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.R.string

@Composable
fun SplashScreen(onNavigateToShoppingList: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFEB3B)
    ) {
        Text(
            text = stringResource(R.string.ali_s_shopping_list),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
    }
    Handler().postDelayed({
        onNavigateToShoppingList()
    }, 3000)
}