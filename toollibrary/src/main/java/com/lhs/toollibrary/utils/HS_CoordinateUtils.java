package com.lhs.toollibrary.utils;

/**
 * 地理坐标工具类
 * **/
public class HS_CoordinateUtils {

    //定义一些常量
    private static double x_pi = Math.PI * 3000.0 / 180.0;
    private static double PI = Math.PI;
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;

    // 平面坐标相关
    private static final int THREE_DEGREE_ZONE = 3;
    private static final int SIX_DEGREE_ZONE = 6;
    private static final double UTM_PROJECTION_SCALE = 0.9996;
    private static final int DEFAULT_EAST_EXTENSION = 500000;
    private static int m_degree = SIX_DEGREE_ZONE;
    private static double m_originLongitude = 0.0;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换 * 即 百度 转 谷歌、高德
     * @param bdLongitude  经度
     * @param bdLatitude   纬度
     * @return {[longitude, latitude]}
     * **/
    public static double[] bd09ToGcj02(double bdLongitude, double bdLatitude) {
        double x = bdLongitude - 0.0065;
        double y = bdLatitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[] {gg_lng, gg_lat};
    }

    /**
     * 火星坐标系 (GCJ-02) 与 百度坐标系 (BD-09)的转换 * 即 谷歌、高德 转 百度
     * @param gcjLongitude  经度
     * @param gcjLatitude   纬度
     * @return {[longitude, latitude]}
     * **/
    public static double[] gcj02ToBd09(double gcjLongitude, double gcjLatitude) {
        double x = gcjLongitude, y = gcjLatitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new double[] {tempLon, tempLat};
    }

    /**
     * GCJ02(国测局) 转换为 WGS84(GPS)
     * @param gcjLongitude 经度
     * @param gcjLatitude  纬度
     * @return {[longitude, latitude]}
     * **/
    public static double[] gcj02ToWgs84(double gcjLongitude, double gcjLatitude) {
        if (outOfChina(gcjLongitude, gcjLatitude)) {
            return new double[] {gcjLongitude, gcjLatitude};
        } else {
            double dlat = transformLatitude(gcjLongitude - 105.0, gcjLatitude - 35.0);
            double dlng = transformLongitude(gcjLongitude - 105.0, gcjLatitude - 35.0);
            double radlat = gcjLatitude / 180.0 * PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = gcjLatitude + dlat;
            double mglng = gcjLongitude + dlng;

            return new double[] {gcjLongitude * 2 - mglng, gcjLatitude * 2 - mglat};
        }
    }

    /**
     * WGS84(GPS) 转换为 GCJ02(国测局)
     * @param wgsLongitude 经度
     * @param wgsLatitude  纬度
     * @return {[longitude, latitude]}
     * **/
    public static double[] wgs84ToGcj02(double wgsLongitude, double wgsLatitude) {
        if (outOfChina(wgsLongitude, wgsLatitude)) {
            return new double[] {wgsLongitude, wgsLatitude};
        } else {
            double dlat = transformLatitude(wgsLongitude - 105.0, wgsLatitude - 35.0);
            double dlng = transformLongitude(wgsLongitude - 105.0, wgsLatitude - 35.0);
            double radlat = wgsLatitude / 180.0 * PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = wgsLatitude + dlat;
            double mglng = wgsLongitude + dlng;

            return new double[] {mglng, mglat};
        }
    }

