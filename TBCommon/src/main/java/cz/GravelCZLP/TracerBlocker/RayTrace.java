package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.List;

public class RayTrace {

	private Vector3D start, end, midpoint;
	
	private double dx, dy, dz;
	
	public RayTrace(Vector3D startLoc, Vector3D endLoc) {
		this.start = startLoc;
		this.end = endLoc;
		
		calculate();
	}
	
	public RayTrace(Vector3D startLoc, double yaw, double pitch, double lenght) {
		this.start = startLoc;
		
		yaw *= -1;
		pitch *= -1;
		
		double yOff = Math.sin(Math.toRadians(pitch));
		double xOff = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double zOff = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		
		double y = yOff * lenght + startLoc.getY();
		double x = xOff * lenght + startLoc.getX();
		double z = zOff * lenght + startLoc.getZ();
		
		this.end = new Vector3D(x, y, z);
		
		calculate();
	}
	
	private void calculate() {
		midpoint = calculateMidpoint(start, end);
		
		dx = (end.getX() - start.getX());
		dy = (end.getY() - start.getY());
		dz = (end.getZ() - start.getZ());
	}
	
	private Vector3D calculateMidpoint(Vector3D vec1, Vector3D vec2) {
		double x = (vec1.getX() + vec2.getX()) / 2.0D;
		double y = (vec1.getY() + vec2.getY()) / 2.0D;
		double z = (vec1.getZ() + vec2.getZ()) / 2.0D;
		
		return new Vector3D(x, y, z);
	}
	
	public List<Vector3D> raytrace(double interval) {
		int amount = (int) (start.distance(end) / interval);
		
		return raytrace(amount);
	}
	
	public List<Vector3D> raytrace(int acc) {
		List<Vector3D> points = new ArrayList<>();
		
		double xOff = getDx() / acc;
		double yOff = getDy() / acc;
		double zOff = getDz() / acc;
		
		for (int i = 0; i <= acc; i++) {
			double xoffset = xOff * i;
			double yoffset = yOff * i;
			double zoffset = zOff * i;
			
			points.add(new Vector3D(start.getX() + xoffset, start.getY() + yoffset, start.getZ() + zoffset));
		}
		
		return points;
	}
	
	public Vector3D getStart() {
		return start;
	}

	public Vector3D getEnd() {
		return end;
	}

	public Vector3D getMidpoint() {
		return midpoint;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public double getDz() {
		return dz;
	}
}
