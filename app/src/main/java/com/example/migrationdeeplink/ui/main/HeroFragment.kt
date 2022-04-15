package com.example.migrationdeeplink.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.migrationdeeplink.utils.DynamicLinkDataHelper.migrationDynamicLink
import com.example.migrationdeeplink.R
import com.example.migrationdeeplink.databinding.HeroFragmentBinding
import com.example.migrationdeeplink.utils.inputMethodManager
import com.example.migrationdeeplink.utils.setOnActionDoneListener
import com.google.android.material.textfield.TextInputLayout

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
		binding.heroName.setEditTextFocusListener {

		}
	}

	private fun addViewModelObservers() {
		viewModel.setMigrationData.observe(viewLifecycleOwner) {
			//handled in viewModel
		}
		viewModel.inputErrorToast.observe(viewLifecycleOwner) {
			it?.let { message ->
				Toast.makeText(context, message, Toast.LENGTH_LONG).show()
			}
		}
		binding.heroGoToAdventureBtn.setOnClickListener {
			if (viewModel.haveAllData()) {
				startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(migrationDynamicLink)))
			}
		}
	}
}

/**
 * Focus listener that updates Input state when focus on the [EditText] field in [TextInputLayout] has changed.
 * By default when view gain focus it's state is cleared by setting it to [InputState.Neutral]
 *
 * @param onFocusGained function to do additional action when focus is passed to the view.
 * @param onFocusLost function to handle focus lost. Current input is provided as parameter
 *
 * @see [InputState]
 * @see [TextInputLayout]
 */
fun TextInputLayout.setEditTextFocusListener(
	processImage: Boolean = true,
	onFocusGained: (() -> Unit)? = null,
	onFocusLost: (String) -> Unit
) {
	editText?.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
		val editTextView = view as? EditText ?: return@OnFocusChangeListener
		val input = editTextView.text?.toString().orEmpty()
		if (hasFocus) {
			editTextView.setSelection(input.length)
			editTextView.showKeyboard()
			onFocusGained?.invoke()
		} else {
			onFocusLost.invoke(input)
		}
	}
}

fun View.showKeyboard() {
	context?.inputMethodManager?.showSoftInput(this, 0)
}