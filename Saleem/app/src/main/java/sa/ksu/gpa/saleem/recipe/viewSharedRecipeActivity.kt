package sa.ksu.gpa.saleem.recipe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_view_shared_recipe.*
import sa.ksu.gpa.saleem.R

class viewSharedRecipeActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener {

    var search: String =""
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_shared_recipe)
        db = FirebaseFirestore.getInstance()

        initView()

    }
    private fun initView() {
        recyclerViewRecipes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //This will for default android divider
        recyclerViewRecipes.addItemDecoration(GridItemDecoration(10, 2))
        val recipeListAdapter = recipeListStaggeredAdapter()
        recyclerViewRecipes.adapter = recipeListAdapter
        recipeListAdapter.setMovieList(generateDummyData())

    }
    private fun generateDummyData(): List<RecipeModel> {

        val listOfRecipes = mutableListOf<RecipeModel>()
            listOfRecipes.clear()
            db.collection("Recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    var recipename= document!!.get("name").toString()
                    var recipeImage= document!!.get("image").toString()
                    var movieModel = RecipeModel(1, recipename, recipeImage)
                    listOfRecipes.add(movieModel)
                    Log.d("here", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("here", "Error getting documents: ", exception)
            }
        return listOfRecipes



    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            search=query
            generateDummyData()
            search= null.toString()
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            search=newText
            generateDummyData()
            search= null.toString()
            return true
        }
        return false
    }
}
