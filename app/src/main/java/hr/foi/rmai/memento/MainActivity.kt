package hr.foi.rmai.memento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rmai.memento.adapters.MainPagerAdapter
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.MockDataLoader
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var navDrawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabs)
        viewPager2 = findViewById(R.id.viewpager)
        navDrawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)

        val mainPagerAdapter = MainPagerAdapter(
            supportFragmentManager,
            lifecycle
        )

        viewPager2.adapter = mainPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(
                mainPagerAdapter.fragmentItems[position].titleRes
            )
            tab.setIcon(
                mainPagerAdapter.fragmentItems[position].iconRes
            )
        }.attach()

        mainPagerAdapter.fragmentItems.withIndex().forEach { (index, fragmentItem) ->
            navView.menu
                .add(fragmentItem.titleRes)
                .setIcon(fragmentItem.iconRes)
                .setCheckable(true)
                .setChecked((index == 0))
                .setOnMenuItemClickListener {
                    viewPager2.setCurrentItem(index, true)
                    navDrawerLayout.closeDrawers()
                    return@setOnMenuItemClickListener true
                }
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navView.menu[position].isChecked = true
            }
        })

        TasksDatabase.buildInstance(applicationContext)
        MockDataLoader.loadMockData()

        val channel = NotificationChannel(
            "task-timer", "Task Timer Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}





