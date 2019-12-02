package io.github.gomcarter.frameworks.base.streaming;

import io.github.gomcarter.frameworks.base.mapper.JsonMapper;

import java.util.List;
import java.util.Map;

public class Example {
    private Long id;
    private String name;

    public static Example[] construct() {
        return new Example[]{
                new Example(1L, "1"), new Example(2L, "2"),
                new Example(3L, "3"), new Example(1L, "11")
        };
    }

    public static Example[] construct2() {
        return new Example[]{new Example(1L, "1"), new Example(4L, "11")};
    }

    public Example(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Example setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Example setName(String name) {
        this.name = name;
        return this;
    }


    public static void main(String[] args) {
//        Map<Long, Collection<Example>> map = Streamable.valueOf(Example.construct())
//                .groupby(Example::getId)
//                .collect();
//
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(map));

        Map<Long, List<String>> map1 = Streamable.valueOf(Example.construct())
                .groupby(Example::getId, Example::getName)
                .collect();


        System.out.println(JsonMapper.buildNonNullMapper().toJson(map1));

//        Map<Long, Collection<Example>> map = Streamable.ExampleOf(Example.construct())
//                .uniqueGroupbySafely(Example::getId)
//                .collect();
//
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(map));
//
//        Map<Long, Collection<Example>> map1 = Streamable.ExampleOf(Example.construct())
//                .uniqueGroupbySafely(Example::getId, Example::getName)
//                .collect();
//
//
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(map1));


//        Groupable a = Streamable.valueOf(Example.construct()).groupby(Example::getId);
//        Groupable b = Streamable.valueOf(Example.construct()).groupby(Example::getId, Example::getName);
//
//        List<Example> r = a.join(b)
//                .flatMap((Long key, Pair<Collection<Example>, Collection<String>> pair) ->
//                        new ArrayList<Example>() {{
//                            addAll(pair.getKey());
//                            pair.getValue().forEach(s -> {
//                                add(new Example(key, s));
//                            });
//                        }}
//                )
//                .collect();
//
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(r));

        /*left join*/
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(
//                Streamable.valueOf(Example.construct())
//                        .groupby(Example::getId)
//                        .leftOuterJoin(Streamable.valueOf(Example.construct2()).groupby(Example::getId))
//                        .collect()
//        ));
    }
}
