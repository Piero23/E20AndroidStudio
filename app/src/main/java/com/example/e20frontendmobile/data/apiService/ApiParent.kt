package com.example.e20frontendmobile.data.apiService

import io.github.cdimascio.dotenv.dotenv

open class ApiParent {
    val ip : String= dotenv {
        directory = "/assets"
        filename = "env"
    }["IP"]
}