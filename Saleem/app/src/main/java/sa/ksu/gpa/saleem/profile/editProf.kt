package sa.ksu.gpa.saleem.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_prof.*
import kotlinx.android.synthetic.main.activity_fragment_one.*
import kotlinx.android.synthetic.main.activity_register_two.*
import sa.ksu.gpa.saleem.R
import android.widget.AdapterView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class editProf : AppCompatActivity() {

    lateinit var userUid: String
    var storage = FirebaseStorage.getInstance()
    var firebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_prof)


        var edname: EditText?
        var edwight: EditText?
        var edheight: EditText?

        var emailText: TextView? = findViewById(R.id.emailSignUpHin)
        var saveEdit: Button?


        userUid = FirebaseAuth.getInstance().currentUser!!.uid

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)

        edname = findViewById(R.id.nameSignUpHin)
        edwight=findViewById(R.id.wightHin)
        edheight=findViewById(R.id.heightHin)

        val spinner: Spinner = findViewById(R.id.planets_spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        val spinnerL: Spinner = findViewById(R.id.planets_spinnerL)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planetsLevel_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerL.adapter = adapter
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {
                var goall =0
               // val selectedModelData = modelDataArrayList.get(pos)
                // After that you can send this object anywhere you want to use

                var goal = parent.getItemAtPosition(pos)

                if (goal=="خسارة الوزن")
                    goall=1
               else if (goal=="الحفاظ على الوزن")
                    goall=2
                else if (goal=="زيادة الوزن")
                    goall=3
               Log.d("TAG", "تم تعديل الهدف"+goall+"hhhhhhhhhhhhhhhhhhhhhhh"+goal)

                val firebaseFirestore = FirebaseFirestore.getInstance()
                var userUid = FirebaseAuth.getInstance().currentUser!!.uid
                val washingtonRef = firebaseFirestore.collection("users").document(userUid)
                washingtonRef
                    .update("goal", goall)
                    .addOnSuccessListener { Log.d("TAG", "تم تعديل الهدف") }
                    .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinnerL.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {
                var levell =0
               // val selectedModelData = modelDataArrayList.get(pos)
                // After that you can send this object anywhere you want to use

                var level = parent.getItemAtPosition(pos)

                if (level=="مبتدى")
                    levell=1
               else if (level=="متوسط")
                    levell=2
                else if (level=="متقدم")
                    levell=3
               Log.d("TAG", "تم تعديل المستوى"+levell+"hhhhhhhhhhhhhhhhhhhhhhh"+level)

                val firebaseFirestore = FirebaseFirestore.getInstance()
                var userUid = FirebaseAuth.getInstance().currentUser!!.uid
                val washingtonRef = firebaseFirestore.collection("users").document(userUid)
                washingtonRef
                    .update("level",levell)
                    .addOnSuccessListener { Log.d("TAG", "تم تعديل المستوى") }
                    .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        saveEdit = findViewById(R.id.edit_profile) as Button
        var auth = FirebaseAuth.getInstance()
        var storageRef = storage.reference

        saveEdit!!.setOnClickListener(View.OnClickListener {
            // save changes
            val n = edname!!.getText().toString()
            val w = edwight!!.getText().toString()
            val h = edheight!!.getText().toString()
          //  val g=planets_spinner.getItemAtPosition(pos).toString()
            editName(n)
            editWight(w)
            editHight(h)

            startActivity(Intent(this,Profile::class.java))
        })

        retriveUserData()
    }

    private fun editHight(h: String) {
        val washingtonRef = firebaseFirestore.collection("users").document(userUid)
        washingtonRef
            .update("height", h + "")
            .addOnSuccessListener { Log.d("TAG", "تم تعديل الطول") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
    }

    private fun editWight(w: String) {
        val washingtonRef = firebaseFirestore.collection("users").document(userUid)
        washingtonRef
            .update("weight", w + "")
            .addOnSuccessListener { Log.d("TAG", "تم تعديل الوزن") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
    }

    private fun editName(n: String) {
        val washingtonRef = firebaseFirestore.collection("users").document(userUid)
        washingtonRef
            .update("name", n + "")
            .addOnSuccessListener { Log.d("TAG", "تم تعديل الاسم") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
    }

    fun retriveUserData() {
        var edname: EditText? = findViewById(R.id.nameSignUpHin)
        var emailText: TextView? = findViewById(R.id.emailSignUpHin)
        var edwight: EditText? = findViewById(R.id.wightHin)
        var edheight: EditText? = findViewById(R.id.heightHin)


        val docRef = firebaseFirestore.collection("users").document(userUid)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {

                    val userName = document.get("name")!!.toString()
                    val email = document.get("email")!!.toString()

                    val weight = document.get("weight")!!.toString()
                    val height = document.get("height")!!.toString()


                    if (userName != null && email != null) {
                        edname?.setText(userName)
                        emailText?.setText(email)
                        edwight?.setText(weight)
                        edheight?.setText(height)



                    }

                }
            }
        }



    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
            // An item was selected. You can retrieve the selected item using

            var goall="1"


            var goal = parent.getItemAtPosition(pos)

            Log.d("TAG", "تم تعديل الطول"+goall+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+goal)


            val firebaseFirestore = FirebaseFirestore.getInstance()
            var userUid = FirebaseAuth.getInstance().currentUser!!.uid
            val washingtonRef = firebaseFirestore.collection("users").document(userUid)
            washingtonRef
                .update("goal", goall + "")
                .addOnSuccessListener { Log.d("TAG", "تم تعديل الطول") }
                .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }


        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback

            Log.d("TAG", "تم تعديل الطولkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+goal)

        }
    }

}
