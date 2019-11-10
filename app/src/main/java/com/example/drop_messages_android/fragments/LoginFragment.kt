package com.example.drop_messages_android.fragments


import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.example.drop_messages_android.R
import com.example.drop_messages_android.ValidatorHelper
import com.example.drop_messages_android.api.InvalidLoginResponseModel
import kotlinx.android.synthetic.main.card_login_fragment.*
import kotlinx.android.synthetic.main.card_login_fragment.view.*

class LoginFragment : Fragment() {
    var whiteBoxAnimator : ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.card_login_fragment, container, false) as ViewGroup
        val listener = activity as LoginUserListener

        // Signup button
        rootView.btn_signup.setOnClickListener {
            println("sign up listener called!")
            listener.navToSignUp()
        }

        // signin button
        rootView.btn_signin.setOnClickListener {
            val email = tv_input_email.editText!!.text.toString()
            val password = tv_input_password.editText!!.text.toString()

            var validated = true

            // email validation
            if (email.isBlank()) {
                tv_input_email.error = "Must not be blank"
                tv_input_email.requestFocus()
                validated = false
            }
            if (!ValidatorHelper.isValidEmail(email)) {
                tv_input_email.error = "Must be a valid email"
                tv_input_email.requestFocus()
                validated = false
            }

            //password validation
            if (password.isBlank()) {
                tv_input_password.error = "Must not be blank"
                tv_input_password.requestFocus()
                validated = false
            }
            if (!ValidatorHelper.isValidPassword(password)) {
                tv_input_password.error = "must be 8 or more chars"
                tv_input_password.requestFocus()
                validated = false
            }

            //fields are validated
            if (validated) {
                val b = Bundle()
                b.putString("email", email)
                b.putString("password", password)

                startProgressBar()
                listener.onSignIn(b, ::errorListener)
            }
        }

        rootView.btn_forgot_pw.setOnClickListener {
            listener.onForgotPassword()
        }

        return rootView
    }

    private fun startProgressBar() {
        progress_container.visibility= View.VISIBLE
        btn_signin.visibility = View.GONE

        //loading animation for Loading text dots
        var slideX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, 90f)
        whiteBoxAnimator = ObjectAnimator.ofPropertyValuesHolder(img_white_cover, slideX).apply {
            interpolator = LinearInterpolator()
            duration = 2000
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ObjectAnimator.INFINITE
        }
        whiteBoxAnimator!!.start()
    }

    private fun stopProgressBar() {
        progress_container.visibility= View.GONE
        btn_signin.visibility = View.VISIBLE
        if (whiteBoxAnimator != null)
            whiteBoxAnimator!!.end()
    }

    // error listener for invalid sign in e.g. invalid username/password
    // callback for userfront activity
    fun errorListener(error: String) {
        tv_signup_error.text = error

        // reset UI back
        stopProgressBar()
        btn_signin.visibility = View.VISIBLE
    }

    interface LoginUserListener {
        fun onSignIn(bundle: Bundle, errorListener: (err: String) -> Unit)
        fun navToSignUp()
        fun onForgotPassword()
    }
}