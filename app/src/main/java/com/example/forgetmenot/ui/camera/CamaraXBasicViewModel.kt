package com.example.forgetmenot.ui.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService

class CameraXBasicViewModel : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null

    private var cameraControl: CameraControl? = null

    private val previewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat(),
            )
        }
    }

    private val imageCaptureUseCase = ImageCapture.Builder().build()

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(previewUseCase)
            .addUseCase(imageCaptureUseCase)
            .build()

        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = DEFAULT_BACK_CAMERA,
            useCaseGroup = useCaseGroup,
        )

        cameraControl = camera.cameraControl

        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
        }
    }

    fun tapToFocus(coordinates: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(coordinates.x, coordinates.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }

    fun takePhoto(
        context: Context,
        callbackExecutor: ExecutorService,
        onImageCaptured: (Uri?) -> Unit,
        onError: (ImageCaptureException) -> Unit,
    ) {
        val name = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.US,
        ).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraXBasic")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues,
            )
            .build()

        imageCaptureUseCase.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            ImageSavedCallback(context, onImageCaptured, onError),
        )
    }
}

private class ImageSavedCallback(
    private val context: Context,
    private val onImageCaptured: (Uri?) -> Unit,
    private val onErrorA: (ImageCaptureException) -> Unit,
) : ImageCapture.OnImageSavedCallback {
    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
        val savedUri = output.savedUri
        Log.d("CameraXComposeApp", "Photo capture succeeded: $savedUri")
        onImageCaptured(savedUri)
    }

    override fun onError(exc: ImageCaptureException) {
        Log.e("CameraXComposeApp", "Photo capture failed: ${exc.message}", exc)
        ContextCompat.getMainExecutor(context).execute {
            Toast.makeText(context, "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT)
                .show()
        }
        onErrorA(exc)
    }
}