    /**
     * 设置椭球投影类型(不对外提供)
     * @param projectType 1: Ellipsoid_TYPE_MIN
     *                    2: WGS84
     *                    3: CGCS2000
     *                    4: XIAN80
     *                    5: BEIJING54
     *                    6: Ellipsoid_TYPE_MAX
     * @return int 成功返回0 失败返回-1
     * **/
    private int setEllipProjectType(int projectType) {
        switch (projectType) {
            case 3:
                System.out.println("CGCS2000");
//                m_a = 6378137.000;
//                m_b = 6356752.31414;
                break;
            case 4:
                System.out.println("XIAN80");
//                m_a = 6378140;
//                m_b = 6356755.28820;
                break;
            case 5:
                System.out.println("BEIJING54");
//                m_a = 6378245;
//                m_b = 6356863.01880;
                break;
            case 2:
            default:
                System.out.println("WGS84或者default");
//                m_a = 6378137.000;
//                m_b = 6356752.31420;
                break;
        }

//        m_f = m_a / (m_a - m_b);
//        m_e2 = 1 - ((m_f - 1) / m_f) * ((m_f - 1) / m_f);
//        m_e12 = (m_f / (m_f - 1)) * (m_f / (m_f - 1)) - 1;
//        m_n = (m_a - m_b) / (m_a + m_b);
//        m_a1 = m_a * (1 - m_n + (Math.pow(m_n, 2) - Math.pow(m_n, 3)) * 5 / 4 +
//                (Math.pow(m_n, 4) - Math.pow(m_n, 5)) * 81 / 64) * Math.PI / 180;
//        m_a2 = -(m_n - Math.pow(m_n, 2) + (Math.pow(m_n, 3) - Math.pow(m_n, 4))
//                * 7 / 8 + Math.pow(m_n, 5) * 55 / 64) * 3 * m_a / 2;
//        m_a3 = (Math.pow(m_n, 2) - Math.pow(m_n, 3) + (Math.pow(m_n, 4) -
//                Math.pow(m_n, 5)) * 3 / 4) * 15 * m_a / 16;
//        m_a4 = -(Math.pow(m_n, 3) - Math.pow(m_n, 4) + Math.pow(m_n, 5) *
//                11 / 16) * 35 * m_a / 48;

        return 0;
    }

    /**
     * 计算中央子午线
     * @param longitude 经度
     * @return int32_t 成功返回0 失败返回-1
     * **/
    public static int calCentralMeridian(double longitude) {
        if (longitude > 180.0 || longitude < -180.0) {
            DebugLogJava.e("longitude out of range!");
            return -1;
        }
        double originLongitude = 0.0;
        int zoneNum = 0;
        switch (m_degree) {
            case THREE_DEGREE_ZONE:
                zoneNum = (int)((longitude + 1.5) / 3);
                originLongitude = zoneNum * 3;
                break;
            default:
                zoneNum = (int)((longitude + 6) / 6);
                originLongitude = zoneNum * 6 - 3;
                break;
        }
        m_originLongitude = originLongitude * Math.PI / 180.0;
        return 0;
    }

    /**
     * 计算中央子午线
     * @param zoneNum 带号 (中国一般是19，相差1，就是差6度)
     * @return int32_t 成功返回0 失败返回-1
     * **/
    public static int calCentralMeridian(int zoneNum) {
        if (zoneNum > 120 || zoneNum < 1) {
            DebugLogJava.e("zoneNum out of range!");
            return -1;
        }

        double originLongitude = 0.0;
        switch (m_degree) {
            case SIX_DEGREE_ZONE:
                originLongitude = (zoneNum - 1) * m_degree + m_degree / 2;
                break;
            default:
                originLongitude = (zoneNum - 1) * m_degree;
                break;
        }
        m_originLongitude = originLongitude * Math.PI / 180.0;
        return 0;
    }

