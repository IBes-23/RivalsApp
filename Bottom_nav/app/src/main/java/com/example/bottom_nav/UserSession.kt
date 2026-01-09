object UserSession {
    var userId: String? = null
    var email: String? = null
    var uid: String? = null
    var username: String? = null
    var password: String? = null

    fun clear() {
        userId = null
        email = null
        uid = null
        username = null
        password = null
    }
}
