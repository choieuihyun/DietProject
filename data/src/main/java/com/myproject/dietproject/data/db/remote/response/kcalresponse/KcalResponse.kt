package com.myproject.dietproject.data.db.remote.response.kcalresponse

import com.google.gson.annotations.SerializedName

data class KcalResponse(
    @SerializedName("I2790")
    val i2790: I2790?
)

data class I2790(
    @SerializedName("RESULT")
    val rESULT: RESULT?,
    @SerializedName("row")
    val row: List<KcalData>?,
    @SerializedName("total_count")
    val totalCount: String?
)

data class RESULT(
    @SerializedName("CODE")
    val cODE: String?,
    @SerializedName("MSG")
    val mSG: String?
)

data class KcalData(
    @SerializedName("DESC_KOR") // 식품명
    val dESCKOR: String?,
//    @SerializedName("FOOD_CD") // 식품 코드
//    val fOODCD: String?,
//    @SerializedName("GROUP_NAME") // 식품군
//    val gROUPNAME: String?,
    @SerializedName("MAKER_NAME") // 제조사명
    val mAKERNAME: String?,
//    @SerializedName("NUM") // 번호
//    val nUM: String?,
    @SerializedName("NUTR_CONT1") // 열량(kcal) NUTR 붙은건 1회 제공량당
    val nUTRCONT1: String?,
    @SerializedName("NUTR_CONT2") // 탄수화물
    val nUTRCONT2: String?,
    @SerializedName("NUTR_CONT3") // 단백질
    val nUTRCONT3: String?,
    @SerializedName("NUTR_CONT4") // 지방
    val nUTRCONT4: String?,
//    @SerializedName("NUTR_CONT5") // 당류
//    val nUTRCONT5: String?,
//    @SerializedName("NUTR_CONT6") // 나트륨
//    val nUTRCONT6: String?,
//    @SerializedName("NUTR_CONT7") // 콜레스테롤
//    val nUTRCONT7: String?,
//    @SerializedName("NUTR_CONT8") // 포화지방산
//    val nUTRCONT8: String?,
//    @SerializedName("NUTR_CONT9") // 트랜스지방
//    val nUTRCONT9: String?,
//    @SerializedName("RESEARCH_YEAR") // 조사년도
//    val rESEARCHYEAR: String?,
//    @SerializedName("SAMPLING_MONTH_CD") // 채취월코드
//    val sAMPLINGMONTHCD: String?,
//    @SerializedName("SAMPLING_MONTH_NAME") // 채취월
//    val sAMPLINGMONTHNAME: String?,
//    @SerializedName("SAMPLING_REGION_CD") // 지역코드
//    val sAMPLINGREGIONCD: String?,
//    @SerializedName("SAMPLING_REGION_NAME") // 지역명
//    val sAMPLINGREGIONNAME: String?,
    @SerializedName("SERVING_SIZE") // 총 내용량
    val sERVINGSIZE: String?,
//    @SerializedName("SUB_REF_NAME") // 자료출처
//    val sUBREFNAME: String?
)