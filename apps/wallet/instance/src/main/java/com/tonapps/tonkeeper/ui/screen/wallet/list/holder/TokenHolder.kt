package com.tonapps.tonkeeper.ui.screen.wallet.list.holder

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.tonapps.tonkeeper.extensions.buildRateString
import com.tonapps.tonkeeper.ui.screen.token.TokenScreen
import com.tonapps.tonkeeper.ui.screen.wallet.list.Item
import com.tonapps.tonkeeperx.R
import com.tonapps.uikit.color.accentOrangeColor
import com.tonapps.uikit.color.textSecondaryColor
import com.tonapps.wallet.api.entity.TokenEntity
import com.tonapps.wallet.data.core.HIDDEN_BALANCE
import com.tonapps.wallet.localization.Localization
import uikit.extensions.drawable
import uikit.navigation.Navigation.Companion.navigation
import uikit.widget.FrescoView

class TokenHolder(parent: ViewGroup): Holder<Item.Token>(parent, R.layout.view_cell_jetton) {

    private val iconView = findViewById<FrescoView>(R.id.icon)
    private val iconView2 = findViewById<FrescoView>(R.id.icon2)
    private val iconView2Content = findViewById<View>(R.id.icon2Content)
    private val titleView = findViewById<AppCompatTextView>(R.id.title)
    private val rateView = findViewById<AppCompatTextView>(R.id.rate)
    private val balanceView = findViewById<AppCompatTextView>(R.id.balance)
    private val balanceFiatView = findViewById<AppCompatTextView>(R.id.balance_currency)

    override fun onBind(item: Item.Token) {
        itemView.background = item.position.drawable(context)
        if ( item.isStaking) {
            titleView.text = getString(Localization.staked)
            iconView.setImageURI(TokenEntity.TON_ICON_URI, this)
            iconView2Content.isVisible = true
            iconView2.setImageURI(item.iconUri, this)
            rateView.visibility = View.VISIBLE
            rateView.text = item.name
            rateView.setTextColor(context.textSecondaryColor)
            balanceView.text = if (item.hiddenBalance) {
                HIDDEN_BALANCE
            } else {
                item.balanceFormat
            }
            if (item.testnet) {
                balanceFiatView.visibility = View.GONE
            } else {
                balanceFiatView.visibility = View.VISIBLE
                if (item.hiddenBalance) {
                    balanceFiatView.text = HIDDEN_BALANCE
                } else {
                    balanceFiatView.text = item.fiatFormat
                }
            }
            itemView.setOnClickListener {
                context.navigation?.add(TokenScreen.newInstance(item.address, item.name, item.symbol, true))
            }
            return
        }

        iconView2Content.isVisible = false
        itemView.setOnClickListener {
            context.navigation?.add(TokenScreen.newInstance(item.address, item.name, item.symbol))
        }
        iconView.setImageURI(item.iconUri, this)
        titleView.text = item.symbol
        balanceView.text = if (item.hiddenBalance) {
            HIDDEN_BALANCE
        } else {
            item.balanceFormat
        }

        if (item.testnet) {
            rateView.visibility = View.GONE
            balanceFiatView.visibility = View.GONE
        } else {
            balanceFiatView.visibility = View.VISIBLE
            if (item.hiddenBalance) {
                balanceFiatView.text = HIDDEN_BALANCE
            } else {
                balanceFiatView.text = item.fiatFormat
            }
            setRate(item.rate, item.rateDiff24h, item.verified)
        }
    }

    private fun setRate(rate: CharSequence, rateDiff24h: String, verified: Boolean) {
        rateView.visibility = View.VISIBLE
        if (verified) {
            rateView.text = context.buildRateString(rate, rateDiff24h)
            rateView.setTextColor(context.textSecondaryColor)
        } else {
            rateView.setText(Localization.unverified_token)
            rateView.setTextColor(context.accentOrangeColor)
        }
    }

}