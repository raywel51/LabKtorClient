package th.co.octagon.interactive.labktorclient.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import th.co.octagon.interactive.labktorclient.R
import th.co.octagon.interactive.labktorclient.activity.MainActivity
import th.co.octagon.interactive.labktorclient.data.remote.Either
import th.co.octagon.interactive.labktorclient.data.remote.PostsService
import th.co.octagon.interactive.labktorclient.data.remote.dto.PostRequest
import th.co.octagon.interactive.labktorclient.data.storage.UserDataStore
import th.co.octagon.interactive.labktorclient.databinding.FragmentMainBinding
import th.co.octagon.interactive.labktorclient.fragmentInterface.MainFragmentInterface
import th.co.octagon.interactive.labktorclient.widget.LoadingDialog
import th.co.octagon.interactive.labktorclient.widget.OneButtonDialog

class SplashFragment : Fragment(), MainFragmentInterface {

    private var mBinding: FragmentMainBinding? = null
    private val binding get() = mBinding!!

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val service = PostsService.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView(){
        binding.fragmentInterface = this

        coroutineScope.launch {
            val userCredentialsFlow = UserDataStore.getUserCredentialsFlow(requireContext())
            userCredentialsFlow.collect { userCredentials ->
                println("Username: ${userCredentials.username}, Password: ${userCredentials.password}")

                userLogin(userCredentials.username ?: "",userCredentials.password ?: "")

            }
        }
    }

    override fun onCLickScreen() {

    }

    private fun loginView() {

        val action = SplashFragmentDirections.actionCardReceiverFragmentToPaymentFragment(
            qrKey = ""
        )

        val currentFragment = findNavController().currentDestination
        if (currentFragment?.id == R.id.fragment_main) {
            findNavController().navigate(action)
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
                    loginView()
                }
            }

        }
    }
}