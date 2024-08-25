package com.example.farmereats.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentFramInfoBinding


class FarmInfoFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFramInfoBinding.inflate(inflater)

        binding.stateAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            binding.stateAutocompleteTextView.setSelection(0)
        }
        val items = listOf(
            "Alabama", "Alaska", "Arizona", "Arkansas",
            "California", "Colorado", "Connecticut", "Delaware",
            "Florida", "Georgia", "Hawaii", "Idaho",
            "Illinois", "Indiana", "Iowa", "Kansas",
            "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi",
            "Missouri", "Montana", "Nebraska", "Nevada",
            "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma",
            "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee", "Texas", "Utah",
            "Vermont", "Virginia", "Washington", "West Virginia",
            "Wisconsin", "Wyoming"
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.list_adpater, items)
        adapter.setDropDownViewResource(R.layout.list_adpater)
        binding.stateAutocompleteTextView.setAdapter(adapter)

        binding.bigContinueBtn.setOnClickListener {
            val newAddress = binding.streetAddressEditText.text.toString()
            val newBusinessName = binding.businessNameEditText.text.toString()
            val newCity = binding.cityEditText.text.toString()
            val newInformalName = binding.informalEditText.text.toString()
            val newState = binding.stateAutocompleteTextView.text.toString()
            val newZipCode = binding.pincodeEditText.text.toString().toIntOrNull()

            val currentUser = sharedViewModel.newUser.value
            val updatedUser = currentUser?.copy(
                address = newAddress,
                business_name = newBusinessName,
                city = newCity,
                informal_name = newInformalName,
                state = newState,
                zip_code = newZipCode
            )

            updatedUser?.let {
                sharedViewModel.updateUser(it)
                findNavController().navigate(R.id.action_framInfoFragment_to_verificationFragment)
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

}
