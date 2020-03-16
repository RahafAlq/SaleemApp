package sa.ksu.gpa.saleem.recipe

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.info_dynamic_ingredients.view.*
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.RecipeModel

class sharedRecipeInformaion : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recipeName: TextView
    private lateinit var recipeCalories: TextView
    private lateinit var recipeDescription:  TextView
    private lateinit var recipeImage:  ImageView
    private lateinit var backButton:  ImageView
    private lateinit var recipeIngLayout:  LinearLayout
    lateinit var recipeID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_recipe_info)
        db = FirebaseFirestore.getInstance()
        recipeName= findViewById(R.id.recipe_info_title)
        recipeCalories= findViewById(R.id.recipe_info_calories)
        recipeImage= findViewById(R.id.recipe_info_image)
        backButton= findViewById(R.id.back_button)
        recipeDescription= findViewById(R.id.recipe_info_dirctions)
        recipeIngLayout= findViewById(R.id.recipe_info_ings)
        recipeID = intent.extras!!.getString("RecipeId").toString()
        backButton.setOnClickListener {
            onBackPressed()
        }


        getRecipeInfo()
    }

    private fun getRecipeInfo() {
        lateinit var  recipename : String
        lateinit var recipeCalproes: String
        lateinit var recipeImage : String
        lateinit var recipePrepration: String
        lateinit var recipeId: String
        db.collection("Recipes").document(recipeID)
                .get()
                .addOnSuccessListener { document ->


                         recipeId= document.id
                         recipename= document.get("name").toString()
                         recipeCalproes=document.get("calories").toString()
                         recipeImage= document.get("image").toString()
                         recipePrepration= document.get("prepration").toString()
                        Log.d("sharedRecipeInformaion","recipePrepration: "+recipePrepration)
                       //  var recipeType= document.get("Type") as ArrayList<String>
                      //   Log.d("sharedRecipeInformaion","size"+recipeType.size)


                      /*  for(row in recipeType){
                            if (!row?.equals("not"))
                                Log.d("sharedRecipeInformaion","not null"+row)
                        }*/
                    addIngrediants()
                    connectRecipesWithVies(recipename,recipeCalproes,recipeImage,recipePrepration)
                    }


                .addOnFailureListener { exception ->
                    Log.w("sharedRecipeInformaion", "Error getting documents: ", exception)
                }
    }
    private fun addIngrediants() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        db.collection("Recipes").document(recipeID).collection("ingredients").get()
                .addOnSuccessListener {  documents ->
                    for (document in documents){
                        var rowView: View = inflater.inflate(R.layout.info_dynamic_ingredients, null)
                        rowView.ingredint_info.text= document.get("ingreidentName").toString()
                        rowView.ingredint_quantity.text=document.get("ingquantity").toString()+" "+document.get("ingredienunit").toString()
                        recipeIngLayout.addView(rowView, recipeIngLayout.getChildCount())

                    }
                }
                .addOnFailureListener {
                    Log.w("sharedRecipeInformaion", "Error getting documents ings: ", it)

                }


    }

    private fun connectRecipesWithVies(recipename: String, recipeCalproes: String, recipeimage: String, recipePrepration: String) {
        recipeName.text=recipename
        recipeCalories.text=recipeCalproes
        recipeDescription.text=recipePrepration
        Glide.with(this).load(recipeimage).into(recipeImage)




    }
}
