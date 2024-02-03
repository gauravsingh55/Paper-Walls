package com.example.paperwalls.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paperwalls.R
import com.example.paperwalls.adapters.ImageAdapter
import com.example.paperwalls.databinding.FragmentHomeBinding
import com.example.paperwalls.models.ImageModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val OPEN_DIRECTORY_REQUEST_CODE = 123
    private var selectedDirectoryUri: Uri? = null

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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Check if directory is selected
        if (!isDirectorySelected(requireContext())) {


            // If not, initiate the document tree selection
            openDocumentTree()
        } else {
            // Directory already selected, proceed with your logic
            selectedDirectoryUri = getSelectedDirectory(requireContext())
            if (selectedDirectoryUri != null) {
                handleSelectedDirectory(selectedDirectoryUri!!)
            }
        }




        // Find Create a custom layout for the Toolbar
        val toolbar = binding.toolbar
        val customToolbar = layoutInflater.inflate(R.layout.custom_toolbar, null)
        toolbar.addView(customToolbar)


        return root
    }


    private fun openDocumentTree() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("HomeFragment", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("HomeFragment", "Selected directory URI: $uri")
                // Save the selected directory URI for future access
                selectedDirectoryUri = uri
                // Save the fact that the directory is selected
                saveDirectorySelectedStatus(requireContext(), true)
                saveSelectedDirectory(requireContext(), uri)
                // Handle the selected directory
                handleSelectedDirectory(uri)
            }
        }
    }


    private fun handleSelectedDirectory(uri: Uri) {
        // Use the document URI to access the selected directory
        val documentTree = DocumentFile.fromTreeUri(requireContext(), uri)

        // Example: List all files in the selected directory
        val imageList = documentTree?.listFiles()?.filter { file ->
            file.type?.startsWith("image/") == true
        }?.mapIndexed { index, file ->
            ImageModel(index.toLong(), file.uri)
        }

        if (imageList != null) {
            setupRecyclerView(imageList)
        }
    }

    private fun setupRecyclerView(imageList: List<ImageModel>) {
        imageAdapter = ImageAdapter(imageList)
        recyclerView.adapter = imageAdapter
    }

    private fun isDirectorySelected(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            "DirectoryPreferences",
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean("directorySelected", false)
    }

    private fun saveDirectorySelectedStatus(context: Context, status: Boolean) {
        val sharedPreferences = context.getSharedPreferences(
            "DirectoryPreferences",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean("directorySelected", status)
        editor.apply()
    }

    private fun getSelectedDirectory(context: Context): Uri? {
        val sharedPreferences = context.getSharedPreferences(
            "DirectoryPreferences",
            Context.MODE_PRIVATE
        )
        val uriString = sharedPreferences.getString("selectedDirectoryUri", null)
        return uriString?.let { Uri.parse(it) }
    }

    private fun saveSelectedDirectory(context: Context, uri: Uri) {
        val sharedPreferences = context.getSharedPreferences(
            "DirectoryPreferences",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("selectedDirectoryUri", uri.toString())
        editor.apply()
    }

}