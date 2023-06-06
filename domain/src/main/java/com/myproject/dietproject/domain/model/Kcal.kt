package com.myproject.dietproject.domain.model

import java.io.Serializable


data class Kcal (

    val dESCKOR: String?, // 식품명
    val mAKERNAME: String?, // 제조사명
    val nUTRCONT1: String?, // 열량(kcal) NUTR 붙은건 1회 제공량당
    val nUTRCONT2: String?, // 탄수화물
    val nUTRCONT3: String?, // 단백질
    val nUTRCONT4: String?, // 지방
    val sERVINGSIZE: String?, // 총 내용량

    ) : Serializable