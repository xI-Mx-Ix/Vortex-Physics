package net.xmx.vortex.physics.terrain.cache;

import com.github.stephengold.joltjni.ShapeRefC;
import java.util.LinkedHashMap;
import java.util.Map;

public class TerrainShapeCache {

    private final LinkedHashMap<Integer, ShapeRefC> cache;
    private final int capacity;

    public TerrainShapeCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, ShapeRefC> eldest) {
                boolean shouldRemove = size() > TerrainShapeCache.this.capacity;
                if (shouldRemove && eldest.getValue() != null) {
                    eldest.getValue().close();
                }
                return shouldRemove;
            }
        };
    }

    public synchronized ShapeRefC get(int key) {
        ShapeRefC shapeRef = cache.get(key);
        if (shapeRef != null && shapeRef.getPtr() != null) {
            return shapeRef.getPtr().toRefC();
        }
        return null;
    }

    public synchronized void put(int key, ShapeRefC shape) {
        if (shape == null) return;
        ShapeRefC oldShape = cache.put(key, shape);
        if (oldShape != null && oldShape != shape && oldShape.getPtr() != shape.getPtr()) {
            oldShape.close();
        }
    }

    public synchronized void clear() {
        cache.values().forEach(shapeRef -> {
            if (shapeRef != null) shapeRef.close();
        });
        cache.clear();
    }
}