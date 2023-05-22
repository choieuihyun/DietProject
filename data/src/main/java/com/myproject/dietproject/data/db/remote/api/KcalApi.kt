package com.myproject.dietproject.data.db.remote.api

import com.myproject.dietproject.data.db.remote.response.kcalresponse.I2790
import com.myproject.dietproject.data.db.remote.response.kcalresponse.KcalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KcalApi {

    @GET("api/") // 버스같은 경우는 데이터가 상세적으로 나뉘어서 baseURL뒤에 추가했는데 이건 그냥 내가 필요한것만 퍼오는거니까 annotaion parameter가 없음.
         // 근데 이렇게 해도 되는게 맞나? 여기서 요청한 key값 넣어주는건 안되나?
    suspend fun getKcalData(@Query("") keyId: String,
                            @Query("") serviceId: String,
                            @Query("") dataType: String,
                            @Query("") startIdx: String,
                            @Query("") endIdx: String,
                            @Query("") desc_kor: String) : Response<KcalResponse> // DESC_KOR= 이걸로 해야되나?

}