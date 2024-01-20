package th.co.octagon.interactive.labktorclient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import th.co.octagon.interactive.labktorclient.R
import th.co.octagon.interactive.labktorclient.data.storage.UserDataStore
import th.co.octagon.interactive.labktorclient.databinding.ActivityMainBinding
import th.co.octagon.interactive.labktorclient.helpers.LocaleHelper

class LoaderActivity : AppCompatActivity() {
    private var mContext = this
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope.launch {
            val getLangFlow = UserDataStore.getLangFlow(mContext)
            getLangFlow.collect { lang ->
                if (lang == "th") {
                    changeButtonLang(true)
                    LocaleHelper.setLocale(mContext, "th")

                } else {
                    changeButtonLang(false)
                }
            }
        }

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//        navController?.navigate(R.id.fragment_main)

        binding.incToolbar.btnLangEng.setOnClickListener {
            changeButtonLang(false)
            LocaleHelper.setLocale(mContext, "en")
            recreate()
        }

        binding.incToolbar.btnLangThai.setOnClickListener {
            changeButtonLang(true)
            LocaleHelper.setLocale(mContext, "th")
            recreate()
        }

//        val windowInsetsController =
//            WindowCompat.getInsetsController(window, window.decorView)
//        windowInsetsController.systemBarsBehavior =
//            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
//            view.onApplyWindowInsets(windowInsets)
//        }
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun changeButtonLang(select: Boolean) {
        coroutineScope.launch {
            val lang = if (select) {
                binding.incToolbar.btnLangThai.setTextColor(ContextCompat.getColor(mContext, R.color.text_color))
                binding.incToolbar.btnLangEng.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_disable))
                "th"
            } else {
                binding.incToolbar.btnLangThai.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_disable))
                binding.incToolbar.btnLangEng.setTextColor(ContextCompat.getColor(mContext, R.color.text_color))
                "en"
            }

            UserDataStore.saveLang(mContext, lang)
            return@launch
        }
    }
}