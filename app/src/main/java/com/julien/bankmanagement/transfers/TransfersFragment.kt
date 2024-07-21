package com.julien.bankmanagement.transfers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.julien.bankmanagement.adapters.transfers.TransfersAdapter
import com.julien.bankmanagement.base.BaseViewModelFragment
import com.julien.bankmanagement.databinding.FragmentTransfersBinding
import com.julien.bankmanagement.transfers.dialog.add.AddTransferDialog
import com.julien.bankmanagement.transfers.dialog.details.ShowTransferDetailsDialog
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils.gone
import com.julien.bankmanagement.utils.Utils.show
import com.julien.bankmanagement.utils.WrapContentLinearLayoutManager

class TransfersFragment: BaseViewModelFragment() {

    private var _binding: FragmentTransfersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TransfersViewModel>()

    private var adapterTransfer: TransfersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransfersBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateAccountID(TransfersFragmentArgs.fromBundle(requireArguments()).accountID)

        setAdapter()

        viewModel.transfers.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
                adapterTransfer?.submitData(viewLifecycleOwner.lifecycle,pagingData)
            }
        }

        initListeners()
    }

    private fun setAdapter(){

        adapterTransfer = TransfersAdapter { transferEntity ->
            ShowTransferDetailsDialog.showTransferDetailsDialog(parentFragmentManager,transferEntity)
        }
        binding.rvTransfers.apply {
            this.adapter = adapterTransfer
            layoutManager = WrapContentLinearLayoutManager(context)
        }

        adapterTransfer?.addOnPagesUpdatedListener {
            if (adapterTransfer?.snapshot().isNullOrEmpty()){
                binding.emptyView.show()
            } else{
                binding.emptyView.gone()
            }
        }
    }
    private fun initListeners(){
        if (TransfersFragmentArgs.fromBundle(requireArguments()).accountStatus == Constants.STATUS_FROZEN){
            binding.addTransferFab.gone()
            binding.btnAddTransfer.gone()
        }

        binding.imgBackBtn.setOnClickListener {
            navigateBack()
        }

        binding.addTransferFab.setOnClickListener {
            viewModel.accountID.value?.let { accountID ->
                AddTransferDialog.showTransfersDialog(parentFragmentManager,
                    accountID
                )
            }
        }

        binding.btnAddTransfer.setOnClickListener {
            viewModel.accountID.value?.let { accountID ->
                AddTransferDialog.showTransfersDialog(parentFragmentManager,
                    accountID
                )
            }
        }
    }

}