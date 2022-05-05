package com.example.migrationdynamiclinkrecipient.ui.main.model

import java.io.Serializable

data class MigrationData(
	val characterName: String,
	val race: String,
	val strength: String,
	val dexterity: String,
	val life: String,
	val avatarIcon: String
):Serializable
