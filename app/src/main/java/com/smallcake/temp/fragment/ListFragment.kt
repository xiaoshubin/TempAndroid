package com.smallcake.temp.fragment

import android.os.Bundle
import android.view.View
import com.smallcake.temp.base.BaseBindFragment
import com.smallcake.temp.databinding.FragmentListBinding

class ListFragment: BaseBindFragment<FragmentListBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.textView.text = "列表"
    }


}