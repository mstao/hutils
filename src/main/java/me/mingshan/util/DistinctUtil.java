/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 计算集合中重复数据工具类，完全自定义检测key
 *
 * @author mingshan
 */
public class DistinctUtil {

    private DistinctUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * 计算非重复数据
     *
     * @param source 源集合
     * @param keyExtractor key生成Function接口，例如： {@code user -> user.getName() + "," + user.getCity()}
     * @param <T> 泛型参数
     * @return 去重后的集合
     */
    public static <T> List<T> streamDistinct(List<T> source, Function<? super T, ?> keyExtractor) {
        Objects.requireNonNull(source);

        return source.parallelStream().filter(distinctByKey(keyExtractor))
                .collect(Collectors.toList());
    }

    /**
     * key生成方式
     *
     * @param keyExtractor key生成Function接口
     * @param <T> 泛型参数
     * @return {@code Predicate<T>}
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Objects.requireNonNull(keyExtractor);

        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    /**
     * 给定两个集合，获取其差集
     *
     * @param source 源集合
     * @param distinctSource 另一个集合
     * @param <T> 泛型参数
     * @return 差集
     */
    public static <T> List<T> getRepeatData(List<T> source, List<T> distinctSource) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(distinctSource);

        ArrayList<T> tempList = new ArrayList<>();
        tempList.addAll(source);
        tempList.removeAll(distinctSource);
        return tempList;
    }

    /**
     * 获取一个集合重复的数据，没有重复返回{@code null}
     *
     * @param source 源集合
     * @param keyExtractor key生成Function接口，例如： {@code user -> user.getName() + "," + user.getCity()}
     * @param <T> 泛型参数
     * @return 重复的数据集合（去重后）
     */
    public static <T> List<T> getRepeatData(List<T> source, Function<? super T, ?> keyExtractor) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(keyExtractor);

        List<T> distinctSource = streamDistinct(source, keyExtractor);
        if (distinctSource.size() == source.size()) {
            return null;
        }

        ArrayList<T> tempList = new ArrayList<>();
        tempList.addAll(source);
        tempList.removeAll(distinctSource);
        return tempList;
    }
}
