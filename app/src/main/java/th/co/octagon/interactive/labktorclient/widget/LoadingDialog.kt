package th.co.octagon.interactive.labktorclient.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import th.co.octagon.interactive.labktorclient.R

object LoadingDialog {
    private var dialog: Dialog? = null
    fun displayLoadingWithText(context: Context?, text: String? = "") {

        hideLoading()

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_loading)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog!!.window!!.statusBarColor =
            ContextCompat.getColor(context, android.R.color.transparent)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog!!.setCancelable(false)

        try {
            dialog!!.show()
        } catch (_: Exception) { }
    }

    fun setCancelable(enable: Boolean){
        dialog!!.setCancelable(enable)
    }

    fun hideLoading() {
        try {
            if (dialog != null) {

                Handler(Looper.getMainLooper()).postDelayed({
                    dialog!!.dismiss()
                }, 1500)
            }
        } catch (_: Exception) { }
    }

    fun isShowing(): Boolean{
        return dialog?.isShowing ?: false
    }
}