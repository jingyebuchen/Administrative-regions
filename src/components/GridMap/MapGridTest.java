package com.hy.mapgrid;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.alibaba.fastjson.JSONObject;
import com.hy.mapgrid.util.LocationUtils;

import com.vividsolutions.jts.JTSVersion;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class MapGridTest {
	/*
	 * 大地坐标系资料WGS-84 长半径a=6378137 短半径b=6356752.3142 扁率f=1/298.2572236
	 */
	/** 长半径a=6378137 */
	private double a = 6378137;
	/** 短半径b=6356752.3142 */
	private double b = 6356752.3142;
	/** 扁率f=1/298.2572236 */
	private double f = 1 / 298.2572236;

	private String maxPointStr = "108.41,33.273";
	private String minPointStr = "116.131,29.05";
	// maxPoint: [108.41, 33.273], //最大点
	// minPoint: [116.131, 29.05], //最小点
	private double sideUnit = 5000;// 边长 米
	private int rowGridNum = 0;// 每行的格子数
	private int colGridNum = 0;// 每列的格子数
    private String area="";
	// 武汉的边界
	private String wuhanArea = "110.989 33.253,111.174 33.117,111.283 32.862,111.574 32.591,111.712 32.608,111.873 32.506,112.306 32.328,112.54 32.406,112.762 32.346,112.868 32.399,113.112 32.378,113.205 32.434,113.422 32.272,113.606 32.363,113.733 32.258,113.854 31.842,114.288 31.754,114.581 31.764,114.639 31.582,114.825 31.461,115.119 31.6,115.205 31.447,115.368 31.407,115.579 31.145,115.707 31.2,116.026 31.011,115.862 30.868,115.758 30.687,115.915 30.518,115.88 30.382,116.053 30.217,116.131 29.825,115.961 29.727,115.663 29.853,115.506 29.843,115.354 29.649,115.226 29.654,115.119 29.512,114.976 29.567,114.864 29.408,114.509 29.326,114.254 29.346,113.937 29.05,113.68 29.09,113.604 29.254,113.734 29.592,113.658 29.687,113.356 29.69,113.139 29.452,112.906 29.61,112.926 29.764,112.645 29.595,112.433 29.635,112.364 29.545,111.909 29.855,111.73 29.924,111.546 29.898,111.237 30.043,110.925 30.066,110.764 30.123,110.487 30.022,110.63 29.763,110.368 29.636,110.114 29.793,109.777 29.761,109.702 29.61,109.524 29.628,109.425 29.53,109.228 29.122,109.106 29.219,109.061 29.403,108.916 29.329,108.882 29.636,108.739 29.659,108.598 29.867,108.514 29.885,108.578 30.259,108.41 30.481,108.694 30.549,108.839 30.509,108.963 30.627,109.146 30.53,109.356 30.553,109.898 30.903,110.146 30.914,110.115 31.092,110.159 31.305,109.919 31.524,109.741 31.58,109.581 31.729,109.635 31.819,109.555 32.221,109.555 32.221,109.498 32.393,109.62 32.599,110.124 32.619,110.096 32.836,109.798 32.879,109.793 32.88,109.787 33.012,109.787 33.013,109.618 33.272,109.619 33.273,110.208 33.164,110.466 33.183,110.707 33.097,110.989 33.253";
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	
	
	public MapGridTest(String maxPointStr,String minPointStr,double  sideUnit,String areaCode) {
		this.maxPointStr=maxPointStr;
		this.minPointStr=minPointStr;
		this.sideUnit=sideUnit;
		this.area=wuhanArea;
	}
	public MapGridTest() {
		
	}

	/**
	 * 计算另一点经纬度
	 * 
	 * @param lon    经度
	 * @param lat    维度
	 * @param lonlat 已知点经纬度
	 * @param brng   方位角
	 * @param dist   距离（米）
	 */
	public double[] computerThatLonLat(double lon, double lat, double brng, double dist) {

		double alpha1 = rad(brng);
		double sinAlpha1 = Math.sin(alpha1);
		double cosAlpha1 = Math.cos(alpha1);

		double tanU1 = (1 - f) * Math.tan(rad(lat));
		double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1));
		double sinU1 = tanU1 * cosU1;
		double sigma1 = Math.atan2(tanU1, cosAlpha1);
		double sinAlpha = cosU1 * sinAlpha1;
		double cosSqAlpha = 1 - sinAlpha * sinAlpha;
		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

		double cos2SigmaM = 0;
		double sinSigma = 0;
		double cosSigma = 0;
		double sigma = dist / (b * A), sigmaP = 2 * Math.PI;
		while (Math.abs(sigma - sigmaP) > 1e-12) {
			cos2SigmaM = Math.cos(2 * sigma1 + sigma);
			sinSigma = Math.sin(sigma);
			cosSigma = Math.cos(sigma);
			double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
					- B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
			sigmaP = sigma;
			sigma = dist / (b * A) + deltaSigma;
		}

		double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
		double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
				(1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
		double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
		double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
		double L = lambda - (1 - C) * f * sinAlpha
				* (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

		double revAz = Math.atan2(sinAlpha, -tmp); // final bearing

		// System.out.println(revAz);
		// System.out.println(lon+deg(L)+","+deg(lat2));
		double[] ret = { lon + deg(L), deg(lat2) };
		return ret;
	}

	/**
	 * 度换成弧度
	 * 
	 * @param d 度
	 * @return 弧度
	 */
	private double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 弧度换成度
	 * 
	 * @param x 弧度
	 * @return 度
	 */
	private double deg(double x) {
		return x * 180 / Math.PI;
	}

	public String createGrid() throws Exception {

		Polygon polygon = null;
		double maxTopLeftLon = Double.parseDouble(maxPointStr.split(",")[0]);
		double maxTopLeftLat = Double.parseDouble(maxPointStr.split(",")[1]);
		double minBottomRightLon = Double.parseDouble(minPointStr.split(",")[0]);
		;
		double minBottomRightLat = Double.parseDouble(minPointStr.split(",")[1]);
		;
		double rowDistance = LocationUtils.getDistance(maxTopLeftLat, maxTopLeftLon, maxTopLeftLat, minBottomRightLon);// 一行的距离
		double colDistance = LocationUtils.getDistance(maxTopLeftLat, maxTopLeftLon, minBottomRightLat, maxTopLeftLon);// 一行的距离
		Point maxPoint = null;
		Point minPoint = null;
		WKTReader reader = new WKTReader(geometryFactory);
		SimpleFeatureType type;
		FeatureJSON fjson = new FeatureJSON();
		List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		SimpleFeatureCollection collection;
		SimpleFeatureBuilder featureBuilder;

		type = DataUtilities.createType("Link", "geometry:Polygon," + // <- the geometry attribute: Point type
				"gid:String" // <- a String attribute

		);

		polygon = (Polygon) reader.read("POLYGON((" + wuhanArea + "))");
		featureBuilder = new SimpleFeatureBuilder(type);

		// 一行有多少个格子
		if (rowDistance % sideUnit == 0) {
			rowGridNum = (int) (rowDistance / sideUnit);
		} else {
			rowGridNum = (int) (rowDistance / sideUnit) + 1;
		}
		// 一列有多少个格子
		if (colDistance % sideUnit == 0) {
			colGridNum = (int) (colDistance / sideUnit);
		} else {
			colGridNum = (int) (colDistance / sideUnit) + 1;
		}

		String[][] arr = new String[rowGridNum][colGridNum];

		for (int i = 0; i < rowGridNum; i++) {
			for (int j = 0; j < colGridNum; j++) {
				
					double[] topLeft = { computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 90, sideUnit * i)[0],
							computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 180, sideUnit * j)[1] };
					double[] topRight = { computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 90, sideUnit * (i + 1))[0],
							computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 180, sideUnit * j)[1] };
					double[] bottomRight = { topRight[0],
							computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 180, sideUnit * (j + 1))[1] };
					double[] bottomLeft = { topLeft[0],
							computerThatLonLat(maxTopLeftLon, maxTopLeftLat, 180, sideUnit * (j + 1))[1] };
					String tempStr = topLeft[0] + " " + topLeft[1] + " " + topRight[0] + " " + topRight[1] + " "
							+ bottomRight[0] + " " + bottomRight[1] + " " + bottomLeft[0] + " " + bottomLeft[1] + " "
							+ topLeft[0] + " " + topLeft[1];
					
					Coordinate[] coordinatesPolygon = new Coordinate[] { new Coordinate(topLeft[0], topLeft[1]),
							new Coordinate(topRight[0], topRight[1]), new Coordinate(bottomRight[0], bottomRight[1]),
							new Coordinate(bottomLeft[0], bottomLeft[1]), new Coordinate(topLeft[0], topLeft[1]) };

					Polygon tempPolygon = geometryFactory.createPolygon(coordinatesPolygon);
					// tempPolygon.intersection(polygon);

					Geometry geometry =tempPolygon.intersection(polygon);
					
					
					if(!geometry.isEmpty()&&geometry.isSimple()&&geometry.isValid()) {
						featureBuilder.add(geometry);
						featureBuilder.add(i + "," + j);
						SimpleFeature feature = featureBuilder.buildFeature(null);
						features.add(feature);
					}

					

					
					// System.out.println(features.size());

				}

			}

		

		collection = new ListFeatureCollection(type, features);
		System.out.println(collection.size());
		 StringWriter writer = new StringWriter();
		fjson.writeFeatureCollection(collection, writer);
		 //System.out.println(writer.toString());
		// System.out.println(rowGridNum);

		// System.out.println(colGridNum);
		 return writer.toString();
	}

	/**
	 * create multi polygon by wkt
	 * 
	 * @return
	 * @throws ParseException
	 */
	public MultiPolygon createMulPolygonByWKT() throws ParseException {
		WKTReader reader = new WKTReader(geometryFactory);
		MultiPolygon mpolygon = (MultiPolygon) reader
				.read("MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))");
		return mpolygon;
	}

	/**
	 * create a polygon(多边形) by WKT
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Polygon createPolygonByWKT(String polygonStr) throws ParseException {
		WKTReader reader = new WKTReader(geometryFactory);
		Polygon polygon = (Polygon) reader.read("POLYGON((" + polygonStr + "))");
		return polygon;
	}

	public static void main(String[] args) {
		MapGridTest test = new MapGridTest();
		

		// test.computerThatLonLat(lon, lat, brng, dist);
		try {
			test.createGrid();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
