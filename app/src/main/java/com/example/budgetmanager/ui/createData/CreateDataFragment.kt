package com.example.budgetmanager.ui.createData

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.R
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.databinding.CreateDataFragmentBinding
import com.example.budgetmanager.ui.displayData.DisplayTokenViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*


class CreateDataFragment : Fragment() {


    private lateinit var binding:CreateDataFragmentBinding
    private lateinit var createDataViewModel: CreateDataViewModel
    private lateinit var displayTokenViewModel: DisplayTokenViewModel

    private var currentToken: Token? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        currentToken = CreateDataFragmentArgs.fromBundle(requireArguments()).currentToken
        binding = CreateDataFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewModelFactory = CreateDataModelFactory(LocalDatabase.getDatabase(requireContext()).dao())
        createDataViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CreateDataViewModel::class.java)

        displayTokenViewModel = ViewModelProvider(requireActivity()).get(DisplayTokenViewModel::class.java)

        retrieveToken()


        createDataViewModel.selectedTags.observe(viewLifecycleOwner) {
            setTagsArray(it)
        }
        createDataViewModel.selectedDate.observe(viewLifecycleOwner) {
            it?.let {
                binding.dateTextView.text = getFormattedDate(it)
            }
        }

        binding.typeChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.incomeChip -> createDataViewModel.selectedType.value = 1
                R.id.expenseChip -> createDataViewModel.selectedType.value = -1
            }
        }
        binding.addTag.setOnClickListener {
            findNavController().navigate(CreateDataFragmentDirections.actionCreateDataFragmentToSelectTagFragment())
        }
        binding.addTagIcon.setOnClickListener {
            findNavController().navigate(CreateDataFragmentDirections.actionCreateDataFragmentToSelectTagFragment())
        }
        setUpCalender()
        binding.saveButton.setOnClickListener {
            submitData()
        }
    }

    private fun retrieveToken() {
        if(displayTokenViewModel.tokenRetrieved.value!!) {
            return
        }
        displayTokenViewModel.tokenRetrieved.value = true
        currentToken?.let {
            binding.amount.setText(it.amount.toString())
            binding.title.setText(it.title)
            createDataViewModel.setSelectedTag(it.tags!!)
            createDataViewModel.selectedDate.value = Date(it.dateLong)
            binding.description.setText(it.description)
            when (it.type) {
                1 -> {
                    createDataViewModel.selectedType.value = 1;
                    binding.typeChipGroup.check(R.id.incomeChip)
                }
                -1 -> {
                    createDataViewModel.selectedType.value = -1;
                    binding.typeChipGroup.check(R.id.expenseChip)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.select_tags_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.selectedTagSave) {
            submitData()
        }
        else {
            requireActivity().onBackPressed()
        }
        return true
    }

    fun submitData() {
        if(validDetails()) {
            if(currentToken != null) {
                createDataViewModel.deleteToken(currentToken!!)
            }
            val newToken = createDataViewModel.addToken(title = binding.title.text.toString(), amount = binding.amount.text.toString(),
                description = binding.description.text.toString())
            if(currentToken != null) {
                displayTokenViewModel.setDisplayToken(newToken)
            }
            requireActivity().onBackPressed()
        }
    }

    private fun setTagsArray(list: List<String>?) {
        list?.let {
            binding.tagChipGroup.removeAllViews()
            for(str in list) {
                addChip(str, binding.tagChipGroup)
            }
        }

    }

    private fun addChip(pItem: String, pChipGroup: ChipGroup) {
        val lChip = Chip(requireContext())
        lChip.text = pItem
        lChip.gravity = Gravity.CENTER_VERTICAL
        pChipGroup.addView(lChip, pChipGroup.childCount - 1)
    }

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }

    private fun setUpCalender() {
        val calendar = Calendar.getInstance()
        if(createDataViewModel.selectedDate.value == null) {
            createDataViewModel.selectedDate.value = calendar.time
        }
        else {
            calendar.timeInMillis = createDataViewModel.selectedDate.value!!.time
        }
        binding.dateTextView.text = getFormattedDate(calendar.time)
        binding.dateContainer.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, date ->
                calendar.set(year, month, date)
                createDataViewModel.selectedDate.value = calendar.time
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(date: Date): String {
        return SimpleDateFormat("EEEE, MMM d, yyyy").format(date)
    }

    private fun validDetails(): Boolean {
        val amountString:String = binding.amount.text.toString()
        if(amountString.isEmpty() || amountString.isBlank() || amountString.toDoubleOrNull() == null) {
            binding.amount.error = "Invalid"
            return false
        }
        val titleString:String = binding.title.text.toString()
        if(titleString.isEmpty() || titleString.isBlank()) {
            binding.title.error = "Can't be empty"
            return false
        }
        return true
    }

}