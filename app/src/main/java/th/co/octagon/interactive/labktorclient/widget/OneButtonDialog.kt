package th.co.octagon.interactive.labktorclient.widget

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import th.co.octagon.interactive.labktorclient.databinding.DialogOneButtonBinding
import java.util.concurrent.TimeUnit

class OneButtonDialog(
    mContext: Context,
    private val message: String,
    private val onClicked: () -> Unit = {},
    private val onTimeOut: ()-> Unit = {}
) : Dialog(mContext) {

    private var timer: CountDownTimer? = null
    private var binding: DialogOneButtonBinding

    init {
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding = DialogOneButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor =
            ContextCompat.getColor(mContext, android.R.color.transparent)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        setText()
        setCancelable(false)
        setOnClickListener()
        autoHideDialogs()
    }

    private fun setText() {
        binding.txvMessage.text = message
    }

    private fun setOnClickListener() {
        binding.btnDialogOneButton.setOnClickListener {
            onClicked.invoke()
            dismiss()
        }
    }

    private fun autoHideDialogs(){

        timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.tvCountdown.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
            }
            override fun onFinish() {
                onTimeOut.invoke()
                dismiss()
            }
        }.start()
    }
}