    /**
     * 大地坐标(GPS)转平面坐标
     * @param wgsLongitude
     * @param wgsLatitude
     * @param earthHeight
     * @return {[pointX, pointY, pointZ]}
     * **/
    public static double[] wgs84ToPlaneCoordinate(double wgsLongitude, double wgsLatitude, double earthHeight) {
        if (wgsLongitude > 180.0 || wgsLongitude < -180.0) {
            DebugLogJava.e("longitude out of range!");
            return new double[]{0, 0, 0};
        }
        if (wgsLatitude > 90.0 || wgsLatitude < -90.0) {
            DebugLogJava.e("latitude out of range!");
            return new double[]{0, 0, 0};
        }

        double xLength;
        double n;
        double t;
        double t2;
        double m;
        double m2;
        double ng2;
        double sinB;
        double cosB;
        double m_longitude = 0.0;
        double m_latitude = 0.0;
        double m_ellipheight = 0.0;
        double m_a1;
        double m_a2;
        double m_a3;
        double m_a4;
        double m_x = 0;
        double m_y = 0;
        double m_z = 0;

        double m_a = 6378137.000;
        double m_b = 6356752.31420;
        double m_n = (m_a - m_b) / (m_a + m_b);
        double m_f = m_a / (m_a - m_b);
        double m_e2 = 1 - ((m_f - 1) / m_f) * ((m_f - 1) / m_f);

        double v2_3 = Math.pow(m_n, 2) - Math.pow(m_n, 3);
        double v3_4 = Math.pow(m_n, 3) - Math.pow(m_n, 4);
        double v4_5 = Math.pow(m_n, 4) - Math.pow(m_n, 5);

        double m_projectionScale = UTM_PROJECTION_SCALE;


        m_a1 = m_a * (1 - m_n + v2_3 * 5 / 4 + v4_5 * 81 / 64) * Math.PI / 180;
        m_a2 = -(m_n - Math.pow(m_n, 2) + v3_4 * 7 / 8 + Math.pow(m_n, 5) * 55 / 64) * 3 * m_a / 2;
        m_a3 = (v2_3 + v4_5 * 3 / 4) * 15 * m_a / 16;
        m_a4 = -(v3_4 + Math.pow(m_n, 5) * 11 / 16) * 35 * m_a / 48;

        m_longitude = wgsLongitude * Math.PI / 180.0;
        m_latitude = wgsLatitude * Math.PI / 180.0;
        m_ellipheight = earthHeight;
        xLength = m_a1 * m_latitude * 180.0 / Math.PI + m_a2 *
                Math.sin(2 * m_latitude) + m_a3 * Math.sin(4 * m_latitude) +
                m_a4 * Math.sin(6 * m_latitude);
        sinB = Math.sin(m_latitude);
        cosB = Math.cos(m_latitude);
        t = Math.tan(m_latitude);
        t2 = t * t;
        n = m_a / Math.sqrt(1 - m_e2 * sinB * sinB);
        m = cosB * (m_longitude - m_originLongitude);
        m2 = m * m;
        ng2 = cosB * cosB * m_e2 / (1 - m_e2);
        m_x = m_projectionScale * (xLength + n * t * ((0.5 + ((5 -
                t2 + 9 * ng2 + 4 * ng2 * ng2) / 24.0 + (61 - 58 * t2 + t2 * t2)
                * m2 / 720.0) * m2) * m2));
        m_y = m_projectionScale * (n * m * (1 + m2 * ((1 - t2 + ng2) / 6.0 +
                m2 * (5 - 18 * t2 + t2 * t2 + 14 * ng2 - 58 * ng2 * t2) / 120.0)));
        m_y += DEFAULT_EAST_EXTENSION;
        m_z = m_ellipheight;

        return new double[] {m_x, m_y, m_z};
    }

