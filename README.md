# Migration Dynamic Link 

application names: **Hero** and **HeroAdventure**

# Migration Dynamic Link app (Hero)

In MigrationDynamicLink (**Hero**) app you can create your character by choosing his skills and race. 
All entered data will be migrated to the MigrationDynamicLinkRecipient (**Hero Adventure**) app via firebase dynamic link.

## First steps:

- **Create firebase account:** https://firebase.google.com/
- **Create project:** Fallow the steps.
- **Go to Dynamic Links section and click  Get Started (<a href ="https://firebase.google.com/docs/dynamic-links/?authuser=0#implementation_paths">documentation</a>):**

add your domain here (in my app is https://dynamiclinkrecipient.page.link) :

![1](https://user-images.githubusercontent.com/54105945/163956410-a433b4c3-65ea-4ddf-a075-11e106a64506.png)


## Add firebase to your Android apps (<a href ="https://firebase.google.com/docs/android/setup?authuser=0&hl=en">documentation</a>):

**Go to Project Overview and click Project Settings:**

Fallow the steps.

![2](https://user-images.githubusercontent.com/54105945/163983144-7eedf911-804d-40f2-a0c0-9d727158bec8.png)

**Note: You must add both applications to the firebase**

## Dynamic link creation Instructions (<a href ="https://firebase.google.com/docs/dynamic-links/create-links?authuser=0">documentation</a>):

**In Migration Dynamic Link app (Hero) go to DynamicLinkDataHelper.**

**When creating the dynamic link prefix, you must add the same domain that you created in firebase. Also we need to add the package name to the app we are going to target. We will do this when we add the dynamic link affix :**

  ```
	private const val MIGRATION_DYNAMIC_LINK_DOMAIN = "https://dynamiclinkrecipient.page.link/?link="

	private const val MIGRATION_DYNAMIC_LINK_TARGET_PACKAGE_NAME = "&apn=com.example.migrationdynamiclinkrecipient"
  ```
  
**We convert the data to json string and then encrypt it in Base64 format :**

  ```
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
  ```
  
  **This is an example of full dynamic link structore :**
  
  ```
	private fun createMigrationLink(encodedMigrationData: String) = StringBuilder().append(
		MIGRATION_DYNAMIC_LINK_DOMAIN
	).append(
		MIGRATION_DEEPLINK_FALLBACK_URL
	).append(
		MIGRATION_DATA_KEY
	).append(
		encodedMigrationData
	).append(
		MIGRATION_DYNAMIC_LINK_TARGET_PACKAGE_NAME
	).toString()
  ```

## Migrate Data to Another App

**When we have all data that we needed to start an Intent that contains our dynamic link :**

```
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(migrationDynamicLink)).apply {
	flags = Intent.FLAG_ACTIVITY_NEW_TASK
}
startActivity(intent)     
```

## What happent next?

- **When intent is passet and we have the target app installed a window is shown whith options to chose :**

![5](https://user-images.githubusercontent.com/54105945/164187046-cc30655d-e868-4148-ba11-32340c1eedff.png)

When open* the app we will see all migrated data.

- **If we don't have app installed we will redirectet do Google Play Store from where to install the target application.**

When the application is installed*  we will see all migrated data.

*receive Intent arguments from migration dynamic link app (Hero) in the Main activity.

# Migration Dynamic Link Recipient app (Hero Adventure)

## Catching the dynamic link

- **We must declare dynamic link domain in Migration Dynamic Link Recipient app (Hero Adventure) Manifest**:

```
<activity
	android:exported="true"
	android:name=".MainActivity">

	<intent-filter>
		<action android:name="android.intent.action.VIEW" />
		<category android:name="android.intent.category.DEFAULT" />
		<category android:name="android.intent.category.BROWSABLE" />

		<data
			android:host="dynamiclinkrecipient.page.link"
			android:scheme="https" />
	</intent-filter>
</activity>
```

- **Dynamic link handling**

In Activity "**OnCreate**" we catch the dynamic link Intent then we procces with handling :

```
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
  
```
  
**If the result is succseed and our Dynamic Link contains the exact key we can decode them :**

```

	private fun processDynamicLink() {
		processUnhandledDynamicLinks(intent) { result ->
			val uri = result.getOrNull()

			when {
				result.isSuccess && uri?.queryParameterNames?.contains(MIGRATION_DATA_KEY) == true -> {
					uri.getQueryParameter(MIGRATION_DATA_KEY)?.let {
						handleMigrationDynamicLinks(it)?.let { migrationData ->

						}
					}
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

```

**That's all, i wish you a successful migration!**

      
