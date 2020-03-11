package sa.ksu.gpa.saleem.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.field.view.*
import pl.utkala.searchablespinner.SearchableSpinner
import sa.ksu.gpa.saleem.R
import java.io.IOException


class ShareRecipeFirst : AppCompatActivity(), View.OnClickListener {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var db: FirebaseFirestore
    private var storageReference: StorageReference? = null
    private lateinit var image :TextView
    private lateinit var Recipeimage :ImageView
    private lateinit var publishRecipe:Button
    private lateinit var nameRecipe:EditText
    private lateinit var preprationRecipe:EditText
    private  var Quantity:EditText? = null
    private  var unit:Spinner? = null
    private  var IngredientNames: SearchableSpinner? = null
    private lateinit var checkBox: CheckBox
    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox
    private lateinit var checkBox5: CheckBox
    private lateinit var addIngrediant:Button
    private lateinit var main:LinearLayout
    private lateinit var uri:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_recipe_first)


        db = FirebaseFirestore.getInstance()
        //to-do
        storageReference = FirebaseStorage.getInstance().getReference().child("12").child("image")

        image=findViewById(R.id.sharedrecipeimage)
        Recipeimage=findViewById(R.id.sharedrecipeimageReal)
        addIngrediant = findViewById(R.id.addIngredient)
        publishRecipe = findViewById(R.id.publishRecipe)
        nameRecipe = findViewById(R.id.Recipename)
        preprationRecipe = findViewById(R.id.prepration)
        checkBox = findViewById(R.id.checkBox)
        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2= findViewById(R.id.checkBox2)
        checkBox3= findViewById(R.id.checkBox3)
        checkBox4= findViewById(R.id.checkBox4)
        checkBox5= findViewById(R.id.checkBox5)
        Quantity= findViewById(R.id.quantity)
        IngredientNames= findViewById(R.id.IngredientNames)
        unit= findViewById(R.id.unit)
        main=findViewById(R.id.main) //to add ings dirctly



        image.setOnClickListener(this)
        addIngrediant.setOnClickListener(this)
        publishRecipe.setOnClickListener(this)



    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sharedrecipeimage -> {
                launchGallery()
            }
            R.id.addIngredient -> {
                addIngrediants()

            }
            R.id.publishRecipe->{
                addRecipe()
                finish()
            }

            else -> {

            }
        }
    }

    private fun addIngrediants() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.field, null)
        main.addView(rowView, main.getChildCount())

    }
    private fun getIngrediants(recipeID:String) {

        if (main.childCount==0) {
            Toast.makeText(this,"الرجاء اضافة المكونات",Toast.LENGTH_LONG)
            return
        }
        var i = main.childCount
        var count =0
        while (i!=0){

            var ingquantity = main.getChildAt(count)!!.quantity.text.toString()
            Log.d("please","quantity"+ingquantity)
            var ingqunit = main.getChildAt(count).IngredientNames.selectedItem.toString()
            Log.d("please","unit"+ingqunit)
            var real=main.getChildAt(count).unit.selectedItem.toString()
            Log.d("please","real"+real)
            val docData = hashMapOf(
                // "UID" to currentUser!!.toString(),
                "ingreidentName" to ingqunit,
                "ingredienunit" to real,
                "ingquantity" to ingquantity
            )
            db.collection("Recipes").document(recipeID).collection("ingredients").document().set(docData).addOnSuccessListener {
                Log.d("please","ing added ")
            }.addOnFailureListener{
                Log.d("please","not added ")

            }
            i--
            count++

        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            val uploadTask=storageReference!!.putFile(data!!.data!!)
            val task=uploadTask.continueWithTask {
                    task->
                if(!task.isSuccessful)
                    Log.d("","some thing went wrong to uploading to storage")
                storageReference!!.downloadUrl
            }.addOnCompleteListener {task ->

                if(task.isSuccessful){
                    val downloadUri=task.result
                    uri = downloadUri!!.toString().substring(0,downloadUri.toString().indexOf("&token"))
                    Log.d("uriii",""+uri)
                }
            }
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                Recipeimage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun onDelete(view: View) {
        main.removeView(view.getParent() as View)
    }
    private fun addRecipe(){

        var  NumberOfRecipes:Int=0
        var recipeID:String=""
        var name:String =nameRecipe!!.text.toString()
        var prepration=preprationRecipe!!.text.toString()



        var type: String? =null
        var type1:String? =null
        var type2:String? =null
        var type3:String? =null
        var type4:String? =null
        var type5:String? =null

        if (checkBox.isChecked==true)
            type = checkBox!!.text.toString()
        if (checkBox1.isChecked==true)
            type1 = checkBox1!!.text.toString()
        if (checkBox2.isChecked==true)
            type2 =checkBox2!!.text.toString()
        if (checkBox3.isChecked==true)
            type3 =checkBox3!!.text.toString()
        if (checkBox4.isChecked==true)
            type4 =checkBox4!!.text.toString()
        if (checkBox5.isChecked==true)
            type5 =checkBox5!!.text.toString()

        val currentuser ="AXBFsLC5GeTGHkCDz8oz"
        val docData = hashMapOf(
           // "UID" to currentuser!!.toString(),
            "UID" to "AXBFsLC5GeTGHkCDz8oz",
            "image" to uri,
            "name" to name,
            "prepration" to prepration,
            "Type" to arrayListOf(type,type1,type2,type3,type4,type5)
        )
        val NumberOfCaloriesDoc = db.collection("Users").document(currentuser)
        NumberOfCaloriesDoc.get().addOnSuccessListener {
                documentSnapshot ->

              NumberOfRecipes= documentSnapshot.get("NumberOfRecipes").toString().toInt()
              recipeID= currentuser+NumberOfRecipes

            db.collection("Recipes").document(recipeID).set(docData).addOnSuccessListener {
                Log.d("please","added")
                NumberOfCaloriesDoc.update("NumberOfRecipes", FieldValue.increment(1))
                getIngrediants(recipeID)
            }.addOnFailureListener{
                Log.d("please","not added"+it.toString())

            }

        }.addOnFailureListener {
            Log.d("Here",""+it)
        }



    }
}