package com.example.crypto.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.crypto.R

interface InitViews {
    fun initializeViews()

    fun observeViewModel()
}

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

abstract class MainNavigationFragment: Fragment(), InitViews {
    private var navigationHost: NavigationHost? = null
    private var fragmentView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view
    }

    override fun onResume() {
        super.onResume()

        val host = navigationHost ?: return
        val mainToolbar: Toolbar = fragmentView?.findViewById(R.id.toolbar) ?: return

        host.registerToolbarWithNavigation(mainToolbar)
    }
}