package com.example.paperwalls.models

import android.net.Uri

// ImageModel.kt

data class ImageModel(
    val id: Long,          // Unique identifier for the image
    val uri: Uri           // URI of the image
)

