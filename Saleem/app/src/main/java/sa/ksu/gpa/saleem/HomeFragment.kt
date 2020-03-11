package sa.ksu.gpa.saleem


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.add_excercise_dialog.view.*
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.AddFoodActivity.OnSave
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    var totalcal  = 0.0
    var consumerCal = 0.0
    var remainderCal = 0.0
    var history_Id = ""
    private lateinit var pagerAdapter: PagerAdapter
    lateinit var dialog:ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = activity?.supportFragmentManager?.let { PagerAdapter(it) }!!
        viewPager.adapter = pagerAdapter
        dotsIndicator.setViewPager(viewPager)
        viewPager.adapter?.registerDataSetObserver(dotsIndicator.dataSetObserver)

//        view.findViewById<ImageView>(R.id.ivAddView).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_breakfast).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_lunch).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_dinner).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_snack).setOnClickListener { addFood() }
        db= FirebaseFirestore.getInstance()
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid

//        val burntCalories = db.collection("Users").document(currentuser)
//        val burntCalories = db.collection("Users")
//            .document("ckS3vhq8P8dyOeSI7CE7D4RgMiv1")//test user
//            .addSnapshotListener(EventListener(){ documentSnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
//                var neededcal = documentSnapshot?.getDouble("needed cal")
//                totalcal = neededcal as Double
//
//                tv_main_number.setText("${totalcal.toInt()}")
//                pb_counter.progress =remainderCal.toInt()
//                pb_counter.max = totalcal.toInt()
//
//                Log.e("hhhh","${totalcal.toInt()}")
//                Log.e("wwww","${consumerCal.toInt()}")
//
//
//            })
        db.collection("History")
            .whereEqualTo("date",getCurrentDate())
            .whereEqualTo("user_id","ckS3vhq8P8dyOeSI7CE7D4RgMiv1")
            .get().addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    val data = hashMapOf(
                        "cal" to remainderCal,
                        "date" to getCurrentDate(),
                        "steps_count" to 1,
                        "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1"
                    )
                    db.collection("History").document().set(data).addOnSuccessListener {
                        for (document in documents) {
                            Log.d("hhh", "${document.id} => ${document.data}")
                            history_Id = document.id
                        }
                    }
                }
                for (document in documents) {
                    Log.d("hhh", "${document.id} => ${document.data}")
                    history_Id = document.id
                    remainderCal = document.get("cal") as Double
                    pb_counter.progress = remainderCal.toInt()
                    remainder_cal.setText("${remainderCal.toInt()}")
                    tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                }
            }
    }
    fun showAddFood(data: ArrayList<String>) {
        val fragment = ItemListDialogFragmentA(data)
        val bundle = Bundle()
        bundle.putStringArrayList("item_data", data)
        this.activity?.supportFragmentManager?.let { fragment.show(it, "tag") }
        fragment.setOnSelectData(object : ItemListDialogFragmentA.Listener {
            override fun onItemClicked(position: Int) {
                when(position){
                    0 -> {
                        startAddFoodActivity()
                    }
                    1 -> {
                        addExcercizeDialog()
                    }
                }
            }
        })
    }

    private fun startAddFoodActivity() {

//        var intent = Intent(activity,AddFoodActivity::class.java).apply{
//
//        }
//        startActivityForResult(intent,1000)
        var onsave  = object : OnSave {
            override fun onSaveSuccess(sum: Double) {
                var d:Int = 0
                d = sum.toInt()
                Log.e("onActivityResult","$d")
                consumerCal -= d
                remainderCal += d
                pb_counter.progress =remainderCal.toInt()
                pb_counter.max = totalcal.toInt()
                remainder_cal.setText("${remainderCal.toInt()}")
                tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                Toast.makeText(context,"تمت اضافة الوجبة", Toast.LENGTH_LONG)
                updateHistory()
            }
        }
        var dialog: AddFoodActivity? = context?.let { AddFoodActivity(it,onsave) }

        dialog?.show()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK){

        }
    }

    fun addFood(){
        val list = ArrayList<String>()
        list.add("وجبة مفصلة")
        list.add("وجبة سريعة")
        showAddFood(list)

    }

    private fun addExcercizeDialog() {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_fast_food, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        var burnt = mDialogView.addExcerciseburentCal!!.text
        val workoutName = mDialogView.addExcerciseWorkoutname!!.text
        Log.d("this",""+burnt+workoutName)

        mDialogView.addExcercise.setOnClickListener{


            if (burnt==null||workoutName==null){
                mDialogView.addExcerciseError.setText("الرجاء ادخال المعلومات الناقصة")
            }
            else{
                var burntStringData = burnt.toString()
                var data =  burntStringData.toInt()
                consumerCal -= data
                remainderCal += data

                pb_counter.progress =remainderCal.toInt()
                pb_counter.max = totalcal.toInt()
                remainder_cal.setText("${remainderCal.toInt()}")
                tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                val data1 = hashMapOf(
                    "food_name" to workoutName.toString(),
                    "type" to "unDetailed",
                    "foods" to ArrayList<AddFoodActivity.Item>(),
                    "date" to getCurrentDate(),
                    "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1",
                    "cal_of_food" to burntStringData.toInt()
                )
                showLoadingDialog()
                db.collection("Foods").document().set(data1 as Map<String, Any>).addOnSuccessListener {
                    dialog.dismiss()
                    Toast.makeText(context,"تمت الاضافة بنجاح",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(context,"حصل خطأ في عملية الاضافة",Toast.LENGTH_SHORT).show();
                };


                mAlertDialog?.dismiss()
                updateHistory()

            }
            // extra detail add a success shape
        }
        mDialogView.cancelExcercise.setOnClickListener{
            mAlertDialog?.dismiss()

        }

    }

    private fun showLoadingDialog() {
        dialog = ProgressDialog.show(
            context, "",
            "Loading. Please wait...", true
        )
    }


    fun getCurrentDate():String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }


    fun updateHistory(){
        val data = hashMapOf(
            "cal" to remainderCal,
            "date" to getCurrentDate(),
            "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1"
        )
        if (!history_Id.equals(""))
            db.collection("History").document(history_Id).update(data as Map<String, Any>);
    }
}
