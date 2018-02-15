package org.lazywizard.lazylib.ext.campaign

import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.fleet.FleetMemberType
import org.lazywizard.lazylib.campaign.CampaignUtils
import org.lazywizard.lazylib.campaign.CampaignUtils.IncludeRep

inline operator fun CampaignFleetAPI.contains(fleetMemberId: String): Boolean = CampaignUtils.isShipInFleet(fleetMemberId, this)
inline fun CampaignFleetAPI.addShip(wingOrVariantId : String, type: FleetMemberType): FleetMemberAPI = CampaignUtils.addShipToFleet(wingOrVariantId, type, this)

inline fun SectorEntityToken.getRelation(other: SectorEntityToken): Float = CampaignUtils.getRelation(this, other)
inline fun SectorEntityToken.getReputation(other: SectorEntityToken): RepLevel = CampaignUtils.getReputation(this, other)
inline fun SectorEntityToken.areAtRep(other: SectorEntityToken, include: IncludeRep, rep: RepLevel): Boolean = CampaignUtils.areAtRep(this, other, include, rep)
inline fun SectorEntityToken.areSameFaction(other: SectorEntityToken): Boolean = CampaignUtils.areSameFaction(this, other)

inline fun SectorEntityToken.getNearestHostileFleet(): CampaignFleetAPI = CampaignUtils.getNearestHostileFleet(this)
inline fun SectorEntityToken.getNearbyHostileFleets(range: Float): List<CampaignFleetAPI> = CampaignUtils.getNearbyHostileFleets(this, range)
inline fun SectorEntityToken.getHostileFleetsInSystem(): List<CampaignFleetAPI> = CampaignUtils.getHostileFleetsInSystem(this)

inline fun <T: SectorEntityToken> SectorEntityToken.getNearestEntityWithTag(entityTag: String): T = CampaignUtils.getNearestEntityWithTag(this, entityTag)
inline fun <T: SectorEntityToken> SectorEntityToken.getNearbyEntitiesWithTag(range: Float, entityTag: String): List<T> = CampaignUtils. getNearbyEntitiesWithTag(this, range, entityTag)

inline fun <T: SectorEntityToken> SectorEntityToken.getNearestEntityFromFaction(entityTag: String, faction: FactionAPI): T = CampaignUtils.getNearestEntityFromFaction(this, entityTag, faction)
inline fun <T: SectorEntityToken> SectorEntityToken.getNearbyEntitiesFromFaction(range: Float, entityTag: String, faction: FactionAPI): List<T> = CampaignUtils.getNearbyEntitiesFromFaction(this, range, entityTag, faction)
inline fun <T: SectorEntityToken> LocationAPI.getEntitiesFromFaction(entityTag: String, faction: FactionAPI): List<T> = CampaignUtils.getEntitiesFromFaction(this, entityTag, faction)

inline fun <T: SectorEntityToken> SectorEntityToken.getNearestEntityWithRep(entityTag: String, include: IncludeRep, rep: RepLevel): T = CampaignUtils.getNearestEntityWithRep(this, entityTag, include, rep)
inline fun <T: SectorEntityToken> SectorEntityToken.getNearbyEntitiesWithRep(range: Float, entityTag: String, include: IncludeRep, rep: RepLevel): List<T> = CampaignUtils.getNearbyEntitiesWithRep(this, range, entityTag, include, rep)
//getEntitiesWithRep

inline fun SectorEntityToken.getNearbyFleets(range: Float): List<CampaignFleetAPI> = CampaignUtils.getNearbyFleets(this, range)