package sa.ksu.gpa.saleem.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sa.ksu.gpa.saleem.R

class recipeListStaggeredAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listOfRecipes = listOf<RecipeModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return sharedRecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recipe_itam_viewshared, parent, false))
    }

    override fun getItemCount(): Int = listOfRecipes.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val recipeViewHolder = viewHolder as sharedRecipeViewHolder
        recipeViewHolder.bindView(listOfRecipes[position])
    }
    fun setMovieList(listOfMovies: List<RecipeModel>) {
        this.listOfRecipes = listOfMovies
        notifyDataSetChanged()
    }
}