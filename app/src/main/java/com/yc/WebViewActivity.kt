package com.yc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    private lateinit var UrlLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        UrlLink = intent.getStringExtra("uri_str")
        webview.settings.javaScriptEnabled = true
        //支持JavaScript脚本
        webview.loadUrl(UrlLink)
        webview.webViewClient = WebViewClient()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.web_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                webview.loadUrl(UrlLink)
                Toast.makeText(this,"回到主界面",Toast.LENGTH_SHORT).show()
            }
            R.id.exit_web -> {
                AlertDialog.Builder(this).apply {
                    setTitle("提示")  //标题  标准函数apply
                    setMessage("是否退出Web界面？")
                    setCancelable(true)  //可否使用back键关闭
                    setPositiveButton("Yes") { _, _ ->
                        finish()
                        Toast.makeText(this@WebViewActivity,"已退出Web界面",Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No") { _, _ -> Toast.makeText(this@WebViewActivity,"仍在Web界面",Toast.LENGTH_SHORT).show() }
                    show()
                }
            }
        }
        return true
    }
}