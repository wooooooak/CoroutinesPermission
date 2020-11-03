package wooooooak.com.coroutinepermission

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.coroutines.flow.collect
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
                    Rationale {
                        title = "This is Rationale Title."
                        message = "Please Press Ok Button!"
                    }
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

        button2.setOnClickListener {
            lifecycleScope.launch {
                CoroutinesPermissionManager
                    .requestPermissionFlow(this@SampleActivity) {
                        permissionList = _permissionList
                        Rationale {
                            title = "This is Rationale Title."
                            message = "Please Press Ok Button!"
                        }
                    }.collect {
                        when (it) {
                            is PermissionResult.Granted -> {
                                println(it)
                            }
                            is PermissionResult.Denied -> {
                                println(it.deniedList)
                                println(it.permanentlyDeniedList)
                            }
                        }
                        Toast.makeText(this@SampleActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}
