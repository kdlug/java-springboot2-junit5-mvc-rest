package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.services.BeerOrderService;
import guru.springframework.brewery.web.model.BeerOrderDto;
import guru.springframework.brewery.web.model.BeerOrderLineDto;
import guru.springframework.brewery.web.model.BeerOrderPagedList;
import guru.springframework.brewery.web.model.OrderStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerTest {
    @MockBean
    BeerOrderService beerOrderService;

    @Autowired
    MockMvc mockMvc;

    BeerOrderDto validOrder;

    BeerOrderLineDto beerOrderLineDto;

    BeerOrderPagedList beerOrderPagedList;

    @AfterEach
    void tearDown() {
        reset(beerOrderService);
    }

    @BeforeEach
    void setUp() {
        beerOrderLineDto = BeerOrderLineDto.builder()
                .orderQuantity(1)
                .createdDate(OffsetDateTime.now())
                .id(UUID.randomUUID())
                .beerId(UUID.randomUUID())
                .version(1)
                .build();

        validOrder = BeerOrderDto.builder()
                .id(UUID.randomUUID())
                .orderStatus(OrderStatusEnum.NEW)
                .version(1)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .customerId(UUID.randomUUID())
                .beerOrderLines(Collections.singletonList(beerOrderLineDto))
                .build();

        List<BeerOrderDto> orders = Collections.singletonList(validOrder);

        beerOrderPagedList = new BeerOrderPagedList(orders,PageRequest.of(1,1),2L);

        given(beerOrderService.listOrders(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(beerOrderPagedList);
    }

    @Test
    void shouldGetValidOrderPagedList() throws Exception {
        mockMvc.perform(get("/api/v1/customers/" + validOrder.getCustomerId() + "/orders").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content.[*].customerId").value(validOrder.getCustomerId().toString()))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andDo(print());
    }
}