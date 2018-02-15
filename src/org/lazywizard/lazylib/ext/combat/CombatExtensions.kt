package org.lazywizard.lazylib.ext.combat

import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.fleet.FleetMemberAPI
import org.lazywizard.lazylib.combat.AIUtils
import org.lazywizard.lazylib.combat.CombatUtils
import org.lwjgl.util.vector.Vector2f

inline fun CombatEntityAPI.getNearestObjective(): BattleObjectiveAPI? = AIUtils.getNearestObjective(this)
inline fun CombatEntityAPI.getNearbyObjectives(range: Float): List<BattleObjectiveAPI> = CombatUtils.getObjectivesWithinRange(this.location, range)

inline fun CombatEntityAPI.getNearestShip(): ShipAPI? = AIUtils.getNearestShip(this)
inline fun CombatEntityAPI.getNearbyShips(range: Float): List<ShipAPI> = CombatUtils.getShipsWithinRange(this.location, range)

inline fun CombatEntityAPI.getNearestMissile(): MissileAPI? = AIUtils.getNearestMissile(this)
inline fun CombatEntityAPI.getNearbyMissiles(range:Float): List<MissileAPI> = CombatUtils.getMissilesWithinRange(this.location, range)

inline fun CombatEntityAPI.getNearestAlly(): ShipAPI? = AIUtils.getNearestAlly(this)
inline fun CombatEntityAPI.getNearbyAllies(range: Float): List<ShipAPI> = AIUtils.getNearbyAllies(this, range)
inline fun CombatEntityAPI.getAlliesOnMap(): List<ShipAPI> = AIUtils.getAlliesOnMap(this)

inline fun CombatEntityAPI.getNearestEnemyMissile(): MissileAPI? = AIUtils.getNearestEnemyMissile(this)
inline fun CombatEntityAPI.getNearbyEnemyMissiles(range: Float): List<MissileAPI> = AIUtils.getNearbyEnemyMissiles(this, range)
inline fun CombatEntityAPI.getEnemyMissilesOnMap(): List<MissileAPI> = AIUtils.getEnemyMissilesOnMap(this)

inline fun CombatEntityAPI.getNearestEnemy(): ShipAPI? = AIUtils.getNearestEnemy(this)
inline fun CombatEntityAPI.getNearbyEnemies(range: Float): List<ShipAPI> = AIUtils.getNearbyEnemies(this, range)
inline fun CombatEntityAPI.getEnemiesOnMap(): List<ShipAPI> = AIUtils.getEnemiesOnMap(this)

inline fun ShipAPI.canUseSystemThisFrame(): Boolean = AIUtils.canUseSystemThisFrame(this)
inline fun ShipAPI.getFleetMember(): FleetMemberAPI? = CombatUtils.getFleetMember(this)
inline fun ShipAPI.isVisibleToSide(side: Int): Boolean = CombatUtils.isVisibleToSide(this, side)

inline fun CombatEntityAPI.applyForce(direction: Vector2f, force: Float) = CombatUtils.applyForce(this, direction, force)
inline fun CombatEntityAPI.applyForce(direction: Float, force: Float) = CombatUtils.applyForce(this, direction, force)