package com.project.yak.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import javax.persistence.*

@Entity
@Table(name = "herd")
data class Herd(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var herdId: Long = 1L,
    @JacksonXmlElementWrapper(useWrapping = false)
    @OneToMany(mappedBy="herd", cascade= [CascadeType.ALL])
    var labyak: MutableList<LabYak> = mutableListOf()
){
    override fun toString(): String {
        var output = "Herd:\n"
        for(yak: LabYak in labyak) {
            output += "\t${yak.name} ${yak.age} years old\n"
        }
        return output
    }
}