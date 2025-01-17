package com.example.bondoman.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.LoginActivity
import com.example.bondoman.MainActivity
import com.example.bondoman.databinding.FragmentSettingBinding
import com.example.bondoman.ui.transaction.TransactionViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val transactionViewModel = (requireActivity() as MainActivity).getTransactionViewModel()

        binding.buttonSaveTransaction.setOnClickListener {
            showSaveDialog(transactionViewModel)
        }

        binding.buttonSendTransaction.setOnClickListener {
            showSendDialog(transactionViewModel)
        }

        binding.buttonLogout.setOnClickListener {
            // Stop all services
            settingViewModel.logout()

            // Redirect to LoginActivity
            val activityIntent = Intent(requireContext(), LoginActivity::class.java)
            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(activityIntent)
            requireActivity().finish()
        }

        binding.buttonRandomize.setOnClickListener {
            settingViewModel.changeRandomizationStatus()
            if (settingViewModel.getIsRandomizationEnabled()) {
                Toast.makeText(requireContext(), "Randomization is enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Randomization is disabled", Toast.LENGTH_SHORT).show()
            }
            // Broadcast
            sendRandomizationIntent()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSaveDialog(transactionViewModel: TransactionViewModel) {
        val options = arrayOf("xlsx", "xls")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose File Extension")
        builder.setItems(options) { _, which ->
            val extension = options[which]
            saveTransactionsToFile(transactionViewModel, extension)
        }
        builder.show()
    }

    private fun showSendDialog(transactionViewModel: TransactionViewModel) {
        val options = arrayOf("xlsx", "xls")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose File Extension")
        builder.setItems(options) { _, which ->
            val extension = options[which]
            sendEmailWithAttachment(transactionViewModel, extension)
        }
        builder.show()
    }

    private fun saveTransactionsToFile(transactionViewModel: TransactionViewModel, extension: String) {
        settingViewModel.saveTransactionsToFile(requireActivity(), transactionViewModel.listTransactions.value, extension)
    }

    private fun sendEmailWithAttachment(transactionViewModel: TransactionViewModel, extension: String) {
        settingViewModel.sendEmailWithAttachment(requireActivity(), transactionViewModel.listTransactions.value, extension)
    }

    // Transaction Randomizer Functions
    // Di SettingFragment.kt
    private fun sendRandomizationIntent() {
        // Initialize Intent
        val randomTransactionIntent = Intent("com.example.bondoman.RANDOM_TRANSACTION_ACTION")
        randomTransactionIntent.putExtra("isRandomizationEnabled", settingViewModel.getIsRandomizationEnabled())
        // Send Intent
        requireContext().sendBroadcast(randomTransactionIntent)
    }
}
