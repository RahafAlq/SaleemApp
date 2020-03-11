package sa.ksu.gpa.saleem.recipe

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recipe_itam_viewshared.view.*
import java.util.*

class sharedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mRandom = Random()
    fun bindView(recipeModel: RecipeModel) {
        itemView.textRecipeTitle.text = recipeModel.recipeTitle
        itemView.imageRecipe.layoutParams.height = getRandomIntInRange(250, 150)

        itemView.setOnClickListener {
           // val intent = Intent(itemView.context, sharedRecipeInformaion::class.java)
           // startActivity(intent)
        }
        Glide.with(itemView.context).load(recipeModel.recipePicture!!).into(itemView.imageRecipe)
    }

    private fun getRandomIntInRange(max: Int, min: Int): Int {
        return mRandom.nextInt(max - min + min) + min
    }


}

