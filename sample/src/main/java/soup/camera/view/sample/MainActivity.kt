package soup.camera.view.sample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (allPermissionsGranted(this)) {
            startCamera()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {
        val previewView: ImageView = findViewById(R.id.previewView)
        val cameraView: CameraView = findViewById(R.id.cameraView)
        cameraView.bindToLifecycle(this)
        cameraView.setAnalyzer { proxy ->
            proxy.use {
                it.image?.use { mediaImage ->
                    val rotation = when (it.imageInfo.rotationDegrees) {
                        0 -> FirebaseVisionImageMetadata.ROTATION_0
                        90 -> FirebaseVisionImageMetadata.ROTATION_90
                        180 -> FirebaseVisionImageMetadata.ROTATION_180
                        270 -> FirebaseVisionImageMetadata.ROTATION_270
                        else -> FirebaseVisionImageMetadata.ROTATION_0
                    }
                    val bitmap = FirebaseVisionImage.fromMediaImage(mediaImage, rotation).bitmap
                    runOnUiThread {
                        previewView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted(this)) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
