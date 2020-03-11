package sa.ksu.gpa.saleem.profile

import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore

import sa.ksu.gpa.saleem.R
import com.google.firebase.auth.FirebaseAuth
import sa.ksu.gpa.saleem.loginn
import sa.ksu.gpa.saleem.register.registerFourActivity
import sa.ksu.gpa.saleem.register.registerOneActivity
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * A placeholder fragment containing a simple view.
 */

class fragmentOne : Fragment() {

    private val TAG = "DocSnippets"
    private lateinit var auth: FirebaseAuth
    private lateinit var neededCall:String


    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        // val userName=findViewById<View>(R.id.section_label)


        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_fragment_one, container, false)
       // val textView: TextView = root.findViewById(R.id.section_label)

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid





        Log.d(registerFourActivity.TAG, "kkkkkkkkkkkkkkkkkkkkk"+userUid)

        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(userUid)

        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(registerFourActivity.TAG, "${document.id} => ${document.data}")

                    val name = document.get("name").toString()
                    val email = document.get("email").toString()
                    val wight = document.get("weight").toString()
                    val hight = document.get("height").toString()
                  //  val neededCal = document.get("needed cal").toString().toDouble()
                    val gender = document.get("gender").toString()
                    val age = document.get("age").toString()

                Log.d(registerFourActivity.TAG, "kkkkkkkkkkkkkkkkkkkkk"+userUid+name+wight)

                    val weight=wight.toString().toDouble()
                    val length=hight.toString().toDouble()

                val goalll = document.get("goal").toString()
                val levelll = document.get("level").toString()
                val goal=goalll.toString().toInt()
                var level=levelll.toString().toInt()

                Log.e(registerOneActivity.TAG, "kkkkkkkkkkkkkkkkkkkkkoo"+level)

                    var bmi = (weight) / (length / 100 * length / 100)

                if (gender=="female") {
                    var neededCal: Double = 0.0
                    var Mifflin = ((10 * weight) + (6.25 * length) - (5 * level) - 161)
                    var Revised =
                        ((9.247 * weight) + (3.098 * length) - (4.330 * level) + 447.593)

                    var Calories = (Mifflin + Revised) / 2

                    when (goal) {
                        1 -> neededCal = Calories - 500
                        2 -> neededCal = Calories + 500
                        3 -> neededCal = Calories
                    }
                    neededCall=roundOffDecimal(neededCal).toString()

                } else if (gender=="male"){
                    var neededCal:Double=0.0
                    var Mifflin =((10*weight)+ (6.25*length)-(5*level )+5)
                    var Revised =((13.397*weight) +(4.799*length) - (5.677*level) + 88.362)

                    var Calories= (Mifflin+Revised)/2

                    when(goal){
                        1 -> neededCal= Calories-500
                        2->  neededCal= Calories+500
                        3 -> neededCal= Calories
                    }
                     neededCall=roundOffDecimal(neededCal).toString()

                }


                    var bmii=roundOffDecimal(bmi)

                   // var neededCall=roundOffDecimal(neededCal)
                    //val BMI = bmii.toString()

                    var goall="goal"
                    when(goal){
                        1 -> goall= "خسارة الوزن"
                        2->  goall= "الحفاظ على الوزن"
                        3 -> goall= "زيادة الوزن"
                    }
                    var levell="level"
                    when(level){
                        1 -> levell= "مبتدى"
                        2->  levell= "متوسط"
                        3 -> levell= "متقدم"
                    }
                    var genderr="gender"
                    when(gender){
                        "male" -> genderr= "ذكر"
                        "gemale"->  genderr= "انثى"

                    }


                    val nameTxt: TextView = root.findViewById(R.id.nameSignUpHin)
                    nameTxt.setText(name)
                    val emailTxt:TextView=root.findViewById(R.id.emailSignUpHin)
                    emailTxt.setText(email)
                    val wightTxt: TextView = root.findViewById(R.id.wightHin)
                    wightTxt.setText(wight)
                    val heightTxt:TextView=root.findViewById(R.id.heighHin)
                    heightTxt.setText(hight)
                    val calTxt: TextView = root.findViewById(R.id.neededCalHin)
                    calTxt.setText(neededCall)
                    val genderTxt:TextView=root.findViewById(R.id.genderHin)
                    genderTxt.setText(genderr)
                    val bmiTxt:TextView=root.findViewById(R.id.bmiHin)
                    bmiTxt.setText(bmii)
                    val ageTxt:TextView=root.findViewById(R.id.ageHin)
                    ageTxt.setText(age)
                    val goalTxt:TextView=root.findViewById(R.id.goalHin)
                    goalTxt.setText(goall)

                    val levelTxt:TextView=root.findViewById(R.id.levelHin)
                    levelTxt.setText(levell)



                }
            }
            .addOnFailureListener { exception ->
                Log.d(registerFourActivity.TAG, "get failed with ", exception)
            }

        var btn=root.findViewById<View>(R.id.edit_profile)
        var rebtn=root.findViewById<View>(R.id.reedit_profile)

        btn.setOnClickListener() {
            FirebaseAuth.getInstance().signOut();
               val intent = Intent(this.activity, loginn::class.java)
               startActivity(intent)

        }

        rebtn.setOnClickListener {
            val intent=Intent(this.activity, editProf::class.java)
            startActivity(intent)
        }
        getCollection()

        pageViewModel.text.observe(this, Observer<String> {
            // textView.text = "HHHHHHHHHHHHHHHHHHHHHHhhh"
        })
        return root
    }

    fun roundOffDecimal(number: Double): String? {


        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val num=df.format(number)
        return num
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): fragmentOne {
            val db = FirebaseFirestore.getInstance()
            val currentuser = FirebaseAuth.getInstance().currentUser!!.uid


            val auth = FirebaseAuth.getInstance()
            val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {

                    val userId = firebaseUser.uid
                    val userEmail = firebaseUser.email
                }
            }/*
            val docRef = db.collection("Users").whereEqualTo("email", "ghedaa.aj@gmail.com").get()  .addOnSuccessListener{ documents ->
                for (document in documents) {


                    putString("${document.id} => ${document.data}")
                }
            }*/
            return fragmentOne().apply {

                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)


                }
            }
        }

        fun getCollection() {



        }
    }
}
