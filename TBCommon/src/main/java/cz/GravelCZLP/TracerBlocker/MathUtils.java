package cz.GravelCZLP.TracerBlocker;

public class MathUtils {

	public static Vector3D toUnitVector(Vector3D start, double radius, double yaw, double pitch) {
		pitch *= -1;
		yaw *= -1;// Math.abs(yaw);
		double x = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double y = Math.sin(Math.toRadians(pitch));
		double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

		x *= radius;
		y *= radius;
		z *= radius;
		
		return start.clone().add(new Vector3D(x, y, z));
	}
	
	public static float[] getAngles(Vector3D start, Vector3D end) {
		double dx = end.getX() - start.getX();
		double dz = end.getZ() - start.getZ();
		double dy = start.getY() - end.getY();
		
		double hypot = Math.hypot(dz, dx);
		
		double angxz = Math.toDegrees(Math.atan2(dz, dx));
		
		double angy = Math.toDegrees(Math.atan2(dy, hypot));
		
		return new float[] { (float) angxz - 90, (float) angy};
	}
	
}
