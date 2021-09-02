package com.smallcake.temp.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric
import com.lsxiao.apollo.core.Apollo
import com.lsxiao.apollo.core.contract.ApolloBinder
import com.smallcake.temp.http.DataProvider
import com.smallcake.temp.module.LoadDialog
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import org.koin.android.ext.android.get


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
    var mContext: Context? = null
    protected val dataProvider: DataProvider = get()//注入数据提供者
    protected lateinit var dialog: LoadDialog //注入单例加载圈
    protected lateinit var provider: LifecycleProvider<Lifecycle.Event>//生命周期提供者
    private var mBinder: ApolloBinder? = null//事件通知者

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflateBindingWithGeneric(layoutInflater)
        provider = AndroidLifecycle.createLifecycleProvider(this)
        mContext = this.context
        dialog=(activity as BaseActivity).getLoadDialog()
        mBinder = Apollo.bind(this)
        return bind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinder?.unbind()
        _binding = null
    }
    fun goActivity(clz: Class<*>) = startActivity(Intent(requireActivity(), clz))
    fun goActivity(clz: Class<*>,id:String?){
        val intent = Intent(requireActivity(), clz)
        intent.putExtra("id",id)
        startActivity(intent)
    }
}