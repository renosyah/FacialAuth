package com.renosyah.facialauth.util

import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class ImageRotation {
    companion object {

        fun getStreamByteFromImage(imageFile: File): ByteArray  {
            var photoBitmap = BitmapFactory.decodeFile(imageFile.getPath())
            val stream = ByteArrayOutputStream()
            val imageRotation = getImageRotation(imageFile)
            if (imageRotation != 0) photoBitmap =
                getBitmapRotatedByDegree(photoBitmap,imageRotation)
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        }


        private fun getImageRotation(imageFile: File): Int {
            var exif: ExifInterface? = null
            var exifRotation = 0
            try {
                exif = ExifInterface(imageFile.getPath())
                exifRotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return if (exif == null) 0 else exifToDegrees(exifRotation)
        }

        private fun exifToDegrees(rotation: Int): Int {
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90) return 90 else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) return 180 else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) return 270
            return 0
        }

        private fun getBitmapRotatedByDegree(bitmap: Bitmap, rotationDegree: Int): Bitmap {
            val matrix = Matrix()
            matrix.preRotate(rotationDegree.toFloat())
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }
}