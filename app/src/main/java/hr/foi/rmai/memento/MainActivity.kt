package hr.foi.rmai.memento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.preference.PreferenceManager
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val settingsLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_LANG_CHANGED) {
                recreate()
            }
        }
    lateinit var onSharedPreferencesListener: SharedPreferences.OnSharedPreferenceChangeListener
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var navDrawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(applyUserSettings(newBase))
    }

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

        val tasksCounterItem = navView.menu
            .add(2, 0, 0, "")
            .setEnabled(false)

        attachMenuItemToTasksCreatedCount(tasksCounterItem)

        navView.menu
            .add(3, 0, 0, "Settings")
            .setIcon(R.drawable.outline_dashboard_2_gear_24)
            .setOnMenuItemClickListener {
                settingsLauncher.launch(Intent(
                    this, PreferencesActivity::class.java
                ))
                navDrawerLayout.closeDrawers()

                return@setOnMenuItemClickListener true
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

    private fun getTasksCreatedCounter(): Int {
        val sharedPreferences = getSharedPreferences(
            "tasks_preferences",
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getInt("tasks_created_counter", 0)
    }

    private fun attachMenuItemToTasksCreatedCount(tasksCounterItem: MenuItem) {
        val sharedPreferences = getSharedPreferences(
            "tasks_preferences", Context.MODE_PRIVATE
        )
        onSharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "tasks_created_counter") {
                updateTasksCreatedCounter(tasksCounterItem, sharedPreferences)
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesListener)
        updateTasksCreatedCounter(tasksCounterItem, sharedPreferences)
    }

    private fun updateTasksCreatedCounter(
        tasksCreatedItem: MenuItem,
        sharedPreferences: SharedPreferences
    ) {
        val counter = sharedPreferences.getInt("tasks_created_counter", 0)
        tasksCreatedItem.title = "Tasks created: $counter"
    }

    private fun applyUserSettings(newContext: Context?): Context? {
        if (newContext == null) return newContext
        PreferenceManager.getDefaultSharedPreferences(newContext)?.let { pref ->
            PreferencesActivity.switchDarkMode(
                pref.getBoolean("preference_dark_mode", false)
            )

            val lang = pref.getString("preference_language", "EN")
            if (lang != null) {
                val locale = Locale(lang)
                if (newContext.resources.configuration.locales[0].language !=
                locale.language) {
                    newContext.resources.configuration.setLocale(locale)
                    Locale.setDefault(locale)

                    return newContext.createConfigurationContext(
                        newContext.resources.configuration
                    )
                }
            }
        }
        return newContext
    }
}





