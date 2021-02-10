package com.yc.method

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.yc.basepackage.MyApplication
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

//读取
fun readerFile(noti: String): ArrayList<String> {
    val als = ArrayList<String>()
    try {
        val fileparent = Environment.getExternalStorageDirectory().absolutePath+"/"
        val fileDir = File(fileparent+"/龠")
        if (!fileDir.exists())
            fileDir.mkdir()
        val file = File(fileDir.toString()+"/龠.txt")
        if (file.exists()) {
            val readerFile = FileReader(file)
            val bufr = BufferedReader(readerFile)
            var eof = bufr.readLine()
            while (eof != null) {
                als.add(eof)
                eof = bufr.readLine()
            }
            Toast.makeText(MyApplication.context,noti, Toast.LENGTH_SHORT)
                .show()
            bufr.close()
            readerFile.close()
        } else
            Toast.makeText(MyApplication.context,"文件不存在", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(MyApplication.context,"失败",Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
    return als
}
//存储
fun writerFile(s: String,noti: String) {
    try {
        val filepathparent = Environment.getExternalStorageDirectory().absolutePath+"/"
        val filepath = File(filepathparent+"/龠")
        if (!filepath.exists())
            filepath.mkdir()
        val file = File(filepath.toString()+"/龠.txt")
        if (!file.exists())
            file.createNewFile()
        val filestr = FileWriter(file)
        filestr.write(s)
        filestr.flush()
        filestr.close()
        Toast.makeText(MyApplication.context,noti,Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(MyApplication.context,"失败",Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}
//有待修正
//fun String.ToastImage(duration: Int = Toast.LENGTH_SHORT) {
//    val toast = Toast.makeText(MyApplication.context,this,duration)
//    val layout = toast.view
//    val img = ImageView(MyApplication.context)
//    img.setImageResource(R .drawable.ic_launcher_foreground)
//    layout.animation = img as Animation
//    toast.show()
//}