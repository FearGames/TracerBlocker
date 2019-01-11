package cz.GravelCZLP.TracerBlocker;

public class MathUtils {

	/**
	 * returns the yaw between the 2 vectors
	 * @param start - start vector
	 * @param end - end vector
	 * @return - the angle between the vectors
	 */
	public static float angleYaw(Vector3D start, Vector3D end) {
		double dx = start.getX() - end.getX();
		double dz = start.getZ() - end.getZ();
		
		double yaw = 0;
		if (dx != 0) {
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx);
		} else {
			yaw = Math.PI;
		}
		return (float) Math.toDegrees(-yaw);
	}
	
	/**
	 * Returns the pitch (up/down) angle
	 * @param start - start vector
	 * @param end - end vector
	 * @return - the angle between the vectors
	 */
	public static float anglePitch(Vector3D start, Vector3D end) {
		double dx = start.getX() - end.getX();
		double dy = start.getY() - end.getY();
		double dz = start.getZ() - end.getZ();

		double dxz = Math.sqrt(dx * dx + dz * dz);
		
		double pitch = Math.atan(dy / dxz);
		return (float) Math.toDegrees(-pitch);
	}

}
