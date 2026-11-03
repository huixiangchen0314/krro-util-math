package top.kzre.krro.util.math;

import top.kzre.krro.util.pool.*;

/**
 * Krro 数学工具类，不做类型抽象，约定 float[] 布局.
 *
 * <p>double 版本方法统一使用大写 {@code D} 后缀（如 {@code minD} / {@code lerpD}）。
 * 旧的小写 {@code d} 后缀方法已标记 {@link Deprecated}，仅作兼容保留。
 *
 * <p>约定：带 {@code out} 参数的重载包含实际计算逻辑，非 out 重载仅分配结果数组并委托。
 */
public final class KMath {

    private KMath() {} // 禁止实例化

    // ═══════════════════════════════════════════════════════════
    // 内部工具
    // ═══════════════════════════════════════════════════════════

    private static float[] requireMinLen(float[] arr, int min, String name) {
        if (arr == null || arr.length < min) {
            throw new IllegalArgumentException(name + " 长度至少为 " + min);
        }
        return arr;
    }

    private static double[] requireMinLen(double[] arr, int min, String name) {
        if (arr == null || arr.length < min) {
            throw new IllegalArgumentException(name + " 长度至少为 " + min);
        }
        return arr;
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：基本比较
    // ═══════════════════════════════════════════════════════════

    /** 两个 float 的最小值 */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    /** 三个 float 的最小值 */
    public static float min(float a, float b, float c) {
        return min(min(a, b), c);
    }

    /** 两个 float 的最大值 */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /** 三个 float 的最大值 */
    public static float max(float a, float b, float c) {
        return max(max(a, b), c);
    }

    /** 将值限制在 [lo, hi] 范围内 */
    public static float clamp(float value, float lo, float hi) {
        return value < lo ? lo : (Math.min(value, hi));
    }

    public static float clamp01(float value) {
        return clamp(value, 0.0f, 1.0f);
    }

    /** 取绝对值 */
    public static float abs(float a) {
        return a < 0.0f ? -a : a;
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：近似比较
    // ═══════════════════════════════════════════════════════════

    /** 两个 float 是否近似相等（默认容差） */
    public static boolean nearlyEqual(float a, float b) {
        return nearlyEqual(a, b, 1e-6f);
    }

    /** 两个 float 是否近似相等（指定容差） */
    public static boolean nearlyEqual(float a, float b, float eps) {
        return abs(a - b) <= eps;
    }

    /** 是否为接近零的值 */
    public static boolean isNearZero(float a) {
        return abs(a) <= 1e-6f;
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：插值与映射
    // ═══════════════════════════════════════════════════════════

    /** 线性插值 a + (b-a)*t */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /** smoothstep (Hermite 平滑) */
    public static float smoothstep(float edge0, float edge1, float x) {
        float t = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    /** 将值从 [inMin, inMax] 映射到 [outMin, outMax] */
    public static float map(float x, float inMin, float inMax, float outMin, float outMax) {
        float t = (x - inMin) / (inMax - inMin);
        return lerp(outMin, outMax, t);
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：幂与根
    // ═══════════════════════════════════════════════════════════

    /** 快速倒数平方根（John Carmack 方法，精度约 1%） */
    public static float fastInvSqrt(float x) {
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        float y = Float.intBitsToFloat(i);
        // 一次牛顿迭代提高精度
        return y * (1.5f - 0.5f * x * y * y);
    }

    /** 平方根（直接委托 Math，但提供统一入口） */
    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：角度
    // ═══════════════════════════════════════════════════════════

    /** 将弧度转换为度 */
    public static float toDegrees(float rad) {
        return rad * 180.0f / (float) Math.PI;
    }

    /** 将度转换为弧度 */
    public static float toRadians(float deg) {
        return deg * (float) Math.PI / 180.0f;
    }

    // ═══════════════════════════════════════════════════════════
    // 标量：double 版本（方法名加 D 后缀）
    // ═══════════════════════════════════════════════════════════

    public static double minD(double a, double b) { return Math.min(a, b); }
    public static double minD(double a, double b, double c) { return minD(minD(a, b), c); }
    public static double maxD(double a, double b) { return Math.max(a, b); }
    public static double maxD(double a, double b, double c) { return maxD(maxD(a, b), c); }

    public static double clampD(double value, double lo, double hi) {
        return value < lo ? lo : (Math.min(value, hi));
    }
    public static double clamp01D(double value) { return clampD(value, 0.0, 1.0); }

    public static double absD(double a) { return a < 0.0 ? -a : a; }

    public static boolean nearlyEqualD(double a, double b) { return nearlyEqualD(a, b, 1e-9); }
    public static boolean nearlyEqualD(double a, double b, double eps) { return absD(a - b) <= eps; }
    public static boolean isNearZeroD(double a) { return absD(a) <= 1e-9; }

    public static double lerpD(double a, double b, double t) { return a + (b - a) * t; }

    public static double smoothstepD(double edge0, double edge1, double x) {
        double t = clampD((x - edge0) / (edge1 - edge0), 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

    public static double mapD(double x, double inMin, double inMax, double outMin, double outMax) {
        double t = (x - inMin) / (inMax - inMin);
        return lerpD(outMin, outMax, t);
    }

    public static double fastInvSqrtD(double x) {
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6eb50c7b537a9L - (i >> 1);
        double y = Double.longBitsToDouble(i);
        return y * (1.5 - 0.5 * x * y * y);
    }

    public static double sqrtD(double x) { return Math.sqrt(x); }
    public static double toDegreesD(double rad) { return rad * 180.0 / Math.PI; }
    public static double toRadiansD(double deg) { return deg * Math.PI / 180.0; }

    // ═══════════════════════════════════════════════════════════
    // 标量：旧的小写 d 后缀 —— @Deprecated，仅作兼容
    // ═══════════════════════════════════════════════════════════

    @Deprecated
    public static double mind(double a, double b) { return minD(a, b); }

    @Deprecated
    public static double mind(double a, double b, double c) { return minD(a, b, c); }

    @Deprecated
    public static double maxd(double a, double b) { return maxD(a, b); }

    @Deprecated
    public static double maxd(double a, double b, double c) { return maxD(a, b, c); }

    @Deprecated
    public static double clampd(double value, double lo, double hi) { return clampD(value, lo, hi); }

    @Deprecated
    public static double clamp01d(double value) { return clamp01D(value); }

    @Deprecated
    public static double absd(double a) { return absD(a); }

    @Deprecated
    public static boolean nearlyEquald(double a, double b) { return nearlyEqualD(a, b); }

    @Deprecated
    public static boolean nearlyEquald(double a, double b, double eps) { return nearlyEqualD(a, b, eps); }

    @Deprecated
    public static boolean isNearZerod(double a) { return isNearZeroD(a); }

    @Deprecated
    public static double lerpd(double a, double b, double t) { return lerpD(a, b, t); }

    @Deprecated
    public static double smoothstepd(double edge0, double edge1, double x) {
        return smoothstepD(edge0, edge1, x);
    }

    @Deprecated
    public static double mapd(double x, double inMin, double inMax, double outMin, double outMax) {
        return mapD(x, inMin, inMax, outMin, outMax);
    }

    @Deprecated
    public static double fastInvSqrtd(double x) { return fastInvSqrtD(x); }

    @Deprecated
    public static double sqrtd(double x) { return sqrtD(x); }

    @Deprecated
    public static double toDegreesd(double rad) { return toDegreesD(rad); }

    @Deprecated
    public static double toRadiansd(double deg) { return toRadiansD(deg); }

    // ═══════════════════════════════════════════════════════════
    // 向量构造
    // ═══════════════════════════════════════════════════════════

    /** 返回二维零向量 (0, 0) */
    public static float[] vec2() {
        return vec2(0.0f, 0.0f, new float[2]);
    }

    /** 构造二维向量 (x, y) */
    public static float[] vec2(float x, float y) {
        return vec2(x, y, new float[2]);
    }

    public static float[] vec2(float x, float y, float[] out) {
        requireMinLen(out, 2, "out");
        out[0] = x; out[1] = y;
        return out;
    }

    /** 返回三维零向量 (0, 0, 0) */
    public static float[] vec3() {
        return vec3(0.0f, 0.0f, 0.0f, new float[3]);
    }

    /** 构造三维向量 (x, y, z) */
    public static float[] vec3(float x, float y, float z) {
        return vec3(x, y, z, new float[3]);
    }

    public static float[] vec3(float x, float y, float z, float[] out) {
        requireMinLen(out, 3, "out");
        out[0] = x; out[1] = y; out[2] = z;
        return out;
    }

    /** 返回四维零向量 (0, 0, 0, 0) */
    public static float[] vec4() {
        return vec4(0.0f, 0.0f, 0.0f, 0.0f, new float[4]);
    }

    /** 构造四维向量 (x, y, z, w) */
    public static float[] vec4(float x, float y, float z, float w) {
        return vec4(x, y, z, w, new float[4]);
    }

    public static float[] vec4(float x, float y, float z, float w, float[] out) {
        requireMinLen(out, 4, "out");
        out[0] = x; out[1] = y; out[2] = z; out[3] = w;
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 向量运算
    // ═══════════════════════════════════════════════════════════

    /**
     * 计算两个向量的点积（内积）。
     * 支持任意维度，但两个向量长度必须相同。
     */
    public static float dot(float[] vec1, float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("向量长度必须相同，但分别为 " + vec1.length + " 和 " + vec2.length);
        }
        float sum = 0.0f;
        for (int i = 0; i < vec1.length; i++) {
            sum += vec1[i] * vec2[i];
        }
        return sum;
    }

    /**
     * 计算两个三维向量的叉积（外积）。
     * 要求两个向量长度至少为 3，取前三个分量参与计算。
     */
    public static float[] cross(float[] vec1, float[] vec2) {
        return cross(vec1, vec2, new float[3]);
    }

    public static float[] cross(float[] v1, float[] v2, float[] out) {
        if (v1.length < 3 || v2.length < 3) {
            throw new IllegalArgumentException("叉积要求向量长度至少为 3");
        }
        requireMinLen(out, 3, "out");
        float x1 = v1[0], y1 = v1[1], z1 = v1[2];
        float x2 = v2[0], y2 = v2[1], z2 = v2[2];
        out[0] = y1 * z2 - z1 * y2;
        out[1] = z1 * x2 - x1 * z2;
        out[2] = x1 * y2 - y1 * x2;
        return out;
    }

    /**
     * 计算两个二维向量的叉积（标量值）。
     * 结果等于 v1.x * v2.y - v1.y * v2.x。
     */
    public static float[] cross2(float[] vec1, float[] vec2) {
        return cross2(vec1, vec2, new float[1]);
    }

    public static float[] cross2(float[] v1, float[] v2, float[] out) {
        if (v1.length < 2 || v2.length < 2) {
            throw new IllegalArgumentException("二维叉积要求向量长度至少为 2");
        }
        requireMinLen(out, 1, "out");
        out[0] = v1[0] * v2[1] - v1[1] * v2[0];
        return out;
    }

    /** 向量长度 */
    public static float length(float[] v) {
        float sum = 0f;
        for (float val : v) sum += val * val;
        return (float) Math.sqrt(sum);
    }

    /** 归一化，返回新数组 */
    public static float[] normalize(float[] v) {
        return normalize(v, new float[v.length]);
    }

    public static float[] normalize(float[] v, float[] out) {
        requireMinLen(out, v.length, "out");
        float sum = 0f;
        for (float val : v) sum += val * val;
        float len = (float) Math.sqrt(sum);
        if (len < 1e-12f) throw new ArithmeticException("零向量无法归一化");
        float inv = 1f / len;
        for (int i = 0; i < v.length; i++) out[i] = v[i] * inv;
        return out;
    }

    /** 两个向量之间的距离 */
    public static float distance(float[] a, float[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("向量长度不一致");
        float sum = 0f;
        for (int i = 0; i < a.length; i++) {
            float d = a[i] - b[i];
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    /** 向量线性插值 */
    public static float[] lerp(float[] a, float[] b, float t) {
        return lerp(a, b, t, new float[a.length]);
    }

    public static float[] lerp(float[] a, float[] b, float t, float[] out) {
        if (a.length != b.length) throw new IllegalArgumentException("向量长度不一致");
        requireMinLen(out, a.length, "out");
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i] + t * (b[i] - a[i]);
        }
        return out;
    }

    /** 关于法线 n 的反射向量 */
    public static float[] reflect(float[] v, float[] n) {
        return reflect(v, n, new float[3]);
    }

    public static float[] reflect(float[] v, float[] n, float[] out) {
        if (v.length < 3 || n.length < 3) {
            throw new IllegalArgumentException("向量长度至少3");
        }
        requireMinLen(out, 3, "out");
        float vx = v[0], vy = v[1], vz = v[2];
        float nx = n[0], ny = n[1], nz = n[2];
        float d = vx * nx + vy * ny + vz * nz;
        out[0] = vx - 2f * d * nx;
        out[1] = vy - 2f * d * ny;
        out[2] = vz - 2f * d * nz;
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 四元数构造
    // ═══════════════════════════════════════════════════════════

    /** 返回单位四元数 (0, 0, 0, 1)，表示无旋转 */
    public static float[] quat() {
        return quat(new float[4]);
    }

    public static float[] quat(float[] out) {
        requireMinLen(out, 4, "out");
        out[0] = 0f; out[1] = 0f; out[2] = 0f; out[3] = 1f;
        return out;
    }

    /**
     * 构造四元数 (x, y, z, w)，通常 w 为实部。
     * 调用者需自行归一化。
     */
    public static float[] quat(float x, float y, float z, float w) {
        return quat(x, y, z, w, new float[4]);
    }

    public static float[] quat(float x, float y, float z, float w, float[] out) {
        requireMinLen(out, 4, "out");
        out[0] = x; out[1] = y; out[2] = z; out[3] = w;
        return out;
    }

    /**
     * 从旋转轴和角度构造四元数（轴角表示）。
     *
     * @param axis  单位旋转轴（三维向量）
     * @param angle 旋转角度（弧度）
     * @return 四元数 (x, y, z, w)
     */
    public static float[] quatFromAxisAngle(float[] axis, float angle) {
        return quatFromAxisAngle(axis, angle, new float[4]);
    }

    public static float[] quatFromAxisAngle(float[] axis, float angle, float[] out) {
        if (axis.length < 3) throw new IllegalArgumentException("轴向量长度至少为 3");
        requireMinLen(out, 4, "out");
        float half = angle * 0.5f;
        float s = (float) Math.sin(half);
        float c = (float) Math.cos(half);
        out[0] = axis[0] * s;
        out[1] = axis[1] * s;
        out[2] = axis[2] * s;
        out[3] = c;
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 四元数运算
    // ═══════════════════════════════════════════════════════════

    /** 四元数乘法 q1 * q2 */
    public static float[] quatMul(float[] q1, float[] q2) {
        return quatMul(q1, q2, new float[4]);
    }

    public static float[] quatMul(float[] q1, float[] q2, float[] out) {
        if (q1.length < 4 || q2.length < 4) {
            throw new IllegalArgumentException("四元数长度至少4");
        }
        requireMinLen(out, 4, "out");
        float x1 = q1[0], y1 = q1[1], z1 = q1[2], w1 = q1[3];
        float x2 = q2[0], y2 = q2[1], z2 = q2[2], w2 = q2[3];
        out[0] = w1 * x2 + x1 * w2 + y1 * z2 - z1 * y2;
        out[1] = w1 * y2 - x1 * z2 + y1 * w2 + z1 * x2;
        out[2] = w1 * z2 + x1 * y2 - y1 * x2 + z1 * w2;
        out[3] = w1 * w2 - x1 * x2 - y1 * y2 - z1 * z2;
        return out;
    }

    /** 四元数共轭 */
    public static float[] quatConjugate(float[] q) {
        return quatConjugate(q, new float[4]);
    }

    public static float[] quatConjugate(float[] q, float[] out) {
        if (q.length < 4) throw new IllegalArgumentException("四元数长度至少4");
        requireMinLen(out, 4, "out");
        out[0] = -q[0];
        out[1] = -q[1];
        out[2] = -q[2];
        out[3] =  q[3];
        return out;
    }

    /** 四元数归一化 */
    public static float[] quatNormalize(float[] q) {
        return quatNormalize(q, new float[4]);
    }

    public static float[] quatNormalize(float[] q, float[] out) {
        if (q.length < 4) throw new IllegalArgumentException("四元数长度至少4");
        requireMinLen(out, 4, "out");
        float norm = (float) Math.sqrt(q[0]*q[0] + q[1]*q[1] + q[2]*q[2] + q[3]*q[3]);
        if (norm < 1e-12f) throw new ArithmeticException("四元数模为零");
        float inv = 1f / norm;
        out[0] = q[0] * inv;
        out[1] = q[1] * inv;
        out[2] = q[2] * inv;
        out[3] = q[3] * inv;
        return out;
    }

    /** 用四元数旋转三维向量 */
    public static float[] quatRotateVec3(float[] q, float[] v) {
        return quatRotateVec3(q, v, new float[3]);
    }

    public static float[] quatRotateVec3(float[] q, float[] v, float[] out) {
        if (q.length < 4 || v.length < 3) {
            throw new IllegalArgumentException("四元数长度至少4，向量长度至少3");
        }
        requireMinLen(out, 3, "out");
        float x = v[0], y = v[1], z = v[2];
        float qx = q[0], qy = q[1], qz = q[2], qw = q[3];
        float ix = qw * x + qy * z - qz * y;
        float iy = qw * y + qz * x - qx * z;
        float iz = qw * z + qx * y - qy * x;
        float iw = -qx * x - qy * y - qz * z;
        out[0] = ix * qw + iw * -qx + iy * -qz - iz * -qy;
        out[1] = iy * qw + iw * -qy + iz * -qx - ix * -qz;
        out[2] = iz * qw + iw * -qz + ix * -qy - iy * -qx;
        return out;
    }

    /** 四元数球面插值 */
    public static float[] quatSlerp(float[] q1, float[] q2, float t) {
        return quatSlerp(q1, q2, t, new float[4]);
    }

    public static float[] quatSlerp(float[] q1, float[] q2, float t, float[] out) {
        if (q1.length < 4 || q2.length < 4) {
            throw new IllegalArgumentException("四元数长度至少4");
        }
        requireMinLen(out, 4, "out");

        float n1 = (float) Math.sqrt(q1[0]*q1[0] + q1[1]*q1[1] + q1[2]*q1[2] + q1[3]*q1[3]);
        float n2 = (float) Math.sqrt(q2[0]*q2[0] + q2[1]*q2[1] + q2[2]*q2[2] + q2[3]*q2[3]);
        if (n1 < 1e-12f || n2 < 1e-12f) throw new ArithmeticException("四元数模为零");
        float i1 = 1f / n1, i2 = 1f / n2;
        float x1 = q1[0]*i1, y1 = q1[1]*i1, z1 = q1[2]*i1, w1 = q1[3]*i1;
        float x2 = q2[0]*i2, y2 = q2[1]*i2, z2 = q2[2]*i2, w2 = q2[3]*i2;

        float dot = x1*x2 + y1*y2 + z1*z2 + w1*w2;
        if (dot < 0f) {
            x2 = -x2; y2 = -y2; z2 = -z2; w2 = -w2;
            dot = -dot;
        }
        dot = Math.min(1f, Math.max(-1f, dot));
        float theta = (float) Math.acos(dot);
        float sinTheta = (float) Math.sin(theta);

        if (sinTheta < 1e-12f) {
            out[0] = (1f - t) * x1 + t * x2;
            out[1] = (1f - t) * y1 + t * y2;
            out[2] = (1f - t) * z1 + t * z2;
            out[3] = (1f - t) * w1 + t * w2;
            return out;
        }

        float wA = (float) Math.sin((1f - t) * theta) / sinTheta;
        float wB = (float) Math.sin(t * theta) / sinTheta;
        out[0] = wA * x1 + wB * x2;
        out[1] = wA * y1 + wB * y2;
        out[2] = wA * z1 + wB * z2;
        out[3] = wA * w1 + wB * w2;
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 4x4 矩阵（列优先）
    // ═══════════════════════════════════════════════════════════

    /**
     * 构造一个 4x4 矩阵，显式指定 16 个元素（列优先顺序）。
     *
     * <p>参数顺序依次为：第0列4个元素，第1列4个元素，第2列4个元素，第3列4个元素。
     * 每个列中的元素按行 0→3 排列。
     *
     * <p>例如，矩阵：
     * <pre>
     * [ a00  a01  a02  a03 ]
     * [ a10  a11  a12  a13 ]
     * [ a20  a21  a22  a23 ]
     * [ a30  a31  a32  a33 ]
     * </pre>
     * 按列优先存储为
     * [a00, a10, a20, a30, a01, a11, a21, a31, a02, a12, a22, a32, a03, a13, a23, a33]。
     */
    public static float[] mat4(
            float m00, float m10, float m20, float m30,
            float m01, float m11, float m21, float m31,
            float m02, float m12, float m22, float m32,
            float m03, float m13, float m23, float m33
    ) {
        return new float[]{
                m00, m10, m20, m30,
                m01, m11, m21, m31,
                m02, m12, m22, m32,
                m03, m13, m23, m33
        };
    }

    /** 4x4 单位矩阵 */
    public static float[] mat4Identity() {
        return mat4Identity(new float[16]);
    }

    public static float[] mat4Identity(float[] out) {
        requireMinLen(out, 16, "out");
        out[0]=1f;  out[1]=0f;  out[2]=0f;  out[3]=0f;
        out[4]=0f;  out[5]=1f;  out[6]=0f;  out[7]=0f;
        out[8]=0f;  out[9]=0f;  out[10]=1f; out[11]=0f;
        out[12]=0f; out[13]=0f; out[14]=0f; out[15]=1f;
        return out;
    }

    @Deprecated
    public static float[] mat4inv(float[] mat) {
        return mat4Inv(mat);
    }

    /**
     * 计算 4x4 矩阵的逆矩阵（列优先存储），使用伴随矩阵 / 行列式公式。
     * 完全基于 float[] 操作，无中间二维数组。
     *
     * @param mat 输入矩阵（长度至少 16，列优先）
     * @return 逆矩阵（新的 16 元素 float 数组，列优先）
     * @throws IllegalArgumentException 如果输入长度不足 16
     * @throws ArithmeticException     如果矩阵奇异（行列式接近零）
     */
    public static float[] mat4Inv(float[] mat) {
        return mat4Inv(mat, new float[16]);
    }

    public static float[] mat4Inv(float[] mat, float[] out) {
        if (mat.length < 16) throw new IllegalArgumentException("矩阵长度必须至少为 16");
        requireMinLen(out, 16, "out");
        if (out == mat) {
            throw new IllegalArgumentException("mat4Inv 的 out 不能与输入矩阵相同");
        }
        FloatsHolder holder = PoolManagers.floats().getHolder();
        FloatsPool pool = holder.getPool(9);
        float[] minor = pool.acquire();
        try {
            float det = 0.0f;
            for (int j = 0; j < 4; j++) {
                det += mat[j * 4] * cofactor4(mat, 0, j, minor);
            }
            if (Math.abs(det) < 1e-12f) {
                throw new ArithmeticException("矩阵奇异，无法求逆（行列式接近零）");
            }
            float invDet = 1.0f / det;
            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 4; row++) {
                    out[col * 4 + row] = cofactor4(mat, row, col, minor) * invDet;
                }
            }
            return out;
        } finally {
            pool.release(minor);
        }
    }

    /**
     * 计算元素 (row, col) 的代数余子式。
     * 使用外部提供的临时数组 minor（长度 9）填充余子矩阵，避免内存分配。
     *
     * @param mat   4x4 列优先矩阵
     * @param row   0~3
     * @param col   0~3
     * @param minor 长度为 9 的 float[]，用于临时存储 3x3 余子矩阵（行优先）
     * @return 代数余子式值
     */
    private static float cofactor4(float[] mat, int row, int col, float[] minor) {
        int idx = 0;
        for (int i = 0; i < 4; i++) {
            if (i == row) continue;
            for (int j = 0; j < 4; j++) {
                if (j == col) continue;
                minor[idx++] = mat[j * 4 + i]; // 列优先读取
            }
        }
        float detMinor = det3(minor);
        return ((row + col) % 2 == 0) ? detMinor : -detMinor;
    }

    /** 计算 3x3 矩阵的行列式（行优先存储） */
    private static float det3(float[] m) {
        return m[0] * (m[4] * m[8] - m[5] * m[7])
                - m[1] * (m[3] * m[8] - m[5] * m[6])
                + m[2] * (m[3] * m[7] - m[4] * m[6]);
    }

    /** 4x4 矩阵乘法：result = a * b（列优先） */
    public static float[] mat4Mul(float[] a, float[] b) {
        return mat4Mul(a, b, new float[16]);
    }

    public static float[] mat4Mul(float[] a, float[] b, float[] out) {
        if (a.length < 16 || b.length < 16) {
            throw new IllegalArgumentException("矩阵长度必须至少为 16");
        }
        requireMinLen(out, 16, "out");
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                float sum = 0f;
                for (int k = 0; k < 4; k++) {
                    sum += a[k * 4 + row] * b[col * 4 + k];
                }
                out[col * 4 + row] = sum;
            }
        }
        return out;
    }

    /** 4x4 矩阵乘以 4D 向量（列向量） */
    public static float[] mat4MulVec4(float[] m, float[] v) {
        return mat4MulVec4(m, v, new float[4]);
    }

    public static float[] mat4MulVec4(float[] m, float[] v, float[] out) {
        if (m.length < 16 || v.length < 4) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少4");
        }
        requireMinLen(out, 4, "out");
        // 先把 v 读入局部变量，允许 out == v
        float v0 = v[0], v1 = v[1], v2 = v[2], v3 = v[3];
        for (int row = 0; row < 4; row++) {
            out[row] = m[row] * v0
                    + m[4 + row] * v1
                    + m[8 + row] * v2
                    + m[12 + row] * v3;
        }
        return out;
    }

    /**
     * mat4 矩阵是否是仿射变换
     */
    public static boolean mat4IsAffine(float[] m) {
        if (m.length < 16) {
            throw new IllegalArgumentException("矩阵长度至少16");
        }
        // 列主序下：m[3], m[7], m[11], m[15] 是矩阵最后一行
        return Math.abs(m[3])  <= 1e-6
                && Math.abs(m[7])  <= 1e-6
                && Math.abs(m[11]) <= 1e-6
                && Math.abs(m[15] - 1F) <= 1e-6;
    }

    public static float[] mat4MulVec3(float[] m, float[] v) {
        return mat4MulVec3(m, v, new float[3]);
    }

    public static float[] mat4MulVec3(float[] m, float[] v, float[] out) {
        if (m.length < 16 || v.length < 3) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少3");
        }
        requireMinLen(out, 3, "out");
        float v0 = v[0], v1 = v[1], v2 = v[2];
        float x = m[0] * v0 + m[4] * v1 + m[8]  * v2 + m[12];
        float y = m[1] * v0 + m[5] * v1 + m[9]  * v2 + m[13];
        float z = m[2] * v0 + m[6] * v1 + m[10] * v2 + m[14];
        float w = m[3] * v0 + m[7] * v1 + m[11] * v2 + m[15];
        if (Math.abs(w) < 1e-12f) throw new ArithmeticException("齐次坐标 w 接近零，无法投影");
        float invW = 1f / w;
        out[0] = x * invW;
        out[1] = y * invW;
        out[2] = z * invW;
        return out;
    }
    public static float[] mat4MulVec3Affine(float[] m, float[] v) {
        return mat4MulVec3Affine(m, v, new float[3]);
    }

    /**
     * 用仿射 4×4 矩阵变换 3D 点：隐含输入 (v0, v1, v2, 1)，
     * 不做齐次除法（w 恒为 1）。
     *
     * <p>调用者需保证 m 是仿射矩阵（最后一行 [0,0,0,1]）。
     * 如果 m 含投影，结果无意义，请改用 {@link #mat4MulVec3}。

     * 4x4 仿射矩阵乘以 3D 方向向量（隐式 w=1） */
    public static float[] mat4MulVec3Affine(float[] m, float[] v, float[] out) {
        if (m.length < 16 || v.length < 3) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少3");
        }
        requireMinLen(out, 3, "out");

        // 先取出，避免 out == v 时被覆盖
        float v0 = v[0];
        float v1 = v[1];
        float v2 = v[2];

        // 列主序：m[0..3] 第1列, m[4..7] 第2列, m[8..11] 第3列, m[12..15] 第4列
        out[0] = m[0] * v0 + m[4] * v1 + m[8]  * v2 + m[12];
        out[1] = m[1] * v0 + m[5] * v1 + m[9]  * v2 + m[13];
        out[2] = m[2] * v0 + m[6] * v1 + m[10] * v2 + m[14];

        return out;
    }

    /** 4x4 矩阵乘以 3D 方向向量（隐式 w=0） */
    public static float[] mat4MulDir3(float[] m, float[] v) {
        return mat4MulDir3(m, v, new float[3]);
    }

    public static float[] mat4MulDir3(float[] m, float[] v, float[] out) {
        if (m.length < 16 || v.length < 3) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少3");
        }
        requireMinLen(out, 3, "out");
        float v0 = v[0], v1 = v[1], v2 = v[2];
        out[0] = m[0] * v0 + m[4] * v1 + m[8]  * v2;
        out[1] = m[1] * v0 + m[5] * v1 + m[9]  * v2;
        out[2] = m[2] * v0 + m[6] * v1 + m[10] * v2;
        return out;
    }

    /** 4x4 矩阵转置 */
    public static float[] mat4Transpose(float[] m) {
        return mat4Transpose(m, new float[16]);
    }

    public static float[] mat4Transpose(float[] m, float[] out) {
        if (m.length < 16) throw new IllegalArgumentException("矩阵长度至少16");
        requireMinLen(out, 16, "out");
        if (out == m) {
            // 原地转置：只交换上三角
            for (int row = 0; row < 4; row++) {
                for (int col = row + 1; col < 4; col++) {
                    float t = out[row * 4 + col];
                    out[row * 4 + col] = out[col * 4 + row];
                    out[col * 4 + row] = t;
                }
            }
            return out;
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                out[row * 4 + col] = m[col * 4 + row];
            }
        }
        return out;
    }

    /** 4x4 平移矩阵 */
    public static float[] mat4TranslateMatrix(float tx, float ty, float tz) {
        return mat4TranslateMatrix(tx, ty, tz, new float[16]);
    }

    public static float[] mat4TranslateMatrix(float tx, float ty, float tz, float[] out) {
        mat4Identity(out);
        out[12] = tx; out[13] = ty; out[14] = tz;
        return out;
    }

    /** 4x4 缩放矩阵 */
    public static float[] mat4ScaleMatrix(float sx, float sy, float sz) {
        return mat4ScaleMatrix(sx, sy, sz, new float[16]);
    }

    public static float[] mat4ScaleMatrix(float sx, float sy, float sz, float[] out) {
        mat4Identity(out);
        out[0] = sx; out[5] = sy; out[10] = sz;
        return out;
    }

    /** 绕 X 轴旋转矩阵 */
    public static float[] mat4RotateXMatrix(float angle) {
        return mat4RotateXMatrix(angle, new float[16]);
    }

    public static float[] mat4RotateXMatrix(float angle, float[] out) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        mat4Identity(out);
        out[5] = c;  out[6] = s;
        out[9] = -s; out[10] = c;
        return out;
    }

    /** 绕 Y 轴旋转矩阵 */
    public static float[] mat4RotateYMatrix(float angle) {
        return mat4RotateYMatrix(angle, new float[16]);
    }

    public static float[] mat4RotateYMatrix(float angle, float[] out) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        mat4Identity(out);
        out[0] = c;  out[2] = -s;
        out[8] = s;  out[10] = c;
        return out;
    }

