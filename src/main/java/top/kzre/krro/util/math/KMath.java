package top.kzre.krro.util.math;

import top.kzre.krro.util.pool.FloatsPools;

/**
 * Krro 数学工具类，不做类型抽象，约定float[] 布局.
 */
public final class KMath {

    private KMath() {} // 禁止实例化

    /**
     * 返回二维零向量 (0, 0)
     */
    public static float[] vec2() {
        return new float[]{0.0f, 0.0f};
    }

    /**
     * 构造二维向量 (x, y)
     */
    public static float[] vec2(float x, float y) {
        return new float[]{x, y};
    }

    /**
     * 返回三维零向量 (0, 0, 0)
     */
    public static float[] vec3() {
        return new float[]{0.0f, 0.0f, 0.0f};
    }

    /**
     * 构造三维向量 (x, y, z)
     */
    public static float[] vec3(float x, float y, float z) {
        return new float[]{x, y, z};
    }

    /**
     * 返回四维零向量 (0, 0, 0, 0)
     */
    public static float[] vec4() {
        return new float[]{0.0f, 0.0f, 0.0f, 0.0f};
    }

    /**
     * 构造四维向量 (x, y, z, w)
     */
    public static float[] vec4(float x, float y, float z, float w) {
        return new float[]{x, y, z, w};
    }

    // ==================== 四元数构造 ====================

    /**
     * 返回单位四元数 (0, 0, 0, 1)，表示无旋转
     */
    public static float[] quat() {
        return new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    }

    /**
     * 构造四元数 (x, y, z, w)，通常 w 为实部。
     * 调用者需自行归一化。
     */
    public static float[] quat(float x, float y, float z, float w) {
        return new float[]{x, y, z, w};
    }

    /**
     * 从旋转轴和角度构造四元数（轴角表示）。
     * @param axis  单位旋转轴（三维向量）
     * @param angle 旋转角度（弧度）
     * @return 四元数 (x, y, z, w)
     */
    public static float[] quatFromAxisAngle(float[] axis, float angle) {
        if (axis.length < 3) {
            throw new IllegalArgumentException("轴向量长度至少为 3");
        }
        float halfAngle = angle * 0.5f;
        float sinHalf = (float) Math.sin(halfAngle);
        float cosHalf = (float) Math.cos(halfAngle);
        return new float[]{
                axis[0] * sinHalf,
                axis[1] * sinHalf,
                axis[2] * sinHalf,
                cosHalf
        };
    }

    /**
     * 计算两个向量的点积（内积）。
     * 支持任意维度，但两个向量长度必须相同。
     *
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 点积结果（标量）
     * @throws IllegalArgumentException 如果向量长度不同
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
     * 返回一个垂直于输入平面的三维向量。
     * 要求两个向量长度至少为 3，取前三个分量参与计算。
     *
     * @param vec1 第一个向量（长度 ≥ 3）
     * @param vec2 第二个向量（长度 ≥ 3）
     * @return 三维向量 [x, y, z] 表示叉积结果
     * @throws IllegalArgumentException 如果任一向量长度小于 3
     */
    public static float[] cross(float[] vec1, float[] vec2) {
        if (vec1.length < 3 || vec2.length < 3) {
            throw new IllegalArgumentException("叉积要求向量长度至少为 3，但收到 " + vec1.length + " 和 " + vec2.length);
        }
        float x1 = vec1[0], y1 = vec1[1], z1 = vec1[2];
        float x2 = vec2[0], y2 = vec2[1], z2 = vec2[2];
        return new float[]{
                y1 * z2 - z1 * y2,
                z1 * x2 - x1 * z2,
                x1 * y2 - y1 * x2
        };
    }

