package com.example.migrationdeeplink.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.migrationdeeplink.DynamicLinkDataHelper
import com.example.migrationdeeplink.DynamicLinkDataHelper.migrationDynamicLink
import com.example.migrationdeeplink.DynamicLinkDataHelper.setMigrationDynamicLinkData
import com.example.migrationdeeplink.R
import com.example.migrationdeeplink.databinding.MainFragmentBinding

class MainFragment : Fragment() {

	private lateinit var binding: MainFragmentBinding

	private lateinit var viewModel: MainViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this)[MainViewModel::class.java]
		binding.migrationDeeplinkBtn.setOnClickListener {
			val name = binding.nameEt.text.toString()
			val sureName = binding.surnameEt.text.toString()
			val email = binding.emailEt.text.toString()
			val phone = binding.phoneEt.text.toString()
			val migrationData = DynamicLinkDataHelper.MigrationData(name, sureName, email, phone)
			setMigrationDynamicLinkData(migrationData)
			if (migrationDynamicLink.isEmpty()) return@setOnClickListener
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(migrationDynamicLink)))
		}
	}
}