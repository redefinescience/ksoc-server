package com.kotlineering.ksoc.server.web

import com.kotlineering.ksoc.server.web.controllers.LoginController
import io.ktor.server.routing.*

fun Routing.login(loginController: LoginController) {
    route("/login") {
        post { loginController.login(this.context) }
    }
}
