package com.example.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    // lazy를 사용해서 미리 선언
    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    // 리스트 안에 공들을 넣어놓음
    private val numTextViewList : List<TextView> by lazy {

        listOf<TextView> (
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6),

            )
    }

    // 실행중인지
    private var didRun = false

    // 사용자가 지정한 숫자를 담아둘 공간
    private val pickNumberSet = hashSetOf<Int>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 숫자 선택할수 있는 picker에 최대,최솟값 주기
        numPick.minValue = 1
        numPick.maxValue = 45


        initAddButton()
        initRunButton()
        initClearButton()

    }


    // 생성버튼 (아무것도 없을땐 6개 자동생성, 번호 2개있다고 가정하면 2개를 제외한 4개 자동생성 )
    private fun initRunButton() {
        runButton.setOnClickListener {
            // 랜덤값 가져오는 함수
            val list = getRandom()

            didRun = true


            list.forEachIndexed { index, number ->

                val textView = numTextViewList[index]
                textView.text = number.toString()       // 공집어넣은 리스트들에 숫자 보이게
                textView.isVisible = true               // 공 보이게

                // 색깔 선택함수 실행
                setNumBack(number, textView)

            }

        }
    }



    // 랜덤값 가져오는 함수
    // 랜덤함수는 반환값이 있음(List형태의 숫자로 반환해줘야함)
    private fun getRandom() : List<Int> {
        // 1~45까지의 숫자중에 pickNumberset(선택한수)에 해당하는 숫자는 뺴고
        // ex) 4,5 선택했으면 4,5빼고
        val numbers = (1..45).filter { it !in pickNumberSet }

        // pickNumberSet에 numbers를 섞어서 더해주고, 6-pickNumberSet만큼의 사이즈 리턴
        return (pickNumberSet + numbers.shuffled().take(6 - pickNumberSet.size)).sorted()

    }


    // 초기화 함수
    private fun initClearButton() {

        clearButton.setOnClickListener {

            // 슷자 담아둔 공간 클리어
            pickNumberSet.clear()

            // 공들 안보이게
            numTextViewList.forEach{it.isVisible = false}

            didRun = false

            numPick.value = 1

        }
    }




    // 숫자 추가 함수
    private fun initAddButton() {
        addButton.setOnClickListener {

            when{
                // 자동실행 버튼이 눌려서 꽉차있는 경우
                didRun -> showToast("초기화 후 시도해주세요")
                // 숫자가 5개 이상일떄
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할수있습니다")
                // 같은 숫자가 있을때
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택된 숫자는 선택할수없습니다")

                else -> {
                    // 예외가 없을때
                    // numTextViewList의 사이즈만큼 넣어줌
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    // 공에 숫자 넣기
                    textView.text = numPick.value.toString()

                    // 색깔 선택함수 불러옴
                    setNumBack(numPick.value , textView)

                    // 숫자 담아둔 공간에 숫자 넣어줌
                    pickNumberSet.add(numPick.value)

                }
            }


        }
    }



    // 색깔 선택 함수
    private fun setNumBack (number : Int, textView : TextView){
        val background = when(number) {

            in 1..10 -> R.drawable.circle_yellow    // 1~10 노랑
            in 11..20 -> R.drawable.circle_blue     // 1~10 블루
            in 21..30 -> R.drawable.circle_red      // 1~10 레드
            in 31..40 -> R.drawable.circle_gray     // 1~10 그레이
            else -> R.drawable.circle_green               // 그외 그린
        }

            textView.background = ContextCompat.getDrawable(this, background)

    }
    
    
    private fun showToast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}