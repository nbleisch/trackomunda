package com.trackomunda.hexagonal.core.domain

interface GangerAttributes{
    val name: String
    val movement: String
    val weaponSkill: String
    val ballisticSkill: String
    val strength: String
    val toughness: Int
    val wounds: Int
    val initiative: String
    val attacks: String
    val leadership: Int
    val coolness: Int
    val willpower: Int
    val intelligence: Int
}


data class Ganger(
    override val name: String,
    override val movement: String,
    override val weaponSkill: String,
    override val ballisticSkill: String,
    override val strength: String,
    override val toughness: Int,
    override val wounds: Int,
    override val initiative: String,
    override val attacks: String,
    override val leadership: Int,
    override val coolness: Int,
    override val willpower: Int,
    override val intelligence: Int,
) : GangerAttributes{

}