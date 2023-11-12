package goorm.tricount.api;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.domain.settlement.SettlementRepresentationModelProcessor;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice(basePackages = {"goorm.tricount.domain", "goorm.tricount.common"})
@RequiredArgsConstructor
@Order(1)
public class HateoasWrapper implements ResponseBodyAdvice<Object> {

    private final SettlementRepresentationModelProcessor settlementProcessor;

    @PostConstruct
    public void init() {
        System.out.println("HateoasWrapper init");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        if(body instanceof Collection<?>) {
            return body;
        } else {
            return settlementProcessor.process(EntityModel.of((SettlementDto.Detail) body));
        }
    }
}
