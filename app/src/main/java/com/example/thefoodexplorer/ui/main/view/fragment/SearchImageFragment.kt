package com.example.thefoodexplorer.ui.main.view.fragment

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.databinding.FragmentSearchImageBinding
import kotlin.Exception

// the fragment initialization parameters
private const val ARG_IMAGE = "image-path"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchImageFragment : Fragment() {
    private var imagePath: String? = null
    private var _binding: FragmentSearchImageBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString(ARG_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
//        TODO("Not yet implemented")
        try {
            val uri = Uri.parse(imagePath)
            binding.image.setImageURI(uri)
        }catch (e: Exception){
            binding.image.setBackgroundColor(resources.getColor(R.color.dark, null))
        }
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
         * @param imagePath
         * @return A new instance of fragment SearchImageFragment.
         */
        @JvmStatic
        fun newInstance(imagePath: String) =
            SearchImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE, imagePath)
                }
            }
    }
}