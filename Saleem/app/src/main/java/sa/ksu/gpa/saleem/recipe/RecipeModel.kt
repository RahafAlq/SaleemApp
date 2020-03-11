package sa.ksu.gpa.saleem.recipe


data class RecipeModel(var recipeId: Int,
                      var recipeTitle: String,
                      var recipePicture: String?) {
    constructor() : this(0, "", null)
}