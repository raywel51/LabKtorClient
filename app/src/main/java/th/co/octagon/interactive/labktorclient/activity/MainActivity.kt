package th.co.octagon.interactive.labktorclient.activity

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import th.co.octagon.interactive.labktorclient.R
import th.co.octagon.interactive.labktorclient.databinding.ActivityMain2Binding
import th.co.octagon.interactive.labktorclient.fragment.LoginFragment
import th.co.octagon.interactive.labktorclient.fragment.MenuHomeFragment

//            // Set bottomNavigation visibility
//            binding.bottomNavigation.visibility = if (item.itemId == R.id.setting) View.GONE else View.VISIBLE

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = resources.getString(R.string.app_name)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.home -> MenuHomeFragment()
                R.id.privilege -> MenuHomeFragment()
                R.id.notify -> MenuHomeFragment()
                R.id.menu -> MenuHomeFragment()
                else -> return@setOnItemSelectedListener false
            }

            replaceFragment(selectedFragment)

            true
        }

        binding.bottomNavigation.selectedItemId = R.id.home

        onBackPressedDispatcher.addCallback(this) {}
    }


    private fun getColoredNavigationIcon(@DrawableRes iconId: Int, @ColorRes colorId: Int): Drawable? {
        val drawable = AppCompatResources.getDrawable(this, iconId)
        val color = ContextCompat.getColor(this, colorId)
        drawable?.mutate()?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return drawable
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }
    }
}