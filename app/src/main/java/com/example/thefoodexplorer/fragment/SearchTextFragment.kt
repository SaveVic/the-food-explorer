package com.example.thefoodexplorer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.databinding.FragmentSearchTextBinding

// the fragment initialization parameters
private const val ARG_QUERY = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchTextFragment : Fragment() {
    private var query: String? = null
    private var _binding: FragmentSearchTextBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param query of food search.
         * @return A new instance of fragment SearchTextFragment.
         */
        @JvmStatic
        fun newInstance(query: String) =
            SearchTextFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }
}