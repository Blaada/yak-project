package com.project.yak.controllers

import com.project.yak.models.Herd
import com.project.yak.models.Stock
import com.project.yak.repositories.HerdRepository
import com.project.yak.utils.Functions
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Controller
class StockController(private val herdRepository: HerdRepository) {

    @GetMapping("/stock/{T}")
    fun getStock(@PathVariable T: Int): ResponseEntity<Stock> {
        val herd: Herd = herdRepository.findAll().iterator().next()
        val stock: Stock = Functions.calculateStock(herd, T).first
        return ResponseEntity.ok(stock)
    }
}