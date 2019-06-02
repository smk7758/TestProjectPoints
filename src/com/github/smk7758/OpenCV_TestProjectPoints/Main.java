package com.github.smk7758.OpenCV_TestProjectPoints;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
	private static final String camparaPathString = "S:\\FingerPencil\\CalclatePoint_Test_2019-05-18\\CameraCalibration_Test_2019-05-18.xml";

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		CameraParameter cameraParameter = new CameraParameter(Paths.get(camparaPathString));

		Mat cameraMatrix = cameraParameter.cameraMatrix;
		MatOfDouble distortionCoefficients = cameraParameter.distortionCoefficients_;

		Mat vectorA = new Mat(3, 1, CvType.CV_64F);
		vectorA.put(0, 0, new double[] { 0, 10, 5 });

		Mat vectorB = new Mat(3, 1, CvType.CV_64F);
		vectorB.put(0, 0, new double[] { 0, 5, 5 });

		System.out.println("vectorA: " + vectorA.dump());

		Point3 pointA = new Point3(vectorA.get(0, 0)[0], vectorA.get(1, 0)[0], vectorA.get(2, 0)[0]);
		System.out.println("pointA: " + pointA.x + ", " + pointA.y + ", " + pointA.z);

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

		MatOfPoint3f pointsSrc = new MatOfPoint3f();
		List<Point3> pointsList = new ArrayList<Point3>();
		pointsList.add(pointA);
		pointsSrc.fromList(pointsList);
		// pointsSrc.push_back(vectorA);
		// pointsSrc.push_back(vectorB);

		MatOfPoint2f pointsDst = new MatOfPoint2f();

		System.out.println("pointsSrc: " + pointsSrc.dump());

		Calib3d.projectPoints(pointsSrc, rotationMatrix, translationVector,
				cameraMatrix, distortionCoefficients, pointsDst);

		System.out.println("pointDst A: " + pointsDst.dump());
	}
}
