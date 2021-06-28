package ca.nkrishnaswamy.picshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ca.nkrishnaswamy.picshare.viewmodels.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var emailEnterText: EditText
    lateinit var passwordEnterText: EditText
    lateinit var errorMessageTV : TextView
    lateinit var loginButton : MaterialButton
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        emailEnterText = findViewById(R.id.emailEnter)
        passwordEnterText = findViewById(R.id.passwordEnter)

        errorMessageTV = findViewById(R.id.errorMessage)

        loginButton = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val email: String = emailEnterText.text.toString()
            val password: String = passwordEnterText.text.toString()
            if ((TextUtils.isEmpty(emailEnterText.text)) || (TextUtils.isEmpty(passwordEnterText.text))){
                errorMessageTV.text = "Email and/or Password are Empty"
            }
            else if (!checkFormatOfEmail(email)){
                errorMessageTV.text = "Email Format is Invalid"
            }
            else{
                CoroutineScope(Dispatchers.IO).launch{
                    val check: Boolean = authViewModel.loginWithEmailAndPassword(email, password)
                    val currentFirebaseUser = authViewModel.getCurrentSignedOnUser() as FirebaseUser
                    if (!check){
                        withContext(Dispatchers.Main){
                            errorMessageTV.text = "Email and/or Password are Incorrect"
                        }
                    }
                    else{
                        errorMessageTV.text=""
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("currentSignedInFirebaseUser", currentFirebaseUser)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun checkFormatOfEmail(email: String): Boolean{
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun goToSignUpPage(view: View){
        val signUpIntent: Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(signUpIntent)
    }

    fun forgotLogin(view: View){
        val forgotLoginIntent: Intent = Intent(this@LoginActivity, FindAccountActivity::class.java)
        startActivity(forgotLoginIntent)
    }
}