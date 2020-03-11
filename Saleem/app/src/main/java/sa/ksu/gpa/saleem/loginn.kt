package sa.ksu.gpa.saleem

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_loginn.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.view.*
import sa.ksu.gpa.saleem.register.registerOneActivity
import java.util.HashMap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Html
import android.text.SpannableString


class loginn : AppCompatActivity() {

    private val TAG = "Login"
    private lateinit var auth: FirebaseAuth
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val verify = false
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginn)

        val login_password = findViewById<View>(R.id.passwordEDL) as EditText?
        val login_email = findViewById<View>(R.id.emailETL) as EditText?

        val  textView = findViewById<View>(R.id. signUpBtn ) as TextView?
        textView?.setText(Html.fromHtml("<u>انشاء حساب</u>"));

        val  forgetPas = findViewById<View>(R.id. forgetPass ) as TextView?
        forgetPas?.setText(Html.fromHtml("<u>نسيت كلمة المرور</u>"));


        auth = FirebaseAuth.getInstance()

        val mFirebaseUser = auth.getCurrentUser()
        if (mFirebaseUser != null) {
            var userID = mFirebaseUser!!.getUid()
        }


        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {


                startActivity(Intent(this, MainActivity::class.java))


            }
        }


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        var forgetPass = findViewById<View>(R.id.forgetPass) as TextView
        var Button = findViewById<View>(R.id.loginBtn) as Button?
        var signUp = findViewById<View>(R.id.signUpBtn) as TextView?

        forgetPass?.setOnClickListener {
            showDialoge()
        }
        Button?.setOnClickListener {

            signIn()
        }
        signUp?.setOnClickListener {
            val intent = Intent(applicationContext, registerOneActivity::class.java)
            startActivity(intent)
        }


        /*     override fun onClick(v: View) {
            when (v.id) {
                R.id.loginBtn -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    // else condition
                }
            }
        }*/
    }

    private fun showDialoge() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.reset_password, null)
        dialogBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        var forget_email = dialogView.findViewById<View>(R.id.edt_commentEmail)
        val button1 = dialogView.findViewById<View>(R.id.buttonSubmit)
        val button2 = dialogView.findViewById<View>(R.id.buttonCancel)

        button2.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
        button1.setOnClickListener(View.OnClickListener {
            forgetPassword(forget_email.text.toString())
            Log.d(TAG, "signInWithEmail:" + forget_email.text.toString())

            dialogBuilder.dismiss()
        })

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private fun forgetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "password is sent",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this, "password is NOT sent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // updateUI(currentUser)
    }

    private fun showDialogWithOkButton(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                //do things
            }
        val alert = builder.create()
        alert.show()
    }

    private fun signIn() {


        val login_password = findViewById<View>(R.id.passwordEDL) as EditText?
        val login_email = findViewById<View>(R.id.emailETL) as EditText?

        var entered_email = login_email?.text.toString()
        var entered_password = login_password?.text.toString()


        if (entered_password == "" && entered_email == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني وكلمة المرور")

        } else if (entered_email == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني")

        }//end if
        else if (entered_password == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال كلمة المرور")


        }

        if (entered_email != "" && entered_password != "") {

            auth.signInWithEmailAndPassword(entered_email, entered_password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                       var user = FirebaseAuth.getInstance().currentUser
                       // val useremailveri = user!!.isEmailVerified()
                        val emailVerified = user!!.isEmailVerified
                        Log.w(TAG, "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+ emailVerified)



                        //val user = auth.currentUser
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(
                            this, "Authentication succeeded",
                            Toast.LENGTH_SHORT
                        ).show()

                        if (emailVerified) {
                            // FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                            startActivity(Intent(this, MainActivity::class.java))

                            showDialogWithOkButton("تحقق من الرابط المرسل على بريدك لإكمال عملية تسجيل الدخول ")
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed google.",
                            Toast.LENGTH_SHORT
                        ).show()
                        showDialogWithOkButton("البريد الإلكتروني غير صالح")


                    }
                    //end of signIn


                }
        }
    }
}







