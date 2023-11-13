package goorm.tricount.api;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.common.HandlesTypes;
import goorm.tricount.common.RepresentModel;
import goorm.tricount.domain.settlement.SettlementRepresentationModelProcessor;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice(basePackages = {"goorm.tricount.domain", "goorm.tricount.common"})
@RequiredArgsConstructor
@Order(1)
public class HateoasWrapper implements ResponseBodyAdvice<Object> {

    private final ApplicationContext ac;
    private Map<Class<?>, RepresentationModelProcessor> processors = new HashMap<>();
    @PostConstruct
    public void init() {
        Map<String, RepresentationModelProcessor> representationModelProcessorMap = ac.getBeansOfType(RepresentationModelProcessor.class);
        representationModelProcessorMap.entrySet().stream().map(Map.Entry::getValue)
                .forEach(processor -> processors.put(getRepresentModel(processor), processor));
    }

    private Class<?> getRepresentModel(RepresentationModelProcessor processor) {
        return processor.getClass().getAnnotation(RepresentModel.class).value();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {

            return null;
        } else if (body instanceof Collection<?>) {

            return body;
        } else {
            var processor = findProcessor(body.getClass());

            if(processor.isPresent()) {
                return processor.get().process(EntityModel.of(body));
            }

            return body;
        }
    }

    private Optional<RepresentationModelProcessor> findProcessor(Class<?> bodyType) {
        return processors.entrySet().stream().filter(entry -> entry.getKey().equals(bodyType)).findFirst().map(Map.Entry::getValue);
    }
}