    /**
     * 计算两个二维向量的叉积（标量值）。
     * 结果等于 v1.x * v2.y - v1.y * v2.x。
     * 返回值为有向面积，正数表示逆时针旋转，负数表示顺时针。
     *
     * @param vec1 第一个向量（至少含有两个元素，取前两个）
     * @param vec2 第二个向量（至少含有两个元素，取前两个）
     * @return 长度为1的 float[]，包含叉积标量值
     * @throws IllegalArgumentException 如果任一向量长度小于 2
     */
    public static float[] cross2(float[] vec1, float[] vec2) {
        if (vec1.length < 2 || vec2.length < 2) {
            throw new IllegalArgumentException("二维叉积要求向量长度至少为 2，但收到 " + vec1.length + " 和 " + vec2.length);
        }
        float scalar = vec1[0] * vec2[1] - vec1[1] * vec2[0];
        return new float[]{scalar};
    }

    /**
     * 构造一个 4x4 矩阵，显式指定 16 个元素（列优先顺序）。
     * <p>
     * 参数顺序依次为：第0列4个元素，第1列4个元素，第2列4个元素，第3列4个元素。
     * 每个列中的元素按行 0→3 排列。
     * <p>
     * 例如，矩阵：
     * <pre>
     * [ a00  a01  a02  a03 ]
     * [ a10  a11  a12  a13 ]
     * [ a20  a21  a22  a23 ]
     * [ a30  a31  a32  a33 ]
     * </pre>
     * 按列优先存储为 [a00, a10, a20, a30, a01, a11, a21, a31, a02, a12, a22, a32, a03, a13, a23, a33]。
     *
     * @param m00 第0列第0行
     * @param m10 第0列第1行
     * @param m20 第0列第2行
     * @param m30 第0列第3行
     * @param m01 第1列第0行
     * @param m11 第1列第1行
     * @param m21 第1列第2行
     * @param m31 第1列第3行
     * @param m02 第2列第0行
     * @param m12 第2列第1行
     * @param m22 第2列第2行
     * @param m32 第2列第3行
     * @param m03 第3列第0行
     * @param m13 第3列第1行
     * @param m23 第3列第2行
     * @param m33 第3列第3行
     * @return 包含 16 个 float 的数组（列优先存储）
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


    @Deprecated
    public  static float[] mat4inv(float[] mat){
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
        if (mat.length < 16) {
            throw new IllegalArgumentException("矩阵长度必须至少为 16");
        }

        // 从池中借用一个长度为 9 的临时数组（用于 3x3 余子式）
        float[] minor = FloatsPools.getPool(9).acquire();
        try {
            // 计算行列式（按第一行展开）
            float det = 0.0f;
            for (int j = 0; j < 4; j++) {
                det += mat[j * 4] * cofactor4(mat, 0, j, minor);
            }
            if (Math.abs(det) < 1e-12f) {
                throw new ArithmeticException("矩阵奇异，无法求逆（行列式接近零）");
            }

            // 计算伴随矩阵（列优先存储）
            float[] adj = new float[16];
            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 4; row++) {
                    adj[col * 4 + row] = cofactor4(mat, row, col, minor);
                }
            }

            // 逆矩阵 = adj / det
            float[] inv = new float[16];
            for (int i = 0; i < 16; i++) {
                inv[i] = adj[i] / det;
            }
            return inv;
        } finally {
            // 归还临时数组到池中
            FloatsPools.getPool(9).release(minor);
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

    /**
     * 计算 3x3 矩阵的行列式（行优先存储）
     */
    private static float det3(float[] m) {
        return m[0] * (m[4] * m[8] - m[5] * m[7])
                - m[1] * (m[3] * m[8] - m[5] * m[6])
                + m[2] * (m[3] * m[7] - m[4] * m[6]);
    }

