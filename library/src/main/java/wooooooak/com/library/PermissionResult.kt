package wooooooak.com.library

sealed class PermissionResult {

    object Granted : PermissionResult()

    class Denied(
        val deniedList: List<String>,
        val permanentlyDeniedList: List<String>
    ) : PermissionResult()

    override fun toString(): String {
        return when (this) {
            is Granted -> "All Permission granted"
            is Denied -> "Some Permission denied : \ndenied - $deniedList, \npermanentlyDeniedList-$permanentlyDeniedList"
        }
    }
}