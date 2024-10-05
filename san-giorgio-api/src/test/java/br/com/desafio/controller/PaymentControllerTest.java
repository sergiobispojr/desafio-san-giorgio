package br.com.desafio.controller;

import br.com.desafio.controller.dto.request.PaymentItemRequestDto;
import br.com.desafio.controller.dto.request.PaymentRequestDto;
import br.com.desafio.domain.exception.EntityNotFoundException;
import br.com.desafio.domain.exception.RestExceptionHandler;
import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.PaymentStatus;
import br.com.desafio.domain.usecase.impl.ConfirmPaymentUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.math.BigDecimal;
import java.util.List;

import static br.com.desafio.util.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    public static final String PATH = "/api/v1/payment";

    @Mock
    private ConfirmPaymentUseCaseImpl confirmPaymentUseCase;

    @InjectMocks
    private PaymentController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }


    @Test
    void testConfirmPayment() throws Exception {
        PaymentItemRequestDto requestItem = new PaymentItemRequestDto("payment-1", BigDecimal.valueOf(10));
        PaymentRequestDto request = new PaymentRequestDto("client-123", List.of(requestItem));

        PaymentItemModel paymentItemModel = new PaymentItemModel(requestItem.paymentId(), requestItem.paymentValue(), PaymentStatus.TOTAL);
        PaymentModel paymentModel = new PaymentModel(request.clientId(), List.of(paymentItemModel));

        when(confirmPaymentUseCase.confirm(request.toPaymentModel())).thenReturn(paymentModel);

        mockMvc.perform(put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.client_id", is(paymentModel.clientId()))
                        , jsonPath("$.payment_items.[0].payment_id", is(paymentModel.paymentItems().get(0).paymentId()))
                        , jsonPath("$.payment_items.[0].payment_value").value(paymentModel.paymentItems().get(0).paymentValue())
                );

    }

    @Test
    void testConfirmPayment_InvalidRequest() throws Exception {
        PaymentRequestDto request = new PaymentRequestDto("client-123", List.of());

        mockMvc.perform(put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpectAll(status().isBadRequest()
                        , jsonPath("$.path", is(PATH))
                        , jsonPath("$.error", is("Erro na requisição"))
                        , jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value()))
                        , jsonPath("$.timestamp", anything())
                        , jsonPath("$.message", anything())
                );

    }

    @Test
    void testConfirmPayment_EntityNotFound() throws Exception {
        PaymentItemRequestDto requestItem = new PaymentItemRequestDto("payment-1", BigDecimal.valueOf(10));
        PaymentRequestDto request = new PaymentRequestDto("client-123", List.of(requestItem));

        when(confirmPaymentUseCase.confirm(request.toPaymentModel())).thenThrow(new EntityNotFoundException("Client not found"));
        mockMvc.perform(put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpectAll(status().isNotFound()
                        , jsonPath("$.path", is(PATH))
                        , jsonPath("$.error", is("Recurso não encontrado"))
                        , jsonPath("$.status", is(HttpStatus.NOT_FOUND.value()))
                        , jsonPath("$.timestamp", anything())
                        , jsonPath("$.message", is("Client not found"))
                );

    }

}
