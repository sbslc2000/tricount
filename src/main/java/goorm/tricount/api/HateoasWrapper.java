package goorm.tricount.api;

import goorm.tricount.api.hateoas.RepresentationModelProcessorResolver;
import goorm.tricount.common.RepresentModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice(basePackages = {"goorm.tricount.domain", "goorm.tricount.common"})
@RequiredArgsConstructor
@Order(1)
public class HateoasWrapper implements ResponseBodyAdvice<Object> {

    private final RepresentationModelProcessorResolver representationModelProcessorResolver;

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
            var processor = representationModelProcessorResolver.getProcessor(body.getClass());

            if(processor.isPresent()) {
                return processor.get().process(EntityModel.of(body));
            } else {
                return body;
            }
        }
    }
}
