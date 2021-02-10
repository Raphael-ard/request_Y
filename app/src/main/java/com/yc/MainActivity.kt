package com.yc

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.yc.method.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var uri: String = "https://pyq.shadiao.app/api.php"
    private val strList = ArrayList<Msg>()
    private val strbuf = StringBuffer()
    private var n = false  //为true时清空
    private var judge = false  //为true时代表已阅读提示信息

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }

        initation()
        val layoutManager = GridLayoutManager(this,1)
        msgrecyclerview.layoutManager = layoutManager
        val adapter = MsgAdapter(this,strList)
        msgrecyclerview.adapter = adapter
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)
        swipeRefresh.setOnRefreshListener {
            if (!n) {
                sendRequestWithOkHttp()
            } else
                n = true
            refresh(adapter)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        navView.setCheckedItem(R.id.nav_article)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_terms -> {
                    uri = "https://chp.shadiao.app/api.php"
                    Toast.makeText(this,"彩虹屁",Toast.LENGTH_SHORT).show()
                }
                R.id.nav_article -> {
                    uri = "https://pyq.shadiao.app/api.php"
                    Toast.makeText(this,"朋友圈文章",Toast.LENGTH_SHORT).show()
                }
                R.id.nav_djt -> {
                    uri = "https://du.shadiao.app/api.php"
                    Toast.makeText(this,"毒鸡汤",Toast.LENGTH_SHORT).show()
                }
                R.id.baidu -> {
                    val intent = Intent(this,WebViewActivity::class.java)
                    intent.putExtra("uri_str","Https://www.baidu.com")
                    startActivity(intent)
                    Toast.makeText(this,"百度",Toast.LENGTH_SHORT).show()
                }
                R.id.help -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("帮助")
                        setMessage("主界面：\n下滑刷新，最新信息显示在下方\n点击右下角悬浮按钮可以刷新信息，信息显示在最下方" +
                                "\n右上角从左到右分别为：\n保存，保存到内部存储/龠/龠.txt文件中\n清空，清空龠.txt文件中所有内容\n设置\n退出程序自动保存主界面所有内容" +
                                "\n侧滑菜单：\n选择彩虹屁，朋友圈文章，毒鸡汤选项，刷新文章\n选择百度进入Web浏览")
                        setCancelable(true)
                        setPositiveButton("OK") {dialog, which ->  }
                        show()
                    }
                }
            }
            sendRequestWithOkHttp()
            refresh(adapter)
            drawerLayout.closeDrawers()
            true
        }
        judge = load()
        if (!judge) {
            AlertDialog.Builder(this).apply {
                        setTitle("帮助")
                        setMessage("主界面：\n下滑刷新，最新信息显示在下方\n点击右下角悬浮按钮可以刷新信息，信息显示在最下方" +
                                "\n右上角从左到右分别为：\n保存，保存到内部存储/龠/龠.txt文件中\n清空，清空龠.txt文件中所有内容\n设置\n退出程序自动保存主界面所有内容" +
                                "\n侧滑菜单：\n选择彩虹屁，朋友圈文章，毒鸡汤选项，刷新文章\n选择百度进入Web浏览")
                        setCancelable(true)
                        setPositiveButton("OK") {dialog, which ->  }
                        show()
                    }
            judge = true
            save(judge)
        }
        fab.setOnClickListener {
            if (!n) {
                sendRequestWithOkHttp()
            } else
                n = true
            refresh(adapter)
            Toast.makeText(this,"刷新",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendRequestWithOkHttp() {
        try {
            for (i in 0 until 5) {
                HttpUtil.sendHttpRequest(uri,object : HttpCallbackListener {
                    override fun onFinish(response: String) {
                        strList.add(Msg(response))
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(this@MainActivity,"请求失败",Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                })
            }
        } catch (e1: Exception) {
            Toast.makeText(this,"联网失败",Toast.LENGTH_SHORT).show()
            e1.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        } else {
            strbuf.setLength(0)
            for (list in strList){
                strbuf.append("\t\t\t"+list.msg_test+"\n")
            }
            writerFile(strbuf.toString(),"数据已保存")
        }
    }

    private fun refresh(adapter: MsgAdapter) {
        thread {
            Thread.sleep(1000)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),2)
        } else {
            val fileparent = Environment.getExternalStorageDirectory().absolutePath+"/"
            val fileDir = File(fileparent+"/龠")
            if (!fileDir.exists())
                fileDir.mkdir()
            val file = File(fileDir.toString()+"/龠.txt")
            if (file.exists()) {
                //如果一开始就有文件存在，则直接读取文件
                val temp: ArrayList<String> = readerFile("读取成功")
                for (i in 0 until temp.size) {
                    strList.add(Msg(temp.get(i)))
                }
            } else {
                sendRequestWithOkHttp()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Clear -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
                } else
                    writerFile("","清空成功")
                strList.clear()
                n = true
            }
            R.id.Save -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                } else {
                    strbuf.setLength(0)
                    for (list in strList) {
                        strbuf.append("\t\t\t"+list.msg_test+"\n")
                    }
                    writerFile(strbuf.toString(),"保存成功")
                }
            }
            R.id.Settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"设置",Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writerFile("","清空成功")
                } else {
                    Toast.makeText(this,"权限被拒绝",Toast.LENGTH_SHORT).show()
                }
            }
            1 -> {
                strbuf.setLength(0)
                for (list in strList){
                    strbuf.append("\t\t\t"+list.msg_test+"\n")
                }
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writerFile(strbuf.toString(),"保存成功")
                } else {
                    Toast.makeText(this,"权限被拒绝",Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this,"权限被拒绝",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun save(judge: Boolean) {
        val editor = getSharedPreferences("data",Context.MODE_PRIVATE).edit().apply {
            putBoolean("judge",judge)
            apply()
        }
    }
    private fun load(): Boolean {
        val editor = getSharedPreferences("data",Context.MODE_PRIVATE)
        return editor.getBoolean("judge",false)
    }
}