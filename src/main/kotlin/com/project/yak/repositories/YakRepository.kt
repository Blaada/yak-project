package com.project.yak.repositories

import com.project.yak.models.LabYak
import org.springframework.data.repository.CrudRepository

interface YakRepository: CrudRepository<LabYak, Long>