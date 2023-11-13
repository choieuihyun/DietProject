package com.myproject.dietproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class BatteryReceiver : BroadcastReceiver() {

    // TODO TestAction = 액션 명 => '패키지명.action.액션명(자유롭게)' 형태 지정
    val batteryAction = "batteryBroadCast"

    override fun onReceive(context: Context?, intent: Intent) {
        // 각 방송 정보가 intent 로 전달
        if (Intent.ACTION_POWER_CONNECTED == intent.action) {
            Toast.makeText(context, "전원 연결", Toast.LENGTH_SHORT).show()
        } else if (Intent.ACTION_POWER_DISCONNECTED == intent.action) {
            Toast.makeText(context, "전원 해제", Toast.LENGTH_SHORT).show()
        } else if (batteryAction == intent.action) {
            Toast.makeText(context, "전원 연결 상태", Toast.LENGTH_SHORT).show()
        }
    }

}