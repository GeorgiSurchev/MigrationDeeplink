package com.example.migrationdeeplink

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.migrationdeeplink.ui.main.HeroFragment
import com.example.migrationdeeplink.utils.hideKeyboard

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)
		supportFragmentManager.beginTransaction()
			.replace(R.id.container, HeroFragment())
			.commitNow()
	}

	//remove focus on all EditTexts when touch outside
	override fun dispatchTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_DOWN) {
			val view: View? = currentFocus
			if (view is EditText) {
				val outRect = Rect()
				view.getGlobalVisibleRect(outRect)
				if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
					view.clearFocus()
					view.hideKeyboard()
				}
			}
		}
		return super.dispatchTouchEvent(event)
	}
}
