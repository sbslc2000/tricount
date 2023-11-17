package goorm.tricount.api.hateoas;

import goorm.tricount.common.RepresentModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepresentationModelProcessorResolver {

    private final ApplicationContext ac;
    private final Map<Class<?>, RepresentationModelProcessor> processors = new HashMap<>();

    @PostConstruct
    protected void init() {
        //스프링 빈 중에서 RepresentationModelProcessor 인터페이스를 구현한 빈을 모두 찾는다.
        Map<String, RepresentationModelProcessor> representationModelProcessorMap = ac.getBeansOfType(RepresentationModelProcessor.class);

        //각각의 빈들을 순회한다...
        representationModelProcessorMap.entrySet().stream().map(Map.Entry::getValue)
                .forEach(processor -> {
                    //프로세서의 클래스로부터 인터페이스 타입을 추출
                    ParameterizedType processorType = (ParameterizedType) processor.getClass().getGenericInterfaces()[0];
                    //인터페이스로부터 EntityModel Type 추출
                    ParameterizedType entityType = (ParameterizedType)processorType.getActualTypeArguments()[0];
                    //EntityModel Type으로부터 DTO 추출
                    Type dtoType = entityType.getActualTypeArguments()[0];
                    // DTO 타입과 함께 저장
                    processors.put((Class<?>) dtoType, processor);
                });
    }

    public Optional<RepresentationModelProcessor> getProcessor(Class<?> bodyType) {
        return processors.entrySet().stream().filter(entry -> entry.getKey().equals(bodyType)).findFirst().map(Map.Entry::getValue);
    }
}
