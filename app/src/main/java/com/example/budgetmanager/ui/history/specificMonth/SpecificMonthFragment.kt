package com.example.budgetmanager.ui.history.specificMonth

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.databinding.FragmentSpecificMonthBinding
import com.example.budgetmanager.ui.displayData.DisplayTokenViewModel
import com.example.budgetmanager.ui.history.MonthlyHistoryModelFactory
import com.example.budgetmanager.ui.history.MonthlyHistoryViewModel
import com.example.budgetmanager.ui.home.*
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.*

class SpecificMonthFragment : Fragment() {

    private lateinit var monthlyViewModel: MonthlyHistoryViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var displayViewModel: DisplayTokenViewModel
    lateinit var binding: FragmentSpecificMonthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        monthlyViewModel = ViewModelProvider(requireActivity(), MonthlyHistoryModelFactory(
            LocalDatabase.getDatabase(requireContext()).dao()
        )).get(MonthlyHistoryViewModel::class.java)

        val homeViewModelFactory = HomeViewModelFactory(LocalDatabase.getDatabase(requireContext()).dao())
        homeViewModel = ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel::class.java)

        displayViewModel = ViewModelProvider(requireActivity()).get(DisplayTokenViewModel::class.java)

//        requireActivity().title = SimpleDateFormat("MMM yyyy").format(Date(monthlyViewModel.specificDateLong.value!!))
        (activity as AppCompatActivity).supportActionBar?.title =
            SimpleDateFormat("MMM yyyy").format(Date(monthlyViewModel.specificDateLong.value!!))

        binding = FragmentSpecificMonthBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TokenAdapter(object : ClickListener {
            override fun onTokenClick(token: Token) {
                displayViewModel.setDisplayToken(token)
                findNavController().navigate(SpecificMonthFragmentDirections.actionSpecificMonthFragmentToDisplayTokenFragment())
            }
        })

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
            ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean  = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedToken = adapter.currentList[viewHolder.absoluteAdapterPosition];
                homeViewModel.deleteToken(adapter.currentList[viewHolder.absoluteAdapterPosition])
                Snackbar.make(binding.recyclerView, "Transaction deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        homeViewModel.addToken(deletedToken)
                    }.show()
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                     dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(requireContext().getColor(R.color.red))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter

        monthlyViewModel.specificMonthTokens.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                monthlyViewModel.calculateSum()
            }
        }
        monthlyViewModel.incomeSum.observe(viewLifecycleOwner) {
            binding.specificIncomeTextView.text = it.toString()
        }
        monthlyViewModel.expenseSum.observe(viewLifecycleOwner) {
            binding.specificExpenseTextView.text = it.toString()
        }

    }
}
