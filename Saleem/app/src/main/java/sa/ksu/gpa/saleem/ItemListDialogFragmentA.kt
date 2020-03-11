package sa.ksu.gpa.saleem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.collections.ArrayList

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 *
 * You activity (or fragment) needs to implement [ItemListDialogFragmentA.Listener].
 */
class ItemListDialogFragmentA(list: ArrayList<String>) : BottomSheetDialogFragment() {
    var list = list;
    private var mListener: Listener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    fun newInstance(list: ArrayList<String>): ItemListDialogFragmentA {
        return ItemListDialogFragmentA(list)
    }
    //onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        view.findViewById<View>(R.id.btn_cancel)
            .setOnClickListener { dismiss() }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(list
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //        final Fragment parent = getParentFragment();
//        if (parent != null) {
//            mListener = (Listener) parent;
//        } else {
//            mListener = (Listener) context;
//        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    fun setOnSelectData(mListener: Listener?) {
        this.mListener = mListener
    }

    interface Listener {
        fun onItemClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.fragment_item_list_dialog_item,
            parent,
            false
        )
    ) {
        val text: TextView
        val text_hint: TextView
        val viewButtom: View
        val linearLayout: LinearLayout

        init { // TODO: Customize the item layout
            text = itemView.findViewById<View>(R.id.text) as TextView
            viewButtom = itemView.findViewById(R.id.view)
            text_hint = itemView.findViewById(R.id.text_hint)
            linearLayout = itemView.findViewById(R.id.linearLayout)
            linearLayout.setOnClickListener { v: View? ->
                if (mListener != null) {
                    mListener!!.onItemClicked(adapterPosition)
                    dismiss()
                }
                dismiss()
            }
        }
    }

    private inner class ItemAdapter(var list: List<String>) :
        RecyclerView.Adapter<ViewHolder>() {
        private val mItemCount: Int
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context),
                parent
            )
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) { //            holder.text.setText(String.valueOf(position));
            holder.text.text = list[position]
            holder.text_hint.text =""
            if (position == itemCount - 1) {
                holder.viewButtom.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return mItemCount
        }

        init {
            mItemCount = list.size
        }
    }

    companion object {
        private const val ARG_ITEM_COUNT = "item_count"
        private const val ARG_ITEM_DATA = "item_data"
        fun newInstance(itemData: ArrayList<String>?): ItemListDialogFragmentA? {
            val fragment = itemData?.let { ItemListDialogFragmentA(it) }
            val args = Bundle()
            //        args.putInt(ARG_ITEM_COUNT, itemCount);
//            args.putStringArrayList(ARG_ITEM_DATA, itemData)
//            fragment.arguments = args
            return fragment
        }
    }
}