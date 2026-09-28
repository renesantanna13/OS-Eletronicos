package com.kaua.ostracker

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kaua.ostracker.databinding.OrderDetailLayoutBinding
import com.kaua.ostracker.databinding.StepItemLayoutBinding

class OrderDetailActivity : AppCompatActivity() {
    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    private lateinit var binding: OrderDetailLayoutBinding
    private lateinit var order: ServiceOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = OrderDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.orderDetailToolbar.setNavigationOnClickListener { finish() }

        val orderId = intent.getStringExtra(ORDER_ID_KEY)
        val foundOrder = orderId?.let { ServiceOrderRepository.findById(it) }
        if (foundOrder == null) {
            Toast.makeText(this, R.string.order_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        order = foundOrder

        bindHeader()
        bindIssue()

        // evita adicionar o fragment de novo quando a tela e recriada (ex.: rotacao)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.order_info_container, OrderInfoFragment.newInstance(order.id))
                .commit()
        }

        binding.fabPreviousStep.setOnClickListener {
            order.undoStep()?.let { updateOrder(it) }
        }
        binding.fabNextStep.setOnClickListener {
            order.advanceStep()?.let { updateOrder(it) }
        }
        updateProgressViews()
    }

    private fun updateOrder(newOrder: ServiceOrder) {
        order = newOrder
        ServiceOrderRepository.update(newOrder)
        updateProgressViews()
    }

    private fun bindHeader() {
        binding.headerDeviceIcon.setImageResource(order.category.iconRes)
        binding.orderNumberTextView.text = getString(R.string.order_number, order.number)
        binding.deviceTitleTextView.text =
            getString(R.string.device_title, order.brand, order.deviceName)

        binding.categoryBadge.badgeTextView.text = getString(order.category.labelRes)
        binding.urgentBadge.root.visibility = if (order.isUrgent) View.VISIBLE else View.GONE
        binding.urgentBadge.badgeTextView.text = getString(R.string.urgent)
        binding.urgentBadge.root.backgroundTintList =
            ColorStateList.valueOf(getColor(R.color.status_urgent))
    }

    private fun bindIssue() {
        binding.reportedIssueTextView.text = order.reportedIssue
        binding.diagnosisTextView.text = order.diagnosis ?: getString(R.string.diagnosis_pending)
    }

    private fun updateProgressViews() {
        binding.statusBadge.badgeTextView.text = getString(order.status.labelRes)
        binding.statusBadge.root.backgroundTintList =
            ColorStateList.valueOf(getColor(order.status.colorRes))

        binding.currentStepTextView.text =
            order.currentStep?.let { getString(R.string.next_step, it) }
                ?: getString(R.string.repair_finished)
        binding.stepsCountTextView.text =
            getString(R.string.steps_count, order.completedSteps, order.totalSteps)
        binding.progressPercentageTextView.text = getString(R.string.percentage, order.progress)
        binding.repairProgressIndicator.setProgressCompat(order.progress, true)

        binding.fabPreviousStep.isEnabled = order.completedSteps > 0
        binding.fabNextStep.isEnabled = order.completedSteps < order.totalSteps

        binding.stepsContainer.removeAllViews()
        order.steps.forEachIndexed { index, step ->
            val item = StepItemLayoutBinding.inflate(layoutInflater, binding.stepsContainer, true)
            val done = index < order.completedSteps
            item.stepText.text = step
            item.stepIcon.setImageResource(
                if (done) R.drawable.ic_check_circle else R.drawable.ic_circle_outline
            )
            item.stepIcon.imageTintList = ColorStateList.valueOf(
                getColor(if (done) R.color.step_done else R.color.text_secondary)
            )
            item.stepIcon.contentDescription =
                getString(if (done) R.string.step_done else R.string.step_pending)
            item.stepText.alpha = if (done) 0.6f else 1f
        }
    }
}
