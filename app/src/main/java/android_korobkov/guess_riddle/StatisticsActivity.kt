package android_korobkov.guess_riddle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android_korobkov.guess_riddle.databinding.ActivityStatisticBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные о количестве загадок и правильных ответов из Intent
        val totalCount = intent.getIntExtra("totalCount", 0)
        val correctAnswerCount = intent.getIntExtra("correctAnswerCount", 0)

        // Вычисляем количество неправильных ответов
        val incorrectAnswerCount = totalCount - correctAnswerCount

        // Устанавливаем тексты с данными на экране статистики
        binding.textViewTotalRiddles.text = "Количество полученных загадок: $totalCount"
        binding.textViewCorrectAnswers.text = "Правильных ответов: $correctAnswerCount"
        binding.textViewIncorrectAnswers.text = "Неправильных ответов: $incorrectAnswerCount"

        // Устанавливаем слушатель на кнопку "Главная"
        binding.buttonHome.setOnClickListener {
            finish() // Закрываем текущую активность и возвращаемся на предыдущий экран
        }
    }
}
