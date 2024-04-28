package global;
import com.simsilica.mathd.Vec3d;
import p01start.Quad;
import p01start.Renderer;
import transforms.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static global.GluUtils.gluLookAt;

public class GLCamera {

	private double azimuth, radius, zenith;

	private boolean first_person, valid; // true -> 1st person, 0 -> 3rd person

	private Vec3D eye, eye_vector, up, pos, centre;

	public enum CollisionDirection {
		NONE,
		LEFT,
		RIGHT,
		FORWARD,
		BACK
	}

	private void compute_matrix() {
		eye_vector = new Vec3D(
				Math.sin(azimuth) * Math.cos(zenith),
				Math.sin(zenith),
				-(Math.cos(azimuth) * Math.cos(zenith))
		);
		up = new Vec3D(
				Math.sin(azimuth) * Math.cos(zenith + Math.PI / 2),
				Math.sin(zenith + Math.PI / 2),
				-(Math.cos(azimuth) * Math.cos(zenith + Math.PI / 2))
		);
		if (first_person) {
			eye = new Vec3D(pos);
			centre = eye.add(eye_vector.mul(radius));
		} else {
			eye = pos.add(eye_vector.mul(-1 * radius));
			centre = new Vec3D(pos);
		}
		valid = true;
	}

	public GLCamera() {
		azimuth = zenith = 0.0f;
		radius = 0.5f;
		pos = new Vec3D(0.0f, 0.0f, 0.0f);
		first_person = true;
		valid = false;
	}

	public GLCamera(GLCamera camera) {
		azimuth = camera.getAzimuth();
		zenith = camera.getZenith();
		radius = camera.getRadius();
		pos = new Vec3D(camera.getPosition());
		first_person = camera.getFirstPerson();
		valid = false;
	}

	public void addAzimuth(double ang) {
		azimuth += ang;
		valid = false;
	}

	public void addRadius(double dist) {
		if (radius + dist < 0.1f)
			return;
		radius += dist;
		valid = false;
	}

	public void mulRadius(double scale) {
		if (radius * scale < 0.1f)
			return;
		radius *= scale;
		valid = false;
	}

	public void addZenith(double ang) {
		if (Math.abs(zenith + ang) <= Math.PI / 2) {
			zenith += ang;
			valid = false;
		}
	}

