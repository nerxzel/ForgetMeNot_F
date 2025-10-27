package com.example.forgetmenot.ui.camera

import android.Manifest
import android.net.Uri
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: CameraXBasicViewModel = viewModel(),
    onPhotoTaken: (Uri) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val imageCaptureCallbackExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            imageCaptureCallbackExecutor.shutdown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ContentWithPermissionHandling(
            cameraPermissionState = cameraPermissionState,
            viewModel = viewModel,
            imageCaptureCallbackExecutor = imageCaptureCallbackExecutor,
            onPhotoTaken = { uri ->
                if (uri != null) {
                    onPhotoTaken(uri)
                }
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ContentWithPermissionHandling(
    cameraPermissionState: PermissionState,
    viewModel: CameraXBasicViewModel,
    imageCaptureCallbackExecutor: ExecutorService,
    modifier: Modifier = Modifier,
    onPhotoTaken: (Uri?) -> Unit
) {
    val context = LocalContext.current
    when (cameraPermissionState.status) {
        is PermissionStatus.Granted -> {
            CameraPreviewContent(
                viewModel = viewModel,
                modifier = modifier,
                lifecycleOwner = LocalLifecycleOwner.current,
                onTakePhotoClick = {
                    viewModel.takePhoto(
                        context = context,
                        callbackExecutor = imageCaptureCallbackExecutor,
                        onImageCaptured = onPhotoTaken,
                        onError = { /* Error */ },
                    )
                },
            )
        }

        is PermissionStatus.Denied -> CameraPermissionDeniedView(
            status = cameraPermissionState.status,
            cameraPermissionState = cameraPermissionState,
            modifier = modifier
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun CameraPermissionDeniedView(
    status: PermissionStatus,
    cameraPermissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val textToShow = if (status.shouldShowRationale) {
            "La cámara es necesaria para tomar fotos. Por favor, concede el permiso."
        } else {
            "El permiso de cámara es requerido para usar esta función."
        }
        Text(text = textToShow)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
            Text("Solicitar Permiso")
        }
    }
}

@Composable
private fun CameraPreviewContent(
    viewModel: CameraXBasicViewModel,
    modifier: Modifier = Modifier,
    onTakePhotoClick: () -> Unit,
    lifecycleOwner: LifecycleOwner,
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }

    val autofocusRequestId = autofocusRequest.first
    val showAutofocusIndicator = autofocusRequest.second.isSpecified
    val autofocusLocation = remember(autofocusRequestId) { autofocusRequest.second }

    if (showAutofocusIndicator) {
        LaunchedEffect(autofocusRequestId) {
            kotlinx.coroutines.delay(1000)
            autofocusRequest = autofocusRequestId to Offset.Unspecified
        }
    }

    surfaceRequest?.let { request ->
        val coordinateTransformer = remember { MutableCoordinateTransformer() }
        Box(modifier = modifier.fillMaxSize()) {
            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = coordinateTransformer,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(viewModel, coordinateTransformer) {
                        detectTapGestures { tapCoords ->
                            with(coordinateTransformer) {
                                viewModel.tapToFocus(tapCoords.transform())
                            }
                            autofocusRequest = UUID.randomUUID() to tapCoords
                        }
                    },
            )

            AnimatedVisibility(
                visible = showAutofocusIndicator,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .offset { autofocusLocation.takeOrElse { Offset.Zero }.round() }
                    .offset((-24).dp, (-24).dp),
            ) {
                Spacer(
                    Modifier
                        .border(2.dp, Color.White, CircleShape)
                        .size(48.dp),
                )
            }

            Button(
                onClick = onTakePhotoClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            ) { Text("Tomar Foto") }
        }
    }
}