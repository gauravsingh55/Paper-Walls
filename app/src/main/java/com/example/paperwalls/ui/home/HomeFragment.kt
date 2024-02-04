package com.example.paperwalls.ui.home


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager


import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paperwalls.R
import com.example.paperwalls.adapters.ImageAdapter
import com.example.paperwalls.databinding.FragmentHomeBinding
import com.example.paperwalls.models.ImageModel
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val directoryPath = Environment.getExternalStorageDirectory().absolutePath + "/Wallpapers"


    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permission granted, access the directory
                    accessDirectory()
                } else {
                    // Permission denied
                    // Handle the denial case as needed
                }
            }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            accessDirectory()
        } else {
            // Request permission
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }





        // Find Create a custom layout for the Toolbar
        val toolbar = binding.toolbar
        val customToolbar = layoutInflater.inflate(R.layout.custom_toolbar, null)
        toolbar.addView(customToolbar)


        return root
    }

    private fun populateRecyclerView(imageList: List<ImageModel>) {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        val adapter = ImageAdapter(imageList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }


    private fun accessDirectory() {
        val directory = File(directoryPath)
        if (directory.exists() && directory.isDirectory) {
            val imageList = mutableListOf<ImageModel>()

            // Filter only image files (you can adjust the extensions based on your needs)
            val imageFiles = directory.listFiles { file ->
                file.isFile && file.extension in arrayOf("jpg", "jpeg", "png")
            }

            imageFiles?.forEachIndexed { index, file ->
                val imageUri = Uri.fromFile(file)
                val imageModel = ImageModel(index.toLong(), imageUri)
                imageList.add(imageModel)
            }

            // Now, you have a list of ImageModel objects containing image URIs
            // You can use this list to populate your RecyclerView
            populateRecyclerView(imageList)
        } else {
            // Handle the case where the directory doesn't exist
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            // Handle the result from SAF
            data?.data?.also { uri ->
                // Use the URI to access the selected directory
                // ...
            }
        }
    }

    companion object {
        private const val OPEN_DIRECTORY_REQUEST_CODE = 42 // Any unique value
    }








}