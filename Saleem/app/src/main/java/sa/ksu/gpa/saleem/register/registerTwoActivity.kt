package sa.ksu.gpa.saleem.register

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_two.*
import sa.ksu.gpa.saleem.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import android.widget.Toast

import android.R.id
import android.widget.RadioGroup
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.widget.Toolbar
import com.google.common.base.Verify.verify
import com.wajahatkarim3.easyvalidation.core.collection_ktx.noNumbersList


class registerTwoActivity : AppCompatActivity() {


    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    val TAG = "MyActivity"
    val user = HashMap<String, Any>()
    var level = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)
        var RadioG = findViewById<View>(R.id.radio_group) as RadioGroup
        val db = FirebaseFirestore.getInstance()
        val intent = Intent(this, registerThreeActivity::class.java)

        val btn = findViewById<View>(R.id.nxtTwoBtn) as Button?
        val toolbar = findViewById<View>(R.id.toolbar)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)


        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        btn?.setOnClickListener {
            Toast.makeText(this@registerTwoActivity, "Click...", Toast.LENGTH_LONG).show()

            var name = getIntent().getStringExtra("name")
            var pass = getIntent().getStringExtra("password")
            var email = getIntent().getStringExtra("email")
            var id: Int = radio_group.checkedRadioButtonId


            if (verify()) {
                val wightTxt = findViewById<View>(R.id.wight) as EditText?
                val heightTxt = findViewById<View>(R.id.height) as EditText?
                var id: Int = radio_group.checkedRadioButtonId

                var wight = wightTxt?.text.toString().toDouble()
                var height = heightTxt?.text.toString().toDouble()

                val radio: RadioButton = findViewById(id)
                var gender = radio?.text.toString();

                Log.d("this2", "" + gender)
                intent.putExtra("gender", gender)

                intent.putExtra("wight", wight)
                intent.putExtra("height", height)

            }

            //  var bmi = (wight) / (height / 100 * height / 100)
            //val type = calculateBmi(wight = wight, height = height)


            // Get the checked radio button id from radio group
        //    var id: Int = radio_group.checkedRadioButtonId
            //   if (id!=-1){ // If any radio button checked from radio group
            // Get the instance of radio button using id

/*
            if (id != -1) {
                val radio: RadioButton = findViewById(id)
                var gender = radio?.text.toString();

                Log.d("this2", "" + gender)
                intent.putExtra("gender", gender)

            } else if (id == -1){


                showDialogWithOkButton("الرجاء اختيار الجنس")

        }*/

            /*         if (gender=="ذكر")
                gender="male"
            if(gender=="انثى")
                gender="female"
*/

            fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dob = Calendar.getInstance()
                val today = Calendar.getInstance()

                dob.set(year, monthOfYear, dayOfMonth)

                var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
                    age--


                val ageInt = age + 1

                var userAge = age?.toString()

                getIntent().putExtra("age", userAge)

                intent.putExtra("age1", userAge)

                Log.d("this2", "age in 2nd activity" + userAge)
                updateDateInView()
            }








          /*  intent.putExtra("BMI", bmi)
            intent.putExtra("type", type)*/
            intent.putExtra("name", name)


            intent.putExtra("password", pass)
            intent.putExtra("email", email)


            Log.d("this2", "" + email)
            Log.d("this2", "" + name)
            Log.d("this2", "" + pass)
            Log.d("this2", "" + height)
            Log.d("this2", "" + wight)
       /*     Log.d("this2", "" + type)
            Log.d("this2", "" + bmi)*/

            // intent.putExtra("age", age)
            if (verify()) {


                startActivity(intent)
            }//------------------------------------------

        }


        // get the references from layout file
        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        textview_date!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dob = Calendar.getInstance()
                val today = Calendar.getInstance()

                dob.set(year, monthOfYear, dayOfMonth)

                var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }

                val ageInt = age + 1

                // val user = HashMap<String, Any>()
               // user.put("DOB", age)
               val userAge=age?.toString()

                getIntent().putExtra("age",userAge)
                intent.putExtra("age",userAge)

                getIntent().putExtra("agee",age)
                intent.putExtra("agee",age)


                Log.d("this2","age in 2nd activity"+userAge)
                updateDateInView()
            }

        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@registerTwoActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }

        })
    }

    private fun verify(): Boolean {

        val wightTxt = findViewById<View>(R.id.wight) as EditText?
        val heightTxt = findViewById<View>(R.id.height) as EditText?


        var wight = wightTxt?.text.toString()
        var height = heightTxt?.text.toString()
        var id: Int = radio_group.checkedRadioButtonId


        if (wight == "") {
            showDialogWithOkButton("الرجاء ادخال الوزن")
            return false
        } else if (height == "") {

            showDialogWithOkButton("الرجاء ادخال الطول")
            return false


        } else if (id == -1) {


            showDialogWithOkButton("الرجاء اختيار الجنس")
            return false

        }
        /*    else if(radio_group.c){

        }*/
        else return true

    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }


    fun message(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }






    fun calculateBmi(wight: Double, height: Double): Int {

        var bmi = (wight)/(height/100*height/100)
        intent.putExtra("BMI",bmi)


        // return
        when {
            18.5 > bmi -> {
                level= 1
                showDialogWithOkButton(" نحافة $bmi ")
                intent.putExtra("type", level)


            }
            18.5 <= bmi || bmi < 25.0 -> {
                showDialogWithOkButton(" طبيعي $bmi")
                level= 2
                intent.putExtra("type", level)
            }
            25.0 <= bmi || bmi < 30.0 -> {
                showDialogWithOkButton("زيادة وزن $bmi")
                level= 3
                intent.putExtra("type", level)
            }

            30.0 <= bmi || bmi < 35.0 -> {
                showDialogWithOkButton("سمنة درجة اولى BMI $bmi")
                level= 4
                intent.putExtra("type", level )
            }
            35.0 <= bmi || bmi < 40.0 -> {
                showDialogWithOkButton(" سمنى درجة ثانية$bmi")
                level= 5
                intent.putExtra("type", level)

            }
            bmi >= 40.0 -> {
                showDialogWithOkButton("$bmi سمنة مفرطة")
               level= 6
                intent.putExtra("type", level)
            }

            else -> print("")
        }
return level
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




}





