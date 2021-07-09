package com.smallcake.temp.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric


/**
 * 扩展函数：省去beginTransaction()和commit()代码
 * @receiver FragmentManager
 * @param func [@kotlin.ExtensionFunctionType] Function1<FragmentTransaction, Unit>
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}
//添加Fragment
fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}
//替换Fragment
fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction{replace(frameId, fragment)}
}
/**
 * Fragment基类
 bind需要在 onViewCreated(view: View, savedInstanceState: Bundle?)后调用
 */
abstract class BaseBindFragment<VB : ViewBinding>: Fragment() {
    private var _binding: VB? = null
    val bind:VB get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflateBindingWithGeneric(layoutInflater)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun goActivity(clz: Class<*>) = startActivity(Intent(requireActivity(), clz))
}