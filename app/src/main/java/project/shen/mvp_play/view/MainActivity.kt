package project.shen.mvp_play.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.databinding.adapters.ViewBindingAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import project.shen.mvp_play.R
import project.shen.mvp_play.databinding.ActivityMainBinding
import project.shen.mvp_play.databinding.ItemMainBinding
import project.shen.mvp_play.imvp.CommonModule
import project.shen.mvp_play.imvp.DaggerCommonComponent
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.User
import project.shen.mvp_play.presenter.LoginPresenter
import project.shen.mvp_play.presenter.MainPresenter
import project.shen.mvp_play.presenter.adapter.BaseDataBindingAdapter
import project.shen.mvp_play.presenter.adapter.ViewHolder
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ICommonView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: MainAdapter

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        DaggerCommonComponent.builder()
            .commonModule(CommonModule(this))
            .build()
            .inject(this)

        initRecyclerView()
        setClickEvent()
    }

    private fun initRecyclerView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            mAdapter = MainAdapter(listOf(User(1, "shen", "pwd"), User(1, "jib", "pwd")))
            adapter = mAdapter
        }
    }

    private fun setClickEvent() {
        btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            presenter.wow()
        }
    }

    override fun getContext(): Context = this
}

class MainAdapter(items: List<User>): BaseDataBindingAdapter<User, ItemMainBinding>(items = items as MutableList<User>) {

    override fun onBindData(viewDataBinding: ItemMainBinding, item: User, itemView: View, position: Int) {
        viewDataBinding.itemTVUserName.text = item.userName
    }

    override fun getLayoutId(type: Int): Int = R.layout.item_main
}


