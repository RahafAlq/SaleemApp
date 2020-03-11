package sa.ksu.gpa.saleem.profile

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.util.Log
import androidx.fragment.app.FragmentPagerAdapter

import sa.ksu.gpa.saleem.R
private val TAG = "profile"
private val TAB_TITLES = arrayOf(
   "tab_text_1",
    "tab_text_2"
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).


        Log.i("TextStats", "POSITION = " + position);
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return fragmentOne()
            }
            1 -> {
                return fragmentTwo()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return fragmentTwo()
            }

            else -> return fragmentTwo()
        }

        return fragmentOne.newInstance(position + 1)
    }

    // this is for fragment tabs


/*    override fun getPageTitle(position: Int): CharSequence? {

        return context.resources.getString(TAB_TITLES[position])
    }*/

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}