package com.example.migrationdynamiclinkrecipient.ui.main

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

private const val GLIDE_REQUEST_TIMEOUT = 10000

@BindingAdapter("logo")
fun ImageView.setLogo(url: String?) {
	url?.let { loadImage(this, url.orEmpty()) }
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
