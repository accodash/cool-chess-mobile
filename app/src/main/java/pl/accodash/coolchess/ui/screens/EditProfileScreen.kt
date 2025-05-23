package pl.accodash.coolchess.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.accodash.coolchess.R
import pl.accodash.coolchess.api.CoolChessServices
import pl.accodash.coolchess.api.models.User
import pl.accodash.coolchess.api.services.UpdateUserRequest

@Composable
fun EditProfileScreen(
    services: CoolChessServices,
    onProfileUpdated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var username by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        user = services.userService.getCurrentUser()
        username = user?.username ?: ""
        imageUrl = user?.imageUrl
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else if (imageUrl != null) {
            AsyncImage(
                model = user?.getBackendImageUrl(),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { imageLauncher.launch("image/*") }
        ) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.upload_image))
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    try {
                        var uploadedImageUrl = imageUrl

                        selectedImageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val file = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
                            file?.let {
                                val part = MultipartBody.Part.createFormData(
                                    "avatar",
                                    "profile.jpg",
                                    it
                                )
                                val response = services.userService.uploadUserAvatar(part)
                                uploadedImageUrl = response.imageUrl
                            }
                        }

                        services.userService.updateCurrentUser(
                            UpdateUserRequest(username = username, imageUrl = uploadedImageUrl)
                        )
                        onProfileUpdated()
                    } catch (e: Exception) {
                        Log.e("EditProfile", "Failed to update profile", e)
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text(stringResource(R.string.save_changes))
        }
    }
}