    /**
     * 4x4 矩阵乘法：result = a * b（列优先）
     */
    public static float[] mat4Mul(float[] a, float[] b) {
        if (a.length < 16 || b.length < 16) {
            throw new IllegalArgumentException("矩阵长度必须至少为 16");
        }
        float[] result = new float[16];
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                float sum = 0f;
                for (int k = 0; k < 4; k++) {
                    sum += a[k * 4 + row] * b[col * 4 + k];
                }
                result[col * 4 + row] = sum;
            }
        }
        return result;
    }

    /**
     * 4x4 矩阵乘以 4D 向量（列向量），返回 4D 向量。
     */
    public static float[] mat4MulVec4(float[] m, float[] v) {
        if (m.length < 16 || v.length < 4) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少4");
        }
        float[] result = new float[4];
        for (int row = 0; row < 4; row++) {
            float sum = 0f;
            for (int col = 0; col < 4; col++) {
                sum += m[col * 4 + row] * v[col];
            }
            result[row] = sum;
        }
        return result;
    }

    /**
     * 4x4 矩阵乘以 3D 向量（隐式 w=1），返回 3D 向量（执行透视除法）。
     */
    public static float[] mat4MulVec3(float[] m, float[] v) {
        if (m.length < 16 || v.length < 3) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少3");
        }
        // 计算齐次坐标
        float x = m[0] * v[0] + m[4] * v[1] + m[8] * v[2] + m[12];
        float y = m[1] * v[0] + m[5] * v[1] + m[9] * v[2] + m[13];
        float z = m[2] * v[0] + m[6] * v[1] + m[10] * v[2] + m[14];
        float w = m[3] * v[0] + m[7] * v[1] + m[11] * v[2] + m[15];
        if (Math.abs(w) < 1e-12f) {
            throw new ArithmeticException("齐次坐标 w 接近零，无法投影");
        }
        float invW = 1f / w;
        return new float[]{x * invW, y * invW, z * invW};
    }

    public static float[] mat4Identity() {
        return new float[]{
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        };
    }

    public static float[] mat4Translate(float tx, float ty, float tz) {
        return new float[]{
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                tx, ty, tz, 1f
        };
    }

    public static float[] mat4Scale(float sx, float sy, float sz) {
        return new float[]{
                sx, 0f, 0f, 0f,
                0f, sy, 0f, 0f,
                0f, 0f, sz, 0f,
                0f, 0f, 0f, 1f
        };
    }

    public static float[] mat4RotateX(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        return new float[]{
                1f, 0f, 0f, 0f,
                0f, c,  s,  0f,
                0f, -s, c,  0f,
                0f, 0f, 0f, 1f
        };
    }

    public static float[] mat4RotateY(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        return new float[]{
                c,  0f, -s, 0f,
                0f, 1f, 0f, 0f,
                s,  0f, c,  0f,
                0f, 0f, 0f, 1f
        };
    }

    public static float[] mat4RotateZ(float angle) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        return new float[]{
                c,  s,  0f, 0f,
                -s, c,  0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        };
    }

    public static float[] mat4Rotate(float angle, float[] axis) {
        if (axis.length < 3) {
            throw new IllegalArgumentException("轴向量长度至少为3");
        }
        float x = axis[0], y = axis[1], z = axis[2];
        float len = (float) Math.sqrt(x*x + y*y + z*z);
        if (len < 1e-12f) {
            throw new IllegalArgumentException("轴向量不能为零");
        }
        float invLen = 1f / len;
        x *= invLen; y *= invLen; z *= invLen;

        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float t = 1f - c;

        // 罗德里格旋转矩阵
        return new float[]{
                t*x*x + c,     t*x*y + s*z,   t*x*z - s*y,   0f,
                t*x*y - s*z,   t*y*y + c,     t*y*z + s*x,   0f,
                t*x*z + s*y,   t*y*z - s*x,   t*z*z + c,     0f,
                0f, 0f, 0f, 1f
        };
    }


    public static float[] mat4Transpose(float[] m) {
        if (m.length < 16) throw new IllegalArgumentException("矩阵长度至少16");
        float[] result = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[row * 4 + col] = m[col * 4 + row];
            }
        }
        return result;
    }

    /**
     * 构建一个透视投影矩阵（列优先），右手坐标系，相机看向 -Z。
     *
     * @param fovDeg 视野角度（度数）
     * @param aspect 宽高比（width / height）
     * @param near   近平面距离（正数）
     * @param far    远平面距离（正数）
     * @return 列优先投影矩阵 float[16]
     */
    public static float[] perspective(float fovDeg, float aspect, float near, float far) {
        float f = 1.0f / (float) Math.tan(Math.toRadians(fovDeg) * 0.5f);
        float nf = 1.0f / (near - far);
        float[] m = new float[16];
        m[0] = f / aspect;
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = f;
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = 0.0f;
        m[9] = 0.0f;
        m[10] = (far + near) * nf;
        m[11] = -1.0f;

        m[12] = 0.0f;
        m[13] = 0.0f;
        m[14] = (2.0f * far * near) * nf;
        m[15] = 0.0f;
        return m;
    }

    /**
     * 构建一个正交投影矩阵（列优先），常用于平行投影。
     *
     * @param left   左平面 x 坐标
     * @param right  右平面 x 坐标
     * @param bottom 底平面 y 坐标
     * @param top    顶平面 y 坐标
     * @param near   近平面距离（正数）
     * @param far    远平面距离（正数）
     * @return 列优先正交投影矩阵 float[16]
     */
    public static float[] ortho(float left, float right, float bottom, float top, float near, float far) {
        float[] m = new float[16];
        m[0] = 2.0f / (right - left);
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = 2.0f / (top - bottom);
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = 0.0f;
        m[9] = 0.0f;
        m[10] = -2.0f / (far - near);
        m[11] = 0.0f;

        m[12] = -(right + left) / (right - left);
        m[13] = -(top + bottom) / (top - bottom);
        m[14] = -(far + near) / (far - near);
        m[15] = 1.0f;
        return m;
    }

    /**
     * 构建一个视图矩阵（世界空间 → 相机空间），相机位置、目标点、上方向均在世界坐标系中。
     * 结果矩阵为列优先，右手坐标系，相机看向 -Z。
     *
     * @param eyeX, eyeY, eyeZ       相机位置
     * @param centerX, centerY, centerZ 观察目标点
     * @param upX, upY, upZ          世界空间的上方向（无需归一化）
     * @return 列优先视图矩阵 float[16]
     */
    public static float[] lookAt(float eyeX, float eyeY, float eyeZ,
                                 float centerX, float centerY, float centerZ,
                                 float upX, float upY, float upZ) {
        // 前向量 f = normalize(center - eye)  → 对应相机局部 -Z
        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;
        float fLen = (float) Math.sqrt(fx*fx + fy*fy + fz*fz);
        if (fLen < 1e-12f) return mat4Identity();
        fx /= fLen; fy /= fLen; fz /= fLen;

        // 右向量 s = normalize(f × up)
        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;
        float sLen = (float) Math.sqrt(sx*sx + sy*sy + sz*sz);
        if (sLen < 1e-12f) {
            // up 与 forward 平行，自动选取备用 up
            // 简单处理：若 forward 接近 ±Y，改用 (1,0,0) 作为 up 重新计算
            float[] altUp = (Math.abs(fy) > 0.99f) ? new float[]{1f,0f,0f} : new float[]{0f,1f,0f};
            sx = altUp[1] * fz - altUp[2] * fy;
            sy = altUp[2] * fx - altUp[0] * fz;
            sz = altUp[0] * fy - altUp[1] * fx;
            sLen = (float) Math.sqrt(sx*sx + sy*sy + sz*sz);
        }
        sx /= sLen; sy /= sLen; sz /= sLen;

        // 上向量 u = s × f
        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        // 构建列优先矩阵
        float[] m = new float[16];
        // 第一列：s
        m[0] = sx;  m[1] = ux;  m[2] = -fx; m[3] = 0f;
        // 第二列：u
        m[4] = sy;  m[5] = uy;  m[6] = -fy; m[7] = 0f;
        // 第三列：-f
        m[8] = sz;  m[9] = uz;  m[10]= -fz; m[11]= 0f;
        // 第四列：平移 = - (s·eye, u·eye, -f·eye)
        m[12] = -(sx*eyeX + sy*eyeY + sz*eyeZ);
        m[13] = -(ux*eyeX + uy*eyeY + uz*eyeZ);
        m[14] = (fx*eyeX + fy*eyeY + fz*eyeZ);
        m[15] = 1f;
        return m;
    }

    public static float[] quatMul(float[] q1, float[] q2) {
        if (q1.length < 4 || q2.length < 4) {
            throw new IllegalArgumentException("四元数长度至少4");
        }
        float x1 = q1[0], y1 = q1[1], z1 = q1[2], w1 = q1[3];
        float x2 = q2[0], y2 = q2[1], z2 = q2[2], w2 = q2[3];
        return new float[]{
                w1*x2 + x1*w2 + y1*z2 - z1*y2,
                w1*y2 - x1*z2 + y1*w2 + z1*x2,
                w1*z2 + x1*y2 - y1*x2 + z1*w2,
                w1*w2 - x1*x2 - y1*y2 - z1*z2
        };
    }

    public static float[] quatConjugate(float[] q) {
        if (q.length < 4) throw new IllegalArgumentException("四元数长度至少4");
        return new float[]{-q[0], -q[1], -q[2], q[3]};
    }

    public static float[] quatNormalize(float[] q) {
        if (q.length < 4) throw new IllegalArgumentException("四元数长度至少4");
        float norm = (float) Math.sqrt(q[0]*q[0] + q[1]*q[1] + q[2]*q[2] + q[3]*q[3]);
        if (norm < 1e-12f) {
            throw new ArithmeticException("四元数模为零");
        }
        float invNorm = 1f / norm;
        return new float[]{q[0]*invNorm, q[1]*invNorm, q[2]*invNorm, q[3]*invNorm};
    }

    public static float[] quatRotateVec3(float[] q, float[] v) {
        if (q.length < 4 || v.length < 3) {
            throw new IllegalArgumentException("四元数长度至少4，向量长度至少3");
        }
        // 使用公式 v' = q * v * q^-1，但优化为直接计算
        float x = v[0], y = v[1], z = v[2];
        float qx = q[0], qy = q[1], qz = q[2], qw = q[3];
        // 计算临时变量
        float ix = qw * x + qy * z - qz * y;
        float iy = qw * y + qz * x - qx * z;
        float iz = qw * z + qx * y - qy * x;
        float iw = -qx * x - qy * y - qz * z;
        // 结果 = (i * q^-1) 的向量部分
        float rx = ix * qw + iw * -qx + iy * -qz - iz * -qy;
        float ry = iy * qw + iw * -qy + iz * -qx - ix * -qz;
        float rz = iz * qw + iw * -qz + ix * -qy - iy * -qx;
        return new float[]{rx, ry, rz};
    }

    public static float[] quatSlerp(float[] q1, float[] q2, float t) {
        if (q1.length < 4 || q2.length < 4) {
            throw new IllegalArgumentException("四元数长度至少4");
        }
        // 归一化输入（假设已归一化，但安全起见）
        float[] q1n = quatNormalize(q1);
        float[] q2n = quatNormalize(q2);
        float dot = q1n[0]*q2n[0] + q1n[1]*q2n[1] + q1n[2]*q2n[2] + q1n[3]*q2n[3];
        // 确保最短路径
        if (dot < 0f) {
            q2n = new float[]{-q2n[0], -q2n[1], -q2n[2], -q2n[3]};
            dot = -dot;
        }
        dot = Math.max(-1f, Math.min(1f, dot));
        float theta = (float) Math.acos(dot);
        float sinTheta = (float) Math.sin(theta);
        if (sinTheta < 1e-12f) {
            // 线性插值
            return new float[]{
                    (1f-t)*q1n[0] + t*q2n[0],
                    (1f-t)*q1n[1] + t*q2n[1],
                    (1f-t)*q1n[2] + t*q2n[2],
                    (1f-t)*q1n[3] + t*q2n[3]
            };
        }
        float w1 = (float) Math.sin((1f-t) * theta) / sinTheta;
        float w2 = (float) Math.sin(t * theta) / sinTheta;
        return new float[]{
                w1*q1n[0] + w2*q2n[0],
                w1*q1n[1] + w2*q2n[1],
                w1*q1n[2] + w2*q2n[2],
                w1*q1n[3] + w2*q2n[3]
        };
    }

    public static float length(float[] v) {
        float sum = 0f;
        for (float val : v) sum += val * val;
        return (float) Math.sqrt(sum);
    }

    public static float[] normalize(float[] v) {
        float len = length(v);
        if (len < 1e-12f) throw new ArithmeticException("零向量无法归一化");
        float invLen = 1f / len;
        float[] result = new float[v.length];
        for (int i = 0; i < v.length; i++) result[i] = v[i] * invLen;
        return result;
    }

    public static float distance(float[] a, float[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("向量长度不一致");
        float sum = 0f;
        for (int i = 0; i < a.length; i++) {
            float d = a[i] - b[i];
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }


    public static float[] lerp(float[] a, float[] b, float t) {
        if (a.length != b.length) throw new IllegalArgumentException("向量长度不一致");
        float[] result = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + t * (b[i] - a[i]);
        }
        return result;
    }


    public static float[] reflect(float[] v, float[] n) {
        if (v.length < 3 || n.length < 3) {
            throw new IllegalArgumentException("向量长度至少3");
        }
        float dot = v[0]*n[0] + v[1]*n[1] + v[2]*n[2];
        return new float[]{
                v[0] - 2f * dot * n[0],
                v[1] - 2f * dot * n[1],
                v[2] - 2f * dot * n[2]
        };
    }

    /**
     * 4x4 矩阵乘以 3D 方向向量（隐式 w=0），返回变换后的方向向量。
     * 忽略矩阵的平移部分，不执行透视除法。
     */
    public static float[] mat4MulDir3(float[] m, float[] v) {
        if (m.length < 16 || v.length < 3) {
            throw new IllegalArgumentException("矩阵长度至少16，向量长度至少3");
        }
        float x = m[0] * v[0] + m[4] * v[1] + m[8] * v[2];
        float y = m[1] * v[0] + m[5] * v[1] + m[9] * v[2];
        float z = m[2] * v[0] + m[6] * v[1] + m[10] * v[2];
        return new float[]{x, y, z};
    }

    // ==================== 2D 仿射矩阵（6 元素） ====================

    /**
     * 返回 2D 仿射单位矩阵 [1, 0, 0, 1, 0, 0]。
     */
    public static float[] mat2dIdentity() {
        return new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
    }

    /**
     * 从平移、缩放、旋转构造 2D 仿射矩阵（列向量约定：点乘矩阵为 p' = M * p）。
     * 变换顺序：先缩放，再旋转，最后平移。
     *
     * @param x       平移 X
     * @param y       平移 Y
     * @param scaleX  X 轴缩放
     * @param scaleY  Y 轴缩放
     * @param rotation 旋转角度（弧度）
     * @return 6 元素 float[] 矩阵 [a, b, c, d, tx, ty]
     */
    public static float[] mat2dCompose(float x, float y,
                                       float scaleX, float scaleY,
                                       float rotation) {
        float cosR = (float) Math.cos(rotation);
        float sinR = (float) Math.sin(rotation);
        return new float[]{
                scaleX * cosR,          // a
                scaleX * sinR,          // b
                scaleY * (-sinR),       // c
                scaleY * cosR,          // d
                x,                      // tx
                y                       // ty
        };
    }

    /**
     * 两个 2D 仿射矩阵相乘：result = a * b。
     * 遵循列向量约定（先应用 b，再应用 a）。
     *
     * @param a 左矩阵
     * @param b 右矩阵
     * @return 乘积矩阵 (6 元素)
     * @throws IllegalArgumentException 如果任一数组长度 < 6
     */
    public static float[] mat2dMul(float[] a, float[] b) {
        if (a.length < 6 || b.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        float a1 = a[0], b1 = a[1], c1 = a[2], d1 = a[3], tx1 = a[4], ty1 = a[5];
        float a2 = b[0], b2 = b[1], c2 = b[2], d2 = b[3], tx2 = b[4], ty2 = b[5];
        return new float[]{
                a1 * a2 + c1 * b2,          // a
                b1 * a2 + d1 * b2,          // b
                a1 * c2 + c1 * d2,          // c
                b1 * c2 + d1 * d2,          // d
                a1 * tx2 + c1 * ty2 + tx1,  // tx
                b1 * tx2 + d1 * ty2 + ty1   // ty
        };
    }

    /**
     * 计算 2D 仿射矩阵的逆矩阵。
     * 若矩阵奇异（行列式接近 0），返回 null。
     *
     * @param m 6 元素矩阵
     * @return 逆矩阵，或 null
     */
    public static float[] mat2dInvert(float[] m) {
        if (m.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        float a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        double det = (double) a * d - (double) b * c;
        if (Math.abs(det) < 1e-10) {
            return null;
        }
        double invDet = 1.0 / det;
        float aInv = (float) (d * invDet);
        float bInv = (float) (-b * invDet);
        float cInv = (float) (-c * invDet);
        float dInv = (float) (a * invDet);
        float txInv = (float) ((c * ty - d * tx) * invDet);
        float tyInv = (float) ((b * tx - a * ty) * invDet);
        return new float[]{aInv, bInv, cInv, dInv, txInv, tyInv};
    }

    /**
     * 用 2D 仿射矩阵变换点 (x, y)，返回 [x', y']。
     *
     * @param m 6 元素矩阵
     * @param x 输入 X
     * @param y 输入 Y
     * @return 长度为 2 的 float[]，包含变换后的坐标
     */
    public static float[] mat2dTransformPoint(float[] m, float x, float y) {
        if (m.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        float a = m[0], b = m[1], c = m[2], d = m[3], tx = m[4], ty = m[5];
        return new float[]{
                a * x + c * y + tx,
                b * x + d * y + ty
        };
    }

    /**
     * 将 4x4 矩阵（列优先）转换为 2D 仿射矩阵（提取平移、旋转、缩放）。
     * 仅当 4x4 矩阵仅包含 2D 仿射变换时有效（即第三行/列为 [0,0,1,0] 等）。
     *
     * @param m4 16 元素 4x4 列优先矩阵
     * @return 6 元素仿射矩阵，若无法提取则返回 null
     */
    public static float[] mat4ToMat2d(float[] m4) {
        if (m4.length < 16) return null;
        // 提取左上 2x2 和第三列平移（列优先）
        return new float[]{
                m4[0], m4[1],  // a, b (col0 row0/1)
                m4[4], m4[5],  // c, d (col1 row0/1)
                m4[12], m4[13] // tx, ty (col3 row0/1)
        };
    }

    /**
     * 将 2D 仿射矩阵扩展为 4x4 列优先矩阵（z=0，w=1）。
     */
    public static float[] mat2dToMat4(float[] m2) {
        if (m2.length < 6) {
            throw new IllegalArgumentException("矩阵长度必须至少为 6");
        }
        float a = m2[0], b = m2[1], c = m2[2], d = m2[3], tx = m2[4], ty = m2[5];
        return new float[]{
                a, b, 0f, 0f,   // col0
                c, d, 0f, 0f,   // col1
                0f, 0f, 1f, 0f, // col2
                tx, ty, 0f, 1f  // col3
        };
    }

    /**
     * 构造 2D 仿射变换的逆矩阵（世界 → 本地），
     * 等效于先构造正变换再求逆，但当缩放为 0 时使用 1e-6 避免除零异常。
     * 这会产生一个近似的逆矩阵，仅用于避免崩溃，并非数学上精确的逆。
     *
     * @param x       平移 X
     * @param y       平移 Y
     * @param scaleX  X 轴缩放
     * @param scaleY  Y 轴缩放
     * @param rotation 旋转角度（弧度）
     * @return 6 元素逆矩阵
     */
    public static float[] mat2dComposeInverse(float x, float y,
                                              float scaleX, float scaleY,
                                              float rotation) {
        float cosR = (float) Math.cos(rotation);
        float sinR = (float) Math.sin(rotation);
        float invSX = 1.0f / (scaleX == 0f ? 1e-6f : scaleX);
        float invSY = 1.0f / (scaleY == 0f ? 1e-6f : scaleY);
        return new float[]{
                cosR * invSX,               // a
                -sinR * invSY,              // b
                sinR * invSX,               // c
                cosR * invSY,               // d
                -(cosR * x + sinR * y) * invSX,  // tx
                (sinR * x - cosR * y) * invSY    // ty
        };
    }
}