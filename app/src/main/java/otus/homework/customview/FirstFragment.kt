package otus.homework.customview

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FirstFragment: Fragment(R.layout.fragment_first) {


    private lateinit var pieChart: PieChartView
    private lateinit var rotateButton: Button
    private lateinit var rotationAnimator: ObjectAnimator

    var isLaunched = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listPayload = readJsonFromRaw()
        pieChart = requireActivity().findViewById(R.id.view_pie_chart)
        pieChart.setValues(listPayload)
        val categoriesChart = requireActivity().findViewById<CategoriesChartView>(R.id.view_categories_chart)
        categoriesChart.setCategories(listPayload)

        val nextButton = requireActivity().findViewById<Button>(R.id.next_btn)
        nextButton.setOnClickListener {
            replaceFragment(NextFragment())
        }

        rotateButton = requireActivity().findViewById(R.id.rotate_btn)
        rotateButton.setOnClickListener {
            launchRotationIfNeeded()
        }
    }

    private fun readJsonFromRaw(): List<PayloadUiModel> {
        val inputStream = this.resources.openRawResource(R.raw.payload)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val listType: Type = object : TypeToken<List<PayloadUiModel>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    private fun launchRotationIfNeeded() {
        if (isLaunched) {
            rotationAnimator.cancel()
            rotateButton.text = "Вращать"
        } else {
            startAnimation()
            rotateButton.text = " Стоп"
        }
        isLaunched = !isLaunched
    }

    private fun startAnimation() {
        rotationAnimator =
            ObjectAnimator.ofFloat(pieChart, "rotation", 0f, 360f).apply {
                duration = 2000
                interpolator = LinearInterpolator()
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }

        rotationAnimator.start()
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_slide_in,
                R.anim.fragment_slide_out,
                R.anim.fragment_slide_in,
                R.anim.fragment_slide_out
            )
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        if (::rotationAnimator.isInitialized && rotationAnimator.isRunning) {
            rotationAnimator.cancel()
        }
        super.onDestroy()
    }
}