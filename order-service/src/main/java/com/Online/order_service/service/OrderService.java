package com.Online.order_service.service;

import com.Online.order_service.dto.InventoryResponse;
import com.Online.order_service.dto.OrderLineItemsDto;
import com.Online.order_service.dto.OrderRequest;
import com.Online.order_service.model.Order;
import com.Online.order_service.model.OrderLineItems;
import com.Online.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional()
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = Optional.ofNullable(orderRequest.getOrderLineItemsDtosList())
                .orElseGet(Collections::emptyList)  // Default to an empty list if the original list is null
                .stream()
                .map(this::mapToDto)  // Using method reference here
                .collect(Collectors.toList());  // U

        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes =order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //call the inventory service and place order if product available in stock
        InventoryResponse[] inventoryResponsesArray =webClient.get()
                .uri("http://localhost:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);

        if(allProductsInStock) {
            orderRepository.save(order);
        }
        else
        {
            throw new IllegalArgumentException("product is not in stock");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
      OrderLineItems orderLineItems= new OrderLineItems();
      orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
      orderLineItems.setPrice(orderLineItemsDto.getPrice());
      orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
      return orderLineItems;
    }
}
