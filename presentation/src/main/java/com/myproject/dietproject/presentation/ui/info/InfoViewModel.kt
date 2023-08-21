package com.myproject.dietproject.presentation.ui.info

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.myproject.dietproject.domain.usecase.AddUserProfileImage
import com.myproject.dietproject.domain.usecase.GetFirebaseStorageRef
import com.myproject.dietproject.domain.usecase.GetUserEmailUseCase
import com.myproject.dietproject.domain.usecase.GetUserNameUseCase
import com.myproject.dietproject.domain.usecase.GetUserProfileImage
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTargetWeightUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeekKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(

    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getUserTargetWeightUseCase: GetUserTargetWeightUseCase,
    private val getUserProfileImage: GetUserProfileImage,
    private val getUserNumerousKcalByFoodUseCase: GetUserWeekKcalUseCase,
    private val getUserNumerousKcalByDateUseCase: GetUserWeekKcalUseCase,
    private val addUserProfileImage: AddUserProfileImage, // 이거 쓰면 이상해짐
    private val getFirebaseStorageRef: GetFirebaseStorageRef

) : ViewModel() {

    private var _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String>
        get() = _name

    private var _email: MutableLiveData<String> = MutableLiveData()
    val email: LiveData<String>
        get() = _email

    private var _recommendKcal: MutableLiveData<String> = MutableLiveData()
    val recommendKcal: LiveData<String>
        get() = _recommendKcal

    private var _targetWeight: MutableLiveData<Int?> = MutableLiveData()
    val targetWeight: LiveData<Int?>
        get() = _targetWeight

    private var _mostNumerousFood: MutableLiveData<Int> = MutableLiveData()
    val mostNumerousFood: LiveData<Int>
        get() = _mostNumerousFood

    private var _mostNumerousDate: MutableLiveData<String?> = MutableLiveData()
    val mostNumerousDate: LiveData<String?>
        get() = _mostNumerousDate

    private var _dayKcal: MutableLiveData<Int?> = MutableLiveData()
    val dayKcal: LiveData<Int?>
        get() = _dayKcal

    private var _overKcal: MutableLiveData<Int?> = MutableLiveData()
    val overKcal: LiveData<Int?>
        get() = _overKcal

    fun getUserEmail(userId: String) {

        viewModelScope.launch {

            getUserEmailUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    _email.postValue(snapshot.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    fun getUserRecommendKcal(userId: String) {

        viewModelScope.launch {

            getUserRecommendKcalUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    _recommendKcal.postValue(snapshot.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    fun getUserName(userId: String) {

        viewModelScope.launch {

            getUserNameUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    _name.postValue(snapshot.value.toString())

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }
    }

    fun getUserTargetWeight(userId: String) {

        viewModelScope.launch {

            getUserTargetWeightUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        _targetWeight.postValue(snapshot.value.toString().toInt())
                    } catch (e : NumberFormatException) {

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                    TODO("Not yet implemented")

                }

            })

        }

    }

    fun getUserMaxKcal(userId: String) { // 원래 이런곳에서 다 해도 되는데 그러면 유지보수할 때 머리가 띵할듯.

        viewModelScope.launch {

            getUserNumerousKcalByFoodUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val iterator = snapshot.children.iterator() // NumerousDate 처럼 구현해도 되고 Iterator를 사용해서 구현해도 된다. 좀 더 명시적일뿐? 차이 없어보임.

                    var max = 0

                    while(iterator.hasNext()) {

                        val childSnapshot = iterator.next()

                        val childValue = childSnapshot.child("kcal").value.toString().toInt()

                        if(childValue > max)
                            max = childValue

                    }

                    _mostNumerousFood.postValue(max)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


    fun getMostNumerousDate(userId: String) {

        viewModelScope.launch {

            getUserNumerousKcalByDateUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val dateTotalMap: Multimap<String, Int?> = ArrayListMultimap.create() // Guava multiMap

                    for (childSnapshot in snapshot.children) { // Iterator를 사용할 필요가 없음.

                        val date = childSnapshot.key.toString()
                        val kcal = childSnapshot.child("kcal").value.toString().toInt()

                        val dateSubstring = date.substring(0,10)
                        dateTotalMap.put(dateSubstring, kcal)

                    }

                    val mergedMap = mutableMapOf<String, Int?>()

                    for(entry in dateTotalMap.entries()) { // multimap 내의 entry를 통해 날짜 연산

                        val date = entry.key
                        val kcal = entry.value
                        val sum = mergedMap[date]?.plus(kcal!!) ?: kcal
                        mergedMap[date] = sum

                    }

                    var maxDate: String? = null
                    var maxKcal = 0
                    var sumKcal = 0

                    for((date, kcal) in mergedMap) {

                        if(kcal != null && kcal > maxKcal) {
                            maxDate = date
                            maxKcal = kcal
                        }

                        if (kcal != null) {
                            sumKcal += kcal
                        }

//                        if (kcal != null) { // 하루 권장 칼로리 초과한 횟수
//                            if(kcal > _recommendKcal.value!!.toInt()) {
//
//                            }
//                        }
                    }

                    if(maxDate != null)
                        _mostNumerousDate.postValue(maxDate)
                    else
                        _mostNumerousDate.postValue("데이터를 입력해주세요")

                    if(sumKcal != 0)
                        _dayKcal.postValue(sumKcal / mergedMap.size)
                    else
                        _dayKcal.postValue(0)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }


    fun getUserProfileImage(c: Context,
                            userId: String,
                            path: String,
                            v: ImageView,
                            nothing: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            getUserProfileImage(userId,path).downloadUrl.addOnSuccessListener { uri ->
                Glide.with(c).load(uri).into(v)
            }.addOnFailureListener {
                Glide.with(c).load(nothing).into(v)
            }
        }
    }

    fun addUserProfileImage(userId: String, path: String, uri: Uri) {

        viewModelScope.launch(Dispatchers.IO) {
            val uploadRef = getFirebaseStorageRef().child(userId).child(path)
            //val imageName = uri.toString().substring(38,43)
            val imageRef = uploadRef.putFile(uri)
            imageRef.addOnCompleteListener { task ->

                if(task.isSuccessful) {

                    Log.d("ImageSuccess2", "success")

                } else {

                    Log.d("ImageFailure2", "${task.exception?.message}")

                }
            }


        }
    }
}

