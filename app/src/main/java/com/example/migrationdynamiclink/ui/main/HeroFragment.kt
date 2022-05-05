package com.example.migrationdynamiclink.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.migrationdynamiclink.R
import com.example.migrationdynamiclink.databinding.HeroFragmentBinding
import com.example.migrationdynamiclink.utils.DynamicLinkDataHelper.migrationDynamicLink
import com.example.migrationdynamiclink.utils.setOnActionDoneListener

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
		addViewModelObservers()
		binding.heroNameEt.setOnActionDoneListener {
			it.clearFocus()
		}
	}

	private fun addViewModelObservers() {
		viewModel.setMigrationData.observe(viewLifecycleOwner) {
			//handled in viewModel
		}
		viewModel.inputErrorToast.observe(viewLifecycleOwner) {
			it?.let { message ->
				Toast.makeText(context, message, Toast.LENGTH_LONG	).show()
			}
		}
		binding.heroGoToAdventureBtn.setOnClickListener {
			if (viewModel.haveAllData()) {
				val intent = Intent(Intent.ACTION_VIEW, Uri.parse(migrationDynamicLink)).apply {
					flags = Intent.FLAG_ACTIVITY_NEW_TASK
				}
				startActivity(intent)
			}
		}
	}
}
