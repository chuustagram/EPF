package my.edu.tarc.epf.ui.investment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import my.edu.tarc.epf.R
import my.edu.tarc.epf.databinding.FragmentInvestmentBinding
import java.util.*
import java.util.Calendar.*

/**
 * A simple [Fragment] subclass.
 * Use the [InvestmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InvestmentFragment : Fragment() {
    private var _binding: FragmentInvestmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInvestmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDOB.setOnClickListener {
            val dateDialogFragment = DateDialogFragment { year, month, day ->
                onDateSelected(year, month, day)
            }
            dateDialogFragment.show(parentFragmentManager, "DatePicker")
        }
        binding.buttonCalculate.setOnClickListener {
            if (binding.editTextBalanceAccount1.text.isEmpty()) {
                binding.editTextBalanceAccount1.error = getString(R.string.error)
                return@setOnClickListener // Terminate program execution
            }
            if (binding.buttonDOB.text == getString(R.string.dob)) {
                binding.buttonDOB.error = getString(R.string.error)
                return@setOnClickListener
            }
            binding.buttonDOB.error = null
            val age = binding.textViewAge.text.toString().toInt()
            val amount = binding.editTextBalanceAccount1.text.toString().toDouble()
            var min_basic = 0.0
            var total = 0.0
            min_basic = when (age) {
                in 16..20 -> {
                    5000.0
                }
                in 21 .. 25 -> {
                    14000.0
                }
                in 26 .. 30 -> {
                    29000.0
                }
                in 31 .. 35 -> {
                    50000.0
                }
                in 36 .. 40 -> {
                    78000.0
                }
                in 41 .. 45 -> {
                    116000.0
                }
                in 46 .. 50 -> {
                    16500.0
                }
                in 51 .. 55 -> {
                    228000.0
                }
                else -> amount
            }
            total = (amount - min_basic) * 0.3
            if (total > 0.0) {
                binding.textViewAmountInvestment.text = String.format("RM %.2f", total)
            } else {
                val toast = Toast.makeText(activity, "You do not have enough money for investment", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonReset.setOnClickListener {
            binding.buttonDOB.error = null
            binding.buttonDOB.text = getString(R.string.dob)
            binding.editTextBalanceAccount1.setText("")
            binding.textViewAge.text = ""
            binding.textViewAmountInvestment.text = ""
        }
    }

    private fun onDateSelected(year: Int, month: Int, day: Int) {
        binding.buttonDOB.text = String.format("%d/%d/%d", day, month+1, year)
        val dob = Calendar.getInstance()
        with(dob) {
            set(YEAR, year)
            set(MONTH, month)
            set(DAY_OF_MONTH, day)
        }
        val today = getInstance()
        val age = daysBetween(dob, today).div(365)
        binding.textViewAge.text = age.toString()
    }

    private fun daysBetween(dob: Calendar, today: Calendar): Long {
        // we clone it so we won't change the passed-in dob
        val startDate = dob.clone() as Calendar
        var daysBetween: Long = 0
        while (startDate.before(today)) {
            startDate.add(DAY_OF_MONTH, 1)
            daysBetween++
        }
        return daysBetween
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Define an inner class of DateDialogFragment
    class DateDialogFragment(val dateSetListener: (year: Int, month: Int, day: Int) -> Unit) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(YEAR)
            val month = c.get(MONTH)
            val day = c.get(DAY_OF_MONTH)

            return DatePickerDialog(requireContext(), this, year, month, day)
        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
            dateSetListener(year, month, day)
        }
    }
}