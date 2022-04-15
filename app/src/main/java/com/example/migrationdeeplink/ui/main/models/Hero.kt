package com.example.migrationdeeplink.ui.main.models

enum class Hero(val id: Int, val strength: String, val dexterity: String, val life: String, val icon: String) {
	Human(
		1,
		strength = "100",
		dexterity = "100",
		life = "400",
		icon = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvL0_QSYb8eSEWx-IKbaSvq8_FZasfSYPpUw&usqp=CAU"
	),
	Elf(
		2,
		strength = "50",
		dexterity = "150",
		life = "300",
		icon = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4VZJwaKf8iuNg-tVOiP_VR3efvfbOOA5R5w&usqp=CAU"
	),
	Orc(
		3,
		strength = "150",
		dexterity = "50",
		life = "600",
		icon = "https://icon-library.com/images/orc-icon/orc-icon-18.jpg"
	),
	Demon(
		4,
		strength = "70",
		dexterity = "70",
		life = "320",
		icon = "https://png.pngtree.com/png-vector/20201223/ourlarge/pngtree-red-devil-cartoon-image-png-image_2592039.jpg"
	);

	companion object {

		fun getNextHeroByIcon(icon: String?): Hero? {
			val nextHeroId = values().firstOrNull { it.icon == icon }?.id?.plus(1).takeIf { it != 5 } ?: 1
			return values().firstOrNull { it.id == nextHeroId }
		}
	}
}
