package org.lazywizard.lazylib.ext

import org.lazywizard.lazylib.VectorUtils
import org.lwjgl.util.vector.Vector2f

operator fun Vector2f.plus(other: Vector2f): Vector2f = Vector2f.add(this, other, Vector2f())
operator fun Vector2f.plusAssign(other: Vector2f) { Vector2f.add(this, other, this) }
operator fun Vector2f.minus(other: Vector2f): Vector2f = Vector2f.sub(this, other, Vector2f())
operator fun Vector2f.minusAssign(other: Vector2f) { Vector2f.sub(this, other, this) }

fun Vector2f.isZeroVector(): Boolean = VectorUtils.isZeroVector(this)
fun Vector2f.getFacing(): Float = VectorUtils.getFacing(this)
fun Vector2f.getAngle(to: Vector2f): Float = VectorUtils.getAngle(this, to)
fun Vector2f.getDirectionalVector(destination: Vector2f): Vector2f = VectorUtils.getDirectionalVector(this, destination)
fun Vector2f.getCrossProduct(other: Vector2f): Float = VectorUtils.getCrossProduct(this, other)
fun Vector2f.resize(length: Float): Vector2f = VectorUtils.resize(this, length, this)
fun Vector2f.clampLength(maxLength: Float): Vector2f = VectorUtils.clampLength(this, maxLength, this)
fun Vector2f.clampLength(minLength: Float, maxLength: Float): Vector2f = VectorUtils.clampLength(this, minLength, maxLength, this)
fun Vector2f.rotate(amount: Float): Vector2f = VectorUtils.rotate(this, amount, this)
fun Vector2f.rotateAroundPivot(pivotPoint: Vector2f, amount: Float): Vector2f = VectorUtils.rotateAroundPivot(this, pivotPoint, amount, this)
