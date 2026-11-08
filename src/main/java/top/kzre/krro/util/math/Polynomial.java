package top.kzre.krro.util.math;

import java.util.Arrays;
import top.kzre.krro.util.pool.DoublesHolder;
import top.kzre.krro.util.pool.DoublesPool;
import top.kzre.krro.util.pool.PoolManagers;

/**
 * 低阶多项式实根求解器（最多四次）。
 *
 * <p><b>两套 API</b>：
 * <ul>
 *   <li><b>便捷</b>：{@code solveXxx(...)} 返回 {@code new double[n]}（n = 实根数，0~4），
 *       精确长度、无 NaN、调用方无需释放。内部中间缓冲池化。</li>
 *   <li><b>零分配</b>：{@code solveXxx(..., out)} 返回根数，根写入 {@code out}。
 *       调用方负责准备 out拿池缓冲，或自备数组）。
 *       实现不分配任何东西。</li>
 * </ul>
 *
 * <p><b>数值策略</b>：二次 q 形式防抵消；三次 Cardano + 三角形式；
 * 四次 Ferrari + 双二次降级；阈值按系数尺度自适应。返回的根<b>不保证有序</b>。
 */
public final class Polynomial {

    private Polynomial() {}

    private static final double EPS = 1e-14;

    private static final DoublesPool SCRATCH_2;
    private static final DoublesPool SCRATCH_3;
    private static final DoublesPool SCRATCH_4;
    static {
        DoublesHolder holder = PoolManagers.doubles().getHolder();
        SCRATCH_2 = holder.getPool(2);
        SCRATCH_3 = holder.getPool(3);
        SCRATCH_4 = holder.getPool(4);
    }

    /** 解 a·x² + b·x + c = 0。满根时返回的临时数组即结果，零额外拷贝。 */
    public static double[] solveQuadratic(double a, double b, double c) {
        double[] r = SCRATCH_2.acquire();
        int n = solveQuadratic(a, b, c, r);
        if (n == 2) {
            return r;
        }else {
            double[] res = Arrays.copyOf(r, n);
            SCRATCH_2.release(res);
            return res;
        }
    }

    /** 解 a·x³ + b·x² + c·x + d = 0。满根时零额外拷贝。 */
    public static double[] solveCubic(double a, double b, double c, double d) {
        double[] r = SCRATCH_3.acquire();
        int n = solveCubic(a, b, c, d, r);
        if (n == 3) {
            return r;
        }else {
            double[] res = Arrays.copyOf(r, n);
            SCRATCH_3.release(res);
            return res;
        }
    }

    /** 解 a·x⁴ + b·x³ + c·x² + d·x + e = 0。满根时零额外拷贝。 */
    public static double[] solveQuartic(double a, double b, double c,
                                        double d, double e) {

        double[] r = SCRATCH_4.acquire();
        int n = solveQuartic(a, b, c, d, e, r);
        if (n == 4) {
            return r;
        }else {
            double[] res = Arrays.copyOf(r, n);
            SCRATCH_4.release(res);
            return res;
        }
    }

    // ==================================================================
    // 零分配主 API：调用方负责 out
    // ==================================================================

    /** 解 a·x²+b·x+c=0，写入 out[0..n)，返回 n。out.length ≥ 2。 */
    public static int solveQuadratic(double a, double b, double c,
                                     double[] out) {
        return solveQuadraticRootsInto(a, b, c, out, 0, 0.0);
    }

    /** 解 a·x³+b·x²+c·x+d=0，写入 out[0..n)，返回 n。out.length ≥ 3。 */
    public static int solveCubic(double a, double b, double c, double d,
                                 double[] out) {
        if (Math.abs(a) < EPS) return solveQuadratic(b, c, d, out);

        final double p = b / a;
        final double q = c / a;
        final double r = d / a;

        final double P     = q - p * p / 3.0;
        final double Q     = r - p * q / 3.0 + 2.0 * p * p * p / 27.0;
        final double shift = p / 3.0;

        final double disc   = Q * Q / 4.0 + P * P * P / 27.0;
        final double scale  = Math.max(Math.abs(Q * Q / 4.0),
                Math.abs(P * P * P / 27.0));
        final double thresh = EPS * Math.max(1.0, scale);

        if (disc > thresh) {
            double sq = Math.sqrt(disc);
            double u  = Math.cbrt(-Q / 2.0 + sq);
            double v  = Math.cbrt(-Q / 2.0 - sq);
            out[0] = u + v - shift;
            return 1;
        } else if (disc < -thresh) {
            double m  = 2.0 * Math.sqrt(-P / 3.0);
            double cs = 3.0 * Q / (P * m);
            if (cs >  1.0) cs =  1.0;
            if (cs < -1.0) cs = -1.0;
            double theta = Math.acos(cs) / 3.0;
            out[0] = m * Math.cos(theta)                       - shift;
            out[1] = m * Math.cos(theta - 2.0 * Math.PI / 3.0) - shift;
            out[2] = m * Math.cos(theta - 4.0 * Math.PI / 3.0) - shift;
            return 3;
        } else {
            double u = Math.cbrt(-Q / 2.0);
            out[0] = 2.0 * u - shift;
            out[1] = -u      - shift;
            return 2;
        }
    }

