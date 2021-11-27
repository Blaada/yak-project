package com.project.yak.controllers

import com.project.yak.models.Herd
import com.project.yak.models.LabYak
import com.project.yak.models.Stock
import com.project.yak.repositories.HerdRepository
import com.project.yak.utils.Functions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@Controller
class HerdController(private val herdRepository: HerdRepository) {

    @PostMapping("/load", consumes = ["application/json", "application/xml"])
    fun loadHerd(@RequestBody herd: Herd): ResponseEntity<Herd> {
        herdRepository.deleteAll()
        repeat(herd.labyak.size) {index -> herd.labyak[index].herd = herd}
        herdRepository.save(herd)
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(herd)
    }

    @GetMapping("/herd/{T}", consumes = ["application/json", "application/xml"])
    fun queryHerd(@RequestBody herd: Herd, @PathVariable T: Int): ResponseEntity<Any> {
        val result: HashMap<String, List<LabYak>> = HashMap()
        val validatedHerd: Herd = Functions.calculateStock(Functions.validateHerd(herd, T), T).second
        result["herd"] = Functions.calculateAge(validatedHerd, T).labyak
        return ResponseEntity.ok(result)
    }
}