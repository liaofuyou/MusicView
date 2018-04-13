package me.ajax.musicview.utils;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by aj on 2018/4/3
 */

public class GeometryUtils {

    public static void main(String[] args) {
        GeometryUtils.getIntersection(new Point(1, 2), new Point(1, 2),
                new Point(1, 2), new Point(1, 2));
        GeometryUtils.getIntersection(new Point(1, 2), new Point(1, 2),
                new Point(1, 4), new Point(1, 4));
        GeometryUtils.getIntersection(new Point(100, 1), new Point(100, 100),
                new Point(100, 101), new Point(100, 400));
        GeometryUtils.getIntersection(new Point(5, 5), new Point(100, 100),
                new Point(100, 5), new Point(5, 100));
    }

    /**
     * 判断两条线是否相交 a 线段1起点坐标 b 线段1终点坐标 c 线段2起点坐标 d 线段2终点坐标 intersection 相交点坐标
     * reutrn 是否相交: 0 : 两线平行 -1 : 不平行且未相交 1 : 两线相交
     */

    private static int getIntersection(Point a, Point b, Point c, Point d) {
        Point intersection = new Point(0, 0);

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) + Math.abs(d.y - c.y)
                + Math.abs(d.x - c.x) == 0) {
            if ((c.x - a.x) + (c.y - a.y) == 0) {
                System.out.println("ABCD是同一个点！");
            } else {
                System.out.println("AB是一个点，CD是一个点，且AC不同！");
            }
            return 0;
        }

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) == 0) {
            if ((a.x - d.x) * (c.y - d.y) - (a.y - d.y) * (c.x - d.x) == 0) {
                System.out.println("A、B是一个点，且在CD线段上！");
            } else {
                System.out.println("A、B是一个点，且不在CD线段上！");
            }
            return 0;
        }
        if (Math.abs(d.y - c.y) + Math.abs(d.x - c.x) == 0) {
            if ((d.x - b.x) * (a.y - b.y) - (d.y - b.y) * (a.x - b.x) == 0) {
                System.out.println("C、D是一个点，且在AB线段上！");
            } else {
                System.out.println("C、D是一个点，且不在AB线段上！");
            }
            return 0;
        }

        if ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y) == 0) {
            System.out.println("线段平行，无交点！");
            return 0;
        }

        intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) -
                c.x * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x)) /
                ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));

        intersection.y = ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y
                * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y))
                / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

        if ((intersection.x - a.x) * (intersection.x - b.x) <= 0
                && (intersection.x - c.x) * (intersection.x - d.x) <= 0
                && (intersection.y - a.y) * (intersection.y - b.y) <= 0
                && (intersection.y - c.y) * (intersection.y - d.y) <= 0) {

            System.out.println("线段相交于点(" + intersection.x + "," + intersection.y + ")！");
            return 1; // '相交
        } else {
            System.out.println("线段相交于虚交点(" + intersection.x + "," + intersection.y + ")！");
            return -1; // '相交但不在线段上
        }
    }

    /**
     * 取得直线交点X
     * <p>
     * 参考： http://www.360doc.com/content/15/0325/22/15028327_458051859.shtml
     */
    static float intersectionX(PointF a, PointF b, PointF c, PointF d) {
        return ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) -
                c.x * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x)) /
                ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));
    }

    static float intersectionX(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        return ((bx - ax) * (cx - dx) * (cy - ay) -
                cx * (bx - ax) * (cy - dy) + ax * (by - ay) * (cx - dx)) /
                ((by - ay) * (cx - dx) - (bx - ax) * (cy - dy));
    }


    /**
     * 取得直线交点Y
     */
    static float intersectionY(PointF a, PointF b, PointF c, PointF d) {
        return ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y
                * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y))
                / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

    }

    static float intersectionY(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        return ((by - ay) * (cy - dy) * (cx - ax) - cy
                * (by - ay) * (cx - dx) + ay * (bx - ax) * (cy - dy))
                / ((bx - ax) * (cy - dy) - (by - ay) * (cx - dx));

    }

    /**
     * 极坐标转换为直角坐标
     */
    public static float polarX(float p, double angle) {
        return (float) (p * Math.cos(Math.toRadians(angle)));
    }

    /**
     * 极坐标转换为直角坐标
     */
    public static float polarY(float p, double angle) {
        return (float) (p * Math.sin(Math.toRadians(angle)));
    }

    /**
     * 直角坐标转换为极坐标
     */
    static float getPFromXY(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算贝塞尔曲线路径
     */
    static Path computeBezierPath(Path path, float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        if (path == null) return null;
        path.reset();

        float line1MidPointX = (ax + bx) / 2;
        float line1MidPointY = (ay + by) / 2;

        float line2MidPointX = (cx + dx) / 2;
        float line2MidPointY = (cy + dy) / 2;

        float line3MidPointX = (line1MidPointX + line2MidPointX) / 2;
        float line3MidPointY = (line1MidPointY + line2MidPointY) / 2;

        float control1X = (line1MidPointX + line3MidPointX) / 2;
        float control1Y = (line1MidPointY + line3MidPointY) / 2;

        float control2X = (line2MidPointX + line3MidPointX) / 2;
        float control2Y = (line2MidPointY + line3MidPointY) / 2;

        //交点
        //float intersectionX = intersectionX(ax, ay, cx, cy, bx, by, dx, dy);
        //float intersectionY = intersectionY(ax, ay, cx, cy, bx, by, dx, dy);

        //绘制贝塞尔
        path.moveTo(ax, ay);
        path.cubicTo(control1X, control1Y, control2X, control2Y, dx, dy);
        path.lineTo(cx, cy);
        path.cubicTo(control2X, control2Y, control1X, control1Y, bx, by);
        path.close();

        return path;
    }

    void l(Object o) {
        Log.e("######", o.toString());
    }
}
