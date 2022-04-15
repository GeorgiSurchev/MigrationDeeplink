package com.example.migrationdeeplink.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.migrationdeeplink.ui.main.models.Hero
import com.example.migrationdeeplink.utils.DynamicLinkDataHelper
import com.example.migrationdeeplink.ui.main.models.Hero.Companion.getNextHeroByIcon
import com.example.migrationdeeplink.ui.main.models.MigrationData
import com.example.migrationdeeplink.utils.PairMediatorLiveData

private const val DEFAULT_STARTING_PINTS = "100"
private const val ZERO_POINTS = "0"
private const val SET_STARTING_POINTS_ERROR_TEXT = "Please set all of your starting points to proceed"
private const val SET_USERNAME_ERROR_TEXT = "Please set your Username to proceed"
private const val HERO_PROPERTY_POINT_STEP = 5

class HeroViewModel : ViewModel() {

	private var heroId = 0
	val userName = MutableLiveData<String>()
	val heroIcon = MutableLiveData<String>()
	val strength = MutableLiveData<String>()
	val dexterity = MutableLiveData<String>()
	val life = MutableLiveData<String>()
	val race = MutableLiveData<String>()
	val startingPoints = MutableLiveData<String>()

	private val _inputErrorToast = MutableLiveData<String>()
	val inputErrorToast: LiveData<String>
		get() = _inputErrorToast

	val haveStartingPoints = startingPoints.map {
		it != ZERO_POINTS
	}

	val haveFullStartingPoints = startingPoints.map {
		it == DEFAULT_STARTING_PINTS
	}

	private val migrationDataTrigger: LiveData<Boolean> =
		PairMediatorLiveData<Boolean, String>(haveStartingPoints, userName).switchMap { mediatorState ->
			val doNotHaveStartingPoints = mediatorState.first == false
			val haveUserName = mediatorState.second.isNullOrBlank().not()

			return@switchMap liveData {
				emit(doNotHaveStartingPoints && haveUserName)
			}
		}

	val setMigrationData = migrationDataTrigger.map {
		if (it) {
			DynamicLinkDataHelper.setMigrationDynamicLinkData(setMigrationData())
		}
	}

	fun lifeIncrease() {
		if (haveStartingPoints()) {
			removeFiveStartingPoints()
			life.value = (life.value?.toInt()?.plus(HERO_PROPERTY_POINT_STEP)).toString()
		}
	}

	fun dexterityIncrease() {
		if (haveStartingPoints()) {
			removeFiveStartingPoints()
			dexterity.value = (dexterity.value?.toInt()?.plus(HERO_PROPERTY_POINT_STEP)).toString()
		}
	}

	fun strengthIncrease() {
		if (haveStartingPoints()) {
			removeFiveStartingPoints()
			strength.value = (strength.value?.toInt()?.plus(HERO_PROPERTY_POINT_STEP)).toString()
		}
	}

	private fun haveStartingPoints() = haveStartingPoints.value == true

	private fun haveFullStartingPoints() = startingPoints.value == DEFAULT_STARTING_PINTS

	private fun removeFiveStartingPoints() {
		startingPoints.value = startingPoints.value?.toInt()?.minus(HERO_PROPERTY_POINT_STEP)?.toString()
	}

	init {
		heroId = Hero.Human.id
		heroIcon.value = Hero.Human.icon
		strength.value = Hero.Human.strength
		dexterity.value = Hero.Human.dexterity
		life.value = Hero.Human.life
		race.value = Hero.Human.name
		startingPoints.value = DEFAULT_STARTING_PINTS
	}

	fun changeHero() {
		if (haveFullStartingPoints()) {
			val hero = getNextHeroByIcon(heroIcon.value)
			heroId = hero?.id ?: 0
			heroIcon.value = hero?.icon
			strength.value = hero?.strength
			dexterity.value = hero?.dexterity
			life.value = hero?.life
			race.value = hero?.name
		}
	}

	private fun setMigrationData(): MigrationData = MigrationData(
		userName = userName.value.orEmpty(),
		id = heroId,
		strength = strength.value.orEmpty(),
		dexterity = dexterity.value.orEmpty(),
		life = life.value.orEmpty(),
		characterName = heroIcon.value.orEmpty(),
		race = race.value.orEmpty()
	)

	private fun verifyInputs(): Boolean {
		_inputErrorToast.value = when {
			haveStartingPoints.value == true -> SET_STARTING_POINTS_ERROR_TEXT
			userName.value.isNullOrBlank() -> SET_USERNAME_ERROR_TEXT
			else -> null
		}
		return inputErrorToast.value == null
	}

	fun haveAllData() = verifyInputs() && DynamicLinkDataHelper.migrationDynamicLink.isNotBlank()
}

