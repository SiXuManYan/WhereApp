package com.jiechengsheng.city.widget.ratingstar;

/**
 * 拐点（顶点）坐标表示实体类
 */
public class VertexF {
    public VertexF() {
    }

    public VertexF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x;
    public float y;

    public VertexF next;
}
