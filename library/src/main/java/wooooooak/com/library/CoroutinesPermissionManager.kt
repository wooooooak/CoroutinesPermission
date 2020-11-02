package wooooooak.com.library

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import kotlinx.coroutines.CompletableDeferred
import wooooooak.com.library.model.PermissionRequest

class CoroutinesPermissionManager : Fragment() {
    private val completableDeferred: CompletableDeferred<PermissionResult> = CompletableDeferred()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            completableDeferred.complete(PermissionResult.Granted)
        } else {
            val deniedPermissionList = permissions.filterIndexed { index, _ ->
                grantResults[index] == PackageManager.PERMISSION_DENIED
            }
            val permanently = deniedPermissionList.filterNot {
                shouldShowRequestPermissionRationale(it)
            }
            completableDeferred.complete(PermissionResult.Denied(deniedPermissionList, permanently))
        }
    }

    fun checkPermission(requestModel: PermissionRequest) {
        val deniedPermissionArray = requestModel.permissionList.filterNot {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (deniedPermissionArray.isEmpty()) {
            completableDeferred.complete(PermissionResult.Granted)
        } else {
            if (deniedPermissionArray.any { shouldShowRequestPermissionRationale(it) } && requestModel.hasRationaleData) {
                showRationaleDialog(requireContext(), deniedPermissionArray, requestModel)
            } else {
                requestPermissions(deniedPermissionArray, REQUEST_CONST)
            }
        }
    }

    private fun showRationaleDialog(
        context: Context,
        permissionList: Array<String>,
        requestModel: PermissionRequest
    ) {
        AlertDialog.Builder(context)
            .setTitle(requestModel.rationale.title)
            .setMessage(requestModel.rationale.message)
            .setCancelable(false)
            .setPositiveButton(
                requestModel.rationale.confirmText
            ) { _, _ ->
                requestPermissions(permissionList, REQUEST_CONST)
            }
            .setNegativeButton(requestModel.rationale.cancelText) { _, _ ->
                completableDeferred.complete(PermissionResult.Denied(permissionList.toList(), listOf()))
            }
            .show()
    }

    companion object {
        private const val TAG = "PERMISSION_MANAGER"
        private const val REQUEST_CONST = 11

        suspend fun requestPermission(
            fragmentActivity: FragmentActivity,
            requestModel: PermissionRequest.() -> Unit
        ): PermissionResult {
            val permissionRequest = PermissionRequest().apply {
                requestModel()
            }
            val permissionManager = CoroutinesPermissionManager()
            val fragmentManager = fragmentActivity.supportFragmentManager
            fragmentManager.findFragmentByTag(TAG)?.let {
                fragmentManager.commitNow {
                    remove(it)
                }
            }
            fragmentManager.commitNow {
                add(permissionManager, TAG)
            }
            permissionManager.checkPermission(permissionRequest)
            return permissionManager.completableDeferred.await()
        }
    }
}