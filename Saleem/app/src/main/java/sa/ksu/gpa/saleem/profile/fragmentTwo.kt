package sa.ksu.gpa.saleem.profile

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*

import sa.ksu.gpa.saleem.R



class fragmentTwo : Fragment() {

    private val TAG = "DocSnippets"



    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val db = FirebaseFirestore.getInstance()
        //val userName=root.findViewById<View>(R.id.txt)


        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {


            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_fragment_two, container, false)
        val textView: TextView = root.findViewById(R.id.txt)



        pageViewModel.text.observe(this, Observer<String> {
             textView.text = "recipe"
        })
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): fragmentOne {
            return fragmentOne().apply {

                arguments = Bundle().apply {
                    putInt(fragmentTwo.ARG_SECTION_NUMBER, sectionNumber)

                }
            }
        }
    }

}
