package com.trackomunda.hexagonal.ports

import com.trackomunda.hexagonal.core.domain.Gang

interface GangImporter {
    fun importGang(gangUrl: String): Gang
}