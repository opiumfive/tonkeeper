package com.tonkeeper.core

import android.util.Log
import androidx.collection.arrayMapOf
import ton.SupportedCurrency
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.pow

object Coin {

    private const val DefaultDecimals = 18
    private const val BASE = 1000000000L
    private const val SMALL_SPACE = " "

    private val symbols = arrayMapOf<String, String>().apply {
        put("USD", "$")
        put("EUR", "€")
        put("RUB", "₽")
        put("AED", "DH")
        put("UAH", "₴")
        put("UZS", "лв")
        put("GBP", "£")
        put("CHF", "₣")
        put("CNY", "¥")
        put("KRW", "₩")
        put("IDR", "Rp")
        put("INR", "₹")
        put("JPY", "¥")
        put("TRY", "₺")
        put("THB", "฿")
        // put("TON", "\uD83D\uDC8E")
        put("TON", "TON")
    }

    private val currencyFormat = (NumberFormat.getCurrencyInstance() as DecimalFormat)
    private val simpleFormat = NumberFormat.getNumberInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    fun parseFloat(
        value: String,
        decimals: Any = DefaultDecimals
    ): Float {
        val actualDecimals = when (decimals) {
            is String -> decimals.toInt()
            else -> decimals as Int
        }
        val floatValue = value.toFloatOrNull() ?: return 0f
        return floatValue * 10.0.pow(-actualDecimals.toDouble()).toFloat()
    }

    fun toNano(value: Float): Long {
        return (value * BASE).toLong()
    }

    fun toCoins(value: Long): Float {
        return value / BASE.toFloat()
    }

    fun format(
        currency: SupportedCurrency,
        value: Long,
        useCurrencyCode: Boolean = false,
        decimals: Int = 2
    ): String {
        return format(currency.code, toCoins(value), useCurrencyCode, decimals)
    }

    fun format(
        currency: SupportedCurrency,
        value: Float,
        useCurrencyCode: Boolean = false,
        decimals: Int = 2
    ): String {
        return format(currency.code, value, useCurrencyCode, decimals)
    }

    fun format(
        currency: String = "",
        value: Long,
        useCurrencyCode: Boolean = false,
        decimals: Int = 2
    ): String {
        return format(currency, toCoins(value), useCurrencyCode, decimals)
    }

    fun format(
        currency: String = "",
        value: Float,
        useCurrencyCode: Boolean = false,
        decimals: Int = 2
    ): String {
        if (currency.isNotEmpty()) {
            val customSymbol = getSymbols(currency, useCurrencyCode)
            if (customSymbol != null) {
                return customFormat(customSymbol, value)
            }
            currencyFormat.currency = Currency.getInstance(currency)
            return currencyFormat.format(value)
        }
        simpleFormat.maximumFractionDigits = decimals
        return simpleFormat.format(value)
    }

    private fun getSymbols(
        currency: String,
        useCurrencyCode: Boolean = false
    ): String? {
        if (useCurrencyCode) {
            return currency
        }
        return symbols[currency]
    }

    private fun customFormat(
        customSymbol: String,
        value: Float
    ): String {
        val symbols = currencyFormat.decimalFormatSymbols
        val pattern = currencyFormat.toPattern()
        symbols.currencySymbol = customSymbol
        if (pattern.indexOf('¤') < pattern.indexOf('#')) {
            symbols.currencySymbol = "$customSymbol$SMALL_SPACE"
        } else {
            symbols.currencySymbol = "$SMALL_SPACE$customSymbol"
        }
        currencyFormat.decimalFormatSymbols = symbols
        return currencyFormat.format(value)
    }
}