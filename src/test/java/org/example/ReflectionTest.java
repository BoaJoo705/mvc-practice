package org.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 */
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    @Test
    void controllerScan() {
        Set<Class<?>> beans = getTypesAnnotatdWith(List.of(Controller.class,Service.class));

        logger.debug("beans:[{}]",beans);

    }

    @Test
    void showClass() {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        logger.debug("User all declared fields: [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared constructors: [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared methods: [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    @Test
    void load() throws ClassNotFoundException {
        //1번째 방법
        Class<User> clazz = User.class; //힙영역에 로드되어있는 클래스타입객체를 가져오는 1번째 방법

        //2번째 방법
        User user = new User("serverwizard","주보아");
        Class<? extends User> clazz2 = user.getClass();

        //3번째 방법
        Class<?> clazz3 = Class.forName("org.example.model.User");

        logger.debug("clazz:[{}]",clazz);
        logger.debug("clazz2:[{}]",clazz2);
        logger.debug("clazz3:[{}]",clazz3);

        assertThat(clazz == clazz2).isTrue();
        assertThat(clazz2 == clazz3).isTrue();
        assertThat(clazz3 == clazz).isTrue();
    }

    private Set<Class<?>> getTypesAnnotatdWith(List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections("org.example");

        Set<Class<?>> beans = new HashSet<>();
        annotations.forEach(annotation ->beans.addAll(reflections.getTypesAnnotatedWith(annotation)));
//        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class)); //org.example밑 class 들의 @이 붙은 controller들을 beans에 담는다.
//        beans.addAll(reflections.getTypesAnnotatedWith(Service.class)); //org.example밑 class 들의 @이 붙은 service들을 beans에 담는다.
        return beans;
    }
}
