package com.example.migrationdynamiclink.utils

import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.migrationdynamiclink.ui.main.models.MigrationData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

object DynamicLinkDataHelper {

	private const val MIGRATION_DYNAMIC_LINK_DOMAIN = "https://dynamiclinkrecipient.page.link/?link="
	private const val MIGRATION_DEEPLINK_FALLBACK_URL = "https://www.google.com"
	private const val MIGRATION_DATA_KEY = "?migrationData="
	private const val MIGRATION_DYNAMIC_LINK_PACKAGE_NAME = "&apn=com.example.migrationdynamiclinkrecipient"

	var migrationDynamicLink = ""

	fun setMigrationDynamicLinkData(migrationData: MigrationData) {
		val jsonString = Gson().toJson(migrationData)
		val encodedMigrationData = String(Base64.encode(jsonString.toByteArray(), Base64.NO_WRAP), Charsets.UTF_8)
		val migrationLink = createMigrationLink(encodedMigrationData)
		Firebase.dynamicLinks.shortLinkAsync {
			longLink = Uri.parse(migrationLink)
		}.addOnSuccessListener {
			migrationDynamicLink = it.shortLink.toString()
		}.addOnFailureListener {
			Log.d("migrationLinkFailed", it.toString())
		}
	}

	private fun createMigrationLink(encodedMigrationData: String) = StringBuilder().append(
		MIGRATION_DYNAMIC_LINK_DOMAIN
	).append(
		MIGRATION_DEEPLINK_FALLBACK_URL
	).append(
		MIGRATION_DATA_KEY
	).append(
		encodedMigrationData
	).append(
		MIGRATION_DYNAMIC_LINK_PACKAGE_NAME
	).toString()

}
