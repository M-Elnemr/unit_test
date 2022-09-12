package com.elnemr.unittest.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elnemr.unittest.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val viewModel by viewModels<ShoppingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}