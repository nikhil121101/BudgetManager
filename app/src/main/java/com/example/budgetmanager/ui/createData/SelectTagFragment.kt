package com.example.budgetmanager.ui.createData

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.example.budgetmanager.R
import com.example.budgetmanager.database.LocalDatabase
import com.example.budgetmanager.databinding.FragmentSelectTagBinding

class SelectTagFragment : Fragment() {

    private lateinit var binding: FragmentSelectTagBinding
    private lateinit var viewModel: CreateDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectTagBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = CreateDataModelFactory(LocalDatabase.getDatabase(requireContext()).dao())
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CreateDataViewModel::class.java)

        viewModel.clearSelectedTags()

        viewModel.availableTags.observe(viewLifecycleOwner) { selectTagsArray ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, selectTagsArray)
            binding.listView.adapter = adapter
        }

        binding.addTag.setOnClickListener {
            if(binding.newTagEditText.text.isNotEmpty()) {
                viewModel.addAvailableTag(binding.newTagEditText.text.toString())
            }
            binding.newTagEditText.text.clear()
        }

        binding.listView.setOnItemLongClickListener { _ , _, pos, _ ->
            AlertDialog.Builder(context).apply {
                setTitle("Delete")
                setMessage("Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.removeAvailableTag(viewModel.availableTags.value!![pos])
                }
                setNegativeButton("NO", null)
                show()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.select_tags_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.selectedTagSave) {
            Log.i("dudea", "came here")
            viewModel.clearSelectedTags()
            val selectedBoolArr = binding.listView.checkedItemPositions
            val selectedTagStringArr = ArrayList<String>()
            for (i in 0..binding.listView.count) {
                if (selectedBoolArr[i]) {
                    selectedTagStringArr.add(viewModel.availableTags.value!![i])
                }
            }
            Log.i("uadai", selectedTagStringArr.toString())
            Log.i("uadai", viewModel.selectedTags.value.toString())
            viewModel.setSelectedTag(selectedTagStringArr)
            Log.i("uadai", viewModel.selectedTags.value.toString())
        }
        requireActivity().onBackPressed()
        return true
    }
}