package sa.ksu.gpa.saleem.recipe.SharedRecipe

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.RecipeModel
import sa.ksu.gpa.saleem.recipe.sharedRecipeInformaion
import java.util.*
import kotlin.collections.ArrayList

class RecipesAdapter(var context:Context, var arrayList:ArrayList<RecipeModel>):
        RecyclerView.Adapter<RecipesAdapter.ItemHolder>(), Filterable {

    var filteringList =ArrayList<RecipeModel>()

    init {
        filteringList=arrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
                .inflate(R.layout.sharedrecipe_cardview, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return filteringList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val recipe:RecipeModel=filteringList.get(position)
        Glide.with(context).load(recipe.recipePicture).into(holder.recipeImage1)
        holder.recipeTitle2.text=recipe.recipeTitle
        holder.recipeCalories3.text=recipe.recipeCalories
        holder.recipeId=recipe.recipeId

        holder.itemView.setOnClickListener {
            var intent = Intent(context, sharedRecipeInformaion::class.java)
            intent.putExtra("RecipeId",holder.recipeId)
            context.startActivity(intent)
        }
    }
    class ItemHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var recipeImage1 = itemView.findViewById<ImageView>(R.id.recipe_image)
        var recipeTitle2= itemView.findViewById<TextView>(R.id.recipe_title)
        var recipeCalories3= itemView.findViewById<TextView>(R.id.recipe_calories)
        lateinit var  recipeId:String


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteringList = arrayList
                } else {
                    val resultList = ArrayList<RecipeModel>()
                    for (row in arrayList) {
                        if (row.recipeTitle.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filteringList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteringList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteringList = results?.values as ArrayList<RecipeModel>
                notifyDataSetChanged()
            }

        }
    }
}