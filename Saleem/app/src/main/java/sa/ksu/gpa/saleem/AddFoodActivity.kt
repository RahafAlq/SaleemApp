package sa.ksu.gpa.saleem

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_food.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class AddFoodActivity (context: Context ,onSave: OnSave) : Dialog(context) {
    var onSave = onSave

    lateinit var adapter: ItemAdapter
    var listdata = ArrayList<Item>()
    var nutritionalValueList = ArrayList<NutritionalValue>()
    private lateinit var db: FirebaseFirestore
    lateinit var dialog:ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_food)
        toolbar.title = "اضافة وجبة مفصلة"
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        db= FirebaseFirestore.getInstance()
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowCustomEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        cancel.setOnClickListener {
            onBackPressed()
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_component)
        adapter = ItemAdapter(listdata, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        initNutritionalValueList()
        save.setOnClickListener {
            var sum = 0.0
            if (listdata.size > 0){
                for (data in listdata){
                    sum += (data.amount.toInt() * data.nutritionalValue)
                    Log.e("hhh","sum ==> $sum")
                    Log.e("hhh","amount ==> ${data.amount.toInt()}")
                    Log.e("hhh","amount ==> ${data.nutritionalValue}")
                }
                addFoodToFirestore(sum,nameOfFood.text.toString())



            }else{
                Toast.makeText(context,"ادخل مكونات الوجبة",Toast.LENGTH_LONG).show()
            }
        }
        delete_button.setOnClickListener {
            addObject(it)
        }


    }

    private fun initNutritionalValueList() {
        //رز أبيض
        var risw: NutritionalValue = NutritionalValue()
        risw.array = ArrayList()
        risw.array.add(2)//mm
        risw.array.add(2)//gram
        risw.array.add(100)//cup
        risw.array.add(25)//spoonC
        risw.array.add(15)//spoonS
        nutritionalValueList.add(risw)
        //رز أسمر
        var risb: NutritionalValue = NutritionalValue()
        risb.array = ArrayList()
        risb.array.add(2)//mm
        risb.array.add(2)//gram
        risb.array.add(100)//cup
        risb.array.add(25)//spoonC
        risb.array.add(15)//spoonS
        nutritionalValueList.add(risb)
        //موزة
        var banana: NutritionalValue = NutritionalValue()
        banana.array = ArrayList()
        banana.array.add(2)//mm
        banana.array.add(2)//gram
        banana.array.add(100)//cup
        banana.array.add(25)//spoonC
        banana.array.add(15)//spoonS
        nutritionalValueList.add(banana)
        //تفاح
        var apple: NutritionalValue = NutritionalValue()
        apple.array = ArrayList()
        apple.array.add(2)//mm
        apple.array.add(2)//gram
        apple.array.add(100)//cup
        apple.array.add(25)//spoonC
        apple.array.add(15)//spoonS
        nutritionalValueList.add(apple)
        //لبن
        var Yogurt: NutritionalValue = NutritionalValue()
        Yogurt.array = ArrayList()
        Yogurt.array.add(2)//mm
        Yogurt.array.add(2)//gram
        Yogurt.array.add(100)//cup
        Yogurt.array.add(25)//spoonC
        Yogurt.array.add(15)//spoonS
        nutritionalValueList.add(Yogurt)
//حليب
        var milk: NutritionalValue = NutritionalValue()
        milk.array = ArrayList()
        milk.array.add(2)//mm
        milk.array.add(2)//gram
        milk.array.add(100)//cup
        milk.array.add(25)//spoonC
        milk.array.add(15)//spoonS
        nutritionalValueList.add(milk)

    }

    inner class MyViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_component, parent, false)
        ) {
        var addIngredientcomp: LinearLayout? = null
        var spFood: TextView? = null
        var spWeight: TextView? = null
        var etNumber: EditText? = null
        var deleteButton: Button? = null

        init { // TODO: Customize the item layout

            addIngredientcomp = itemView.findViewById(R.id.addIngredientcomp)
            spFood = itemView.findViewById(R.id.spFood)
            spWeight = itemView.findViewById(R.id.spWeight)
            etNumber = itemView.findViewById(R.id.et_number)
            deleteButton = itemView.findViewById(R.id.delete_button)

        }


    }


    inner class ItemAdapter(var list: ArrayList<Item>, val context: Context) :
        RecyclerView.Adapter<MyViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return MyViewHolder(inflater, parent)
        }

        fun addItem(item: Item) {
            list.add(item)
            Log.e("hhh", "hhh === === >> list size" + list.size)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.spFood?.text = list[position].name
            holder.spWeight?.text = list[position].weight
            holder.etNumber?.text =
                Editable.Factory.getInstance().newEditable(list[position].amount)
            holder.deleteButton?.setOnClickListener {
                list.removeAt(position)
                notifyDataSetChanged()
            }
//            holder.amount.text = list[position].amount
//            holder.weight.text = list[position].weight
//  var addIngredientcomp: LinearLayout? = null
//        var spFood: Spinner? = null
//        var spWeight: Spinner? = null
//        var etNumber: EditText? = null
//        var deleteButton: Button? = null
        }


    }

    inner class Item {
        lateinit var name: String
        lateinit var amount: String
        lateinit var weight: String
        var nutritionalValue: Int = 0

    }

    //      <item>مل</item>
//        <item>كأس</item>
//        <item>جم</item>
//        <item>ملعقة طعام كبيرة</item>
//        <item>ملعقة صغيرة</item>Nutritional value
    inner class NutritionalValue {
        lateinit var array: ArrayList<Int>


        var mm: Int = 0
        var cup: Int = 0
        var gram: Int = 0
        var spoonC: Int = 0
        var spoonS: Int = 0

    }

    fun addObject(view: View) {
        if(et_number.text.toString() .equals("")){
            Toast.makeText(context,"ادخل الكمية المضافة ",Toast.LENGTH_SHORT).show()
            return
        }
        var item = Item()
        item.name = spFood.selectedItem.toString()
        item.weight = spWeight.selectedItem.toString()
        item.amount = et_number.text.toString()
        var nutritionalValue =   nutritionalValueList.get(spFood.selectedItemPosition)
        var ww = nutritionalValue.array[spWeight.selectedItemPosition]
        item.nutritionalValue = ww

        listdata.add(item)
        Log.e("hhh", "list size ==> " + listdata.size)
        rv_component.adapter?.notifyDataSetChanged()

    }

    fun addFoodToFirestore(sum: Double,name:String){
        val data = hashMapOf(
            "food_name" to name,
            "type" to "Detailed",
            "foods" to listdata,
            "date" to getCurrentDate(),
            "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1",
            "cal_of_food" to sum
        )
        showLoadingDialog()
        db.collection("Foods").document().set(data as Map<String, Any>).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(context,"تمت الاضافة بنجاح",Toast.LENGTH_SHORT).show()
            dismiss()


            var intent : Intent
            intent = Intent()
            intent.putExtra("data",sum)

            onSave.onSaveSuccess(sum)
            dismiss()
//                intent.putExtra("object",Gson().toJson(listdata))

        }.addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(context,"حصل خطأ في عملية الاضافة",Toast.LENGTH_SHORT).show();
        };
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
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }
    public interface OnSave{
        fun onSaveSuccess(sum:Double )
    }
}
