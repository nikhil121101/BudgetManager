package com.example.budgetmanager.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.database.models.MonthlyResult
import com.example.budgetmanager.databinding.MonthlyHistoryFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class MonthlyHistoryFragment : Fragment() {

    lateinit var viewModel: MonthlyHistoryViewModel
    lateinit var binding: MonthlyHistoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MonthlyHistoryFragmentBinding.inflate(layoutInflater, container, false)
        val viewModelFactory = MonthlyHistoryModelFactory(LocalDatabase.getDatabase(requireContext()).dao())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MonthlyHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MonthlyAdapter(object: MonthlyClickListener {
            override fun onMonthlyClick(monthlyResult: MonthlyResult) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = monthlyResult.dateLong
                viewModel.setSpecificMonthToken(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
                viewModel.specificDateLong.value = monthlyResult.dateLong
                findNavController().navigate(MonthlyHistoryFragmentDirections.actionMonthlyHistoryFragmentToSpecificMonthFragment())
            }
        })
        binding.monthlyRecyclerView.adapter = adapter
        viewModel.monthlyList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}