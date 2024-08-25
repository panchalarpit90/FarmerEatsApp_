package com.example.farmereats.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentBusinessHoursBinding
import com.example.farmereats.models.BusinessHours
import com.example.farmereats.utils.ApiResponse
import com.example.farmereats.utils.getRandomString

class BusinessHoursFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentBusinessHoursBinding
    private val dayTimeMap = mutableMapOf<String, MutableList<String>>()
    private var selectedDay: String? = null
    private val buttonStateMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialButtonColors()
        val buttonIds = listOf(
            R.id.btn_slot_1 to "8:00am - 10:00am",
            R.id.btn_slot_2 to "10:00am - 1:00pm",
            R.id.btn_slot_3 to "1:00pm - 4:00pm",
            R.id.btn_slot_4 to "4:00pm - 7:00pm",
            R.id.btn_slot_5 to "7:00pm - 10:00pm"
        )
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonIds.forEach { (buttonId, timeSlot) ->
            val button = binding.root.findViewById<Button>(buttonId)

            buttonStateMap[buttonId] = false

            button.setOnClickListener {
                toggleButtonColor(buttonId, timeSlot)
            }
        }

        binding.weekChipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedDay = when (checkedId) {
                R.id.mon_chip -> "Mon"
                R.id.tue_chip -> "Tue"
                R.id.wed_chip -> "Wed"
                R.id.thu_chip -> "Thu"
                R.id.fri_chip -> "Fri"
                R.id.sat_chip -> "Sat"
                R.id.sun_chip -> "Sun"
                else -> null
            }

            selectedDay?.let { day ->
                updateGridLayout(dayTimeMap[day] ?: mutableListOf())
            }
        }

        viewModel.registrationResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
                        .show()
                    if (response.data.success == "true") {
                        findNavController().navigate(R.id.action_businessHoursFragment_to_confirmationFragment)
                    }
                }

                is ApiResponse.Error -> {
                    Toast.makeText(requireContext(), response.exception.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.bigSignBtn.setOnClickListener {
            val randomId = getRandomString(16)

            val businessHours = BusinessHours(
                mon = dayTimeMap["Mon"] ?: listOf(),
                tue = dayTimeMap["Tue"] ?: listOf(),
                wed = dayTimeMap["Wed"] ?: listOf(),
                thu = dayTimeMap["Thu"] ?: listOf(),
                fri = dayTimeMap["Fri"] ?: listOf(),
                sat = dayTimeMap["Sat"] ?: listOf(),
                sun = dayTimeMap["Sun"] ?: listOf()
            )

            val updatedUser = viewModel.newUser.value?.copy(
                business_hours = businessHours,
                device_token = randomId,
                role = "farmer",
                social_id = randomId,
                type = "email"
            )

            updatedUser?.let {
                viewModel.updateUser(it)
                viewModel.registerUser()
            }
        }
    }

    private fun updateGridLayout(selectedTimes: List<String>) {
        val buttonIds = listOf(
            R.id.btn_slot_1 to "8:00am - 10:00am",
            R.id.btn_slot_2 to "10:00am - 1:00pm",
            R.id.btn_slot_3 to "1:00pm - 4:00pm",
            R.id.btn_slot_4 to "4:00pm - 7:00pm",
            R.id.btn_slot_5 to "7:00pm - 10:00pm"
        )

        buttonIds.forEach { (buttonId, timeSlot) ->
            val button = binding.root.findViewById<Button>(buttonId)
            button.isSelected = selectedTimes.contains(timeSlot)
        }
    }

    private fun toggleButtonColor(buttonId: Int, timeSlot: String) {
        val button = binding.root.findViewById<Button>(buttonId)
        val isSelected = buttonStateMap[buttonId] ?: false

        if (isSelected) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tintColor))
            buttonStateMap[buttonId] = false
        } else {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
            buttonStateMap[buttonId] = true
        }

        selectedDay?.let { day ->
            val times = dayTimeMap[day] ?: mutableListOf()
            if (isSelected) {
                times.remove(timeSlot)
            } else {
                times.add(timeSlot)
            }
            dayTimeMap[day] = times

        }
    }

    private fun setInitialButtonColors() {
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.tintColor)
        listOf(
            R.id.btn_slot_1,
            R.id.btn_slot_2,
            R.id.btn_slot_3,
            R.id.btn_slot_4,
            R.id.btn_slot_5
        ).forEach { buttonId ->
            val button = binding.root.findViewById<Button>(buttonId)
            button.setBackgroundColor(defaultColor)
        }
    }

}
