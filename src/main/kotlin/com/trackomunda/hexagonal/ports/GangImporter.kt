package com.trackomunda.hexagonal.ports

import com.trackomunda.hexagonal.core.Gang

interface GangImporter {
    fun importGang(gangUrl: String): Gang
}