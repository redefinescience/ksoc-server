package com.kotlineering.ksoc.server.web

import com.kotlineering.ksoc.server.web.controllers.AuthController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.auth(authController: AuthController) {
    route("/authorize") {
        post { authController.authorizeUser(this.context) }
    }

    // TODO: this should be /profile/{userId} - also, authController/service should
    // check the base users table to see if the user even exists ...
    route("/profile/{userId}") {
        authenticate {
            put { authController.updateUserProfile(this.context) }
        }
    }

    route("/refresh") {
        post { authController.refresh(this.context) }
    }

    route("/revoke") {
        post { } // todo:
    }
}
