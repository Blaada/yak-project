package com.project.yak.controllers

import com.project.yak.models.Herd
import com.project.yak.models.LabYak
import com.project.yak.models.Order
import com.project.yak.models.Stock
import com.project.yak.repositories.HerdRepository
import com.project.yak.utils.Functions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@Controller
class OrderController(private val herdRepository: HerdRepository) {
    @PostMapping("/order/{T}", consumes = ["application/json", "application/xml"])
    fun queryOrder(@PathVariable T: Int, @RequestBody order: Order): ResponseEntity<Any> {
        val herd: Herd = herdRepository.findAll().iterator().next()
        val stock: Stock = Functions.calculateStock(herd, T).first
        val result: HashMap<String, Any> = HashMap()
        if(order.order.milk <= stock.milk) {result["milk"] = order.order.milk}
        if(order.order.skins <= stock.skins) {result["skins"] = order.order.skins}
        val stockMilk = result.containsKey("milk")
        val stockSkins = result.containsKey("skins")
        return if(stockMilk && stockSkins) {
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } else if (stockMilk || stockSkins) {
            ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry ${order.customer}, we could not fulfil your order")
        }

    }
}