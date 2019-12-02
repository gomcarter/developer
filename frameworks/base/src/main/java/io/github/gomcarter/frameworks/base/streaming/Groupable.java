package io.github.gomcarter.frameworks.base.streaming;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @Author： gomcarter
 */
public class Groupable<KEY, VAL> {
    private Map<KEY, VAL> map;

    public static <KEY, VAL> Groupable<KEY, VAL> of(Map<KEY, VAL> map) {
        return new Groupable<>(map);
    }

    private Groupable(Map<KEY, VAL> map) {
        if (map == null) {
            this.map = new HashMap<>();
        } else {
            this.map = map;
        }
    }

    public <OTHER> Groupable join(Groupable<KEY, OTHER> other) {
        Map<KEY, VAL> map = this.map;
        Map<KEY, OTHER> otherMap = other.map;
        return of(new HashMap<KEY, Pair<VAL, OTHER>>(this.map.size()) {{
            map.forEach((k, m) -> Optional.ofNullable(otherMap.get(k)).ifPresent(s -> put(k, new ImmutablePair<>(m, s))));
        }});
    }

    public <OTHER> Groupable leftOuterJoin(Groupable<KEY, OTHER> other) {
        Map<KEY, VAL> map = this.map;
        Map<KEY, OTHER> otherMap = other.map;
        return of(new HashMap<KEY, Pair<VAL, OTHER>>(this.map.size()) {{
            map.forEach((k, m) -> put(k, new ImmutablePair<>(m, otherMap.get(k))));
        }});
    }

    public <RESULT extends Collection> Streamable flatMap(BiFunction<KEY, VAL, RESULT> mapper) {
        Map<KEY, VAL> map = this.map;
        return Streamable.valueOf(new ArrayList<RESULT>() {{
            map.forEach((k, m) -> Optional.ofNullable(mapper.apply(k, map.get(k))).ifPresent(this::addAll));
        }});
    }

    public Map<KEY, VAL> collect() {
        return map;
    }
}
