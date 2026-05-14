package com.shaale_vikas.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream =
                context.contentResolver.openInputStream(uri) ?: return null

            val original = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val maxSize = 400

            val ratio = minOf(
                maxSize.toFloat() / original.width,
                maxSize.toFloat() / original.height
            )

            val width = (original.width * ratio).toInt()
            val height = (original.height * ratio).toInt()

            val scaled =
                Bitmap.createScaledBitmap(original, width, height, true)

            val outputStream = ByteArrayOutputStream()

            scaled.compress(
                Bitmap.CompressFormat.JPEG,
                60,
                outputStream
            )

            Base64.encodeToString(
                outputStream.toByteArray(),
                Base64.DEFAULT
            )

        } catch (e: Exception) {
            null
        }
    }

    fun loadBase64(
        base64: String,
        imageView: android.widget.ImageView
    ) {
        if (base64.isEmpty()) return

        try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )

            imageView.setImageBitmap(bitmap)

        } catch (e: Exception) {

        }
    }
}