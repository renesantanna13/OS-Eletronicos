package com.kaua.ostracker

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaua.ostracker.databinding.ActivityHomeBinding
import com.kaua.ostracker.databinding.HomeListItemLayoutBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ServiceOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ServiceOrderAdapter(ServiceOrderRepository.getAll())
        binding.ordersRecyclerView.adapter = adapter
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // atualiza a lista ao voltar do detalhe
    override fun onResume() {
        super.onResume()
        val orders = ServiceOrderRepository.getAll()
        adapter.updateList(orders)

        val openCount = orders.count { it.status != OrderStatus.READY }
        val readyCount = orders.size - openCount
        binding.summaryTextView.text =
            getString(R.string.home_summary, openCount, readyCount)
    }
}

class ServiceOrderAdapter(private var list: List<ServiceOrder>) :
    RecyclerView.Adapter<ServiceOrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: HomeListItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
    }

    fun updateList(newList: List<ServiceOrder>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            HomeListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = list[position]
        val context = holder.context

        with(holder.binding) {
            deviceIcon.setImageResource(order.category.iconRes)
            orderNumber.text = context.getString(R.string.order_number, order.number)
            deviceName.text = context.getString(R.string.device_title, order.brand, order.deviceName)
            customerName.text = order.customerName
            stepsCount.text =
                context.getString(R.string.steps_count, order.completedSteps, order.totalSteps)
            itemProgress.progress = order.progress

            statusBadge.badgeTextView.text = context.getString(order.status.labelRes)
            statusBadge.root.backgroundTintList =
                ColorStateList.valueOf(context.getColor(order.status.colorRes))

            urgentBadge.root.visibility = if (order.isUrgent) View.VISIBLE else View.GONE
            urgentBadge.badgeTextView.text = context.getString(R.string.urgent)
            urgentBadge.root.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.status_urgent))

            root.setOnClickListener {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(OrderDetailActivity.ORDER_ID_KEY, order.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
