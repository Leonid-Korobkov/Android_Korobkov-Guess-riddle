package android_korobkov.guess_riddle

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android_korobkov.guess_riddle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val ANSWER_REQUEST_CODE = 1 // Уникальный код запроса для ответа на загадку

    // Список загадок и ответов
    private val riddlesAndAnswers = listOf(
        Pair("Что можно увидеть с закрытыми глазами?", "Сон"),
        Pair("Что делают бегемоты на рождество?", "Подарки"),
//        Pair("Что можно сломать, если захотеть?", "Обещание"),
//        Pair("Что вчера было сегодня, а завтра будет вчера?", "Вчера"),
//        Pair("Что упало с неба и встало на землю?", "Дождь"),
//        Pair("Что бывает только один раз?", "Судный день"),
//        Pair("Какое слово написать можно с ошибкой?", "Ошибку"),
//        Pair("Что движется быстрее света?", "Мысли"),
//        Pair("Что наступает после смерти?", "Плывут"),
//        Pair("Что делают те, кто умеют плавать, но не умеют ходить?", "Плывут")
    )

    private var currentRiddleIndex = 0 // Переменная для отслеживания количества отгаданных загадок
    private var correctAnswerCount = 0 // Количество правильных ответов
    private var totalCount = riddlesAndAnswers.size // Общее количество загадок

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Назначаем обработчики нажатия кнопок
        binding.buttonGetRiddle.setOnClickListener {
            onButtonGetRiddleClick()
        }

        binding.buttonStatistics.setOnClickListener {
            onButtonStatisticsClick()
        }

        binding.buttonAnswer.setOnClickListener {
            onButtonAnswerClick()
        }
        binding.buttonNewGame.setOnClickListener {
            onButtonNewGameClick()
        }

        // Изначально кнопки "Ответ" и "Статистика" отключены
        binding.buttonAnswer.isEnabled = false
        binding.buttonStatistics.isEnabled = false

        // Обновляем текст с информацией о прогрессе
        updateProgressText()
    }

    // Генерация случайной загадки
    private fun generateRandomRiddle(): String {
        val randomIndex = (riddlesAndAnswers.indices).random()
        return riddlesAndAnswers[randomIndex].first
    }

    // Обработчик нажатия кнопки "Ответ"
    private fun onButtonAnswerClick() {
        val currentRiddle = binding.textViewRiddle.text.toString()
        val answerIntent = Intent(this, AnswerActivity::class.java).apply {
            putExtra("riddle", currentRiddle)
            putExtra("correctAnswer", getCorrectAnswer(currentRiddle))
            putStringArrayListExtra("answers", ArrayList(riddlesAndAnswers.map { it.second }))
        }
        startActivityForResult(answerIntent, ANSWER_REQUEST_CODE)

        currentRiddleIndex++
        updateProgressText()

        // Проверяем, нужно ли отключить кнопку "Ответ" и включить кнопку "Статистика"
        if (currentRiddleIndex == totalCount) {
            binding.buttonGetRiddle.isEnabled = false
            binding.buttonAnswer.isEnabled = false
            binding.buttonStatistics.isEnabled = true
        } else {
            binding.buttonGetRiddle.isEnabled = true
            binding.buttonAnswer.isEnabled = false
        }
    }

    // Обработчик нажатия кнопки "Загадка"
    private fun onButtonGetRiddleClick() {
        val randomRiddle = generateRandomRiddle()
        binding.textViewRiddle.text = randomRiddle
        binding.textViewRiddle.visibility = View.VISIBLE

        binding.buttonAnswer.isEnabled = true
        binding.buttonGetRiddle.isEnabled = false

        binding.textViewResult.text = ""
        binding.textViewUserAnswer.text = ""
    }

    // Обработчик нажатия кнопки "Статистика"
    private fun onButtonStatisticsClick() {
        val statisticIntent = Intent(this, StatisticsActivity::class.java).apply {
            putExtra("correctAnswerCount", correctAnswerCount)
            putExtra("totalCount", totalCount)
        }
        startActivity(statisticIntent)
    }

    // Обработчик нажатия кнопки "Новая игра"
    private fun onButtonNewGameClick() {
        currentRiddleIndex = 0
        correctAnswerCount = 0
        updateProgressText()

        binding.buttonGetRiddle.isEnabled = true
        binding.buttonAnswer.isEnabled = false
        binding.buttonStatistics.isEnabled = false

        binding.textViewResult.text = ""
        binding.textViewUserAnswer.text = ""
        binding.textViewRiddle.text = ""
    }

    // Обновление текста с информацией о прогрессе
    private fun updateProgressText() {
        binding.textViewRiddlesCount.text = "Решено: $currentRiddleIndex/$totalCount"
    }

    // Получение правильного ответа по текущей загадке
    private fun getCorrectAnswer(currentRiddle: String): String {
        return riddlesAndAnswers.firstOrNull { it.first == currentRiddle }?.second ?: ""
    }

    // Обработка результата ответа на загадку из AnswerActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST_CODE) {
            val selectedAnswer = data?.getStringExtra("selectedAnswer")
            if (resultCode == Activity.RESULT_OK) {
                binding.textViewUserAnswer.text = selectedAnswer
                binding.textViewResult.text = "Правильно!"
                correctAnswerCount++
            } else if (resultCode == Activity.RESULT_CANCELED) {
                val result = data?.getStringExtra("result")
                binding.textViewUserAnswer.text = selectedAnswer
                binding.textViewResult.text = result
            }
        }
    }
}
