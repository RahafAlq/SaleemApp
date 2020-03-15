package sa.ksu.gpa.saleem.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.RecipeModel

class sharedRecipeInformaion : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_recipe_info)
        db = FirebaseFirestore.getInstance()
        var recipeID= intent.extras!!.getString("RecipeId")

        getRecipeInfo()
    }

    private fun getRecipeInfo() {
        db.collection("Recipes")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        var recipeId= document.id
                        var recipename= document.get("name").toString()
                        var recipeCalproes=document.get("calories").toString()
                        var recipeImage= document.get("image").toString()
                        var recipePrepration= document.get("prepration").toString()
                        Log.d("sharedRecipeInformaion","recipePrepration: "+recipePrepration)
                         var recipeType= document.get("Type") as ArrayList<String>
                        Log.d("sharedRecipeInformaion","size"+recipeType.size)


                        for(row in recipeType){
                            if (!row?.equals("not"))
                                Log.d("sharedRecipeInformaion","not null"+row)
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("sharedRecipeInformaion", "Error getting documents: ", exception)
                }
    }
}