    /** 解 a·x⁴+b·x³+c·x²+d·x+e=0，写入 out[0..n)，返回 n。out.length ≥ 4。 */
    public static int solveQuartic(double a, double b, double c,
                                   double d, double e, double[] out) {
        if (Math.abs(a) < EPS) return solveCubic(b, c, d, e, out);

        final double a3 = b / a;
        final double b3 = c / a;
        final double c3 = d / a;
        final double d3 = e / a;

        final double p = b3 - 3.0 * a3 * a3 / 8.0;
        final double q = c3 - a3 * b3 / 2.0 + a3 * a3 * a3 / 8.0;
        final double r = d3 - a3 * c3 / 4.0
                + a3 * a3 * b3 / 16.0
                - 3.0 * a3 * a3 * a3 * a3 / 256.0;
        final double shift = a3 / 4.0;

        final double qScale = EPS * Math.max(1.0,
                Math.max(Math.abs(p), Math.abs(r)));
        if (Math.abs(q) < qScale) {
            return solveBiquadraticInto(p, r, shift, out);
        }

        final double P3 = p;
        final double Q3 = (p * p - 4.0 * r) / 4.0;
        final double R3 = -q * q / 8.0;

        final double z = cubicMaxRealRoot(1.0, P3, Q3, R3);
        if (!(z > EPS)) return 0;

        final double sqrt2z = Math.sqrt(2.0 * z);
        final double term   = q / (2.0 * sqrt2z);
        final double s      = p / 2.0 + z;

        int n = 0;
        n = solveQuadraticRootsInto(1.0, -sqrt2z, s + term, out, n, shift);
        n = solveQuadraticRootsInto(1.0,  sqrt2z, s - term, out, n, shift);
        return n;
    }

    // ==================================================================
    // 私有工具（全部零分配）
    // ==================================================================

    /** 解 a·y²+b·y+c=0，结果做 x = y−shift 后写入 out[offset..)，返回新 offset。 */
    private static int solveQuadraticRootsInto(double a, double b, double c,
                                               double[] out, int offset,
                                               double shift) {
        if (Math.abs(a) < EPS) {
            if (Math.abs(b) < EPS) return offset;
            out[offset] = -c / b - shift;
            return offset + 1;
        }

        final double disc   = b * b - 4.0 * a * c;
        final double scale  = Math.max(Math.abs(b * b), Math.abs(4.0 * a * c));
        final double thresh = EPS * Math.max(1.0, scale);

        if (disc < -thresh) return offset;
        if (disc <  thresh) {
            out[offset] = -b / (2.0 * a) - shift;
            return offset + 1;
        }

        final double sq = Math.sqrt(disc);
        final double q  = -0.5 * (b + Math.copySign(sq, b));
        out[offset]     = q / a - shift;
        out[offset + 1] = c / q - shift;
        return offset + 2;
    }

    /** 解双二次 y⁴+p·y²+r=0，做 x=y−shift 后写入 out[0..n)，返回 n。 */
    private static int solveBiquadraticInto(double p, double r, double shift,
                                            double[] out) {
        final double discS   = p * p - 4.0 * r;
        final double scaleS  = Math.max(Math.abs(p * p), Math.abs(4.0 * r));
        final double threshS = EPS * Math.max(1.0, scaleS);

        if (discS < -threshS) return 0;

        double s1, s2;
        if (discS < threshS) {
            s1 = s2 = -p / 2.0;
        } else {
            double sq = Math.sqrt(discS);
            double q2 = -0.5 * (p + Math.copySign(sq, p));
            s1 = q2;
            s2 = r / q2;
        }

        int n = 0;
        if (s1 >= 0.0) {
            double sy = Math.sqrt(s1);
            out[n++] =  sy - shift;
            if (sy > EPS) out[n++] = -sy - shift;
        }
        if (s2 >= 0.0 && s2 != s1) {
            double sy = Math.sqrt(s2);
            out[n++] =  sy - shift;
            if (sy > EPS) out[n++] = -sy - shift;
        }
        return n;
    }

    /** 解 a·x³+b·x²+c·x+d=0，只返回最大实根；无实根返回 −∞。零分配。 */
    private static double cubicMaxRealRoot(double a, double b, double c, double d) {
        if (Math.abs(a) < EPS) {
            final double disc   = c * c - 4.0 * b * d;
            final double scale  = Math.max(Math.abs(c * c), Math.abs(4.0 * b * d));
            final double thresh = EPS * Math.max(1.0, scale);
            if (disc < -thresh) return Double.NEGATIVE_INFINITY;
            if (disc <  thresh) return -c / (2.0 * b);
            final double sq = Math.sqrt(disc);
            final double q  = -0.5 * (c + Math.copySign(sq, c));
            return Math.max(q / b, d / q);
        }

        final double p = b / a;
        final double q = c / a;
        final double r = d / a;

        final double P     = q - p * p / 3.0;
        final double Q     = r - p * q / 3.0 + 2.0 * p * p * p / 27.0;
        final double shift = p / 3.0;

        final double disc   = Q * Q / 4.0 + P * P * P / 27.0;
        final double scale  = Math.max(Math.abs(Q * Q / 4.0),
                Math.abs(P * P * P / 27.0));
        final double thresh = EPS * Math.max(1.0, scale);

        if (disc > thresh) {
            double sq = Math.sqrt(disc);
            double u  = Math.cbrt(-Q / 2.0 + sq);
            double v  = Math.cbrt(-Q / 2.0 - sq);
            return u + v - shift;
        } else if (disc < -thresh) {
            // 三实根，i=0 时 θ∈[0,π/3]，cos 最大 ⇒ 最大根
            double m  = 2.0 * Math.sqrt(-P / 3.0);
            double cs = 3.0 * Q / (P * m);
            if (cs >  1.0) cs =  1.0;
            if (cs < -1.0) cs = -1.0;
            double theta = Math.acos(cs) / 3.0;
            return m * Math.cos(theta) - shift;
        } else {
            double u = Math.cbrt(-Q / 2.0);
            return Math.max(2.0 * u, -u) - shift;
        }
    }
}