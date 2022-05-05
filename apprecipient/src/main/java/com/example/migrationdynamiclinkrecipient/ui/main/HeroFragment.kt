package com.example.migrationdynamiclinkrecipient.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.migrationdynamiclinkrecipient.MIGRATION_DATA_KEY
import com.example.migrationdynamiclinkrecipient.R
import com.example.migrationdynamiclinkrecipient.databinding.HeroFragmentBinding
import com.example.migrationdynamiclinkrecipient.ui.main.model.MigrationData

class HeroFragment : Fragment() {

	private lateinit var binding: HeroFragmentBinding

	private lateinit var viewModel: HeroViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.hero_fragment, container, false)
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProvider(this)[HeroViewModel::class.java]
		binding.heroViewModel = viewModel
		(arguments?.get(MIGRATION_DATA_KEY) as? MigrationData)?.let { migrationData ->
			viewModel.setHeroData(migrationData)
		}
	}
}
