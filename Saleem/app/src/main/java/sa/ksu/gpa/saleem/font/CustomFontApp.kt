package sa.ksu.gpa.saleem.font

import android.app.Application


class CustomFontApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/ArbFONTS-GE-SS-Two-Light_28.otf")
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/ArbFONTS-GE-SS-Two-Light_28.otf")
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/ArbFONTS-GE_SS_Two_Light-1.otf")
    } //onCreate
} //end class
