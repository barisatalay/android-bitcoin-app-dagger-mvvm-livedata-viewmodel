package com.barisatalay.bitcoinveotesi

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barisatalay.bitcoinveotesi.core.model.Coin
import com.barisatalay.bitcoinveotesi.ui.adapter.CoinAdapter
import com.barisatalay.cointracker.CoinAndBeyond
import com.barisatalay.cointracker.data.Koineks
import com.barisatalay.cointracker.data.Paribu
import com.barisatalay.cointracker.service.model.enmCoin
import com.barisatalay.cointracker.service.model.mdlCoin
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val adapter = CoinAdapter(arrayListOf())

    private val paribu = CoinAndBeyond(Paribu(),this,this)
    private lateinit var paribuCoinDetail : HashMap<enmCoin, ArrayList<mdlCoin>>

    private val koineks = CoinAndBeyond(Koineks(),this,this)
    private lateinit var koineksCoinDetail : HashMap<enmCoin, ArrayList<mdlCoin>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideLoading()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        coinList.layoutManager = linearLayoutManager
        coinList.adapter = adapter
        coinList.setHasFixedSize(true)

        refreshAllDataset()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                showLoading()
                when (p0?.position){
                    0-> {
                        adapter.setAll(prepareCoinToList(paribuCoinDetail))
                    }
                    1-> {
                        adapter.setAll(prepareCoinToList(koineksCoinDetail))
                    }

                }

                hideLoading()
            }
        })
    }

    private fun showLoading() {
        progres_layout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progres_layout.visibility = View.GONE
    }

    private fun prepareCoinToList(source: HashMap<enmCoin, ArrayList<mdlCoin>>): ArrayList<Coin> {
        val list = ArrayList<Coin>()

        for( item in source){
            if (!item.value.isEmpty()) {
                list.add(Coin(0, item.key, item.value[0]))
            }
        }
        return list
    }

    private fun refreshAllDataset() {
        paribu.getCoins(Observer {response->
            paribuCoinDetail = response.getCoinDetail()

            if (adapter.itemCount == 0)
                adapter.setAll(prepareCoinToList(paribuCoinDetail))
        })
        koineks.getCoins(Observer {response->
            koineksCoinDetail = response.getCoinDetail()
        })

    }
}