    /**
     * 平面坐标转大地坐标(GPS)
     * @param pointX 平面坐标点X
     * @param pointY 平面坐标点Y
     * @param pointZ 平面坐标点Z
     * @return {[longitude, latitude]}
     * **/
    public static double[] planeCoordinateToWgs84(double pointX, double pointY, double pointZ) {
        double sinB;
        double cosB;
        double t;
        double t2;
        double n;
        double ng2;
        double v;
        double yN;
        double preB0;
        double curB0;
        double eta;
        double m_x;
        double m_y;
        double m_z;
        double m_a1;
        double m_a2;
        double m_a3;
        double m_a4;
        double m_a = 6378137.000;
        double m_b = 6356752.31420;
        double m_f = m_a / (m_a - m_b);
        double m_n = (m_a - m_b) / (m_a + m_b);
        double m_e2 = 1 - ((m_f - 1) / m_f) * ((m_f - 1) / m_f);
        double m_projectionScale = UTM_PROJECTION_SCALE;

        double v2_3 = Math.pow(m_n, 2) - Math.pow(m_n, 3);
        double v3_4 = Math.pow(m_n, 3) - Math.pow(m_n, 4);
        double v4_5 = Math.pow(m_n, 4) - Math.pow(m_n, 5);

        m_a1 = m_a * (1 - m_n + v2_3 * 5 / 4 + v4_5 * 81 / 64) * Math.PI / 180;
        m_a2 = -(m_n - Math.pow(m_n, 2) + v3_4 * 7 / 8 + Math.pow(m_n, 5) * 55 / 64) * 3 * m_a / 2;
        m_a3 = (v2_3 + v4_5 * 3 / 4) * 15 * m_a / 16;
        m_a4 = -(v3_4 + Math.pow(m_n, 5) * 11 / 16) * 35 * m_a / 48;

        m_x = pointX;
        m_y = pointY;
        m_z = pointZ;
        m_y -= DEFAULT_EAST_EXTENSION;
        m_y /= m_projectionScale;
        m_x /= m_projectionScale;
        curB0 = m_x / m_a1;

        do {
            preB0 = curB0;
            curB0 = curB0 * Math.PI / 180.0;
            curB0 = (m_x - (m_a2 * Math.sin(2 * curB0) + m_a3 * Math.sin(4 * curB0)
                    + m_a4 * Math.sin(6 * curB0))) / m_a1;
            eta = Math.abs(curB0 - preB0);
        } while (eta > 0.000000001);

        curB0 = curB0 * Math.PI / 180.0;
        sinB = Math.sin(curB0);
        cosB = Math.cos(curB0);
        t = Math.tan(curB0);
        t2 = t * t;
        n = m_a / Math.sqrt(1 - m_e2 * sinB * sinB);
        ng2 = cosB * cosB * m_e2 / (1 - m_e2);
        v = Math.sqrt(1 + ng2);
        yN = m_y / n;

        double tempLongitude = m_originLongitude + (yN - (1 + 2 * t2 + ng2) * yN * yN * yN /
                6.0 + (5 + 28 * t2 + 24 * t2 * t2 + 6 * ng2 + 8 * ng2 * t2) * yN * yN * yN
                * yN * yN / 120.0) / cosB;

        double tempLatitude = curB0 - (yN * yN - (5 + 3 * t2 + ng2 - 9 * ng2 * t2) * yN * yN
                * yN * yN / 12.0 + (61 + 90 * t2 + 45 * t2 * t2) * yN * yN * yN * yN
                * yN * yN / 360.0) * v * v * t / 2;

        return new double[] {tempLongitude * 180.0 / Math.PI, tempLatitude * 180.0 / Math.PI};

    }


    private static double transformLatitude(double longtitude, double latitude) {
        double ret = -100.0 + 2.0 * longtitude + 3.0 * latitude + 0.2 * latitude * latitude + 0.1 * longtitude * latitude + 0.2 * Math.sqrt(Math.abs(longtitude));
        ret += (20.0 * Math.sin(6.0 * longtitude * PI) + 20.0 * Math.sin(2.0 * longtitude * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(latitude * PI) + 40.0 * Math.sin(latitude / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(latitude / 12.0 * PI) + 320 * Math.sin(latitude * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }
    private static double transformLongitude(double longtitude, double latitude) {
        double ret = 300.0 + longtitude + 2.0 * latitude + 0.1 * longtitude * longtitude + 0.1 * longtitude * latitude + 0.1 * Math.sqrt(Math.abs(longtitude));
        ret += (20.0 * Math.sin(6.0 * longtitude * PI) + 20.0 * Math.sin(2.0 * longtitude * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(longtitude * PI) + 40.0 * Math.sin(longtitude / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(longtitude / 12.0 * PI) + 300.0 * Math.sin(longtitude / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static boolean outOfChina(double lng, double lat) {
        return (lng < 72.004 || lng > 137.8347) || ((lat < 0.8293 || lat > 55.8271) || false);
    }
}
