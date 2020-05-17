package wooooooak.com.coroutinepermission

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.coroutines.launch
import wooooooak.com.library.CoroutinesPermissionManager
import wooooooak.com.library.PermissionResult

class SampleActivity : AppCompatActivity() {

    private val _permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        button.setOnClickListener {
            lifecycleScope.launch {
                val result = CoroutinesPermissionManager.requestPermission(this@SampleActivity) {
                    permissionList = _permissionList
                    rationaleTitle = "Alert"
                    rationaleMessage = "Please Press Ok Button!"
                }
                when (result) {
                    is PermissionResult.Granted -> {
                        println(result)
                    }
                    is PermissionResult.Denied -> {
                        println(result.deniedList)
                        println(result.permanentlyDeniedList)
                    }
                }
                Toast.makeText(this@SampleActivity, result.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}
