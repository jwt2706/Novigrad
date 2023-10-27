class User(
        private val userName: String,
        private val email: String,
        private val password: String,
        private val driverLicense: String,
        private val role: String
    ) {

        fun getUserName(): String {
            return userName
        }

        fun getEmail(): String {
            return email
        }

        fun getPassword(): String {
            return password
        }

        fun getDriverLicense(): String {
            return driverLicense
        }

        fun getRole(): String {
            return role
        }
    }

