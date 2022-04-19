package com.example.migrationdeeplinkrecipient

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.os.bundleOf
import com.example.migrationdeeplinkrecipient.ui.main.HeroFragment
import com.example.migrationdeeplinkrecipient.ui.main.model.MigrationData
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.gson.Gson
import java.net.URLDecoder

const val MIGRATION_DATA_KEY = "migrationData"
private const val DYNAMIC_LINKS_TAG = "DYNAMIC_LINKS_TAG"
private const val UTF_FORMAT = "UTF-8"

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)
		processDynamicLink()
		supportFragmentManager.beginTransaction().replace(R.id.container, HeroFragment()).commitNow()
	}

	private fun processDynamicLink() {
		processUnhandledDynamicLinks(intent) { result ->
			val uri = result.getOrNull()

			when {
				result.isSuccess && uri?.queryParameterNames?.contains(MIGRATION_DATA_KEY) == true -> {
					uri.getQueryParameter(MIGRATION_DATA_KEY)?.let {
						handleMigrationDynamicLinks(it)?.let { migrationData ->
							supportFragmentManager.beginTransaction()
								.replace(
									R.id.container,
									HeroFragment::class.java,
									bundleOf(MIGRATION_DATA_KEY to migrationData)
								).commitNow()
						}
					}
				}

				result.isFailure -> {
					Log.e(DYNAMIC_LINKS_TAG, "Unable to process External link")
				}
			}
		}
	}

	private fun handleMigrationDynamicLinks(migrationDataValue: String): MigrationData? = try {
		val paramValue = URLDecoder.decode(migrationDataValue, UTF_FORMAT)
		val jsonString = String(Base64.decode(paramValue, Base64.NO_WRAP), Charsets.UTF_8)
		Gson().fromJson(jsonString, MigrationData::class.java)
	} catch (exception: Exception) {
		Log.d(ContentValues.TAG, exception.toString())
		null
	}

	/**
	 * This will check for a Pending dynamic link from Firebase.
	 * Dynamic links can have the following situations:
	 *  1. Intent is null -> then we consider that there is nothing to process (Success + Uri == null)
	 *  2. There is no pending dynamic link -> nothing to process (Success + Uri == null)
	 *  3. There is a pending dynamic link but deep link is missing -> nothing to process (Success + Uri == null)
	 *  4. There is a pending dynamic link and deep link is present -> There is something to process (Success + Uri != null)
	 *  5. There is an error extracting the dynamic link -> Error occurred
	 */
	private fun processUnhandledDynamicLinks(intent: Intent?, resultHandler: (Result<Uri?>) -> Unit) {
		if (intent == null) {
			return resultHandler(Result.success(null))
		}
		FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
			.addOnSuccessListener { pendingDynamicLinkData: PendingDynamicLinkData? ->
				resultHandler(Result.success(pendingDynamicLinkData?.link))
			}
			.addOnFailureListener { exception ->
				resultHandler(Result.failure(exception))
			}
	}
}
