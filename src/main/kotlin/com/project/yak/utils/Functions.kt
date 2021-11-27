package com.project.yak.utils

import com.project.yak.models.Herd
import com.project.yak.models.LabYak
import com.project.yak.models.Stock
import kotlin.math.round


class Functions {
    companion object {
         fun calculateStock(herd: Herd, T: Int): Pair<Stock, Herd> {
            var liters = 0.0
            var skins = 0
            val calculatedHerd: Herd = Herd()
            for(yak: LabYak in herd.labyak) {
                val ageInDays: Int = convertAgetoDays(yak.age)
                val skinsAndLastShave = shavesToDay(ageInDays, T)
                skins += skinsAndLastShave.first
                yak.ageLastShaved = skinsAndLastShave.second
                repeat(T) {i -> liters += getMilk(ageInDays, i)}
                calculatedHerd.labyak.add(yak)
            }
            return Pair(Stock(milk = liters.roundNumber(3), skins = skins), calculatedHerd)
        }

        fun calculateAge(herd: Herd, T: Int): Herd {
            repeat(herd.labyak.size) {index -> herd.labyak[index].age += (T * 0.01)}
            return herd
        }

        fun validateHerd(herd: Herd, T: Int): Herd {
            val validatedHerd: Herd = Herd()
            for(yak: LabYak in herd.labyak) {
                if(convertAgetoDays(yak.age) + T < 1000) {
                    validatedHerd.labyak.add(yak)
                }
            }
            return validatedHerd
        }

        fun shavesToDay(age: Int, T: Int): Pair<Int, Double> {
            val ageToDay: Int = age + T
            var skins: Int = 0
            var intervalOfShaving: Double = 0.0
            var ageOfLastShave: Double = age.toDouble()

            if(ageToDay >= 100) {
                skins++
            }
            while(ageToDay > (ageOfLastShave + intervalOfShaving)
                && (ageOfLastShave + intervalOfShaving) < 1000){
                intervalOfShaving = calculateIntervalOfShaving(ageOfLastShave)
                if((ageOfLastShave + intervalOfShaving) < ageToDay) {
                    skins++
                    ageOfLastShave += intervalOfShaving
                }
            }
            return Pair(skins, (ageOfLastShave * 0.01).roundNumber(2))
        }

        private fun convertAgetoDays(age: Double): Int {
            return (age * 100).toInt()
        }

        private fun getMilk(age: Int, day: Int): Double {
            val currentDay: Int = age + day
            return if (currentDay < 1000)  50 - (age + day ) * 0.03 else 0.0
        }

        private fun calculateIntervalOfShaving(age: Double): Double {
            return ( 8 + (age) * 0.01 ) + 1
        }

        private fun Double.roundNumber(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }
    }
}