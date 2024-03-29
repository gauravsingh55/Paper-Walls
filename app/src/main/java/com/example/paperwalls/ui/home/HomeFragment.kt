package com.example.paperwalls.ui.home


import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paperwalls.R
import com.example.paperwalls.adapters.ImageAdapter
import com.example.paperwalls.databinding.FragmentHomeBinding
import com.example.paperwalls.models.ImageModel
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var imageList: List<ImageModel>
    private lateinit var recyclerView: RecyclerView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with directory picker
                pickDirectory()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(
                    requireContext(),
                    "Permission denied. Please grant the necessary permission in settings.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val pickDirectoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    val directoryPath = getDirectoryPath(uri)
                    imageList = getImagesFromDirectory(directoryPath,requireContext())
                    recyclerView = requireView().findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                    recyclerView.adapter = ImageAdapter(imageList)
                }
            }
        }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Request permission if not granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Permission already granted, proceed with directory picker
            pickDirectory()
        }


        // Find Create a custom layout for the Toolbar
        val toolbar = binding.toolbar
        val customToolbar = layoutInflater.inflate(R.layout.custom_toolbar, null)
        toolbar.addView(customToolbar)


        return root
    }

    private fun pickDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        pickDirectoryLauncher.launch(intent)
    }



    private fun getDirectoryPath(uri: Uri?): String {
        if (uri == null) {
            // Handle the case where the URI is null (no directory selected)
            Toast.makeText(requireContext(), "No directory selected", Toast.LENGTH_SHORT).show()
            return ""
        }

        try {
            // Get the last segment of the URI as the directory path
            val directoryPath = uri.lastPathSegment ?: ""

            if (directoryPath.isNotEmpty()) {
                // Split the path using ":" and get the segment after "primary"
                val segments = directoryPath.split(":")
                val pathAfterPrimary = if (segments.size > 1) segments[1] else segments[0]

                // Get the root directory of external storage
                val rootDirectory = Environment.getExternalStorageDirectory()

                // Append the directory path to get the absolute path
                val absolutePath = File(rootDirectory, pathAfterPrimary).absolutePath

                Toast.makeText(requireContext(), "DONE: $absolutePath", Toast.LENGTH_SHORT).show()

                return absolutePath
            }
        } catch (e: Exception) {
            // Handle any potential exceptions
        }

        // Handle the case where a file is selected instead of a directory
        Toast.makeText(requireContext(), "Please select a directory", Toast.LENGTH_SHORT).show()
        return ""
    }













    private fun getImagesFromDirectory(directoryPath: String, context: Context): List<ImageModel> {
        val images = mutableListOf<ImageModel>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )

        val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("$directoryPath%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor : Cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                val image = ImageModel(id, contentUri.toString())
                images.add(image)
            }
        }

        return images
    }
}