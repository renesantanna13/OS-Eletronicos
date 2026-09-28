package com.kaua.ostracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaua.ostracker.databinding.DetailRowLayoutBinding
import com.kaua.ostracker.databinding.FragmentOrderInfoBinding
import java.text.NumberFormat
import java.util.Locale

class OrderInfoFragment : Fragment() {
    companion object {
        private const val ARG_ORDER_ID = "orderId"

        fun newInstance(orderId: String): OrderInfoFragment {
            val fragment = OrderInfoFragment()
            fragment.arguments = Bundle().apply { putString(ARG_ORDER_ID, orderId) }
            return fragment
        }
    }

    // binding so existe entre onCreateView e onDestroyView
    private var _binding: FragmentOrderInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString(ARG_ORDER_ID) ?: return
        val order = ServiceOrderRepository.findById(orderId) ?: return

        bindDetails(order)
        bindCallButton(order.customerPhone)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindDetails(order: ServiceOrder) {
        val currency = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

        val rows = listOf(
            R.string.customer to order.customerName,
            R.string.phone to (order.customerPhone ?: getString(R.string.not_informed)),
            R.string.model to (order.model ?: getString(R.string.not_informed)),
            R.string.technician to (order.technician ?: getString(R.string.not_assigned)),
            R.string.received_at to order.receivedAt,
            R.string.estimated_delivery to (order.estimatedDelivery ?: getString(R.string.no_estimate)),
            R.string.estimated_cost to (order.estimatedCost?.let { currency.format(it) }
                ?: getString(R.string.to_be_defined))
        )

        for ((titleRes, value) in rows) {
            val row = DetailRowLayoutBinding.inflate(layoutInflater, binding.detailsContainer, true)
            row.rowTitle.text = getString(titleRes)
            row.rowValue.text = value
        }
    }

    private fun bindCallButton(phone: String?) {
        if (phone == null) {
            binding.callCustomerButton.visibility = View.GONE
            return
        }

        binding.callCustomerButton.setOnClickListener {
            val digits = phone.filter { it.isDigit() }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$digits"))
            startActivity(intent)
        }
    }
}
