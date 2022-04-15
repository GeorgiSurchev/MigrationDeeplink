package com.example.migrationdeeplink.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

private const val GLIDE_REQUEST_TIMEOUT = 10000

val Context.inputMethodManager: InputMethodManager?
	get() = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

fun View.hideKeyboard(keepFocus: Boolean = false) {
	if (keepFocus.not()) {
		clearFocus()
	}

	context?.inputMethodManager?.hideSoftInputFromWindow(applicationWindowToken, 0)
}

@BindingAdapter("logo")
fun ImageView.setLogo(url: String?) {
	url?.let {
		loadImage(this, url.orEmpty())
	}
}

/**
 * Loads image from url
 * @param targetView the receiver view
 * @param url to download from
 */
fun loadImage(
	targetView: ImageView,
	url: String
) {

	Glide.with(targetView.context).load(url).timeout(GLIDE_REQUEST_TIMEOUT).apply(
		RequestOptions().override(
			Target.SIZE_ORIGINAL,
			Target.SIZE_ORIGINAL
		)
	).listener(object : RequestListener<Drawable> {
		override fun onLoadFailed(
			exception: GlideException?,
			model: Any?,
			target: Target<Drawable>?,
			isFirstResource: Boolean
		): Boolean {
			return false
		}

		override fun onResourceReady(
			resource: Drawable?, model: Any?,
			target: Target<Drawable>?,
			dataSource: DataSource,
			isFirstResource: Boolean
		): Boolean {
			return false
		}
	}).into(targetView)
}

@BindingAdapter("visibleGone")
fun View.showHide(show: Boolean) {
	visibility = if (show) View.VISIBLE else View.GONE
}

/**
 * Set a callback for when actionDone is performed from the soft keyboard.
 *
 * @param action the callback to be invoked when actionDone is performed.
 */
fun EditText.setOnActionDoneListener(action: (view: View) -> Unit) {
	setOnEditorActionListener { view, actionId, _ ->
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			view?.hideKeyboard(keepFocus = true)
			action(view)
			return@setOnEditorActionListener true
		}
		false
	}
}
