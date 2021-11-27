package com.project.yak.models

data class Stock(val milk: Double, val skins: Int){
    override fun toString(): String =
        """
            In Stock :
                ${this.milk}  liters of milk
                ${this.skins}  skins of wool
        """.trimIndent()
}