package wooooooak.com.library.model

data class PermissionRequest(
    var permissionList: List<String> = listOf()
) {
    val hasRationaleData: Boolean
        get() = rationale.title != null || rationale.message != null

    var rationale: Rationale = Rationale()
        private set

    fun Rationale(init: Rationale.() -> Unit) {
        rationale = rationale.apply {
            init()
        }
    }
}

data class Rationale(
    var title: String? = null,
    var message: String? = null,
    var confirmText: String? = "OK",
    var cancelText: String? = "cancel"
)
