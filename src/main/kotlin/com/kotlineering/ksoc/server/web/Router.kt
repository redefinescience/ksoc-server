package com.kotlineering.ksoc.server.web

import com.kotlineering.ksoc.server.web.controllers.AuthController
import io.ktor.server.routing.*

fun Routing.auth(authController: AuthController) {
    route("/authorize") {
        post { authController.authorizeUser(this.context) }
    }
    route("/profile") {
        put { authController.updateUserProfile(this.context) }
    }
}
