package com.project.yak.repositories

import com.project.yak.models.Herd
import org.springframework.data.repository.CrudRepository

interface HerdRepository: CrudRepository<Herd, Long>