package cz.GravelCZLP.TracerBlocker;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Vector3D {

	private double x, y, z;
	
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D add(Vector3D vec) {
		x += vec.getX();
		y += vec.getY();
		z += vec.getZ();
		return this;
	}

	public Vector3D subtract(Vector3D vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	public Vector3D multiply(Vector3D vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		return this;
	}

	public Vector3D divide(Vector3D vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		return this;
	}

	public Vector3D multiply(double m) {
		x *= m;
		y *= m;
		z *= m;
		return this;
	}

	public double distance(Vector3D o) {
        return (x - o.x) * (x - o.x) + (y - o.y) * (y - o.y) + (z - o.z) * (z - o.z);
    }
	
    public double distanceSqrt(Vector3D o) {
        return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y) + (z - o.z) * (z - o.z));
    }
	
	public Vector3D midpoint(Vector3D vec) {
		x = (x + vec.getX()) / 2;
		y = (y + vec.getY()) / 2;
		z = (z + vec.getZ()) / 2;
		return this;
	}

	public Vector3D clone() {
		return new Vector3D(x, y, z);
	}
	
	public Location toLocation(World w) {
		return new Location(w, x, y, z);
	}

	public Vector toBukkitVector() {
		return new Vector(x, y, z);
	}
	
	public static Vector3D fromLocation(Location loc) {
		return new Vector3D(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return "Vector3D[x: " + x + ", y: " + y + " z: " + z + "]";
	}
	
}
