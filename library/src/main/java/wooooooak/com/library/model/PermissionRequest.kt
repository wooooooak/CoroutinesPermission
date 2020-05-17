package wooooooak.com.library.model

data class PermissionRequest(
    var permissionList: List<String> = listOf(),
    var rationaleTitle: String? = null,
    var rationaleMessage: String? = null,
    var rationaleConfirmText: String = "OK",
    var rationaleCancelText: String = "cancel"
) {
    val hasRationaleData: Boolean
        get() = rationaleTitle != null || rationaleMessage != null
}