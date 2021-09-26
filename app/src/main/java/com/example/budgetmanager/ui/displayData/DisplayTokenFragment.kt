package com.example.budgetmanager.ui.displayData

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.R
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.databinding.FragmentDisplayTokenBinding
import com.example.budgetmanager.ui.home.HomeViewModel
import com.example.budgetmanager.ui.home.HomeViewModelFactory

class DisplayTokenFragment : Fragment() {

    lateinit var displayViewModel: DisplayTokenViewModel
    lateinit var binding: FragmentDisplayTokenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        displayViewModel = ViewModelProvider(requireActivity()).get(DisplayTokenViewModel::class.java)

        binding = FragmentDisplayTokenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = displayViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.display_token_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.displayEdit) {
            displayViewModel.tokenRetrieved.value = false
            findNavController().navigate(
                DisplayTokenFragmentDirections.actionDisplayTokenFragmentToCreateDataFragment(
                    displayViewModel.displayToken.value!!
                )
            )
        }
        else {
            requireActivity().onBackPressed()
        }
        return true
    }

}