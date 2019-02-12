package cz.GravelCZLP.TracerBlocker;

import org.bukkit.Location;
import org.bukkit.Particle;

public class MathUtils {

	public static void renderParticleAtAngle3D(Location loc, double radius, double yaw, double pitch) {
		pitch *= -1;
		yaw *= -1;// Math.abs(yaw);
		double x = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double y = Math.sin(Math.toRadians(pitch));
		double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

		x *= radius;
		y *= radius;
		z *= radius;

		loc.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(x, y, z), 0, 1, 1, 0);
	}

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
	
	public static void renderAxisHelper(Location loc, double radius) {
		for (int i = 0; i < 360; i++) {
			double xoff = 0;
			float r = 1, g = 0, b = 0;
			if (i == 0) { // purple
				r = 1;
				g = 0;
				b = 1;
				xoff = 0.2;
			} else if (i == 90) { // green
				r = Float.MIN_VALUE;
				g = 1;
				b = 0;
				xoff = 0.2;
			} else if (i == 180) { // blue
				r = Float.MIN_VALUE;
				g = 0;
				b = 1;
				xoff = 0.2;
			} else if (i == 270) { // yellow
				r = 1;
				g = 1;
				b = 0;
				xoff = 0.2;
			}

			double angle = Math.toRadians(i);
			double y = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;

			Location particleLoc = loc.clone().add(xoff, y, z);
			if (i % 10 == 0) {
				loc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 0, r, g, b);
			}
		}
		for (int i = 0; i < 360; i++) {
			double zoff = 0;
			float r = 1, g = 0, b = 0;
			if (i == 0) { // purple
				r = 1;
				g = 0;
				b = 1;
				zoff = 0.2;
			} else if (i == 90) { // green
				r = Float.MIN_VALUE;
				g = 1;
				b = 0;
				zoff = 0.2;
			} else if (i == 180) { // blue
				r = Float.MIN_VALUE;
				g = 0;
				b = 1;
				zoff = 0.2;
			} else if (i == 270) { // yellow
				r = 1;
				g = 1;
				b = 0;
				zoff = 0.2;
			}

			double angle = Math.toRadians(i);
			double y = Math.sin(angle) * radius;
			double x = Math.cos(angle) * radius;

			Location particleLoc = loc.clone().add(x, y, zoff);
			if (i % 10 == 0) {
				loc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 0, r, g, b);
			}
		}
		for (int i = 0; i < 360; i++) {
			double yoff = 0;
			float r = 1, g = 0, b = 0;
			if (i == 0) { // purple
				r = 1;
				g = 0;
				b = 1;
				yoff = 0.2;
			} else if (i == 90) { // green
				r = Float.MIN_VALUE;
				g = 1;
				b = 0;
				yoff = 0.2;
			} else if (i == 180) { // blue
				r = Float.MIN_VALUE;
				g = 0;
				b = 1;
				yoff = 0.2;
			} else if (i == 270) { // yellow
				r = 1;
				g = 1;
				b = 0;
				yoff = 0.2;
			}

			double angle = Math.toRadians(i);
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;

			Location particleLoc = loc.clone().add(x, yoff, z);
			if (i % 10 == 0) {
				loc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 0, r, g, b);
			}
		}
	}

	public static float[] getAngles(Vector3D start, Vector3D end) {
		double dx = start.getX() - end.getX();
		double dy = start.getY() - end.getY();
		double dz = start.getZ() - end.getZ();
		
		double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
		
		float yaw = (float) (Math.atan2(dx, dz) * 180.0 / Math.PI);
		float pitch = (float) (Math.atan2(dist, dy) * 180.0 / Math.PI);
		return new float[] { yaw, pitch};
	}

}