    /** 绕 Z 轴旋转矩阵 */
    public static float[] mat4RotateZMatrix(float angle) {
        return mat4RotateZMatrix(angle, new float[16]);
    }

    public static float[] mat4RotateZMatrix(float angle, float[] out) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        mat4Identity(out);
        out[0] = c;  out[1] = s;
        out[4] = -s; out[5] = c;
        return out;
    }

    /** 绕任意轴旋转矩阵 */
    public static float[] mat4RotateMatrix(float angle, float[] axis) {
        return mat4RotateMatrix(angle, axis, new float[16]);
    }

    public static float[] mat4RotateMatrix(float angle, float[] axis, float[] out) {
        if (axis.length < 3) throw new IllegalArgumentException("轴向量长度至少为3");
        requireMinLen(out, 16, "out");
        float x = axis[0], y = axis[1], z = axis[2];
        float len = (float) Math.sqrt(x*x + y*y + z*z);
        if (len < 1e-12f) throw new IllegalArgumentException("轴向量不能为零");
        float inv = 1f / len;
        x *= inv; y *= inv; z *= inv;
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float t = 1f - c;

        out[0]  = t*x*x + c;     out[1]  = t*x*y + s*z;   out[2]  = t*x*z - s*y;   out[3]  = 0f;
        out[4]  = t*x*y - s*z;   out[5]  = t*y*y + c;     out[6]  = t*y*z + s*x;   out[7]  = 0f;
        out[8]  = t*x*z + s*y;   out[9]  = t*y*z - s*x;   out[10] = t*z*z + c;     out[11] = 0f;
        out[12] = 0f;            out[13] = 0f;            out[14] = 0f;            out[15] = 1f;
        return out;
    }

    /** 透视投影矩阵（列优先），右手坐标系，相机看向 -Z */
    public static float[] mat4PerspectiveMatrix(float fovDeg, float aspect, float near, float far) {
        return mat4PerspectiveMatrix(fovDeg, aspect, near, far, new float[16]);
    }

    public static float[] mat4PerspectiveMatrix(float fovDeg, float aspect, float near, float far, float[] out) {
        requireMinLen(out, 16, "out");
        float f = 1.0f / (float) Math.tan(Math.toRadians(fovDeg) * 0.5f);
        float nf = 1.0f / (near - far);

        out[0] = f / aspect; out[1] = 0f; out[2] = 0f;                 out[3] = 0f;
        out[4] = 0f;         out[5] = f;  out[6] = 0f;                 out[7] = 0f;
        out[8] = 0f;         out[9] = 0f; out[10] = (far + near) * nf; out[11] = -1.0f;
        out[12] = 0f;        out[13] = 0f; out[14] = (2.0f * far * near) * nf; out[15] = 0f;
        return out;
    }

    /** 正交投影矩阵（列优先） */
    public static float[] mat4OrthoMatrix(float left, float right, float bottom, float top,
                                          float near, float far) {
        return mat4OrthoMatrix(left, right, bottom, top, near, far, new float[16]);
    }

    public static float[] mat4OrthoMatrix(float left, float right, float bottom, float top,
                                          float near, float far, float[] out) {
        requireMinLen(out, 16, "out");
        out[0] = 2.0f / (right - left); out[1] = 0f; out[2] = 0f; out[3] = 0f;
        out[4] = 0f; out[5] = 2.0f / (top - bottom); out[6] = 0f; out[7] = 0f;
        out[8] = 0f; out[9] = 0f; out[10] = -2.0f / (far - near); out[11] = 0f;
        out[12] = -(right + left) / (right - left);
        out[13] = -(top + bottom) / (top - bottom);
        out[14] = -(far + near) / (far - near);
        out[15] = 1.0f;
        return out;
    }

    /** 视图矩阵（世界空间 → 相机空间），右手坐标系，相机看向 -Z */
    public static float[] mat4LookAtMatrix(float eyeX, float eyeY, float eyeZ,
                                           float centerX, float centerY, float centerZ,
                                           float upX, float upY, float upZ) {
        return mat4LookAtMatrix(eyeX, eyeY, eyeZ, centerX, centerY, centerZ,
                upX, upY, upZ, new float[16]);
    }

    public static float[] mat4LookAtMatrix(float eyeX, float eyeY, float eyeZ,
                                           float centerX, float centerY, float centerZ,
                                           float upX, float upY, float upZ,
                                           float[] out) {
        requireMinLen(out, 16, "out");
        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;
        float fLen = (float) Math.sqrt(fx*fx + fy*fy + fz*fz);
        if (fLen < 1e-12f) return mat4Identity(out);
        fx /= fLen; fy /= fLen; fz /= fLen;

        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;
        float sLen = (float) Math.sqrt(sx*sx + sy*sy + sz*sz);
        if (sLen < 1e-12f) {
            // 不分配 altUp，用标量分支
            float auxX, auxY, auxZ;
            if (Math.abs(fy) > 0.99f) { auxX = 1f; auxY = 0f; auxZ = 0f; }
            else                       { auxX = 0f; auxY = 1f; auxZ = 0f; }
            sx = auxY * fz - auxZ * fy;
            sy = auxZ * fx - auxX * fz;
            sz = auxX * fy - auxY * fx;
            sLen = (float) Math.sqrt(sx*sx + sy*sy + sz*sz);
        }
        sx /= sLen; sy /= sLen; sz /= sLen;

        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        out[0]  = sx;  out[1]  = ux;  out[2]  = -fx; out[3]  = 0f;
        out[4]  = sy;  out[5]  = uy;  out[6]  = -fy; out[7]  = 0f;
        out[8]  = sz;  out[9]  = uz;  out[10] = -fz; out[11] = 0f;
        out[12] = -(sx*eyeX + sy*eyeY + sz*eyeZ);
        out[13] = -(ux*eyeX + uy*eyeY + uz*eyeZ);
        out[14] =  (fx*eyeX + fy*eyeY + fz*eyeZ);
        out[15] = 1f;
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 2D 仿射矩阵（6 元素，float）
    // 布局：[a b c d tx ty]，列向量约定 p' = M · p
    // ═══════════════════════════════════════════════════════════

    /** 2D 仿射单位矩阵 [1, 0, 0, 1, 0, 0] */
    public static float[] mat2dIdentity() {
        return mat2dIdentity(new float[6]);
    }

    public static float[] mat2dIdentity(float[] out) {
        requireMinLen(out, 6, "out");
        out[0] = 1f; out[1] = 0f; out[2] = 0f;
        out[3] = 1f; out[4] = 0f; out[5] = 0f;
        return out;
    }

    public static boolean mat2dIsIdentity(float[] m) {
        if (m == null || m.length != 6) return false;
        return Math.abs(m[0] - 1.0f) < 1e-6f &&
                Math.abs(m[1]) < 1e-6f &&
                Math.abs(m[2]) < 1e-6f &&
                Math.abs(m[3] - 1.0f) < 1e-6f &&
                Math.abs(m[4]) < 1e-6f &&
                Math.abs(m[5]) < 1e-6f;
    }

    /**
     * 绕 (cx, cy) 旋转 angle 弧度的 2D 仿射矩阵。
     * 等价于 T(cx, cy) · R(angle) · T(-cx, -cy)。
     */
    public static float[] mat2dRotateMatrix(float cx, float cy, float angle) {
        return mat2dRotateMatrix(cx, cy, angle, new float[6]);
    }

    public static float[] mat2dRotateMatrix(float cx, float cy, float angle, float[] out) {
        requireMinLen(out, 6, "out");
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        out[0] = c;  out[1] = s;  out[2] = -s; out[3] = c;
        out[4] = cx * (1f - c) + s * cy;
        out[5] = cy * (1f - c) - s * cx;
        return out;
    }

    /** 以 (cx, cy) 为中心缩放 (sx, sy) 的 2D 仿射矩阵 */
    public static float[] mat2dScaleMatrix(float cx, float cy, float sx, float sy) {
        return mat2dScaleMatrix(cx, cy, sx, sy, new float[6]);
    }

    public static float[] mat2dScaleMatrix(float cx, float cy, float sx, float sy, float[] out) {
        requireMinLen(out, 6, "out");
        out[0] = sx; out[1] = 0f; out[2] = 0f; out[3] = sy;
        out[4] = cx * (1f - sx);
        out[5] = cy * (1f - sy);
        return out;
    }

    /** 沿经过 (px, py)、方向 (dx, dy) 的直线镜像的 2D 仿射矩阵 */
    public static float[] mat2dMirrorMatrix(float px, float py, float dx, float dy) {
        return mat2dMirrorMatrix(px, py, dx, dy, new float[6]);
    }

    public static float[] mat2dMirrorMatrix(float px, float py, float dx, float dy, float[] out) {
        requireMinLen(out, 6, "out");
        float len = (float) Math.hypot(dx, dy);
        if (len < 1e-12f) throw new IllegalArgumentException("mirror axis direction must be non-zero");
        float nx = -dy / len;
        float ny =  dx / len;
        float a = 1f - 2f * nx * nx;
        float b = -2f * nx * ny;
        float d = 1f - 2f * ny * ny;
        out[0] = a; out[1] = b; out[2] = b; out[3] = d;
        out[4] = px - (a * px + b * py);
        out[5] = py - (b * px + d * py);
        return out;
    }

    /**
     * 以 (cx, cy) 为中心斜切的 2D 仿射矩阵。
     * kx 影响 Y 位移（y' 受 x 影响），ky 影响 X 位移（x' 受 y 影响）。
     */
    public static float[] mat2dSkewMatrix(float cx, float cy, float kx, float ky) {
        return mat2dSkewMatrix(cx, cy, kx, ky, new float[6]);
    }

    public static float[] mat2dSkewMatrix(float cx, float cy, float kx, float ky, float[] out) {
        requireMinLen(out, 6, "out");
        out[0] = 1f;  out[1] = kx; out[2] = ky; out[3] = 1f;
        out[4] = -ky * cy;
        out[5] = -kx * cx;
        return out;
    }

    /**
     * 从平移、缩放、旋转构造 2D 仿射矩阵（列向量约定：p' = M * p）。
     * 变换顺序：先缩放，再旋转，最后平移。
     */
    public static float[] mat2dCompose(float x, float y,
                                       float scaleX, float scaleY,
                                       float rotation) {
        return mat2dCompose(x, y, scaleX, scaleY, rotation, new float[6]);
    }

    public static float[] mat2dCompose(float x, float y, float scaleX, float scaleY,
                                       float rotation, float[] out) {
        requireMinLen(out, 6, "out");
        float cosR = (float) Math.cos(rotation);
        float sinR = (float) Math.sin(rotation);
        out[0] = scaleX * cosR;
        out[1] = scaleX * sinR;
        out[2] = scaleY * (-sinR);
        out[3] = scaleY * cosR;
        out[4] = x;
        out[5] = y;
        return out;
    }

    /**
     * 构造 2D 仿射变换的逆矩阵（世界 → 本地）。
     * 缩放为 0 时使用 1e-6 避免除零异常（近似逆，仅用于避免崩溃）。
     */
    public static float[] mat2dComposeInverse(float x, float y,
                                              float scaleX, float scaleY,
                                              float rotation) {
        return mat2dComposeInverse(x, y, scaleX, scaleY, rotation, new float[6]);
    }

    public static float[] mat2dComposeInverse(float x, float y, float scaleX, float scaleY,
                                              float rotation, float[] out) {
        requireMinLen(out, 6, "out");
        float cosR = (float) Math.cos(rotation);
        float sinR = (float) Math.sin(rotation);
        float invSX = 1.0f / (scaleX == 0f ? 1e-6f : scaleX);
        float invSY = 1.0f / (scaleY == 0f ? 1e-6f : scaleY);
        out[0] = cosR * invSX;
        out[1] = -sinR * invSY;
        out[2] = sinR * invSX;
        out[3] = cosR * invSY;
        out[4] = -(cosR * x + sinR * y) * invSX;
        out[5] =  (sinR * x - cosR * y) * invSY;
        return out;
    }

    /**
     * 从 2D 仿射矩阵中提取 X 轴缩放因子。
     * 矩阵格式：[a, b, c, d, tx, ty]，scaleX = sqrt(a² + b²)
     */
    public static float mat2dScaleX(float[] m) {
        return (float) Math.sqrt(m[0] * m[0] + m[1] * m[1]);
    }

    /**
     * 从 2D 仿射矩阵中提取 Y 轴缩放因子。
     * 矩阵格式：[a, b, c, d, tx, ty]，scaleY = sqrt(c² + d²)
     */
    public static float mat2dScaleY(float[] m) {
        return (float) Math.sqrt(m[2] * m[2] + m[3] * m[3]);
    }

    /**
     * 从 2D 仿射矩阵中提取 X 轴缩放因子。
     *
     * @deprecated 名字有歧义（与加法无关），已由 {@link #mat2dScaleX} 取代。
     */
    @Deprecated
    public static float add(float[] m) {
        return mat2dScaleX(m);
    }

    /** 两个 2D 仿射矩阵相乘：result = a * b（列向量约定，先 b 后 a） */
    public static float[] mat2dMul(float[] a, float[] b) {
        return mat2dMul(a, b, new float[6]);
    }

    public static float[] mat2dMul(float[] a, float[] b, float[] out) {
        if (a.length < 6 || b.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        requireMinLen(out, 6, "out");
        float a1 = a[0], b1 = a[1], c1 = a[2], d1 = a[3], tx1 = a[4], ty1 = a[5];
        float a2 = b[0], b2 = b[1], c2 = b[2], d2 = b[3], tx2 = b[4], ty2 = b[5];
        out[0] = a1 * a2 + c1 * b2;
        out[1] = b1 * a2 + d1 * b2;
        out[2] = a1 * c2 + c1 * d2;
        out[3] = b1 * c2 + d1 * d2;
        out[4] = a1 * tx2 + c1 * ty2 + tx1;
        out[5] = b1 * tx2 + d1 * ty2 + ty1;
        return out;
    }

    /**
     * 计算 2D 仿射矩阵的逆矩阵。
     * 若矩阵奇异（行列式接近 0），返回 null。
     */
    public static float[] mat2dInv(float[] m) {
        float[] out = new float[6];
        return mat2dInv(m, out) ? out : null;
    }

    /**
     * 计算 2D 仿射矩阵的逆并写入 out。
     *
     * @return true 表示成功写入 out；false 表示矩阵奇异，out 未被修改。
     */
    public static boolean mat2dInv(float[] m, float[] out) {
        if (m.length < 6) throw new IllegalArgumentException("矩阵长度必须至少为 6");
        requireMinLen(out, 6, "out");
        float a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        double det = (double) a * d - (double) b * c;
        if (Math.abs(det) < 1e-10) return false;
        double invDet = 1.0 / det;
        out[0] = (float) (d * invDet);
        out[1] = (float) (-b * invDet);
        out[2] = (float) (-c * invDet);
        out[3] = (float) (a * invDet);
        out[4] = (float) ((c * ty - d * tx) * invDet);
        out[5] = (float) ((b * tx - a * ty) * invDet);
        return true;
    }

    /** 用 2D 仿射矩阵变换点 (x, y) */
    public static float[] mat2dTransformPoint(float[] m, float x, float y) {
        return mat2dTransformPoint(m, x, y, new float[2]);
    }

    public static float[] mat2dTransformPoint(float[] m, float x, float y, float[] out) {
        if (m.length < 6) throw new IllegalArgumentException("矩阵长度必须至少为 6");
        requireMinLen(out, 2, "out");
        float a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        out[0] = a * x + c * y + tx;
        out[1] = b * x + d * y + ty;
        return out;
    }

    /** 将 2D 仿射矩阵扩展为 4x4 列优先矩阵（z=0，w=1） */
    public static float[] mat2dToMat4(float[] m2) {
        return mat2dToMat4(m2, new float[16]);
    }

    public static float[] mat2dToMat4(float[] m2, float[] out) {
        if (m2.length < 6) throw new IllegalArgumentException("矩阵长度必须至少为 6");
        requireMinLen(out, 16, "out");
        out[0]  = m2[0]; out[1]  = m2[1]; out[2]  = 0f; out[3]  = 0f;
        out[4]  = m2[2]; out[5]  = m2[3]; out[6]  = 0f; out[7]  = 0f;
        out[8]  = 0f;    out[9]  = 0f;    out[10] = 1f; out[11] = 0f;
        out[12] = m2[4]; out[13] = m2[5]; out[14] = 0f; out[15] = 1f;
        return out;
    }

    /** 将 4x4 矩阵（列优先）转换为 2D 仿射矩阵 */
    public static float[] mat4ToMat2d(float[] m4) {
        if (m4.length < 16) return null;
        return mat4ToMat2d(m4, new float[6]);
    }

    public static float[] mat4ToMat2d(float[] m4, float[] out) {
        requireMinLen(out, 6, "out");
        if (m4.length < 16) throw new IllegalArgumentException("矩阵长度必须至少为 16");
        out[0] = m4[0];  out[1] = m4[1];
        out[2] = m4[4];  out[3] = m4[5];
        out[4] = m4[12]; out[5] = m4[13];
        return out;
    }

    // ═══════════════════════════════════════════════════════════
    // 2D 仿射矩阵（6 元素，double）
    // ═══════════════════════════════════════════════════════════

    /** 2D 仿射单位矩阵 [1, 0, 0, 1, 0, 0]（double） */
    public static double[] mat2dIdentityD() {
        return new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
    }

    public static boolean mat2dIsIdentityD(double[] m) {
        if (m == null || m.length != 6) return false;
        return Math.abs(m[0] - 1.0) < 1e-9 &&
                Math.abs(m[1]) < 1e-9 &&
                Math.abs(m[2]) < 1e-9 &&
                Math.abs(m[3] - 1.0) < 1e-9 &&
                Math.abs(m[4]) < 1e-9 &&
                Math.abs(m[5]) < 1e-9;
    }

    public static double[] mat2dRotateMatrixD(double cx, double cy, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new double[]{
                c,  s,  -s, c,
                cx * (1.0 - c) + s * cy,
                cy * (1.0 - c) - s * cx
        };
    }

    public static double[] mat2dScaleMatrixD(double cx, double cy, double sx, double sy) {
        return new double[]{
                sx, 0.0, 0.0, sy,
                cx * (1.0 - sx),
                cy * (1.0 - sy)
        };
    }

    public static double[] mat2dMirrorMatrixD(double px, double py, double dx, double dy) {
        double len = Math.hypot(dx, dy);
        if (len < 1e-12) {
            throw new IllegalArgumentException("mirror axis direction must be non-zero");
        }
        double nx = -dy / len;
        double ny =  dx / len;
        double a  = 1.0 - 2.0 * nx * nx;
        double b  = -2.0 * nx * ny;
        double d  = 1.0 - 2.0 * ny * ny;
        return new double[]{
                a, b, b, d,
                px - (a * px + b * py),
                py - (b * px + d * py)
        };
    }

    public static double[] mat2dSkewMatrixD(double cx, double cy, double kx, double ky) {
        return new double[]{
                1.0, kx, ky, 1.0,
                -ky * cy,
                -kx * cx
        };
    }

    /**
     * 从平移、缩放、旋转构造 2D 仿射矩阵（double，列向量约定：p' = M * p）。
     * 变换顺序：先缩放，再旋转，最后平移。
     */
    public static double[] mat2dComposeD(double x, double y,
                                         double scaleX, double scaleY,
                                         double rotation) {
        double cosR = Math.cos(rotation);
        double sinR = Math.sin(rotation);
        return new double[]{
                scaleX * cosR,          // a
                scaleX * sinR,          // b
                scaleY * (-sinR),       // c
                scaleY * cosR,          // d
                x,                      // tx
                y                       // ty
        };
    }

    /**
     * 构造 2D 仿射变换的逆矩阵（double，世界 → 本地）。
     * 缩放为 0 时使用 1e-12 避免除零异常（近似逆，仅用于避免崩溃）。
     */
    public static double[] mat2dComposeInverseD(double x, double y,
                                                double scaleX, double scaleY,
                                                double rotation) {
        double cosR = Math.cos(rotation);
        double sinR = Math.sin(rotation);
        double invSX = 1.0 / (scaleX == 0.0 ? 1e-12 : scaleX);
        double invSY = 1.0 / (scaleY == 0.0 ? 1e-12 : scaleY);
        return new double[]{
                cosR * invSX,                       // a
                -sinR * invSY,                      // b
                sinR * invSX,                       // c
                cosR * invSY,                       // d
                -(cosR * x + sinR * y) * invSX,     // tx
                (sinR * x - cosR * y) * invSY       // ty
        };
    }

    /**
     * 从 2D 仿射矩阵中提取 X 轴缩放因子（double）。
     * 矩阵格式：[a, b, c, d, tx, ty]，scaleX = sqrt(a² + b²)
     */
    public static double mat2dScaleXD(double[] m) {
        return Math.sqrt(m[0] * m[0] + m[1] * m[1]);
    }

    /**
     * 从 2D 仿射矩阵中提取 Y 轴缩放因子（double）。
     * 矩阵格式：[a, b, c, d, tx, ty]，scaleY = sqrt(c² + d²)
     */
    public static double mat2dScaleYD(double[] m) {
        return Math.sqrt(m[2] * m[2] + m[3] * m[3]);
    }

    /** 两个 2D 仿射矩阵相乘（double）：result = a * b（列向量约定，先 b 后 a） */
    public static double[] mat2dMulD(double[] a, double[] b) {
        if (a.length < 6 || b.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        double a1 = a[0], b1 = a[1], c1 = a[2], d1 = a[3], tx1 = a[4], ty1 = a[5];
        double a2 = b[0], b2 = b[1], c2 = b[2], d2 = b[3], tx2 = b[4], ty2 = b[5];
        return new double[]{
                a1 * a2 + c1 * b2,          // a
                b1 * a2 + d1 * b2,          // b
                a1 * c2 + c1 * d2,          // c
                b1 * c2 + d1 * d2,          // d
                a1 * tx2 + c1 * ty2 + tx1,  // tx
                b1 * tx2 + d1 * ty2 + ty1   // ty
        };
    }

    /**
     * 计算 2D 仿射矩阵的逆矩阵（double）。
     * 若矩阵奇异（行列式接近 0），返回 null。
     */
    public static double[] mat2dInvD(double[] m) {
        if (m.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        double a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        double det = a * d - b * c;
        if (Math.abs(det) < 1e-18) {
            return null;
        }
        double invDet = 1.0 / det;
        return new double[]{
                d  * invDet,                // aInv
                -b * invDet,                // bInv
                -c * invDet,                // cInv
                a  * invDet,                // dInv
                (c * ty - d * tx) * invDet, // txInv
                (b * tx - a * ty) * invDet  // tyInv
        };
    }

    /** 用 2D 仿射矩阵变换点 (x, y)（double） */
    public static double[] mat2dTransformPointD(double[] m, double x, double y) {
        if (m.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        double a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        return new double[]{
                a * x + c * y + tx,
                b * x + d * y + ty
        };
    }

    /**
     * 用 2D 仿射矩阵变换方向向量 (dx, dy)（double）。
     * 忽略平移部分——方向向量不受平移影响。
     */
    public static double[] mat2dTransformDirectionD(double[] m, double dx, double dy) {
        if (m.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        double a = m[0], b = m[1], c = m[2], d = m[3];
        return new double[]{
                a * dx + c * dy,
                b * dx + d * dy
        };
    }

    /** 将 2D 仿射矩阵扩展为 4x4 列优先矩阵（double，z=0，w=1） */
    public static double[] mat2dToMat4D(double[] m2) {
        if (m2.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        double a = m2[0], b = m2[1], c = m2[2], d = m2[3], tx = m2[4], ty = m2[5];
        return new double[]{
                a, b, 0.0, 0.0,     // col0
                c, d, 0.0, 0.0,     // col1
                0.0, 0.0, 1.0, 0.0, // col2
                tx, ty, 0.0, 1.0    // col3
        };
    }

    /** 将 4x4 矩阵（列优先，double）转换为 2D 仿射矩阵 */
    public static double[] mat4ToMat2dD(double[] m4) {
        if (m4.length < 16) return null;
        return new double[]{
                m4[0], m4[1],  // a, b
                m4[4], m4[5],  // c, d
                m4[12], m4[13] // tx, ty
        };
    }

    // ═══════════════════════════════════════════════════════════
    // 线段相交（float）
    // ═══════════════════════════════════════════════════════════

    public static class SegmentInterceptionResult {
        private final boolean intercepted;
        private final float x;
        private final float y;

        private SegmentInterceptionResult(boolean intercepted, float x, float y) {
            this.intercepted = intercepted;
            this.x = x;
            this.y = y;
        }

        public static SegmentInterceptionResult hit(float x, float y) {
            return new SegmentInterceptionResult(true, x, y);
        }

        private static final SegmentInterceptionResult MISS = new SegmentInterceptionResult(false, 0, 0);

        public static SegmentInterceptionResult miss() {
            return MISS;
        }

        public boolean isIntercepted() {
            return intercepted;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public static SegmentInterceptionResult segmentIntersection(float x1, float y1, float x2, float y2,
                                                                float x3, float y3, float x4, float y4) {
        float d1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        float d2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        float d3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        float d4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);

        if ((d1 > 0 && d2 < 0 || d1 < 0 && d2 > 0) &&
                (d3 > 0 && d4 < 0 || d3 < 0 && d4 > 0)) {
            float t = d1 / (d1 - d2);
            float x = x1 + t * (x2 - x1);
            float y = y1 + t * (y2 - y1);
            return SegmentInterceptionResult.hit(x, y);
        }

        if (Math.abs(d1) < 1e-6f && isPointOnSegment(x1, y1, x3, y3, x4, y4)) {
            return SegmentInterceptionResult.hit(x1, y1);
        }
        if (Math.abs(d2) < 1e-6f && isPointOnSegment(x2, y2, x3, y3, x4, y4)) {
            return SegmentInterceptionResult.hit(x2, y2);
        }
        if (Math.abs(d3) < 1e-6f && isPointOnSegment(x3, y3, x1, y1, x2, y2)) {
            return SegmentInterceptionResult.hit(x3, y3);
        }
        if (Math.abs(d4) < 1e-6f && isPointOnSegment(x4, y4, x1, y1, x2, y2)) {
            return SegmentInterceptionResult.hit(x4, y4);
        }

        return SegmentInterceptionResult.miss();
    }

    public static boolean isPointOnSegment(float px, float py,
                                           float ax, float ay,
                                           float bx, float by) {
        float dx = bx - ax;
        float dy = by - ay;
        float len2 = dx*dx + dy*dy;
        if (len2 < 1e-12f) {
            return Math.abs(px - ax) < 1e-6f && Math.abs(py - ay) < 1e-6f;
        }
        float t = ((px - ax)*dx + (py - ay)*dy) / len2;
        return t >= 0 && t <= 1 &&
                Math.abs((px - ax) - t*dx) < 1e-6f &&
                Math.abs((py - ay) - t*dy) < 1e-6f;
    }

    public static class LineIntersectionResult {
        private final boolean intersected;
        private final float x;
        private final float y;

        private LineIntersectionResult(boolean intersected, float x1, float y1) {
            this.intersected = intersected;
            this.x = x1;
            this.y = y1;
        }

        public boolean isIntersected() {
            return intersected;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public static LineIntersectionResult hit(float x, float y) {
            return new LineIntersectionResult(true, x, y);
        }

        private static final LineIntersectionResult MISS = new LineIntersectionResult(false, 0, 0);

        public static LineIntersectionResult miss() {
            return MISS;
        }
    }

    /**
     * 两条参数直线求交：
     *   L1: P1 + t * d1
     *   L2: P2 + s * d2
     */
    public static LineIntersectionResult lineIntersection(
            float px1, float py1, float dx1, float dy1,
            float px2, float py2, float dx2, float dy2) {
        float denom = dx1 * dy2 - dy1 * dx2;
        if (Math.abs(denom) < 1e-12) return LineIntersectionResult.miss();
        float t = ((px2 - px1) * dy2 - (py2 - py1) * dx2) / denom;
        float hitX = px1 + t * dx1;
        float hitY = py1 + t * dy1;
        return LineIntersectionResult.hit(hitX, hitY);
    }

    // ═══════════════════════════════════════════════════════════
    // 线段相交（double）
    // ═══════════════════════════════════════════════════════════

    public static class SegmentIntersectionResultD {
        private final boolean intersected;
        private final double x;
        private final double y;

        private SegmentIntersectionResultD(boolean intersected, double x, double y) {
            this.intersected = intersected;
            this.x = x;
            this.y = y;
        }

        public static SegmentIntersectionResultD hit(double x, double y) {
            return new SegmentIntersectionResultD(true, x, y);
        }

        private static final SegmentIntersectionResultD MISS =
                new SegmentIntersectionResultD(false, 0, 0);

        public static SegmentIntersectionResultD miss() {
            return MISS;
        }

        public boolean isIntersected() {
            return intersected;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public static SegmentIntersectionResultD segmentIntersectionD(
            double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4) {
        double d1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        double d2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        double d3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        double d4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);

        if ((d1 > 0 && d2 < 0 || d1 < 0 && d2 > 0) &&
                (d3 > 0 && d4 < 0 || d3 < 0 && d4 > 0)) {
            double t = d1 / (d1 - d2);
            double x = x1 + t * (x2 - x1);
            double y = y1 + t * (y2 - y1);
            return SegmentIntersectionResultD.hit(x, y);
        }

        if (Math.abs(d1) < 1e-12 && isPointOnSegmentD(x1, y1, x3, y3, x4, y4)) {
            return SegmentIntersectionResultD.hit(x1, y1);
        }
        if (Math.abs(d2) < 1e-12 && isPointOnSegmentD(x2, y2, x3, y3, x4, y4)) {
            return SegmentIntersectionResultD.hit(x2, y2);
        }
        if (Math.abs(d3) < 1e-12 && isPointOnSegmentD(x3, y3, x1, y1, x2, y2)) {
            return SegmentIntersectionResultD.hit(x3, y3);
        }
        if (Math.abs(d4) < 1e-12 && isPointOnSegmentD(x4, y4, x1, y1, x2, y2)) {
            return SegmentIntersectionResultD.hit(x4, y4);
        }

        return SegmentIntersectionResultD.miss();
    }

    public static boolean isPointOnSegmentD(double px, double py,
                                            double ax, double ay,
                                            double bx, double by) {
        double dx = bx - ax;
        double dy = by - ay;
        double len2 = dx * dx + dy * dy;
        if (len2 < 1e-24) {
            return Math.abs(px - ax) < 1e-12 && Math.abs(py - ay) < 1e-12;
        }
        double t = ((px - ax) * dx + (py - ay) * dy) / len2;
        return t >= 0 && t <= 1
                && Math.abs((px - ax) - t * dx) < 1e-12
                && Math.abs((py - ay) - t * dy) < 1e-12;
    }

    public static class LineIntersectionResultD {
        private final boolean intersected;
        private final double x;
        private final double y;

        private LineIntersectionResultD(boolean intersected, double x1, double y1) {
            this.intersected = intersected;
            this.x = x1;
            this.y = y1;
        }

        public boolean isIntersected() {
            return intersected;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public static LineIntersectionResultD hit(double x, double y) {
            return new LineIntersectionResultD(true, x, y);
        }

        private static final LineIntersectionResultD MISS = new LineIntersectionResultD(false, 0, 0);

        public static LineIntersectionResultD miss() {
            return MISS;
        }
    }

    /**
     * 两条参数直线求交：
     *   L1: P1 + t * d1
     *   L2: P2 + s * d2
     */
    public static LineIntersectionResultD lineIntersectionD(
            double px1, double py1, double dx1, double dy1,
            double px2, double py2, double dx2, double dy2) {
        double denom = dx1 * dy2 - dy1 * dx2;
        if (Math.abs(denom) < 1e-12) return LineIntersectionResultD.miss();
        double t = ((px2 - px1) * dy2 - (py2 - py1) * dx2) / denom;
        double hitX = px1 + t * dx1;
        double hitY = py1 + t * dy1;
        return LineIntersectionResultD.hit(hitX, hitY);
    }
}