package com.github.smk7758.OpenCV_TestProjectPoints;

import java.nio.file.Paths;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Size;

public class Main {
	private static final String camparaPathString = "S:\\new_program\\CameraCalibration_TestProjectPoints_2019-06-02.xml";

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		testProjectPoints();
	}

	public static void testProjectPoints() {
		CameraParameter cameraParameter = new CameraParameter(Paths.get(camparaPathString));

		Mat cameraMatrix = cameraParameter.cameraMatrix;
		MatOfDouble distortionCoefficients = cameraParameter.distortionCoefficients_;

		Mat vectorAw = new Mat(3, 1, CvType.CV_64F);
		vectorAw.put(0, 0, new double[] { 0, 0, 4 });

		Mat vectorBw = new Mat(3, 1, CvType.CV_64F);
		vectorBw.put(0, 0, new double[] { 0, 5, 4 });

		System.out.println("vectorAw: " + vectorAw.dump());
		System.out.println("vectorBw: " + vectorBw.dump());

		Point3 pointAw = new Point3(vectorAw.get(0, 0)[0], vectorAw.get(1, 0)[0], vectorAw.get(2, 0)[0]);
		System.out.println("pointAw: " + pointAw.x + ", " + pointAw.y + ", " + pointAw.z);

		Point3 pointBw = new Point3(vectorBw.get(0, 0)[0], vectorBw.get(1, 0)[0], vectorBw.get(2, 0)[0]);
		System.out.println("pointBw: " + pointBw.x + ", " + pointBw.y + ", " + pointBw.z);

		Mat rotationMatrix = Mat.eye(new Size(3, 3), CvType.CV_64FC1);
		rotationMatrix.put(1, 1, new double[] { -1 });

		System.out.println("R: " + rotationMatrix.dump());

		Mat translationVector = new Mat(new Size(3, 1), CvType.CV_64FC1);
		{
			translationVector.put(0, 0, new double[] { 0 });
			translationVector.put(0, 1, new double[] { 10 });
			translationVector.put(0, 2, new double[] { 1 });
		}

		System.out.println("t: " + translationVector.dump());

		MatOfPoint3f pointsSrc = new MatOfPoint3f(pointAw, pointBw);
		// List<Point3> pointsList = new ArrayList<Point3>();
		// pointsList.add(pointAw);
		// pointsList.add(pointBw);
		// pointsSrc.fromList(pointsList);

		// 下は無理
		// pointsSrc.push_back(vectorAw);
		// pointsSrc.push_back(vectorBw);

		MatOfPoint2f pointsDst = new MatOfPoint2f();

		System.out.println("pointsSrc: " + pointsSrc.dump());

		Calib3d.projectPoints(pointsSrc, rotationMatrix, translationVector,
				cameraMatrix, distortionCoefficients, pointsDst);

		System.out.println("pointDst: " + pointsDst.dump());
	}
}
