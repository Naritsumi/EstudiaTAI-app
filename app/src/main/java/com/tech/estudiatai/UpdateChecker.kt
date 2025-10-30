package com.tech.estudiatai

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

data class GitHubRelease(
    val tag_name: String,
    val html_url: String,
    val name: String?,
    val body: String?
)

object UpdateChecker {
    private const val TAG = "UpdateChecker"
    private const val REPO_API_URL = "https://api.github.com/repos/Naritsumi/EstudiaTAI-app/releases/latest"

    suspend fun checkForUpdates(currentVersion: String): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL(REPO_API_URL)
            val connection = url.openConnection() as HttpURLConnection

            connection.apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("Accept", "application/vnd.github.v3+json")
            }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val release = Gson().fromJson(response, GitHubRelease::class.java)

                val latestVersion = release.tag_name.removePrefix("v")
                val current = currentVersion.removePrefix("v")

                Log.d(TAG, "Current version: $current, Latest version: $latestVersion")

                if (isNewerVersion(latestVersion, current)) {
                    UpdateInfo(
                        latestVersion = latestVersion,
                        downloadUrl = release.html_url,
                        releaseNotes = release.body
                    )
                } else {
                    null
                }
            } else {
                Log.e(TAG, "Error: HTTP ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking for updates", e)
            null
        }
    }

    private fun isNewerVersion(latest: String, current: String): Boolean {
        try {
            val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
            val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

            val maxLength = maxOf(latestParts.size, currentParts.size)

            for (i in 0 until maxLength) {
                val latestPart = latestParts.getOrNull(i) ?: 0
                val currentPart = currentParts.getOrNull(i) ?: 0

                if (latestPart > currentPart) return true
                if (latestPart < currentPart) return false
            }

            return false
        } catch (e: Exception) {
            Log.e(TAG, "Error comparing versions", e)
            return false
        }
    }
}

data class UpdateInfo(
    val latestVersion: String,
    val downloadUrl: String,
    val releaseNotes: String?
)