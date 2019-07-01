# QuotesandWallpaperApp
Android App with Firebase to save image and text
### BottomNavigationKotlin
Basic Bottom Navigation with Kotlin.

**Screenshoot**

<table>
    <tr>
        <td><img src="https://github.com/ridwanharts/BottomNavigationKotlin/blob/master/app/src/main/res/screenshoot/Screenshot_2019-07-01-11-02-38.png"></td>
        <td><img src="https://github.com/ridwanharts/BottomNavigationKotlin/blob/master/app/src/main/res/screenshoot/Screenshot_2019-07-01-11-02-49.png"></td>
        <td><img src="https://github.com/ridwanharts/BottomNavigationKotlin/blob/master/app/src/main/res/screenshoot/Screenshot_2019-07-01-11-02-44.png"></td>
    </tr>
</table>

**MainActivity.kt**
```kotlin
package com.labs.jangkriek.bottomnavigationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.mainFragment)

        bottomNav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}

```
