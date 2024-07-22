package com.myproject.dietproject.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
import com.myproject.dietproject.domain.model.KcalDataForCalendar
import com.myproject.dietproject.domain.repository.FirebaseRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.roundToInt

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : FirebaseRepository {

    override val firebaseStorageReference = firebaseDataSource.getFirebaseStorageReference

    // 이걸 사용하는 클래스에서 굳이 또 인스턴스를 생성하는 구조인데 이럴꺼면 왜 전역으로 만들었나?
    // repositoryImpl에서 작업하는게 맞다고 생각해 Calendar객체를 이렇게 만들었음.
    // 너가 23:59 분에 켜서 등록하려는데 24:01에 등록을 완료한다면 23:59 기준을 되는게 맞으니까 앱이 켜지는 시점에서.
    // 근데 이렇게 안하면 앱의 뷰모델에서 계속 오늘날짜로 받고 인스턴스 생성하는 식으로 해야하는데, 나는 홈에서 전 날로 옮기면 그 화면에서 이동 시 같은 날짜로 뜨게하고 싶다.
    private val calendar: Calendar = Calendar.getInstance()

    private var _todayKcal: MutableLiveData<Int> = MutableLiveData() // 오늘 섭취한 총 칼로리
    override val todayKcal: LiveData<Int>
        get() = _todayKcal

    private var _homeDateText: MutableLiveData<String> = MutableLiveData() // 날짜 text -> 12-09 (금)
    override val homeDateText: LiveData<String>
        get() = _homeDateText

    private var _homeDateTextByDate: MutableLiveData<String> = MutableLiveData() // 날짜 기준 데이터 -> 2023-12-09
    override val homeDateTextByDate: LiveData<String>
        get() = _homeDateTextByDate

    private var _calculTodayKcal = 0 // 연산용도 오늘 섭취한
    override val calculTodayKcal: Int
        get() = _calculTodayKcal

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData() // 일일 권장 칼로리
    override val recommendKcal: LiveData<Int>
        get() = _recommendKcal

    private var _scarceKcal: MutableLiveData<Int> = MutableLiveData() // 부족한 섭취량 (Kcal)
    override val scarceKcal: LiveData<Int>
        get() = _scarceKcal

    private var _calculRecommendKcal = 0 // 연산용도 권장 칼로리
    override val calculRecommendKcal: Int
        get() = _calculRecommendKcal

    private val _dayKcal: MutableLiveData<Int> = MutableLiveData()
    override val dayKcal: LiveData<Int>
        get() = _dayKcal

    private val _dayKcalList: MutableLiveData<ArrayList<KcalDataForCalendar?>> = MutableLiveData()
    override val dayKcalList: LiveData<ArrayList<KcalDataForCalendar?>>
        get() = _dayKcalList

    fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    override suspend fun test(value: String) {
        //firebaseDataSource.dbTest().setValue(value)
    }

    override suspend fun addUser(userId: String, userEmail: String) {
        firebaseDataSource.addUser(userId, userEmail)
    }

    override suspend fun getUser(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId)
    }

    override suspend fun getUserTodayKcal(userId: String) =
        suspendCancellableCoroutine { continuation ->

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val today = dateFormat.format(calendar.time)
            var dateText = today.substring(5, 10) // 6-28로 자르기
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            var sum = 0
            var isResumed = false

            when (dayOfWeek) {

                Calendar.MONDAY -> dateText = "$dateText (월)"

                Calendar.TUESDAY -> dateText = "$dateText (화)"

                Calendar.WEDNESDAY -> dateText = "$dateText (수)"

                Calendar.THURSDAY -> dateText = "$dateText (목)"

                Calendar.FRIDAY -> dateText = "$dateText (금)"

                Calendar.SATURDAY -> dateText = "$dateText (토)"

                Calendar.SUNDAY -> dateText = "$dateText (일)"

            }
            val userReference = firebaseDataSource.getUserTodayKcal(userId)

            userReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isResumed) {
                        for (data in snapshot.children) {
                            val dataDate = data.key?.substring(0, 10)
                            if (dataDate == today.substring(0, 10)) {
                                val kcal = data.child("kcal").value
                                sum += kcal.toString().toInt()
                            }
                        }
                        _todayKcal.value = sum
                        _calculTodayKcal = sum
                        _homeDateText.value = dateText
                        _homeDateTextByDate.value = today // 이게 없어가지고 혼났네

                        isResumed = true
                        continuation.resume(Unit)
                        userReference.removeEventListener(this)


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

    override suspend fun getUserRecommendKcal(userId: String) =
        suspendCancellableCoroutine { continuation ->

            val userReference = firebaseDataSource.getUserRecommendKcal(userId)

            userReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        _recommendKcal.value = snapshot.value.toString().toInt()
                        _calculRecommendKcal = _recommendKcal.value.toString().toInt()
                        _scarceKcal.value = calculRecommendKcal - calculTodayKcal
                    } catch (e: NumberFormatException) {

                    }

                    continuation.resume(Unit)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    override suspend fun getUserPreviousDateKcal(userId: String) =
        suspendCancellableCoroutine { continuation ->

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val previousDataByDate = dateFormat.format(calendar.time)
            var previousDateText = dateFormat.format(calendar.time).substring(5, 10)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            var sum = 0
            var isResumed = false // 상태 변수 추가

            val userReference = firebaseDataSource.getUserTodayKcal(userId)

            userReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (!isResumed) {
                        for (data in snapshot.children) {
                            val dataDate = data.key?.substring(0, 10)

                            if (dataDate == previousDataByDate.substring(0, 10)) {
                                val kcal = data.child("kcal").value
                                sum += kcal.toString().toInt()
                            }
                        }
                        _todayKcal.value = sum
                        _calculTodayKcal = sum
                        _scarceKcal.value = calculRecommendKcal - calculTodayKcal

                        isResumed = true
                        continuation.resume(Unit)
                        userReference.removeEventListener(this)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            when (dayOfWeek) {

                Calendar.MONDAY -> previousDateText = "$previousDateText (월)"

                Calendar.TUESDAY -> previousDateText = "$previousDateText (화)"

                Calendar.WEDNESDAY -> previousDateText = "$previousDateText (수)"

                Calendar.THURSDAY -> previousDateText = "$previousDateText (목)"

                Calendar.FRIDAY -> previousDateText = "$previousDateText (금)"

                Calendar.SATURDAY -> previousDateText = "$previousDateText (토)"

                Calendar.SUNDAY -> previousDateText = "$previousDateText (일)"

            }

            _homeDateText.value = (previousDateText)
            _homeDateTextByDate.value = (previousDataByDate)
        }

    override suspend fun getUserNextDateKcal(userId: String) =
        suspendCancellableCoroutine { continuation ->

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            val nextDataByDate = dateFormat.format(calendar.time)
            var nextDate = dateFormat.format(calendar.time).substring(5, 10)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            var sum = 0;
            var isResumed = false // 상태 변수 추가

            val userReference = firebaseDataSource.getUserTodayKcal(userId)

            userReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (!isResumed)
                        for (data in snapshot.children) {
                            val dataDate = data.key?.substring(0, 10)

                            if (dataDate == nextDataByDate.substring(0, 10)) {
                                val kcal = data.child("kcal").value
                                sum += kcal.toString().toInt()
                            }
                        }
                    _todayKcal.value = sum
                    _calculTodayKcal = sum
                    _scarceKcal.value = calculRecommendKcal - calculTodayKcal

                    isResumed = true
                    continuation.resume(Unit)
                    userReference.removeEventListener(this)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

            when (dayOfWeek) {

                Calendar.MONDAY -> nextDate = "$nextDate (월)"

                Calendar.TUESDAY -> nextDate = "$nextDate (화)"

                Calendar.WEDNESDAY -> nextDate = "$nextDate (수)"

                Calendar.THURSDAY -> nextDate = "$nextDate (목)"

                Calendar.FRIDAY -> nextDate = "$nextDate (금)"

                Calendar.SATURDAY -> nextDate = "$nextDate (토)"

                Calendar.SUNDAY -> nextDate = "$nextDate (일)"

            }

            _homeDateText.value = (nextDate)
            _homeDateTextByDate.value = (nextDataByDate)


        }

    override suspend fun getCalendarDetailData(userId: String, date: String) =
        suspendCancellableCoroutine { continuation ->

            val userReference = firebaseDataSource.getUserTodayKcal(userId)
            var sum = 0
            val kcalArrayList = ArrayList<KcalDataForCalendar?>()

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {

                        val dateData = data.key?.substring(0, 10)

                        if (dateData == date) {

                            val kcal = data.child("kcal").value

                            sum += kcal.toString().toInt()

                            val kcalDataForCalendar =
                                KcalDataForCalendar(
                                    kcal = kcal.toString().toFloat().roundToInt(),
                                    foodName = data.child("foodName").value.toString(),
                                    makerName = data.child("makerName").value.toString()
                                )
                            kcalArrayList.add(kcalDataForCalendar)
                        }
                    }

                    _dayKcalList.value = kcalArrayList
                    _dayKcal.value = sum

                    continuation.resume(Unit)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    override suspend fun getDayKcalList(): ArrayList<KcalDataForCalendar?>? {
        return dayKcalList.value
    }

    override suspend fun getDayKcal(): Int? {
        return dayKcal.value
    }

    override suspend fun getUserWeekKcal(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("todayKcal")
    }

    override suspend fun getUserName(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("name")
    }

    override suspend fun getUserEmail(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("email")
    }

    override suspend fun getUserTargetWeight(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("targetWeight")
    }

    override suspend fun getUserWeight(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("weight")
    }

    override suspend fun getUserOverKcal(userId: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("overKcal")
    }

    override suspend fun getUserActivity(userId: String): DatabaseReference {
        return firebaseDataSource.getUserActivity(userId)
    }

    override suspend fun addUserInfo(
        userId: String,
        name: String,
        gender: String,
        age: Int,
        height: Float,
        weight: Float,
        targetWeight: Float,
        recommendKcal: Int,
        activity: String
    ) {
        firebaseDataSource.addUserInfo(
            userId,
            name,
            gender,
            age,
            height,
            weight,
            targetWeight,
            recommendKcal,
            activity
        )
    }

    override suspend fun addTodayKcal(
        userId: String,
        kcal: Float,
        foodName: String,
        makerName: String,
        date: String
    ) {
        firebaseDataSource.addTodayKcal(userId, kcal, foodName, makerName, date)
    }

    override suspend fun addOverKcal(userId: String, overKcal: Int) {
        firebaseDataSource.addOverKcal(userId, overKcal)
    }

    override suspend fun getProfileImage(userId: String, path: String): StorageReference {
        val reference = firebaseDataSource.getFirebaseStorageReference
        return reference.child(userId).child(path)
    }

    override suspend fun addProfileImage(userId: String, path: String) {
        firebaseDataSource.getFirebaseStorageReference.child(userId).child(path)
    }


}