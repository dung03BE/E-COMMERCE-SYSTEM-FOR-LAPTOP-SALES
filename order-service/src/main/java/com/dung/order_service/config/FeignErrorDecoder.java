    package com.dung.order_service.config;

    import com.dung.order_service.exception.DataNotFoundException;
    import feign.Response;
    import feign.codec.ErrorDecoder;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Component;

    @Component
    public class FeignErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new DataNotFoundException("Product không tồn tại");
            }
            return new Exception("Generic error");
        }
    }
