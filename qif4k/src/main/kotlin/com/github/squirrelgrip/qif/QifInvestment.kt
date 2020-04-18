package com.github.squirrelgrip.qif

import java.math.BigDecimal

class QifInvestment : QifTransaction() {
    var action: String? = null
    var price: BigDecimal? = null
    var quantity: BigDecimal? = null
    var security: String? = null
    var commission: BigDecimal? = null

    /**
     * Note: Transfer In or Transfer Out actions use this field to indicate the
     * destination when the value is wrapped like this: `[Checking]`
     *
     * @param category The category assigned to the Investment
     */
    var category: String? = null

}