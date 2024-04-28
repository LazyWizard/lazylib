package org.lazywizard.lazylib.ext

import com.fs.starfarer.api.combat.ShipAPI
import org.lwjgl.util.vector.Vector2f


/**
 * Returns the [Vector2f] of where the ship is looking (facing) at
 * @return the ship's forward vector, similar to [com.fs.starfarer.api.util.Misc.getUnitVectorAtDegreeAngle] used with the ship's [ShipAPI.getFacing]
 */
fun ShipAPI.getForwardVector(): Vector2f {
    val rotationRadians = Math.toRadians(this.facing.toDouble())

    // Calculate the components of the forward vector
    val x = cos(rotationRadians).toFloat()
    val y = sin(rotationRadians).toFloat()

    // Return the forward vector
    return Vector2f(x, y)
}

/**
 * Returns the angle (in degrees) between this ship's Forward Vector and *anotherShip*
 * @return the difference in degrees
 * @see [ShipAPI.getForwardVector]
 */
fun ShipAPI.getAngleToAnotherShip(anotherShip: ShipAPI): Float {
    val targetDirectionAngle = VectorUtils.getAngle(this.location, anotherShip.location)
    val myForwardVector = this.getForwardVector()
    val myAngle = VectorUtils.getAngle(myForwardVector, anotherShip.location)
    val differenceInDegrees = (myAngle - targetDirectionAngle)

    return differenceInDegrees
}

/**
 * Returns the absolue angle (in degrees) between this ship and *anotherShip*
 * @return the difference in degrees, as absolute value
 * @see [ShipAPI.getAngleToAnotherShip]
 */
fun ShipAPI.getAbsoluteAngleToAnotherShip(anotherShip: ShipAPI): Float {
    return this.getAngleToAnotherShip(anotherShip).absoluteValue
}