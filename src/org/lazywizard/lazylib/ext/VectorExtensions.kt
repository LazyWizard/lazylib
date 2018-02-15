package org.lazywizard.lazylib.ext

import com.fs.starfarer.api.combat.BoundsAPI
import com.fs.starfarer.api.combat.CombatEntityAPI
import org.lazywizard.lazylib.CollisionUtils
import org.lazywizard.lazylib.MathUtils
import org.lazywizard.lazylib.VectorUtils
import org.lwjgl.util.vector.Vector2f

inline operator fun Vector2f.plus(other: Vector2f): Vector2f = Vector2f.add(this, other, Vector2f())
inline operator fun Vector2f.plusAssign(other: Vector2f) { Vector2f.add(this, other, this) }
inline operator fun Vector2f.minus(other: Vector2f): Vector2f = Vector2f.sub(this, other, Vector2f())
inline operator fun Vector2f.minusAssign(other: Vector2f) { Vector2f.sub(this, other, this) }
inline operator fun BoundsAPI.SegmentAPI.contains(point: Vector2f) : Boolean = MathUtils.isPointOnLine(point, this.p1, this.p2)
inline operator fun CombatEntityAPI.contains(point: Vector2f) : Boolean = CollisionUtils.isPointWithinBounds(point, this)

inline fun Vector2f.getFacing(): Float = VectorUtils.getFacing(this)
inline fun Vector2f.getAngle(to: Vector2f): Float = VectorUtils.getAngle(this, to)
inline fun Vector2f.getDirectionalVector(destination: Vector2f): Vector2f = VectorUtils.getDirectionalVector(this, destination)
inline fun Vector2f.getCrossProduct(other: Vector2f): Float = VectorUtils.getCrossProduct(this, other)
inline fun Vector2f.rotate(amount: Float): Vector2f = VectorUtils.rotate(this, amount, Vector2f())
inline fun Vector2f.rotateAroundPivot(pivotPoint: Vector2f, amount: Float): Vector2f = VectorUtils.rotateAroundPivot(this, pivotPoint, amount, Vector2f())