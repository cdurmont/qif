package com.github.squirrelgrip.qif4k.write

enum class QifInvestmentAction(val text: String) {
    BUY("Buy"), SELL("Sell"), DIV("Div"), INTEREST_INCOME("IntInc"), TRANSFER_IN("XIn"), TRANSFER_OUT("XOut");

    companion object {
        fun forText(text: String?): QifInvestmentAction {
            for (action in values()) {
                if (action.text == text) {
                    return action
                }
            }
            throw IllegalArgumentException("$text is not a QIF Invst action")
        }
    }

}