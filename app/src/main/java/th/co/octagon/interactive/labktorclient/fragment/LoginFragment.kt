package th.co.octagon.interactive.labktorclient.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import th.co.octagon.interactive.labktorclient.activity.MainActivity
import th.co.octagon.interactive.labktorclient.data.remote.Either
import th.co.octagon.interactive.labktorclient.data.remote.PostsService
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostRequest
import th.co.octagon.interactive.labktorclient.data.storage.UserDataStore
import th.co.octagon.interactive.labktorclient.databinding.FragmentTestApiCallerBinding
import th.co.octagon.interactive.labktorclient.fragmentInterface.TestApiCallerFragmentInterface
import th.co.octagon.interactive.labktorclient.widget.LoadingDialog
import th.co.octagon.interactive.labktorclient.widget.OneButtonDialog

class LoginFragment : Fragment(), TestApiCallerFragmentInterface {

    private var mBinding: FragmentTestApiCallerBinding? = null
    private val binding get() = mBinding!!

    private val args: LoginFragmentArgs by navArgs()

    private val service = PostsService.create()
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTestApiCallerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.fragmentInterface = this

        val color = ContextCompat.getColor(requireContext(), androidx.appcompat.R.color.material_blue_grey_800)
        val iconDrawable = binding.ivLogo.drawable.mutate()
        iconDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        binding.ivLogo.setImageDrawable(iconDrawable)

//        try {
//            val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
//            binding.tvAppVersion.text = "Version. ${packageInfo.versionName}"
//        } catch (_: Exception) {}

        binding.editTextTextPassword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                LoadingDialog.displayLoadingWithText(requireContext(), "กำลังโหลด")

                val username = binding.editTextText.text.toString()
                val password = binding.editTextTextPassword.text.toString()

                userLogin(username = username, password = password)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.btnBackToMain.setOnClickListener {

            LoadingDialog.displayLoadingWithText(requireContext(), "กำลังโหลด")

            val username = binding.editTextText.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            userLogin(username = username, password = password)
        }
    }

    private fun userLogin(username: String, password: String) {
        coroutineScope.launch {
            val createPost = PostRequest(
                username = username,
                password = password
            )

            when (val posts = service.createPost(createPost)) {
                is Either.OnSuccess -> {
                    val callBack = posts.value
                    Toast.makeText(requireContext(), callBack.token, Toast.LENGTH_SHORT).show()
                    LoadingDialog.hideLoading()

                    UserDataStore.saveUserCredentials(requireContext(), createPost.username, createPost.password)

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
                is Either.OnFails -> {
                    val callBack = posts.value

                    Log.e("OnFails", callBack.status.toString())

                    try {
                        val json = Json.parseToJsonElement(callBack.message)

                        if (json is JsonObject) {
                            val messageJson = json["message"]
                            if (messageJson is JsonPrimitive) {
                                val message = messageJson.content
                                println("Message: $message")
                                OneButtonDialog(requireContext(), message, {
                                    println("Hide Dialog")
                                }).show()
                            } else {
                                println("Error: 'message' field is not a primitive type")
                            }
                        } else {
                            println("Error: Invalid JSON format")
                        }
                        LoadingDialog.hideLoading()
                    } catch (e: Exception) {
                        println("Error parsing JSON: ${e.message}")
                        LoadingDialog.hideLoading()
                    }
                }
            }

        }
    }
}