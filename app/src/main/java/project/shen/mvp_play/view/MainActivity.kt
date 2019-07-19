package project.shen.mvp_play.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import project.shen.mvp_play.MyApplication
import project.shen.mvp_play.R
import project.shen.mvp_play.databinding.ActivityMainBinding
import project.shen.mvp_play.databinding.ItemMainBinding
import project.shen.mvp_play.imvp.CommonModule
import project.shen.mvp_play.imvp.DaggerCommonComponent
import project.shen.mvp_play.imvp.ICommonView
import project.shen.mvp_play.model.User
import project.shen.mvp_play.presenter.MainPresenter
import project.shen.mvp_play.presenter.adapter.BaseDataBindingAdapter
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
            mAdapter = MainAdapter(listOf(
                User(1, "shen", "1321"),
                User(1, "jib", "dqhjdqbb"),
                User(1, "wow", "sa2d4154")
            ))
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
        viewDataBinding.itemTVPWD.text = item.pwd
        viewDataBinding.itemTVUserName.setOnClickListener {
            this.addItems(
                listOf(
                    User(2, "dnjka", "asnjas"),
                    User(2, "asd", "asnjas")
                )
            )
        }
        viewDataBinding.itemTVPWD.setOnClickListener {
            Toast.makeText(MyApplication.getApplication(), item.pwd, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayoutId(type: Int): Int = R.layout.item_main
}


