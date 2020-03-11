package sa.ksu.gpa.saleem.register

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_one.*
import kotlinx.android.synthetic.main.activity_register_two.*
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.loginn

class registerOneActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_one)

        val  textView = findViewById<View>(R.id. signUpBtn ) as TextView?
        textView?.setText(Html.fromHtml("<u>تسجيل الدخول</u>"));

        textView?.setOnClickListener {
            val intent = Intent(applicationContext, loginn::class.java)
            startActivity(intent)
        }

        val intent = Intent(this, registerTwoActivity::class.java)

        auth = FirebaseAuth.getInstance()

        val nameTxt = findViewById<View>(R.id.nameET) as EditText?
        val emailTxt = findViewById<View>(R.id.emailET) as EditText?
        val passTxt = findViewById<View>(R.id.passwordED) as EditText?
        val repassTxt = findViewById<View>(R.id.repasswordED) as EditText?

        var namet = nameTxt?.text.toString()
        var emailt = emailTxt?.text.toString()
        var passt = passTxt?.text.toString()
        var repasst = repassTxt?.text.toString()


        val btn = findViewById<View>(R.id.nxtOneBtn) as Button?
        btn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                if( android.util.Patterns.EMAIL_ADDRESS.matcher(emailt).matches()){
                    showDialogWithOkButton("الرجاءالتاكد من البريد الالكتروني")


                }




                        Log.d("this1", "" + emailTxt?.text.toString())
                        Log.d("this1", "" + nameTxt?.text.toString())
                        Log.d("this1", "" + passt)
                        Log.d("this1", "" + repasst)

                        intent.putExtra("name", nameTxt?.text.toString())
                        intent.putExtra("email", emailTxt?.text.toString())
                        intent.putExtra("password", passTxt?.text.toString())

                    if(verify()&&checkPassword(passt, repasst)) {

                        //  if (verify()) {
                        startActivity(intent)

                    }
                    }

           // }

        })

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

    private fun sendEmailVerification() {
        // Disable button
        //verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]

        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                // verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val nameTxt = findViewById<View>(R.id.nameET) as EditText?
        val emailTxt = findViewById<View>(R.id.emailET) as EditText?
        val passTxt = findViewById<View>(R.id.passwordED) as EditText?
        val repassTxt = findViewById<View>(R.id.repasswordED) as EditText?

        var nameEd = nameTxt?.text.toString()
        var emailEd = emailTxt?.text.toString()
        var passEd = passTxt?.text.toString()
        var repassEd = repassTxt?.text.toString()



        if (TextUtils.isEmpty(emailEd) && TextUtils.isEmpty(nameEd) && TextUtils.isEmpty(passEd) && TextUtils.isEmpty(
                repassEd
            )
        )
        {

            showDialogWithOkButton("الرجاء ادخال اسم المستخدم و البريد الالكتروني وكلمة المرور")

        } else if (TextUtils.isEmpty(emailEd)) {

            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني")

        } else
            if (TextUtils.isEmpty(passEd)) {

                showDialogWithOkButton("الرجاء تعين كلمة المرور")
            } else
                if (TextUtils.isEmpty(repassEd)) {
                    repasswordED.error = "Required."
                    showDialogWithOkButton("الرجاء اعادة تعين كلمة المرور")

                } else
                    if (TextUtils.isEmpty(nameEd)) {


                        showDialogWithOkButton("الرجاء ادخال اسم المستخدم")
                    }
        else if(passEd.length < 5){
                        showDialogWithOkButton("الرجاء ادخال كلمة المرور من 6 او اكثر ارقام")

                    }



        // checkPassword(passEd,repassEd)


        return valid
    }

/*    private fun updateUI(user: FirebaseUser? {
        hideProgressBar()
        if (user != null) {
            status.text = getString(R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            emailPasswordButtons.visibility = View.GONE
            emailPasswordFields.visibility = View.GONE
            signedInButtons.visibility = View.VISIBLE

            verifyEmailButton.isEnabled = !user.isEmailVerified
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            emailPasswordButtons.visibility = View.VISIBLE
            emailPasswordFields.visibility = View.VISIBLE
            signedInButtons.visibility = View.GONE
        }
    }*/


    companion object {
        const val TAG = "EmailPassword"
    }

    private fun checkPassword(pass: String, repass: String): Boolean {
        if (pass != repass) {
            showErrorMsg()
        }
        return pass == repass
    }

    private fun showErrorMsg() {
        // Toast.makeText(SignUp.this, "password didn't match", Toast.LENGTH_LONG).show();
        showDialogWithOkButton("كلمتا المرور غير متطابقتين")
    }

    private fun verify(): Boolean {

        val nameTxt = findViewById<View>(R.id.nameET) as EditText?
        val emailTxtt = findViewById<View>(R.id.emailET) as EditText?
        val passTxtt = findViewById<View>(R.id.passwordED) as EditText?
        val repassTxt = findViewById<View>(R.id.repasswordED) as EditText?

        val emailTxt = emailTxtt?.text.toString()
        val passTxt = passTxtt?.text.toString()
        val repass = repassTxt?.text.toString()

        if (passTxt != repass) {
            showErrorMsg()
            return false
        }
        //input

        else if (emailTxt == "" && passTxt == "" && nameTxt?.text.toString() == "" && repassTxt?.text.toString() == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال اسم المستخدم و البريد الالكتروني وكلمة المرور")
            return false

        } else if (emailTxt == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال البريد الالكتروني")
            return false

        }//end if
        else if (nameTxt?.text.toString() == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال اسم المستخدم")
            return false

        }//end if
        else if (repassTxt?.text.toString() == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء اعادة تعين كلمة المرور")
            return false
        }//end if
        else if (passTxt == "") {
            //show a popup for result
            showDialogWithOkButton("الرجاء ادخال كلمة المرور")
            return false


        } else if(passTxt.length < 5){
            showDialogWithOkButton("الرجاء ادخال كلمة المرور من 6 او اكثر ارقام")
            return false

        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
            showDialogWithOkButton("الرجاء ادخال الايميل بطريقة صحيحة")

            return false
        }
        else return true

    }
}


