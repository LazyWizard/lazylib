package org.lazywizard.lazylib.ext.campaign

import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.fleet.FleetMemberType
import org.lazywizard.lazylib.MathUtils
import org.lazywizard.lazylib.campaign.CampaignUtils
import org.lazywizard.lazylib.campaign.CampaignUtils.IncludeRep
import org.lwjgl.util.vector.Vector2f

operator fun SectorEntityToken.contains(point: Vector2f): Boolean = MathUtils.isPointWithinCircle(point, this.location, this.radius)
operator fun CampaignFleetAPI.contains(fleetMemberId: String): Boolean = CampaignUtils.isShipInFleet(fleetMemberId, this)

fun CampaignFleetAPI.addShip(wingOrVariantId: String, type: FleetMemberType): FleetMemberAPI = CampaignUtils.addShipToFleet(wingOrVariantId, type, this)

fun SectorEntityToken.getRelation(other: SectorEntityToken): Float = CampaignUtils.getRelation(this, other)
fun SectorEntityToken.getReputation(other: SectorEntityToken): RepLevel = CampaignUtils.getReputation(this, other)
fun SectorEntityToken.areAtRep(other: SectorEntityToken, include: IncludeRep, rep: RepLevel): Boolean = CampaignUtils.areAtRep(this, other, include, rep)
fun SectorEntityToken.areSameFaction(other: SectorEntityToken): Boolean = CampaignUtils.areSameFaction(this, other)

fun SectorEntityToken.getNearestHostileFleet(): CampaignFleetAPI? = CampaignUtils.getNearestHostileFleet(this)
fun SectorEntityToken.getNearbyHostileFleets(range: Float): List<CampaignFleetAPI> = CampaignUtils.getNearbyHostileFleets(this, range)
fun SectorEntityToken.getHostileFleetsInSystem(): List<CampaignFleetAPI> = CampaignUtils.getHostileFleetsInSystem(this)

fun <T : SectorEntityToken> SectorEntityToken.getNearestEntityWithTag(entityTag: String): T? = CampaignUtils.getNearestEntityWithTag(this, entityTag)
fun <T : SectorEntityToken> SectorEntityToken.getNearbyEntitiesWithTag(range: Float, entityTag: String): List<T> = CampaignUtils.getNearbyEntitiesWithTag(this, range, entityTag)

fun <T : SectorEntityToken> SectorEntityToken.getNearestEntityFromFaction(entityTag: String, faction: FactionAPI): T? = CampaignUtils.getNearestEntityFromFaction(this, entityTag, faction)
fun <T : SectorEntityToken> SectorEntityToken.getNearbyEntitiesFromFaction(range: Float, entityTag: String, faction: FactionAPI): List<T> = CampaignUtils.getNearbyEntitiesFromFaction(this, range, entityTag, faction)
fun <T : SectorEntityToken> LocationAPI.getEntitiesFromFaction(entityTag: String, faction: FactionAPI): List<T> = CampaignUtils.getEntitiesFromFaction(this, entityTag, faction)

fun <T : SectorEntityToken> SectorEntityToken.getNearestEntityWithRep(entityTag: String, include: IncludeRep, rep: RepLevel): T? = CampaignUtils.getNearestEntityWithRep(this, entityTag, include, rep)
fun <T : SectorEntityToken> SectorEntityToken.getNearbyEntitiesWithRep(range: Float, entityTag: String, include: IncludeRep, rep: RepLevel): List<T> = CampaignUtils.getNearbyEntitiesWithRep(this, range, entityTag, include, rep)
fun <T : SectorEntityToken> SectorEntityToken.getEntitiesWithRep(entityTag: String, include: IncludeRep, rep: RepLevel): List<T> = CampaignUtils.getEntitiesWithRep(this, entityTag, include, rep)

fun SectorEntityToken.getNearbyFleets(range: Float): List<CampaignFleetAPI> = CampaignUtils.getNearbyFleets(this, range)