	public void setAzimuth(double ang) {
		azimuth = ang;
		valid = false;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setRadius(double dist) {
		radius = dist;
		valid = false;
	}

	public void setZenith(double ang) {
		zenith = ang;
		valid = false;
	}

	public double getZenith() {
		return zenith;
	}

	public double getRadius() {
		return radius;
	}

	public void backward(double speed) {
		forward((-1) * speed);
	}

	public void forward(double speed) {
		pos = pos.add(new Vec3D(
				Math.sin(azimuth) * Math.cos(zenith),
				Math.sin(zenith),
				-Math.cos(azimuth) * Math.cos(zenith))
				.mul(speed));
		valid = false;
	}

	public Vec3D calculateForward(double speed) {
		return pos.add(new Vec3D(
				Math.sin(azimuth) * Math.cos(zenith),
				Math.sin(zenith),
				-Math.cos(azimuth) * Math.cos(zenith))
				.mul(speed));
	}

	public Vec3D calculateBackward(double speed) {
		return calculateForward((-1) * speed);
	}

	public Vec3D calculateRight(double speed) {
		return pos.add(new Vec3D(
				-Math.sin(azimuth - Math.PI / 2),
				0.0f,
				+Math.cos(azimuth - Math.PI / 2))
				.mul(speed));
	}

	public Vec3D calculateLeft(double speed) {
		return new Vec3D(calculateRight(-(1) * speed));
	}

	public void left(double speed) {
		right((-1) * speed);
	}

	public void right(double speed) {
		pos = pos.add(new Vec3D(
				-Math.sin(azimuth - Math.PI / 2),
				0.0f,
				+Math.cos(azimuth - Math.PI / 2))
				.mul(speed));
		valid = false;
	}

	public void down(double speed) {
		pos = pos.withY(pos.getY() - speed);
		valid = false;
	}

	public void up(double speed) {
		pos = pos.withY(pos.getY() + speed);
		valid = false;
	}

	public void move(Vec3D dir) {
		pos = pos.add(dir);
		valid = false;
	}

	public void setPosition(Vec3D apos) {
		pos = new Vec3D(apos);
		valid = false;
	}

	public boolean getFirstPerson() {
		return first_person;
	}

	public void setFirstPerson(boolean fp) {
		first_person = fp;
		valid = false;
	}

	public Vec3D getEye() {
		if (!valid)
			compute_matrix();
		return eye;
	}

	public Vec3D getEyeVector() {
		if (!valid)
			compute_matrix();
		return eye_vector;
	}

	public Vec3D getPosition() {
		if (!valid)
			compute_matrix();
		return pos;
	}

	public void setMatrix() {
		if (!valid)
			compute_matrix();
		gluLookAt(
				eye.getX(), eye.getY(), eye.getZ(),
				centre.getX(), centre.getY(), centre.getZ(),
				up.getX(), up.getY(), up.getZ()
		);
	}

	public String toString(final String format) {
		return String.format(Locale.US,
				"Camera()\n" +
						"	.withFirstPerson(" + getFirstPerson() + ")\n" +
						"	.withPosition(new Vec3D" + getPosition().toString(format) + ")\n" +
						"	.withAzimuth(" + format + ")\n" +
						"	.withZenith(" + format + ")\n" +
						"	.withRadius(" + format + ")",
				getAzimuth(), getZenith(), getRadius()
		);
	}

	public boolean collidesWithQuad(List<Quad> vertices, GLCamera camera, CollisionDirection direction) {
		Vec3D newPos = new Vec3D();
		switch (direction){
			case FORWARD:
				newPos = calculateForward(0.25f);
				break;
			case BACK:
				newPos = calculateBackward(0.25f);
				break;
			case LEFT:
				newPos = calculateLeft(0.25f);
				break;
			case RIGHT:
				newPos = calculateRight(0.25f);

		}
		float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
		boolean isColliding = false;
		for (Quad q : vertices) {
			for (Vec3D vertex : q.vertexes) {
				System.out.println(vertex.toString());
				Mat3 rot = calculateTheRotationMatrix(q.rotation, q.angleOfRotation);
				Vec3D rotatedVertex = new Vec3D(vertex.mul(rot));
				Vec3D translatedVertex = new Vec3D(rotatedVertex.add(q.translation));
				minX = (float) Math.min(minX, translatedVertex.getX());
				minY = (float) Math.min(minY, translatedVertex.getY());
				minZ = (float) Math.min(minZ, translatedVertex.getZ());
				maxX = (float) Math.max(maxX, translatedVertex.getX());
				maxY = (float) Math.max(maxY, translatedVertex.getY());
				maxZ = (float) Math.max(maxZ, translatedVertex.getZ());
			}
			isColliding = (Math.abs(newPos.getX() - (minX + maxX) / 2) *1.001 < (camera.getRadius() + (maxX - minX) / 2)) &&
					(Math.abs(newPos.getY() - (minY + maxY) / 2) *1.001 < (camera.getRadius() + (maxY - minY) / 2)) &&
					(Math.abs(newPos.getZ() - (minZ + maxZ) / 2) *1.001 < (camera.getRadius() + (maxZ - minZ) / 2));

		}
		if (isColliding)
			return true;
		return isColliding;
	}

	public boolean checkForAmmoBoxCollision(List<Quad> ammoBoxes){
		for(Quad q : ammoBoxes) {
			for (Vec3D ammoBox : q.vertexes) {
				Mat3 rot = calculateTheRotationMatrix(q.rotation, q.angleOfRotation);
				Vec3D rotatedVertex = new Vec3D(ammoBox.mul(rot));
				Vec3D translatedVertex = new Vec3D(rotatedVertex.add(q.translation));
				double distanceX = translatedVertex.getX() - pos.getX();
				double distanceY = translatedVertex.getY() - pos.getY();
				double distanceZ = translatedVertex.getZ() - pos.getZ();

				double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
				if (distance <= 2.0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private Mat3 calculateTheRotationMatrix(Vec3D rot, int angle) {
		double angleRadians = Math.toRadians(angle);
		double x = rot.getX();
		double y = rot.getY();
		double z = rot.getZ();

		if (x >= y && x >= z) {
			return new Mat3(new Mat3RotX(angleRadians));
		} else if (y >= x && y >= z) {
			return new Mat3(new Mat3RotY(angleRadians));
		} else if (z >= x && z >= y) {
			return new Mat3(new Mat3RotZ(angleRadians));
		}
		return new Mat3Identity();
	}
	public String toString() {
		return toString("%4.2f");
	}
}