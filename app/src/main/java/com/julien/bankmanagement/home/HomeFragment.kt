package com.julien.bankmanagement.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.julien.bankmanagement.R
import com.julien.bankmanagement.adapters.home.AccountsAdapter
import com.julien.bankmanagement.adapters.home.OnAccountClickListener
import com.julien.bankmanagement.base.BaseViewModelFragment
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.databinding.FragmentHomeBinding
import com.julien.bankmanagement.home.dialog.AddAccountDialog
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.Utils.gone
import com.julien.bankmanagement.utils.Utils.show
import com.julien.bankmanagement.utils.WrapContentLinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment:BaseViewModelFragment(),OnAccountClickListener  {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private var adapterAccounts: AccountsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.accounts.collect { pagingData ->
                if (pagingData != null) {
                    adapterAccounts?.submitData(pagingData)
                }
            }
        }

        initListeners()
    }

    private fun setAdapter(){
        adapterAccounts = AccountsAdapter(this)
        binding.rvAccounts.apply {
            this.adapter = adapterAccounts
            layoutManager = WrapContentLinearLayoutManager(context)
        }

        adapterAccounts?.addOnPagesUpdatedListener {
            if (adapterAccounts?.snapshot().isNullOrEmpty()){
                binding.emptyView.show()
                binding.addAccountFab.gone()
            } else{
                binding.emptyView.gone()
                binding.addAccountFab.show()
            }
        }
    }

    private fun initListeners(){
        binding.addAccountFab.setOnClickListener {
            AddAccountDialog.showAddAccountDialog(parentFragmentManager)
        }

        binding.btnAddAccount.setOnClickListener {
            AddAccountDialog.showAddAccountDialog(parentFragmentManager)
        }
    }

    override fun delete(account: BankAccountEntity) {
        viewModel.deleteAccount(account)
    }

    override fun changeStatus(account: BankAccountEntity,position: Int) {
        viewModel.updateAccount(
            accountID = account.accountID,
            userID = account.userID,
            name = account.name,
            iBAN = account.iBAN,
            status = if (account.status == Constants.STATUS_ACTIVE) Constants.STATUS_FROZEN else Constants.STATUS_ACTIVE,
            availableAmount = account.availableAmount,
            createdOn = account.createdOn,
            modifiedOn = (System.currentTimeMillis() / 1000L).toString()
        )
    }

    override fun optionsMenuClick(account: BankAccountEntity, itemID: Int) {
        when(itemID) {
            R.id.transfers -> navigateTo( action = HomeFragmentDirections.actionHomeFragmentToTransfersFragment(account.accountID,account.status))
            R.id.details -> navigateTo( action = HomeFragmentDirections.actionHomeFragmentToAccountDetailsFragment(account.accountID))
        }
    }

}