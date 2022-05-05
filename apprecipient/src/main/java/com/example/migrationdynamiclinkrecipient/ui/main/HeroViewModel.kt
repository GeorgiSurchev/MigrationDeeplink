package com.example.migrationdynamiclinkrecipient.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.migrationdynamiclinkrecipient.ui.main.model.MigrationData

private const val DEFAULT_CHARACTER_NAME = "Lord Voldemort"
private const val DEFAULT_RACE = "Dark Wizard"
private const val DEFAULT_HERO_ICON =
	"https://cdn.shopify.com/s/files/1/0025/9709/3489/products/Nemesis-Now-Lord-Voldemort-b5792u1-Harry-Potter_1_480x480.jpg?v=1631483688"
private const val DEFAULT_CHARACTER_DEX_STR = "500"
private const val DEFAULT_CHARACTER_LIFE = "7 Horcrux"

class HeroViewModel : ViewModel() {

	val characterName = MutableLiveData<String>()
	val heroIcon = MutableLiveData<String>()
	val strength = MutableLiveData<String>()
	val dexterity = MutableLiveData<String>()
	val life = MutableLiveData<String>()
	val race = MutableLiveData<String>()

	init {
		characterName.value = DEFAULT_CHARACTER_NAME
		race.value = DEFAULT_RACE
		heroIcon.value = DEFAULT_HERO_ICON
		strength.value = DEFAULT_CHARACTER_DEX_STR
		dexterity.value = DEFAULT_CHARACTER_DEX_STR
		life.value = DEFAULT_CHARACTER_LIFE
	}

	fun setHeroData(migrationData: MigrationData) {
		with(migrationData) {
			this@HeroViewModel.characterName.value = characterName
			this@HeroViewModel.race.value = race
			this@HeroViewModel.strength.value = strength
			this@HeroViewModel.dexterity.value = dexterity
			this@HeroViewModel.life.value = life
			this@HeroViewModel.heroIcon.value = avatarIcon
		}
	}
}
