package goorm.tricount.api.hateoas;

import goorm.tricount.common.RepresentModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepresentationModelProcessorResolver {

    private final ApplicationContext ac;
    private final Map<Class<?>, RepresentationModelProcessor> processors = new HashMap<>();
    @PostConstruct
    protected void init() {
        Map<String, RepresentationModelProcessor> representationModelProcessorMap = ac.getBeansOfType(RepresentationModelProcessor.class);
        representationModelProcessorMap.entrySet().stream().map(Map.Entry::getValue)
                .forEach(processor -> processors.put(getRepresentModel(processor), processor));
    }

    private Class<?> getRepresentModel(RepresentationModelProcessor processor) {
        return processor.getClass().getAnnotation(RepresentModel.class).value();
    }

    public Optional<RepresentationModelProcessor> getProcessor(Class<?> bodyType) {
        return processors.entrySet().stream().filter(entry -> entry.getKey().equals(bodyType)).findFirst().map(Map.Entry::getValue);
    }
}
