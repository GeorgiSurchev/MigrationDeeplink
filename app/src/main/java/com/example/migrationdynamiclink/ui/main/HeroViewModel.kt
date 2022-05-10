package com.example.migrationdynamiclink.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.migrationdynamiclink.ui.main.models.Hero
import com.example.migrationdynamiclink.utils.DynamicLinkDataHelper
import com.example.migrationdynamiclink.ui.main.models.Hero.Companion.getNextHeroByIcon
import com.example.migrationdynamiclink.ui.main.models.MigrationData
import com.example.migrationdynamiclink.utils.PairMediatorLiveData

private const val DEFAULT_STARTING_PINTS = "100"
private const val ZERO_POINTS = "0"
private const val SET_STARTING_POINTS_ERROR_TEXT = "Please set all of your starting points to proceed"
private const val SET_USERNAME_ERROR_TEXT = "Please set your Username to proceed"
private const val HERO_PROPERTY_POINT_STEP = 5

class HeroViewModel : ViewModel() {

	val characterName = MutableLiveData<String>()
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
		PairMediatorLiveData<Boolean, String>(haveStartingPoints, characterName).switchMap { mediatorState ->
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
			heroIcon.value = hero?.icon
			strength.value = hero?.strength
			dexterity.value = hero?.dexterity
			life.value = hero?.life
			race.value = hero?.name
		}
	}

	private fun setMigrationData(): MigrationData = MigrationData(
		characterName = characterName.value.orEmpty(),
		race = race.value.orEmpty(),
		strength = strength.value.orEmpty(),
		dexterity = dexterity.value.orEmpty(),
		life = life.value.orEmpty(),
		avatarIcon = heroIcon.value.orEmpty()
	)

	private fun verifyInputs(): Boolean {
		_inputErrorToast.value = when {
			haveStartingPoints.value == true -> SET_STARTING_POINTS_ERROR_TEXT
			characterName.value.isNullOrBlank() -> SET_USERNAME_ERROR_TEXT
			else -> null
		}
		return inputErrorToast.value == null
	}

	fun haveAllData() = verifyInputs() && DynamicLinkDataHelper.migrationDynamicLink.isNotBlank()
}

