package com.taku24.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private val COLLECTION_PATH = "Content"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDb()
    }

    /**
     * FireBaseStoreの初期化
     */
    private fun initDb() {
        val db = FirebaseFirestore.getInstance()

        // 追加ボタンの設定をする
        findViewById<Button>(R.id.add_data).setOnClickListener {
            val data = Content("id", "追加日：${Date()}")
            db.collection(COLLECTION_PATH)
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "データ送信が完了しました", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "データ送信に失敗しました", Toast.LENGTH_SHORT).show()
                }
        }

        // データ取得ボタンの設定をする
        findViewById<Button>(R.id.fetch_data).setOnClickListener {
            db.collection(COLLECTION_PATH)
                .get()
                .addOnSuccessListener { _ ->
                    Toast.makeText(this, "データ取得に成功しました", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "データ取得に失敗しました", Toast.LENGTH_SHORT).show()
                }
        }

        db.collection(COLLECTION_PATH)
            .addSnapshotListener { snapShot, e ->
                if (snapShot == null || snapShot.isEmpty) {
                    return@addSnapshotListener
                }
                var content = ""
                for (item in snapShot) {
                    val data = item.data
                    content += "ID：${data["id"]}, Content:${data["content"]}\n"
                }
                findViewById<TextView>(R.id.content).text = content
                Toast.makeText(this, "データベースが更新されました", Toast.LENGTH_SHORT).show()
            }
    }


}
