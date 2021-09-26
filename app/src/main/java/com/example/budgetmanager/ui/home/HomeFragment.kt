package com.example.budgetmanager.ui.home

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.databinding.FragmentHomeBinding
import com.example.budgetmanager.ui.displayData.DisplayTokenViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var displayViewModel: DisplayTokenViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding:FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModelFactory = HomeViewModelFactory(LocalDatabase.getDatabase(requireContext()).dao())
        homeViewModel = ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel::class.java)
        displayViewModel = ViewModelProvider(requireActivity()).get(DisplayTokenViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayAdapter.createFromResource(requireContext(), R.array.cur_day_week_month,
            R.layout.spinner_item).also {
            it.setDropDownViewResource(R.layout.spinner_item)
            binding.spinner.adapter = it
        }

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemLongClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                homeViewModel.selectionType.value = pos + 1
            }
            override fun onItemLongClick( p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long): Boolean  = false
            override fun onNothingSelected(p0: AdapterView<*>?) = homeViewModel.setAllTokens()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToCreateDataFragment())
        }
        val adapter = TokenAdapter(object : ClickListener {
            override fun onTokenClick(token: Token) {
                    displayViewModel.setDisplayToken(token)
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDisplayTokenFragment())
            }
        })
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
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
        homeViewModel.tokensList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                homeViewModel.calculateSum()
            }
        }
        homeViewModel.incomeSum.observe(viewLifecycleOwner) {
            binding.incomeTextView.text = it.toString()
        }
        homeViewModel.expenseSum.observe(viewLifecycleOwner) {
            binding.expenseTextView.text = it.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}