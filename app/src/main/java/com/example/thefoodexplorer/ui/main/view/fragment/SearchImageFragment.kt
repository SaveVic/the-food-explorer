package com.example.thefoodexplorer.ui.main.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thefoodexplorer.R
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ActivityHomeBinding
import com.example.thefoodexplorer.databinding.ContentHomeBinding
import com.example.thefoodexplorer.databinding.FragmentSearchImageBinding
import com.example.thefoodexplorer.ui.base.ViewModelFactory
import com.example.thefoodexplorer.ui.main.adapter.FoodQueryListAdapter
import com.example.thefoodexplorer.ui.main.view.FoodDetailActivity
import com.example.thefoodexplorer.ui.main.viewmodel.HomeViewModel
import com.example.thefoodexplorer.util.ApiResponseType
import java.io.File

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
    private var viewModel: HomeViewModel? = null
    private var adapter: FoodQueryListAdapter? = null

    private val binding get() = _binding!!
    private var emptyMsg = ""
    private var errorMsg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString(ARG_IMAGE)
        }
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = activity?.let {
            ViewModelProvider(
                it,
                ViewModelFactory.getInstance()
            )[HomeViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupObserver() {
        imagePath?.let { path ->
            val file = File(path)
            viewModel?.getFoodByImage(file)?.observe(viewLifecycleOwner, { result ->
                when (result.type) {
                    ApiResponseType.SUCCESS -> {
                        renderList(result.data)
                    }
                    ApiResponseType.LOADING -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.empty.root.visibility = View.GONE
                        binding.rv.visibility = View.GONE
                    }
                    else -> {
                        val err = result.msg ?: ""
                        val msg = "$errorMsg\n$err"
                        binding.empty.msg.text = msg
                        binding.loading.visibility = View.GONE
                        binding.empty.root.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun setupUI() {
        emptyMsg = resources.getString(R.string.msg_list_empty)
        errorMsg = resources.getString(R.string.msg_list_error)
        try {
            val uri = Uri.parse(imagePath)
            binding.image.setImageURI(uri)
        } catch (e: Exception) {
            binding.image.setBackgroundColor(resources.getColor(R.color.dark, null))
        }
        binding.rv.layoutManager = GridLayoutManager(activity, 2)
        adapter = FoodQueryListAdapter(arrayListOf())
        binding.rv.adapter = adapter
        adapter?.setOnItemClickCallback(object : FoodQueryListAdapter.OnItemClickCallback {
            override fun onItemClicked(food: FoodQuery) {
                onNavigateDetailFood(food)
            }
        })
    }

    private fun onNavigateDetailFood(food: FoodQuery) {
        val intent = Intent(activity, FoodDetailActivity::class.java)
        intent.putExtra(FoodDetailActivity.DATA, food)
        startActivity(intent)
    }

    private fun renderList(data: List<FoodQuery>?) {
        val len = data?.size ?: 0
        adapter?.replaceList(data ?: listOf())
        adapter?.notifyDataSetChanged()
        if (len > 0) {
            binding.loading.visibility = View.GONE
            binding.empty.root.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE

        } else {
            binding.empty.msg.text = emptyMsg
            binding.loading.visibility = View.GONE
            binding.empty.root.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
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